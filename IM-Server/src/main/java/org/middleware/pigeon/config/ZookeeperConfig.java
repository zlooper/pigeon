package org.middleware.pigeon.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Configuration
@Data
public class ZookeeperConfig implements Serializable {

    @Value("#{systemEnvironment['PIGEON.ZOOKEEPER.CONN_STRING'] ?: '127.0.0.1:2181'}")
    private String connString;

    @Value("#{systemEnvironment['PIGEON.ZOOKEEPER.CONN.RETRY.MAX_COUNT'] ?: '3'}")
    private int connRetryCount;

    @Value("#{systemEnvironment['PIGEON.ZOOKEEPER.CONN.RETRY.WAIT_TIME'] ?: '5'}")
    private int connRetryWaitTime;

    @Value("#{systemEnvironment['PIGEON.ZOOKEEPER.CONN.TIMEOUT'] ?: '5'}")
    private int connTimeout;

    @Value("#{systemEnvironment['PIGEON.ZOOKEEPER.SESSION.TIMEOUT'] ?: '10'}")
    private int sessionTimeout;

    @Value("#{systemEnvironment['PIGEON.ZOOKEEPER.NAMESPACE'] ?: 'PIGEON_IM'}")
    private String namespace;

    @Value("#{systemEnvironment['PIGEON.ZOOKEEPER.DIGEST'] ?: ''}")
    private String digest;
}
