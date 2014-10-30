package httpclient;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SendThread implements Runnable {
    private Channel channel;
    private String host;
    //private int port;
    private String method;
    private String url;
    private String content;
    
    private Logger logger;
    
    public SendThread(Channel channel, String host, int port, String method, String url, String content) {
        // TODO Auto-generated constructor stub
        this.channel = channel;
        this.host = host;
        //this.port = port;
        this.method = method;
        this.url = url;
        this.content = content;
        LogLib.loginit();
        this.logger = LogManager.getLogger(SendThread.class.getName());
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        DefaultFullHttpRequest request;
        URI uri = null;
        try {
            uri = new URI(this.url);
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            this.logger.error(e.toString());
        }
        
        if (this.method.equals("POST")) {
            request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,
                    HttpMethod.POST, uri.toASCIIString(), Unpooled.wrappedBuffer(this.content.getBytes()));
        } else if (this.method.equals("GET")) {
            request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,
                    HttpMethod.GET, uri.toASCIIString(), null);
        } else {
            //System.out.println("ERROR : not supported http method");
            this.logger.error("not supported http method");
            return;
        }
        
        request.headers().set(HttpHeaders.Names.HOST, this.host);
        request.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/json;charset=utf-8");
        request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, request.content().readableBytes());
        
        this.channel.write(request);
        this.channel.flush();
        //System.out.println("client write ...");
        this.logger.debug("client write ...");
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }
}
