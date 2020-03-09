package com.qianlima.reptile.statistics.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qianlima.reptile.statistics.utils.HrefSuffixConfig;
import com.qianlima.reptile.statistics.utils.HttpClientUtils;
import com.qianlima.reptile.statistics.utils.ThreadPoolSize;
import com.qianlima.reptile.statistics.utils.TypeContent;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: bazhuaya
 * @description:
 * @author: Zhao Xiaoyang
 * @create: 2019-06-18 10:59
 **/
@Component
public class BazhuayuTask {
    private String username = "15210787003";


    private String password = "abc15210787003";


    public static String token = "";

    public static String refresh_token = "";

//

    /**
     * 获取任务组中的任务
     *
     * @param map1
     */
    public List<Map<String, String>> getTasks(Map<Integer, String> map1) {
        List<Map<String, String>> list = new ArrayList<>();
        for (Integer taskId : map1.keySet()) {
            //"1936232" -> "8-30-冯"
            /*if(taskId!=1936232){
                continue;
            }*/
            String url = getUrl("api/Task?taskGroupId=" + taskId);
            String result = HttpClientUtils.getHttpClient(url, getHeaders());
            if(result.contains("错误")){
                refreshToken();
                result = HttpClientUtils.getHttpClient(url, getHeaders());
            }
            try {
                JSONArray jsonArray = JSONObject.parseObject(result).getJSONArray("data");
                Iterator<Object> iterator = jsonArray.iterator();
                while (iterator.hasNext()) {
                    Map<String, String> map = new HashMap<>();
                    JSONObject jsonObject = (JSONObject) iterator.next();
                    map.put(jsonObject.getString("taskId"), jsonObject.getString("taskName"));
                    map.put("taskId", jsonObject.getString("taskId"));
                    map.put("taskName", jsonObject.getString("taskName"));
                    map.put("taskGroupName", map1.get(taskId));
                    list.add(map);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }


    public String getUrl(String url) {
        String baseUrl = "https://advancedapi.bazhuayu.com/";
        return baseUrl + url;
    }


    /**
     * 获得token
     *
     * @return
     */
    public void getToken() {
        String url = getUrl("token");
        String tokenString = HttpClientUtils.postHttpClient(url, "username=" + username + "&password=" + password + "&grant_type=password");
        System.err.println(tokenString);
        JSONObject jsonObject = JSONObject.parseObject(tokenString);
        token = jsonObject.getString("access_token");
        refresh_token = jsonObject.getString("refresh_token");
    }



    /**
     * 刷新token
     */
    public void refreshToken() {
        String url = getUrl("token");
        String tokenString = HttpClientUtils.postHttpClient(url, "refresh_token=" + refresh_token + "&grant_type=refresh_token");
        JSONObject jsonObject = JSONObject.parseObject(tokenString);
        token = jsonObject.getString("access_token");
        refresh_token = jsonObject.getString("refresh_token");
    }

    /**
     * 获取任务组
     *
     * @return
     */
    public Map<Integer, String> getAllTaskGroup() {
        List<Integer> list = new ArrayList<>();
        Map<Integer, String> map = new HashMap<>();
        String url = getUrl("api/TaskGroup");
        String result = HttpClientUtils.getHttpClient(url, getHeaders());
        if (result.contains("错误")) {
            refreshToken();
            result = HttpClientUtils.getHttpClient(url, getHeaders());
        }
            JSONArray jsonArray = JSONObject.parseObject(result).getJSONArray("data");
            Iterator<Object> jsonIt = jsonArray.iterator();
            while (jsonIt.hasNext()) {
                JSONObject o = (JSONObject) jsonIt.next();
                map.put(o.getIntValue("taskGroupId"), o.getString("taskGroupName"));
            }

        return map;
    }

    public Map<String, String> getHeaders() {
        Map<String, String> map = new HashMap<>();
        map.put("Authorization", "bearer " + token);
        return map;
    }


}
