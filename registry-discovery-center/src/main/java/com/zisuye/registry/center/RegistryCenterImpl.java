package com.zisuye.registry.center;

import com.zisuye.registry.center.constants.Constants;
import java.util.List;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.springframework.stereotype.Component;

@Component
public class RegistryCenterImpl implements RegistryCenter {

  // zk 连接对象
  private CuratorFramework curatorFramework = null;

  public CuratorFramework getCuratorFramework(String zkAddress) {
    if (curatorFramework == null) {
      synchronized (this) {
        if (curatorFramework == null) {
          curatorFramework = CuratorFrameworkFactory
              .builder()
              .connectString(zkAddress)
              .connectionTimeoutMs(10000)
              .retryPolicy(new RetryNTimes(3, 2000))
              .build();

          curatorFramework.start();

          // 阻塞
          try {
            curatorFramework.blockUntilConnected();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }

          System.out.println("zk连接成功...");
        }
      }
    }

    return curatorFramework;
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
    curatorFramework = getCuratorFramework(zkAddress);

    System.out.println(zkAddress);
    System.out.println(serviceName);
    System.out.println(serviceAddress);

    try {
      // 根节点
      if (curatorFramework.checkExists().forPath( Constants.REGISTRY) == null) {
        curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath(Constants.REGISTRY);
      }

      System.out.println(zkAddress);
      System.out.println(serviceName);
      System.out.println(serviceAddress);

      // 服务节点
      String serviceNode = Constants.REGISTRY + "/" + serviceName + "/" + serviceAddress;
      curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
          .forPath(serviceNode);

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
    curatorFramework = getCuratorFramework(zkAddress);

    // 服务节点
    String serviceNode = Constants.REGISTRY + "/" + serviceName;

    try {
      return curatorFramework.getChildren().forPath(serviceNode);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
