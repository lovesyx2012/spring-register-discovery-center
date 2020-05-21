package com.zisuye.registry.center.context;

import java.util.List;
import java.util.Random;

public class RegistryCenterContext {
  private List<String> serverList;

  public RegistryCenterContext(List<String> serverList) {
    this.serverList = serverList;
  }

  public String getServer() {
    // 负载均衡
    int index = new Random().nextInt(serverList.size());
    return serverList.get(index);
  }
}
