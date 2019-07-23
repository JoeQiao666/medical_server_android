package com.medical.waste.http.interceptor;

import com.socks.library.KLog;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class LogInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        try {
            RequestBody requestBody = request.body();
            String bodyStr = "null";
            if (requestBody != null) {
                Buffer bufferRequest = new Buffer();
                requestBody.writeTo(bufferRequest);
                Charset charsetRequest = Charset.forName("UTF-8");
                MediaType contentTypeRequest = requestBody.contentType();
                if (contentTypeRequest != null) {
                    charsetRequest = contentTypeRequest.charset(charsetRequest);
                }
                if (isPlaintext(bufferRequest)) {
                    bodyStr = bufferRequest.readString(charsetRequest);
                }
            }
            final Response response = chain.proceed(request);

            KLog.e("请求网址: \n" + request.url() + " \n " + "请求头部信息：\n" + request.headers() + "请求body信息：\n" + bodyStr + "\n响应头部信息：\n" + response.headers());

            final ResponseBody responseBody = response.body();
            if (responseBody == null) {
                return response;
            }
            final long contentLength = responseBody.contentLength();
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(charset);
                } catch (UnsupportedCharsetException e) {
                    KLog.e("Couldn't decode the response body; charset is likely malformed.");
                }
            }

            if (contentLength != 0) {
                KLog.e("--------------------------------------------开始打印返回数据----------------------------------------------------");
                KLog.e(buffer.clone().readString(charset));
                KLog.e("--------------------------------------------结束打印返回数据----------------------------------------------------");
            }
            return response;
        } catch (Exception e) {
            KLog.e("打印出错,请求网址: \n" + request.url());
            KLog.e(e == null ? "null" : e.toString());
        }
        return chain.proceed(chain.request());
    }

    public static boolean isPlaintext(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }
}
