package com.qianlima.reptile.statistics.entity;

import lombok.Data;

/**
 * @author liuchanglin
 * @version 1.0
 * @ClassName: PotDetail
 * @date 2020/4/1 1:46 下午
 */
@Data
public class PotDetail {
    /**
     * potId
     */
    private Integer id;
    /**
     * 网站名称
     */
    private String name;
    /**
     * pot
     */
    private String domain;
    /**
     * 地区
     */
    private String area;

    /**
     * 创建者
     */
    private String creater;
    /**
     * 状态
     */
    private String states;


}
