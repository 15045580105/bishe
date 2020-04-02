package com.qianlima.reptile.statistics.service;

import com.qianlima.reptile.statistics.entity.Response;

public interface PotDetailsService {
    Response getPotDetails(Integer potId, String states);
}
