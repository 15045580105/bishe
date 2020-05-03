package com.yongxv.bishe.zhanshi.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @program: fault-tmplt
 * @description:
 * @author: Zhao Xiaoyang
 * @create: 2019-06-14 09:34
 **/
@Slf4j
public class ThreadPoolSize {

    /**
     * 获取当前线程数
     * @return
     */
    public static Integer getActiveCount(ExecutorService executorService) {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executorService;
        Integer activeCount = threadPoolExecutor.getActiveCount();
        Long taskCount = threadPoolExecutor.getTaskCount();
        Long completedTaskCount = threadPoolExecutor.getCompletedTaskCount();
        log.info("【总任务数】:" + taskCount + ",【线程活跃数】:" + activeCount + ",【已完成任务数】:" + completedTaskCount);
        return activeCount;
    }
}
