package com.applicativeconsult.legoshop.configuration;

import com.applicativeconsult.legoshop.domain.ShopItem;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "application")
@Configuration
@Data
public class AppConfiguration {
  private List<ShopItem> items;
}
