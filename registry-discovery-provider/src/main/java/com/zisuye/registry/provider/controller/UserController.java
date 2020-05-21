package com.zisuye.registry.provider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @GetMapping("/user/sayHi")
  public String sayHi() {
    return "hi hi hi...";
  }
}
