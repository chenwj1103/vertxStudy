package com.ifeng.chen.utils;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

/**
 * Created by Chen Weijie on 2017/5/26.
 */
public class HttpClientUtils extends AbstractVerticle {



    private static final Logger LOGGER= LogManager.getLogger(HttpClientUtils.class);

    private  HttpClient httpClient = null;

    private Vertx vertx;

    private static HttpClientUtils httpClientUtils;

    private HttpClientOptions options = null;

    private HttpClientUtils(Vertx vertx) {
        this.vertx = vertx;
        options = new HttpClientOptions()
                .setConnectTimeout(1000)
                .setKeepAlive(false);
    }


    public static HttpClientUtils getInstance(Vertx vertx) {
        if (httpClientUtils == null) {
            httpClientUtils = new HttpClientUtils(vertx);
        }
        return httpClientUtils;
    }


    private HttpClient getHttpClient() {

        if (httpClient == null) {
            httpClient = this.vertx.createHttpClient(options);
        }
        return httpClient;
    }



    public void setGet(String url, int timeout, Handler<HttpClientResponse> responseHandler ,Handler<? super Throwable> throwaleHandler ){

        HttpClient client=getHttpClient();
        try {
            URL _url=new URL(url);
            int port = _url.getPort() > 0 ? _url.getPort() : _url.getDefaultPort();
            String host=_url.getHost();
            String file=_url.getFile();

        HttpClientRequest request=client.get(port,host,file,responseHandler);

        request.setTimeout(timeout);
        request.exceptionHandler(ex->{
            LOGGER.error("http request error:" + ex.getMessage()+",url:"+url);
            throwaleHandler.handle(ex);
        });
        request.end();
        } catch (MalformedURLException e) {
            LOGGER.error("url is illegal, url=" + url);
        }

    }


    public void sendPost(String header,String url, int timeout, String params, Handler<HttpClientResponse> responseHandler, Handler<? super Throwable> throwableHandler) {
        HttpClient client = getHttpClient();
        try {
            URL _url = new URL(url);

            int port = _url.getPort() > 0 ? _url.getPort() : options.getDefaultPort();

            HttpClientRequest request = client.post(port, _url.getHost(), _url.getFile(), responseHandler);

            request.setTimeout(timeout);
            if (Objects.nonNull(header)) {
                request.putHeader("Content-type", header);
            } else {
                request.putHeader("Content-type", "application/x-www-form-urlencoded");
            }
            request.exceptionHandler(ex -> {
                LOGGER.error("http request error:url:{}params:{}", _url, params, ex);
                throwableHandler.handle(ex);
            });

            request.end(params);


        } catch (MalformedURLException e) {
            LOGGER.error("url is illegal, url=" + url);
        }

    }


    public void setGetWithParams(String url, String params, int timeout, Handler<HttpClientResponse> responseHandler, Handler<? super Throwable> throwaleHandler) {

        HttpClient client = getHttpClient();
        try {
            URL _url = new URL(url);
            int port = _url.getPort() > 0 ? _url.getPort() : _url.getDefaultPort();
            String host = _url.getHost();
            String query = _url.getQuery() == null ? "" : _url.getQuery();

            String file = _url.getPath() + "?" + params + "&" + query;
            HttpClientRequest request = client.get(port, host, file, responseHandler);

            request.setTimeout(timeout);
            request.exceptionHandler(ex -> {
                LOGGER.error("http request error:" + ex.getMessage() + ",url:" + url);
                throwaleHandler.handle(ex);
            });
            request.end();
        } catch (MalformedURLException e) {
            LOGGER.error("url is illegal, url=" + url);
        }

    }















}
