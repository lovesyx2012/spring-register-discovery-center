package com.zisuye.registry.center.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.registry.zookeeper")
@Getter
@Setter
public class RegistryProperties {

  // zk地址
  private String address;

  // 是否注册
  private Boolean register;

  // 是否发现
  private Boolean discovery;
}
