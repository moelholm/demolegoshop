package com.applicativeconsult.legoshop.api;

import com.applicativeconsult.legoshop.configuration.AppConfiguration;
import com.applicativeconsult.legoshop.domain.ShopItem;
import com.applicativeconsult.legoshop.model.Basket;
import com.applicativeconsult.legoshop.model.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("${openapi.legoShop.base-path:/v1}")
public class ShopController implements ShopApi {
  @Autowired
  private AppConfiguration appConf;

  private Basket basket;

  public ShopController() {
    basket = new Basket();
    basket.setId("legoShopBasket");
    basket.setName("ThePrimarySingleUserBasketOfThisShop");
  }

  private <T> ResponseEntity<T> error(String errorDescription) {
    final String errorHeaderName = "X-HTTP-Error-Description";
    HttpHeaders header = new HttpHeaders();
    header.set(errorHeaderName, errorDescription);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(header).build();
  }

  @Override public ResponseEntity<List<Item>> allItems(@Valid List<String> status) {
    List<Item> requestedItems = map(appConf.getItems());
    if (status != null && !status.contains(Item.StatusEnum.SOLDOUT.getValue())) {
      requestedItems = requestedItems.stream().filter(i -> i.getStatus() != Item.StatusEnum.SOLDOUT).collect(Collectors.toList());
    }
    if (status != null && !status.contains(Item.StatusEnum.AVAILABLE.getValue())) {
      requestedItems = requestedItems.stream().filter(i -> i.getStatus() != Item.StatusEnum.AVAILABLE).collect(Collectors.toList());
    }
    return ResponseEntity.ok(requestedItems);
  }

  @Override public ResponseEntity<Void> emptyBasket() {
    basket.setContent(new ArrayList<>());
    return ResponseEntity.ok(null);
  }

  @Override public ResponseEntity<Basket> getBasket() {
    return ResponseEntity.ok(basket);
  }

  @Override public ResponseEntity<Item> getItemById(String itemId) {
    Optional<Item> item = map(appConf.getItems()).stream().filter(i -> i.getId().equals(itemId)).findAny();
    if (item.isEmpty()) {
      return error("No item with id: " + itemId);
    }
    return ResponseEntity.ok(item.get());
  }

  @Override public ResponseEntity<Void> placeItemInBasket(String itemId) {
    if (basket.getContent() != null && basket.getContent().size() > 100) {
      return error("Too many items in basket");
    }

    ResponseEntity<Item> resp = getItemById(itemId);
    if (resp.getStatusCode().is2xxSuccessful()) {
      if (resp.getBody().getStatus() == Item.StatusEnum.SOLDOUT) {
        return error("Item " + resp.getBody().getName() + " is not available for purchase");
      }
      basket.addContentItem(resp.getBody());
      return ResponseEntity.ok(null);
    } else {
      return error("No item with id: " + itemId);
    }
  }

  @Override public ResponseEntity<Void> removeItemInBasket(String itemId) {
    List<Item> bc = Optional.ofNullable(basket.getContent()).orElse(List.of());
    List<Item> itemsMatching = bc.stream()
        .filter(i -> i.getId().equals(itemId))
        .collect(Collectors.toList());

    List<Item> itemsNotMatching = bc.stream()
        .filter(i -> !i.getId().equals(itemId))
        .collect(Collectors.toList());

    // Remove a single item with the id matching itemId, so we can rebuild the basket content
    if (itemsMatching.size() > 0) {
      itemsMatching.remove(0);
    }

    // Readd everything, minus a single matching item, to the basket
    itemsMatching.addAll(itemsNotMatching);
    basket.setContent(itemsMatching);
    return ResponseEntity.ok(null);
  }

  @Override public ResponseEntity<Void> updateItem(@Valid Item updateItem) {
    Optional<ShopItem> si = appConf.getItems().stream().filter(i -> i.getId().equals(updateItem.getId())).findAny();
    if (si.isEmpty()) {
      return error("No such item in the shop");
    }
    si.get().setPrice(updateItem.getPrice());
    si.get().setName(updateItem.getName());
    si.get().setStatus(updateItem.getStatus());
    return ResponseEntity.ok(null);
  }

  private List<Item> map(List<ShopItem> items) {
    return items.stream().map(si -> {
      Item i = new Item();
      i.setStatus(si.getStatus());
      i.setPrice(si.getPrice());
      i.setName(si.getName());
      i.setId(si.getId());
      return i;
    }).collect(Collectors.toList());
  }
}
