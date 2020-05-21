package com.zisuye.registry.config;

import com.zisuye.registry.RegistryCenter;
import com.zisuye.registry.properties.RegistryProperties;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan(basePackages = {"com.zisuye.registry"})
public class RegistryAutoConfiguration {

  @Autowired
  private RegistryCenter registryCenter;

  @Autowired
  private RegistryProperties registryProperties;

  @Autowired
  private Environment environment;

  // 初始化
  @PostConstruct
  public void initRegistryCenter() {
    String zkAddress = registryProperties.getAddress();
    boolean register = registryProperties.getRegister();
    boolean discovery = registryProperties.getDiscovery();

    // 服务名
    String serviceName = environment.getProperty("spring.application.name");

    // 服务地址
    String serviceAddress = "";
    try {
      String ip = InetAddress.getLocalHost().getHostAddress();
      String port = environment.getProperty("server.port");
      serviceAddress = ip + ":" + port;
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }

    if (register) {
      registryCenter.registryService(zkAddress, serviceName, serviceAddress);
    }

    if (discovery) {

    }
  }
}
