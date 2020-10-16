package org.middleware.pigeon.model;

import cn.hutool.json.JSONUtil;
import lombok.Data;

import java.io.Serializable;

@Data
public class ServerInstanceMetaData implements Serializable {

    public static final String NODE_PATH = "server_instances";

    /**
     * Local IP
     */
    private String serverIP;

    /**
     * Local PID
     */
    private String serverPID;

    /**
     * Client Conn Port
     */
    private String exposePort;

    /**
     * Server Sync Port
     */
    private String bridgePort;

    public String uniqueKey(){
        return serverIP + "#" + serverPID;
    }

    public String metaBody(){
        return JSONUtil.toJsonStr(this);
    }

}
