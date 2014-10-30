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
import net.sf.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpClient {
    private String host;
    private int port;
    private String method;
    private String url;
    private String content;
    public static boolean flag = false;
    private Logger logger;
    
    public HttpClient(String host, int port, String method, String url, String content) {
        // TODO Auto-generated constructor stub
        this.host = host;
        this.port = port;
        this.method = method;
        this.url = url;
        this.content = content;
        LogLib.loginit();
        this.logger = LogManager.getLogger(HttpClient.class.getName());
    }
    public void start() {
        EventLoopGroup group = new NioEventLoopGroup();
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
                    ch.pipeline().addLast(new HttpClientHandler(true));
                }
            });
            //等待客户端channel初始化
            ChannelFuture f = b.connect(this.host, this.port).sync();
            //启动发送线程
            SendThread st = new SendThread(f.channel(), this.host, this.port, this.method, this.url, this.content);
            Thread th = new Thread(st);
            th.start();
            while (!HttpClient.flag) {
                Thread.sleep(10);
            }
            //关闭channel，主动关闭
            f.channel().close();
            //等待channel关闭，如果不关闭，则主线程一直等待
            //f.channel().closeFuture().sync();
        } catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
            this.logger.error(e.toString());
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        LogLib.loginit();
        Logger logger = LogManager.getLogger(HttpClient.class.getName());
        logger.info("httpclient start");
        String host;
        int port;
        String method;
        String url;
        String content;

        if (args.length != 3) {
            HelpLib.disinfo("httpclient.jar");
            logger.info("httpclient help info");
            logger.info("httpclient complete");
            return;
        } else {
            try {
                host = args[0];
                port = Integer.parseInt(args[1]);
                FileLib f = new FileLib(args[2]);
                String datastr = f.gettestdatastring();
                JSONObject jsondata = f.getjsondata(datastr);
                method = jsondata.getString("method");
                url = jsondata.getString("url");
                content = jsondata.getJSONObject("data").toString();
            } catch (Exception e) {
                // TODO: handle exception
                logger.error(e.toString());
                return;
            }
        }
        
        try {
            HttpClient asynchttpclient = new HttpClient(host, port, method, url, content);
            asynchttpclient.start();
        } catch (Exception e) {
            // TODO: handle exception
            logger.error(e.toString());
            return;
        } finally {
            logger.info("httpclient complete");
        }
    }
}
