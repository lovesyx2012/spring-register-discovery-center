package com.zisuye.registry.consumer.controller;

import com.zisuye.registry.center.context.RegistryCenterContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class UserController {
  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private RegistryCenterContext registryCenterContext;

  @GetMapping("/user/remote/sayHi")
  public String sayHi() {
    String serviceURL = "http://" + registryCenterContext.getServer("registry-discovery-provider") + "/user/sayHi";
    System.out.println(serviceURL);
    ResponseEntity<String> responseEntity = restTemplate.getForEntity(serviceURL, String.class);
    String body = responseEntity.getBody();
    System.out.println(body);
    return body;
  }
}
