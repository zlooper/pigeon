package org.middleware.polestar.config;

import org.apache.commons.lang3.StringUtils;
import org.middleware.polestar.common.NetworkUtil;
import org.middleware.polestar.model.ServerInstanceMetaData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerMetaConfig {

    @Value("#{systemEnvironment['POLESTAR.SERVER.EXPOSE.PORT'] ?: '10011'}")
    private String serverExposePort;

    @Value("#{systemEnvironment['POLESTAR.SERVER.BRIDGE.PORT'] ?: '10012'}")
    private String serverBridgePort;

    @Value("#{systemEnvironment['POLESTAR.SERVER.EXPOSE.IP'] ?: ''}")
    private String serverIP;

    @Value("#{systemEnvironment['POLESTAR.SERVER.EXPOSE.PID'] ?: ''}")
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
