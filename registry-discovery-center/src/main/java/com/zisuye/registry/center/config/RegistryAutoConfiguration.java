package com.zisuye.registry.center.config;

import com.zisuye.registry.center.RegistryCenter;
import com.zisuye.registry.center.context.RegistryCenterContext;
import com.zisuye.registry.center.properties.RegistryProperties;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@ConditionalOnClass(ZkClient.class)
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
      Map<String, List<String>> serviceList = registryCenter.discoveryService(zkAddress);
      registryCenterContext(serviceList);
    }
  }

  @Bean
  public RegistryCenterContext registryCenterContext(Map<String, List<String>> serviceList) {
    return new RegistryCenterContext(serviceList);
  }
}
