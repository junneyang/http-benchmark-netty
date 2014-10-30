package httpclient;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.AttributeKey;

import java.net.URI;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Sharable
public class HttpBenchmarkClientHandler extends ChannelInboundHandlerAdapter {
    private String host;
    //private int port;
    private long end_time;
    private int print_res;
    private JSONObject jsondata;
    
    private String method;
    private JSONArray testdataarray;

    private AttributeKey<Long> start_time = AttributeKey.valueOf("start_time" + new Random().nextInt());
    private AttributeKey<JSONObject> expect_data = AttributeKey.valueOf("expect_data" + new Random().nextInt());
    
    private Logger logger;
    
    public HttpBenchmarkClientHandler(String host, int port, long end_time, int print_res, JSONObject jsondata) {
        // TODO Auto-generated constructor stub
        this.host = host;
        //this.port = port;
        this.end_time = end_time;
        this.print_res = print_res;
        this.jsondata = jsondata;
        
        this.method = this.jsondata.getString("method");
        this.testdataarray = this.jsondata.getJSONArray("data");
        
        LogLib.loginit();
        this.logger = LogManager.getLogger(HttpBenchmarkClientHandler.class.getName());
    }
    public void sendRequest(ChannelHandlerContext ctx) throws Exception {
        DefaultFullHttpRequest request;
        JSONObject expect = null;
        int arraysize = this.testdataarray.size();
        int index = 0;
        if (arraysize != 1) {
            //生成[0,contentdatalen-1]之间的随机数
            index = (int) (0 + Math.random() * (arraysize - 0 + 1 - 1));
        }
        if (this.method.equals("POST")) {
            URI uri = new URI(this.testdataarray.getJSONObject(0).getString("url"));
            String content = this.testdataarray.getJSONObject(index).getJSONObject("body").toString();
            expect = this.testdataarray.getJSONObject(index).getJSONObject("expect");
            request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,
                    HttpMethod.POST, uri.toASCIIString(), Unpooled.wrappedBuffer(content.getBytes()));
        } else if (this.method.equals("GET")) {
            String url = this.testdataarray.getJSONObject(index).getString("url");
            expect = this.testdataarray.getJSONObject(index);
            URI uri = new URI(url);
            request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,
                    HttpMethod.GET, uri.toASCIIString(), null);
        } else {
            System.out.println("ERROR : not supported http method");
            ctx.close();
            return;
        }
        
        request.headers().set(HttpHeaders.Names.HOST, this.host);
        request.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/json;charset=utf-8");
        request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, request.content().readableBytes());
        
        ctx.attr(start_time).set(System.currentTimeMillis());
        ctx.attr(expect_data).set(expect);
        //long st = (Long)ctx.attr(start_time).get();
        //System.out.println("start time : " + st);
        ctx.write(request);
        ctx.flush();
        
        synchronized (HttpBenchmark.lock) {
            HttpBenchmark.total_req += 1;
        }
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("client active ...");
        this.sendRequest(ctx);
        //System.out.println("client write request ...");
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /*if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;
            //System.out.println("CONTENT_TYPE:" + response.headers().get(HttpHeaders.Names.CONTENT_TYPE));
            if (! response.getStatus().equals(HttpResponseStatus.OK)) {
                synchronized (httpbenchmark.lock) {
                    httpbenchmark.total_err += 1;
                }
            }
        }*/
        if (msg instanceof HttpContent){
            long st = (Long) ctx.attr(start_time).get();
            JSONObject expect = (JSONObject) ctx.attr(expect_data).get();
            //System.out.println("start time : " + st);
            
            HttpContent content = (HttpContent) msg;
            ByteBuf buf = content.content();
            JSONObject retstr = JSONObject.fromObject(buf.toString(io.netty.util.CharsetUtil.UTF_8));
            if (this.print_res == 1) {
                System.out.println(retstr);
            }
            //释放内存空间，防止内存泄漏
            buf.release();
            
            long et = System.currentTimeMillis();
            long elapsed = et - st;
            synchronized (HttpBenchmark.lock) {
                if (expect.equals(retstr)) {
                    HttpBenchmark.total_res += 1;
                } else {
                    HttpBenchmark.total_err += 1;
                }
                HttpBenchmark.total_res_time += elapsed;
                if (elapsed < 10) {
                    HttpBenchmark.below_10 += 1;
                } else if (elapsed >= 10 && elapsed < 20) {
                    HttpBenchmark.between_10_20 += 1;
                } else if (elapsed >= 20 && elapsed < 30) {
                    HttpBenchmark.between_20_30 += 1;
                } else {
                    HttpBenchmark.over_30 += 1;
                }
            }
            
            long ct = System.currentTimeMillis();
            if (ct <= this.end_time) {
                this.sendRequest(ctx);
            } else {
                ctx.close();
            }
        }

    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        synchronized (HttpBenchmark.lock) {
            HttpBenchmark.total_err += 1;
        }
        if (this.print_res == 1) {
            logger.error(cause.toString());
        }
        //ctx.close();
    }
}
