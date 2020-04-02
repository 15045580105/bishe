package com.qianlima.reptile.statistics.entity;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 15:57 2020-04-02
 * @Modified By :
 */

import com.qianlima.reptile.statistics.domain.PotInformation;
import lombok.Data;

import java.util.List;

/**
 * @author gyx
 * @date 2020-04-02 15:57
 */
@Data
public class PotInformationDto {
    /**
     * 查询时间  格式 2020-02-06
     */
    private String queryDate;
    /**
     * 链接
     */
    private String pot;
    /**
     *模版数
     */
    private long templateNumber;
    /**
     *pot状态
     */
    private String state;
    /**
     *近30天采集量
     */
    private long collectNumber;
    /**
     *近30天发布量
     */
    private long releaseNumber;
    /**
     *创建时间
     */
    private String createTime;
    /**
     *修改时间
     */
    private String updateTime;
    /**
     *完全重复pot数
     */
    private String repeatPotCount;
    /**
     *完全重复pot
     */
    private List<PotInformation> children;

}
