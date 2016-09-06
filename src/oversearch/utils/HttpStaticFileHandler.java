package oversearch.utils;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;

import java.io.File;
import java.io.RandomAccessFile;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static oversearch.OverSearchServer.ROOT_DIR;
import static oversearch.utils.HttpNotFoundHandler.sendError;

/**
 * Created by msgundam00 on 2016. 8. 27..
 */
public class HttpStaticFileHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private String path;
    private String filename;

    private static String CHECK_JS = ".js";
    private static String CHECK_CSS = ".css";
    private static String RESPONSE_JS = "application/javascript";
    private static String RESPONSE_CSS = "text/css";

    public HttpStaticFileHandler(String path) {
        super(false);
        this.path = ROOT_DIR+path;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        // TODO: Pass Static Files??!!?!? Only in resDir
        if(req.method().equals(HttpMethod.GET)) {
            this.filename = req.uri();
            if(!filename.endsWith(CHECK_JS) && !filename.endsWith(CHECK_CSS)) {
                //불러올수 없음을 처리
                sendError(ctx, HttpResponseStatus.NOT_ACCEPTABLE);
                req.release();
                return;
            }
            File checkfile = new File(this.path+this.filename);
            if(!checkfile.exists() || !checkfile.isFile()) {
                //불러올수 없음을 처리
                sendError(ctx, HttpResponseStatus.NOT_FOUND);
                req.release();
                return;
            }

            RandomAccessFile raf = new RandomAccessFile(checkfile, "r");

            HttpResponse res = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
            HttpUtil.setContentLength(res, raf.length());
            if (filename.endsWith(CHECK_JS)) res.headers().set(CONTENT_TYPE, RESPONSE_JS);
            else res.headers().set(CONTENT_TYPE, RESPONSE_CSS);
            if (HttpUtil.isKeepAlive(req)) {
                HttpUtil.setKeepAlive(res, true);
            }
            ctx.write(res); // 응답 헤더 전송

            ctx.write(new DefaultFileRegion(raf.getChannel(), 0, raf.length()));

            ChannelFuture f = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!HttpUtil.isKeepAlive(req)) {
                f.addListener(ChannelFutureListener.CLOSE);
            }
        }
        else {
            req.retain();
        }
    }
}
