package com.qianlima.reptile.statistics.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @program: qianlima-reptile-selfcheck-service
 * @description:
 * @author: Zhao Xiaoyang
 * @create: 2019-08-05 21:58
 **/
@Component
public class HttpRequestUtil {

//    @Value("${ip_proxy_url}")
    private String proxyUrl = "http://10.10.10.33:8686/proxy/get?channel=001&size=1";
    @Resource
    private DriverUtil driverUtil;

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestUtil.class);

    /**
     * 获取代理ip
     * @return
     */
    public String getProxyIp(){
        Document doc = null;
        try {
            doc = Jsoup.connect(proxyUrl).ignoreContentType(true).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc.text();
    }
    public Map<String,Object> getHttpDriverCodeAndContent(String url, int use_proxy,WebDriver webDriver){
        int httpCode = 0;
        Map<String,Object> map =  new HashMap<>(2);
        if(webDriver==null){
            webDriver = driverUtil.getDriver(webDriver,use_proxy);
        }
        String source = null;
        try {
            webDriver.get(url);
            source = Jsoup.parse(webDriver.getPageSource()).text();
            if("".equals(source)){
                Thread.sleep(5000);
                source = Jsoup.parse(webDriver.getPageSource()).text();
            }
                LogEntries logs = webDriver.manage().logs().get("performance");
                Iterator<LogEntry> iterator = logs.iterator();
                while (iterator.hasNext()){
                    LogEntry entry = iterator.next();
                    try {
                        JSONObject json = JSON.parseObject(entry.getMessage());
                        JSONObject message = json.getJSONObject("message");
                        String method = message.getString("method");
                        if (method != null&& "Network.responseReceived".equals(method)) {
                            JSONObject params = message.getJSONObject("params");
                            JSONObject response = params.getJSONObject("response");
                            String messageUrl = response.getString("url");
                            String urlEscapeHttp = "";
                            String messageUrlEscapeHttp = "";
                            if(messageUrl.startsWith("http:")){
                                messageUrlEscapeHttp = messageUrl.replace("http:","");
                            }else{
                                messageUrlEscapeHttp = messageUrl.replace("https:","");
                            }
                            if(url.startsWith("http:")){
                                urlEscapeHttp = url.replace("http:","");
                            }else{
                                urlEscapeHttp = url.replace("https:","");
                            }
                            if (urlEscapeHttp.equals(messageUrlEscapeHttp)) {
                                httpCode = response.getInteger("status");
                                if(httpCode==200){
                                    break;
                                }
                            }
                        }
                    } catch (JSONException e) {
                        LOGGER.error(e.getMessage());
                    }
                }
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }finally {
            if(webDriver!=null){
                webDriver.close();
                webDriver.quit();
            }
        }
        map.put("httpCode",httpCode);
        map.put("content",source);
        return map;
    }

}
