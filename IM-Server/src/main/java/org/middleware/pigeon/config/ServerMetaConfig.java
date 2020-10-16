package org.middleware.pigeon.config;

import org.apache.commons.lang3.StringUtils;
import org.middleware.pigeon.common.NetworkUtil;
import org.middleware.pigeon.model.ServerInstanceMetaData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerMetaConfig {

    @Value("#{systemEnvironment['PIGEON.SERVER.EXPOSE.PORT'] ?: '10011'}")
    private String serverExposePort;

    @Value("#{systemEnvironment['PIGEON.SERVER.BRIDGE.PORT'] ?: '10012'}")
    private String serverBridgePort;

    @Value("#{systemEnvironment['PIGEON.SERVER.EXPOSE.IP'] ?: ''}")
    private String serverIP;

    @Value("#{systemEnvironment['PIGEON.SERVER.EXPOSE.PID'] ?: ''}")
    private String ServerPID;

    @Bean
    public ServerInstanceMetaData serverInstanceMetaData(){
        ServerInstanceMetaData metaData = new ServerInstanceMetaData();
        metaData.setServerIP(StringUtils.isNotBlank(serverIP) ? serverIP : NetworkUtil.getLocalIP());
        metaData.setServerPID(StringUtils.isNotBlank(ServerPID) ? ServerPID : NetworkUtil.getProcessId());
        metaData.setExposePort(serverExposePort);
        metaData.setBridgePort(serverBridgePort);
        return metaData;
    }
}
