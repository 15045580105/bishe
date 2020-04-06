package com.qianlima.reptile.statistics.service;

import com.qianlima.reptile.statistics.entity.Response;

public interface PotDetailsService {
    Response getPotDetails(String potName, String states,String startTime);
}
