package com.qianlima.reptile.statistics.service;

import com.qianlima.reptile.statistics.constant.ResultCode;
import com.qianlima.reptile.statistics.entity.Response;
import org.apache.commons.lang3.StringUtils;

public class BaseService {

    protected Response validateTime(String startTime, String endTime){
        if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)){
            return Response.error(ResultCode.PARAM_NOT_BLANK.getCode(),ResultCode.PARAM_NOT_BLANK.getMsg());
        }
        return Response.success(null);
    }
}
