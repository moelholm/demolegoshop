package com.applicativeconsult.legoshop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SwaggerRedirecter {
  @RequestMapping("/")
  public String get() {
    return "redirect:/swagger-ui.html";
  }
}
