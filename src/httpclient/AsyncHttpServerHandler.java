package httpclient;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AsyncHttpServerHandler extends ChannelInboundHandlerAdapter {
    private HttpRequest request;
    private Logger logger;
    
    public AsyncHttpServerHandler() {
        // TODO Auto-generated constructor stub
        LogLib.loginitsrv();
        this.logger = LogManager.getLogger(AsyncHttpServerHandler.class.getName());
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("remote address : " + ctx.channel().remoteAddress() + " active");
        super.channelActive(ctx);
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {    
        if (msg instanceof HttpRequest) {
            request = (HttpRequest) msg;
            //String url = request.getUri();
            //System.out.println("url : " + url);
        }
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            ByteBuf buf = content.content();
            logger.debug("cnt : " +  buf.toString(io.netty.util.CharsetUtil.UTF_8));
            buf.release();
            
            String res = "hello world !!!";
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK, Unpooled.wrappedBuffer(res.getBytes("UTF-8")));
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
            if (HttpHeaders.isKeepAlive(this.request)) {
                response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            }
            ctx.write(response);
            ctx.flush();
        }
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("remote address : " + ctx.channel().remoteAddress() + " inactive");
        super.channelInactive(ctx);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error(cause.toString());
        ctx.close();
    }

}
