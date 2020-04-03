package com.qianlima.reptile.statistics.entity;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 20:20 2020-04-02
 * @Modified By :
 */

import lombok.Data;

/**
 * @author gyx
 * @date 2020-04-02 20:20
 */
@Data
public class CrawlconfigDo {

    private String id;

    private String user;

    private String potName;

    private String isXm;

    private String state;

    private String cat;

    private String createTime;

    private String updateTime;

    private String collectStrategy;

    private String configData;

}
