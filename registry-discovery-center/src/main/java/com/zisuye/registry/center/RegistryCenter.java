package com.zisuye.registry.center;

import java.util.List;
import java.util.Map;

public interface RegistryCenter {

  void registryService(String zkAddress, String serviceName, String serviceAddress);

  Map<String, List<String>> discoveryService(String zkAddress);
}
