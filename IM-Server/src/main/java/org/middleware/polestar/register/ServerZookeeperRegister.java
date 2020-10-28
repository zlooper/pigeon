package org.middleware.polestar.register;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.CuratorConnectionLossException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.middleware.polestar.model.ServerInstanceMetaData;
import org.middleware.polestar.config.ZookeeperConfig;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ServerZookeeperRegister implements ServerRegister {

    private final ZookeeperConfig zookeeperConfig;

    private final ServerInstanceMetaData serverInstanceMetaData;

    private CuratorFramework curatorFramework;

    public ServerZookeeperRegister(ZookeeperConfig zookeeperConfig,ServerInstanceMetaData serverInstanceMetaData) throws CuratorConnectionLossException, InterruptedException {
        this.zookeeperConfig = Objects.requireNonNull(zookeeperConfig);
        this.serverInstanceMetaData = Objects.requireNonNull(serverInstanceMetaData);
    }

    private void serverMetaRegistration(ServerInstanceMetaData metaData) {
        String path = "/" + ServerInstanceMetaData.NODE_PATH + "/" + metaData.uniqueKey();
        String metaBody = metaData.metaBody();
        byte[] bodyBytes = metaBody.getBytes(Charset.defaultCharset());
        try {
            curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path,bodyBytes);
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
    }

    @Override
    public void registerInit() throws Exception {
        //初始化Client
        this.curatorFramework = initCurator();
        //连接ZK
        this.zookeeperConnect();
        //注册Local
        serverMetaRegistration(serverInstanceMetaData);
    }

    @Override
    public void registerClose() {
        //Client Close
        curatorFramework.close();
    }

    private CuratorFramework initCurator(){
        log.info("Zookeeper Config [{}]",zookeeperConfig.toString());
        int connRetryWaitTime = (int) TimeUnit.SECONDS.toMillis(zookeeperConfig.getConnRetryWaitTime());
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(zookeeperConfig.getConnString())
                .retryPolicy(new ExponentialBackoffRetry(connRetryWaitTime, zookeeperConfig.getConnRetryCount()))
                .connectionTimeoutMs((int) TimeUnit.SECONDS.toMillis(zookeeperConfig.getConnTimeout()))
                .sessionTimeoutMs((int) TimeUnit.SECONDS.toMillis(zookeeperConfig.getSessionTimeout()))
                .namespace(zookeeperConfig.getNamespace());
        String digest = zookeeperConfig.getDigest();
        if(StringUtils.isNotBlank(digest)){
            builder.authorization("digest",digest.getBytes(Charset.defaultCharset()))
                    .aclProvider(new ACLProvider() {

                        @Override
                        public List<ACL> getDefaultAcl() {
                            return ZooDefs.Ids.CREATOR_ALL_ACL;
                        }

                        @Override
                        public List<ACL> getAclForPath(final String path) {
                            return ZooDefs.Ids.CREATOR_ALL_ACL;
                        }
                    });
        }
        return builder.build();
    }

    private void zookeeperConnect() throws CuratorConnectionLossException, InterruptedException {
        log.info("Zookeeper Connecting...");
        curatorFramework.start();
        int timeout = zookeeperConfig.getConnTimeout() * zookeeperConfig.getConnRetryCount();
        boolean connected;
        try {
            connected = curatorFramework.blockUntilConnected(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.info("Zookeeper Connection Thread Interrupted");
            curatorFramework.close();
            throw new InterruptedException();
        }
        if(connected){
            log.info("Zookeeper Connected");
        }else {
            log.info("Zookeeper Connection Failed");
            throw new CuratorConnectionLossException();
        }
    }
}
