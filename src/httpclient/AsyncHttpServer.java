package httpclient;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AsyncHttpServer {
    private int port;
    private int backlog = 128;
    private Logger logger;
    
    public AsyncHttpServer(int port) {
        // TODO Auto-generated constructor stub
        this.port = port;
        LogLib.loginitsrv();
        this.logger = LogManager.getLogger(AsyncHttpServer.class.getName());
    }
    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup);
            b.channel(NioServerSocketChannel.class);
            //b.handler(new LoggingHandler(LogLevel.INFO));
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // TODO Auto-generated method stub
                    ChannelPipeline p = ch.pipeline();
                    p.addLast(new HttpResponseEncoder());
                    p.addLast(new HttpRequestDecoder());
                    p.addLast(new AsyncHttpServerHandler());
                }
                
            });
            b.option(ChannelOption.SO_BACKLOG, this.backlog);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = b.bind(this.port).sync();
            logger.info(AsyncHttpServer.class.getName() + " started and listen on " + f.channel().localAddress()); 
            f.channel().closeFuture().sync();
            logger.info(AsyncHttpServer.class.getName() + " closed listen channel");
        } finally {
            // TODO: handle exception
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            logger.info(AsyncHttpServer.class.getName() + " closed boss and worker group");
        }
    }
    

    public static void main(String[] args) {
        // TODO Auto-generated method stub
    }

}
