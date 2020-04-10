package com.qianlima.reptile.statistics.entity;

import lombok.Data;

/**
 * @author liuchanglin
 * @version 1.0
 * @ClassName: PotManageTmplReq
 * @date 2020/4/10 11:07 上午
 */
@Data
public class PotManageTmplReq {

    private Integer id;
    private Integer state;
    /**
     * -2=未配置；-1=删除；0=待审; 1=正常
     */
    private String cat;

    private String startTime;
    private String endTime;

    private Integer page;
    private Integer size;

    private String sortField;
    private String sortMode;
}
