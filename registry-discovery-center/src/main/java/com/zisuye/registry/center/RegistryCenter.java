package com.zisuye.registry.center;

import java.util.List;

public interface RegistryCenter {

  void registryService(String zkAddress, String serviceName, String serviceAddress);

  List<String> discoveryService(String zkAddress, String serviceName);
}
