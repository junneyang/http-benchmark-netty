package httpclient;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpClientHandler extends ChannelInboundHandlerAdapter {
    private boolean isstdout;
    private Logger logger;
    public HttpClientHandler(boolean isstdout) {
        // TODO Auto-generated constructor stub
        this.isstdout = isstdout;
        LogLib.loginit();
        this.logger = LogManager.getLogger(HttpClientHandler.class.getName());
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("client active ...");
        //this.sendRequest(ctx);
        //System.out.println("client write request ...");
        this.logger.info("client active ...");
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /*if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;
            //System.out.println("CONTENT_TYPE : " + response.headers().get(HttpHeaders.Names.CONTENT_TYPE));
            //System.out.println("STATUS       : " + response.getStatus());
        }*/
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            ByteBuf buf = content.content();
            if (this.isstdout) {
                System.out.println(buf.toString(io.netty.util.CharsetUtil.UTF_8));
            }
            //释放内存空间
            buf.release();
            //ctx.close();
            //修改完成标记
            HttpClient.flag = true;
            this.logger.info("client read complete ...");
            //强制垃圾回收
            System.gc();
        }
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("client inactive ...");
        this.logger.info("client inactive ...");
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        this.logger.error(cause.toString());
        //cause.printStackTrace();
        //ctx.close();
    }
    public static void main(String[] args) {
        // TODO Auto-generated method stub
    }
}
