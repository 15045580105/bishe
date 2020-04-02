package com.qianlima.reptile.statistics.entity;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author liuchanglin
 * @version 1.0
 * @ClassName: PotNote
 * @Description: pot备注
 * @date 2020/4/1 1:54 下午
 */
@Data
public class PotNote {
    /**
     * 备注id
     */
    private Integer id;
    /**
     * potId
     */
    private Integer cid;
    /**
     * 1 pot 2 url
     */
    private Integer type;
    /**
     * 内容
     */
    private String content;
    /**
     * 编辑人员
     */
    private String who;
    /**
     * 时间
     */
    private Timestamp when;

}
