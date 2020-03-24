package com.qianlima.reptile.statistics.service;

import com.qianlima.reptile.statistics.constant.ResultCode;
import com.qianlima.reptile.statistics.entity.Response;
import com.qianlima.reptile.statistics.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BaseService {

    private static final Logger logger = LoggerFactory.getLogger(BaseService.class);

    protected Response validateTime(String startTime, String endTime){
        if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)){
            return Response.error(ResultCode.PARAM_NOT_BLANK.getCode(),ResultCode.PARAM_NOT_BLANK.getMsg());
        }
        return Response.success(null);
    }

    /**
     * 获取某个时间段内的月份
     * @param startTime
     * @param endTime
     * @return
     */
    protected List<String> getMonths(String startTime, String endTime){
        //取查询月份之间的所有月份
        List<String> listMonth= new ArrayList<>();
        try {
             listMonth = DateUtils.getMonths(startTime, endTime);
            return listMonth;
        }catch (Exception e){
            logger.info("fillMonth has error e ={}",e);
        }
        return listMonth;
    }
}
