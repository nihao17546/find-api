package com.appcnd.find.api.util;

import com.appcnd.find.api.exception.FindException;
import com.appcnd.find.api.exception.FindRuntimeException;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * Created by nihao on 17/7/18.
 */
public class HttpClientUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtils.class);

    private static final int timeOut = 10 * 1000 * 6;
    private static final int maxTotal = 200;
    private static final int maxPerRoute = 40;
    private static CloseableHttpClient httpClient = null;
    private final static Object syncLock = new Object();

    public static void config(HttpRequestBase httpRequestBase) {
        // 设置Header等
        // httpRequestBase.setHeader("User-Agent", "Mozilla/5.0");
        // httpRequestBase
        // .setHeader("Accept",
        // "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        // httpRequestBase.setHeader("Accept-Language",
        // "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");// "en-US,en;q=0.5");
        // httpRequestBase.setHeader("Accept-Charset",
        // "ISO-8859-1,utf-8,gbk,gb2312;q=0.7,*;q=0.7");

        // 配置请求的超时设置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(timeOut)
                .setConnectTimeout(timeOut).setSocketTimeout(timeOut).build();
        httpRequestBase.setConfig(requestConfig);
    }

    public static void config(HttpRequestBase httpRequestBase, int time_out) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(time_out)
                .setConnectTimeout(time_out).setSocketTimeout(time_out).build();
        httpRequestBase.setConfig(requestConfig);
    }

    private static void hand(String url){
        String hostname = url.split("/")[2];
        int port = 80;
        if (hostname.contains(":")) {
            String[] arr = hostname.split(":");
            hostname = arr[0];
            port = Integer.parseInt(arr[1]);
        }
    }

    /**
     * 获取HttpClient对象
     * @return
     */
    public static CloseableHttpClient getHttpClient() {
        if(httpClient == null){
            synchronized (syncLock){
                if(httpClient == null){
                    httpClient = createHttpClient();
                }
            }
        }
        return httpClient;
    }

    /**
     * 创建HttpClient对象
     * @return
     */
    private static CloseableHttpClient createHttpClient() {
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory
                .getSocketFactory();
        LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory
                .getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder
                .<ConnectionSocketFactory> create().register("http", plainsf)
                .register("https", sslsf).build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(
                registry);
        // 将最大连接数增加
        cm.setMaxTotal(maxTotal);
        // 将每个路由基础的连接增加
        cm.setDefaultMaxPerRoute(maxPerRoute);

        // 将目标主机的最大连接数增加
//        HttpHost httpHost = new HttpHost(hostname, port);
//        cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);

        // 请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception,
                                        int executionCount, HttpContext context) {
                if (executionCount >= 5) {// 如果已经重试了5次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// SSL握手异常
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext
                        .adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .setRetryHandler(httpRequestRetryHandler).build();

        return httpClient;
    }

    public static String doPost(String url, Map<String,Object> param) throws FindException {
        HttpPost httpPost = new HttpPost(url);
        if (param != null && !param.isEmpty()) {
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            for (String key : param.keySet()) {
                Object value = param.get(key);
                if (value == null) {
                    continue;
                }
                ContentBody contentBody;
                if (value instanceof File) {
                    contentBody = new FileBody((File) value);
                }
                else if (value instanceof InputStream) {
                    contentBody = new InputStreamBody((InputStream) value, ContentType.DEFAULT_BINARY);
                }
                else if (value instanceof byte[]) {
                    contentBody = new ByteArrayBody((byte[]) value, key);
                }
                else {
                    contentBody = new StringBody(value.toString(), ContentType.create(
                            "text/plain", Consts.UTF_8));
                }
                entityBuilder.addPart(key, contentBody);
            }
            httpPost.setEntity(entityBuilder.build());
        }
        CloseableHttpClient httpClient = getHttpClient();
        try (CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
            if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new FindException("response code " + httpResponse.getStatusLine().getStatusCode() +
                        ",reason phrase" + httpResponse.getStatusLine().getReasonPhrase());
            }
            HttpEntity entity = httpResponse.getEntity();
            String resultStr = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
            return resultStr;
        } catch (ClientProtocolException e) {
            LOGGER.error("http post error,url is {}", url, e);
            throw new FindRuntimeException(e);
        } catch (IOException e) {
            LOGGER.error("http post error,url is {}", url, e);
            throw new FindRuntimeException(e);
        }
    }
}
