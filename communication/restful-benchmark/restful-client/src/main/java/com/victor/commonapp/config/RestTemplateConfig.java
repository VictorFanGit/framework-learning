package com.victor.commonapp.config;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(clientHttpRequestFactory());
        restTemplate.setErrorHandler(new ResponseErrorHandler() {    //用于自己处理异常返回
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return true;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {

            }
        });
        return restTemplate;
    }

    @Bean
    public CustomHttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            }).build();
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            System.out.println("Exception occurred when build ssl context.");
            return null;
        }
        httpClientBuilder.setSSLContext(sslContext);
        NoopHostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslConnectionSocketFactory).build();
        //设置连接池
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        poolingHttpClientConnectionManager.setMaxTotal(20000); //最大连接数
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(10000); //同路由并发数
        httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(3, true));
        CloseableHttpClient httpClient = httpClientBuilder.build();
        CustomHttpComponentsClientHttpRequestFactory httpRequestFactory = new CustomHttpComponentsClientHttpRequestFactory(httpClient);
        httpRequestFactory.setConnectTimeout(20000); //连接超时时间
        httpRequestFactory.setReadTimeout(30000);  //数据读取超时时间
        httpRequestFactory.setConnectionRequestTimeout(20000); //连接不够用的等待时间
        return httpRequestFactory;
    }

    //以下两个内部类是为了让GET方法支持传递body字段
    public class CustomHttpComponentsClientHttpRequestFactory extends HttpComponentsClientHttpRequestFactory {

        public CustomHttpComponentsClientHttpRequestFactory(HttpClient httpClient) {
            super(httpClient);
        }

        @Override
        protected HttpUriRequest createHttpUriRequest(HttpMethod httpMethod, URI uri) {
            if(httpMethod == HttpMethod.GET) {
                return new CustomHttpGetRequestWithEntity(uri);
            }
            return super.createHttpUriRequest(httpMethod, uri);
        }

    }

    private static class CustomHttpGetRequestWithEntity extends HttpEntityEnclosingRequestBase {
        public CustomHttpGetRequestWithEntity(final URI uri) {
            super.setURI(uri);
        }

        @Override
        public String getMethod() {
            return HttpMethod.GET.name();
        }
    }
}
