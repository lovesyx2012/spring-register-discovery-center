package com.zisuye.registry.zkclient;

import java.io.Serializable;

public class User implements Serializable {
  private Integer userId;

  private String username;

  public String getUsername() {
    return username;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }
}
