package com.qianlima.reptile.statistics.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author liuchanglin
 * @version 1.0
 * @ClassName: PotDetailResponse
 * @date 2020/4/1 6:33 下午
 */
@Data
public class PotDetailResponse {
    private PotDetail potDetail;
    private PotNote potNote;
    private Map<String, ReleaseAndCollectCount> releaseAndCollectCountMap;
    private List<TemplateInfo> templateInfos;
    private List<String> associatedPots;
}
