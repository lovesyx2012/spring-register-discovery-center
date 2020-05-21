package com.zisuye.registry.center;

import com.zisuye.registry.center.constants.Constants;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.springframework.stereotype.Component;

@Component
public class RegistryCenterImpl implements RegistryCenter {

  // zk 连接对象
  private ZkClient zkClient;

  public ZkClient getZkClient(String zkAddress) {
    if (zkClient == null) {
      synchronized (this) {
        if (zkClient == null) {
          zkClient = new ZkClient(zkAddress,5000,5000,new SerializableSerializer());
          System.out.println("zk连接成功...");
        }
      }
    }

    return zkClient;
  }


  /**
   * 服务注册
   *
   * @param zkAddress      zk地址
   * @param serviceName    服务名
   * @param serviceAddress 服务地址
   */
  @Override
  public void registryService(String zkAddress, String serviceName, String serviceAddress) {
    // 连接 zk
    zkClient = getZkClient(zkAddress);

    try {
      // 根节点
      if (!zkClient.exists(Constants.REGISTRY)) {
        zkClient.createPersistent(Constants.REGISTRY);
      }

      // 服务节点
      String serviceNode = Constants.REGISTRY + "/" + serviceName + "/" + serviceAddress;
      if (!zkClient.exists(Constants.REGISTRY + "/" + serviceName)) {
        zkClient.createPersistent(Constants.REGISTRY + "/" + serviceName);
      }
      if (!zkClient.exists(serviceNode)) {
        zkClient.createEphemeral(serviceNode);
      }

      System.out.println("服务注册完毕....." + serviceNode);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 服务发现
   *
   * @param zkAddress   zk地址
   * @return
   */
  @Override
  public Map<String, List<String>> discoveryService(String zkAddress) {
    // 连接 zk
    zkClient = getZkClient(zkAddress);

    // 服务节点
    String registryNode = Constants.REGISTRY;

    List<String> serviceNodes =  zkClient.getChildren(registryNode);

    Map<String, List<String>> map = new HashMap<>();
    for (String serviceNode : serviceNodes) {
      map.put(serviceNode, zkClient.getChildren(registryNode + "/" + serviceNode));
    }

    return map;
  }
}
