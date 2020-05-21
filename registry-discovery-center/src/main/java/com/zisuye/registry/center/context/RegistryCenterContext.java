package com.zisuye.registry.center.context;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class RegistryCenterContext {
  private Map<String, List<String>> serviceList;

  public RegistryCenterContext(Map<String, List<String>> serviceList) {
    this.serviceList = serviceList;
  }

  public String getServer(String service) {
    // 负载均衡
    List<String> serverList = serviceList.get(service);
    int index = new Random().nextInt(serverList.size());
    return serverList.get(index);
  }
}
