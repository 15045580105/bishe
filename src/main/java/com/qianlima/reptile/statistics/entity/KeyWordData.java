package com.qianlima.reptile.statistics.entity;

import lombok.Data;

/**
 * @description: 关键词数据
 * @author: sx
 * @create: 2020-03-11 11:20:29
 **/
@Data
public class KeyWordData {
    private Integer order;
    private String title;
    private String url;
    private String area;
    private Integer type;
    private Integer status;
}
