package org.middleware.polestar.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Configuration
@Data
public class ZookeeperConfig implements Serializable {

    @Value("#{systemEnvironment['POLESTAR.ZOOKEEPER.CONN_STRING'] ?: '127.0.0.1:2181'}")
    private String connString;

    @Value("#{systemEnvironment['POLESTAR.ZOOKEEPER.CONN.RETRY.MAX_COUNT'] ?: '3'}")
    private int connRetryCount;

    @Value("#{systemEnvironment['POLESTAR.ZOOKEEPER.CONN.RETRY.WAIT_TIME'] ?: '5'}")
    private int connRetryWaitTime;

    @Value("#{systemEnvironment['POLESTAR.ZOOKEEPER.CONN.TIMEOUT'] ?: '5'}")
    private int connTimeout;

    @Value("#{systemEnvironment['POLESTAR.ZOOKEEPER.SESSION.TIMEOUT'] ?: '10'}")
    private int sessionTimeout;

    @Value("#{systemEnvironment['POLESTAR.ZOOKEEPER.NAMESPACE'] ?: 'POLESTAR_IM'}")
    private String namespace;

    @Value("#{systemEnvironment['POLESTAR.ZOOKEEPER.DIGEST'] ?: ''}")
    private String digest;
}
