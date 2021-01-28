package com.applicativeconsult.legoshop.domain;

import com.applicativeconsult.legoshop.model.Item;
import lombok.Data;

@Data
public class ShopItem {
  private String name;
  private String id;
  private int price;
  private Item.StatusEnum status;
}
