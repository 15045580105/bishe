package com.yongxv.bishe.zhanshi.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class HttpClientUtils {

    /**
     * 访问链接，返回状态码
     *
     * @param url
     * @return
     */
    public static String postHttpClient(String url,String param) {
        Integer code = 0;
        StringEntity postEntity = null;
        try {
           postEntity = new StringEntity(param);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url); // 创建httpget实例
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(3000)
                .setConnectTimeout(10000)
                .setConnectionRequestTimeout(3000)
                .build();
        httpPost.setConfig(requestConfig);
        httpPost.setEntity(postEntity);
        httpPost.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36 OPR/56.0.3051.116");
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost); // 执行http get请求
            code = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity(); // 获取返回实体
            if (code == 200) {
                return EntityUtils.toString(entity, "utf-8");
            }else{
                return  "状态码错误为: "+code;
            }
        } catch (Exception e) {
            if (e.getMessage().contains("connect timed out")) {
                code = 4;
            }
            log.error("getClient is error:" + url);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } // response关闭
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            } // httpClient关闭
        }
        return null;
    }

    /**
     * 访问链接，返回内容
     *
     * @param url
     */
    public static String getHttpClient(String url, Map<String,String> headerMap) {
        long startTime = System.currentTimeMillis()/1000;
        System.out.println("开始时间为"+startTime);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url); // 创建httpget实例
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(10000)
                .setConnectTimeout(10000)
                .setConnectionRequestTimeout(10000).build();
        httpGet.setConfig(requestConfig);
        for(String header:headerMap.keySet()){
            httpGet.setHeader(header,headerMap.get(header));
        }
        httpGet.setHeader("Content-Type","application/json, text/json");
        httpGet.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36 OPR/56.0.3051.116");
        CloseableHttpResponse response = null;
        long end_time = 0;
        Integer code = -1;
        try {
            response = httpClient.execute(httpGet); // 执行http get请求
             code = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity(); // 获取返回实体
            if (code == 200) {
                return EntityUtils.toString(entity, "utf-8");
            }else if(code == 401){
                return "错误"+ EntityUtils.toString(entity, "utf-8");
            }else{
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(code!=200){
                log.info("访问出错，出错url："+url+" ,状态码为："+code);
            }
            end_time = System.currentTimeMillis()/1000;
            System.out.println("结束时间："+end_time);
            log.info("总用时："+(end_time-startTime));
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } // response关闭
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            } // httpClient关闭
        }
        return null;
    }

    /**
     * 访问链接，返回内容
     *
     * @param url
     */
    public static String postHttpClient(String url, Map<String,String> headerMap,String param) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpGet = new HttpPost(url); // 创建httpget实例
        StringEntity postEntity = null;
        try {
            postEntity = new StringEntity(param);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)
                .setConnectTimeout(10000)
                .setConnectionRequestTimeout(30000).build();
        httpGet.setConfig(requestConfig);
        httpGet.setEntity(postEntity);
        for(String header:headerMap.keySet()){
            httpGet.setHeader(header,headerMap.get(header));
        }
        httpGet.setHeader("Content-Type","application/json");
        httpGet.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36 OPR/56.0.3051.116");
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet); // 执行http get请求
            Integer code = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity(); // 获取返回实体
            if (code == 200) {
                return EntityUtils.toString(entity, "utf-8");
            }else if(code == 401){
                return "错误"+ EntityUtils.toString(entity, "utf-8");
            }else{
                return "";
            }
        } catch (Exception e) {
            log.error("getClient is error:" + url);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } // response关闭
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            } // httpClient关闭
        }
        return null;
    }


    /**
     * 访问链接，返回内容
     *
     * @param url
     */
    public static String postHttpClient(String url, Map<String,String> headerMap) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpGet = new HttpPost(url); // 创建httpget实例
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)
                .setConnectTimeout(10000)
                .setConnectionRequestTimeout(30000).build();
        httpGet.setConfig(requestConfig);
        for(String header:headerMap.keySet()){
            httpGet.setHeader(header,headerMap.get(header));
        }
        httpGet.setHeader("Content-Type","application/json, text/json");
        httpGet.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36 OPR/56.0.3051.116");
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet); // 执行http get请求
            Integer code = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity(); // 获取返回实体
            if (code == 200) {
                return EntityUtils.toString(entity, "utf-8");
            }else if(code == 401){
                return "错误"+ EntityUtils.toString(entity, "utf-8");
            }else{
                return "";
            }
        } catch (Exception e) {
            log.error("getClient is error:" + url);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } // response关闭
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            } // httpClient关闭
        }
        return null;
    }
    /**
     * 返回状态码
     * @param rurl
     * @return
     */
    public static Integer getJsoupCode(String rurl) {
        Integer code = 0;
        try {
            if (rurl.startsWith("https")) {
                HttpsUrlValidator.retrieveResponseFromServer(rurl);
            }
            URL url = new URL(rurl);
            URLConnection rulConnection = url.openConnection();
            HttpURLConnection httpUrlConnection = (HttpURLConnection) rulConnection;
            httpUrlConnection.setConnectTimeout(3000);
            httpUrlConnection.setReadTimeout(3000);
            httpUrlConnection.connect();
            code = httpUrlConnection.getResponseCode();
        } catch (Exception e) {
            log.error("getJsoupClient is error");
        }
        return code;
    }


    public static Integer getJsoupClient(String rurl, String ip, String port) {
        Integer code = 0;
        try {
            if (rurl.startsWith("https")) {
                HttpsUrlValidator.retrieveResponseFromServer(rurl);
            }
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, Integer.valueOf(port)));
            URL url = new URL(rurl);
            URLConnection rulConnection = url.openConnection();
            HttpURLConnection httpUrlConnection = (HttpURLConnection) rulConnection;
            httpUrlConnection.setConnectTimeout(3000);
            httpUrlConnection.setReadTimeout(3000);
            httpUrlConnection.connect();
            code = httpUrlConnection.getResponseCode();

        } catch (Exception e) {
            log.error("getJsoupClient is error");
        }
        return code;
    }

    public static void main(String[] args) {
//        log.info(getHttpCode("http://xxgk.nantong.gov.cn/govdiropen/jcms_files/jcms1/web63/site/zfxxgk/search.jsp?showsub=0&orderbysub=0&divid=div1044&binlay=0&urltype=0&showhref=1&cid=6706&jdid=63", "43.226.37.90", "16819") + "");
//        log.info(getHttpClient("http://att.teda.gov.cn","",""));
        Pattern p = Pattern.compile("本网站将于（[\\S]+月[\\S]+日）关闭");
        Matcher m = p.matcher("awdawd本网站将于12（2018年2月三日）关闭");
        if (m.find()) {
            log.info("匹配");
        }
    }

}
