package org.middleware.pigeon.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.middleware.pigeon.model.MessageProto;
import org.middleware.pigeon.model.ServerInstanceMetaData;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class NettyIMServer implements IMServer {

    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);

    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private final ServerInstanceMetaData serverInstanceMetaData;

    private Channel channel;

    public NettyIMServer(ServerInstanceMetaData serverInstanceMetaData){
        this.serverInstanceMetaData = Objects.requireNonNull(serverInstanceMetaData);
    }

    @Override
    public void serverInit() throws Exception {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ProtobufVarint32FrameDecoder());
                        pipeline.addLast(new ProtobufDecoder(MessageProto.Message.getDefaultInstance()));
                        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
                        pipeline.addLast(new ProtobufEncoder());
                        pipeline.addLast(new SimpleChannelInboundHandler<MessageProto.Message>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, MessageProto.Message msg) throws Exception {
                                //Read
                                //Write
                            }
                        });
                    }
                });
        ChannelFuture channelFuture = serverBootstrap.bind(Integer.parseInt(serverInstanceMetaData.getExposePort())).sync();
        this.channel = channelFuture.channel();
    }

    @Override
    public void serverClose() throws Exception {
        channel.close().sync();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
