package org.middleware.pigeon.facade;

import lombok.extern.slf4j.Slf4j;
import org.middleware.pigeon.register.ServerRegister;
import org.middleware.pigeon.server.IMServer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

@Component
@Slf4j
public class ServerApplicationDefaultFacade implements ServerApplicationFacade {

    @Resource(name = "serverZookeeperRegister")
    private ServerRegister serverRegister;

    @Resource(name = "nettyIMServer")
    private IMServer imServer;

    @Override
    @PostConstruct
    public void applicationStart() throws Exception {
        log.info("Server Start...");
        imServer.serverInit();
        serverRegister.registerInit();
        log.info("Server Started");
    }

    @Override
    @PreDestroy
    public void applicationShutdown() throws Exception {
        log.info("Server Shutdown...");
        serverRegister.registerClose();
        imServer.serverClose();
        log.info("Server Shutdown over");
    }
}
