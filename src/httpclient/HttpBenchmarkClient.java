package httpclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.sf.json.JSONObject;

public class HttpBenchmarkClient {
    private String host;
    private int port;
    private int client_num;
    private double test_time;
    private int print_res;
    private JSONObject jsondata;
    
    private Logger logger;

    public HttpBenchmarkClient(String host, int port, int client_num, double test_time, int print_res, JSONObject jsondata) {
        // TODO Auto-generated constructor stub
        this.host = host;
        this.port = port;
        this.client_num = client_num;
        this.test_time = test_time;
        this.print_res = print_res;
        this.jsondata = jsondata;
        
        LogLib.loginit();
        this.logger = LogManager.getLogger(HttpBenchmarkClient.class.getName());
    }

    public void start() throws Exception {
        EventLoopGroup group;
        group = new NioEventLoopGroup();
        
        HttpBenchmark.start_time = System.currentTimeMillis();
        HttpBenchmark.end_time = HttpBenchmark.start_time + (long) this.test_time * 60 * 1000;
        try {
            Bootstrap b = new Bootstrap();
            b.group(group);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // TODO Auto-generated method stub
                    ch.pipeline().addLast(new HttpRequestEncoder());
                    ch.pipeline().addLast(new HttpResponseDecoder());
                    ch.pipeline().addLast(new HttpBenchmarkClientHandler(host, port, HttpBenchmark.end_time, print_res, jsondata));
                }
            });
            ArrayList<ChannelFuture> channelfuture_list = new ArrayList<ChannelFuture>();
            for (int i = 0; i < this.client_num; i++) {
                ChannelFuture f = b.connect(this.host, this.port);
                channelfuture_list.add(f);
            }
            logger.info("all client sockets init success");
            
            for (int i = 0; i < channelfuture_list.size(); i++) {
                channelfuture_list.get(i).channel().closeFuture().sync();
            }
            logger.info("all client sockets shut down success");
        } finally {
            group.shutdownGracefully();
        }
    }
    
    public static void main(String[] args) {
    }
}
