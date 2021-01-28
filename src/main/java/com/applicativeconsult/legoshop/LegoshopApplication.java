package com.applicativeconsult.legoshop;

import com.applicativeconsult.legoshop.model.Item;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class LegoshopApplication {
  public static void main(String[] args) {
		SpringApplication.run(LegoshopApplication.class, args);
	}

}
