package org.middleware.polestar.facade;

public interface ServerApplicationFacade {

    /**
     * 应用启动
     */
    void applicationStart() throws Exception;

    /**
     * 应用关闭
     */
    void applicationShutdown() throws Exception;
}
