package com.zisuye.registry.center;

import com.zisuye.registry.center.constants.Constants;
import java.util.List;
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

    System.out.println(zkAddress);
    System.out.println(serviceName);
    System.out.println(serviceAddress);

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
   * @param serviceName 服务名
   * @return
   */
  @Override
  public List<String> discoveryService(String zkAddress, String serviceName) {
    // 连接 zk
    zkClient = getZkClient(zkAddress);

    // 服务节点
    String serviceNode = Constants.REGISTRY + "/" + serviceName;

    return zkClient.getChildren(serviceNode);
  }
}
