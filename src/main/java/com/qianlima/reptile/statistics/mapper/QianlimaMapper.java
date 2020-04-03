package com.qianlima.reptile.statistics.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qianlima.reptile.statistics.entity.PhpcmsContentDo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @program: qianlima-reptile-statistics-service
 * @description:
 * @author: Zhao Xiaoyang
 * @create: 2019-11-08 16:50
 **/
@Component
@DS("qianlima")
public interface QianlimaMapper extends BaseMapper<Map> {

    @Select("select count(1) from phpcms_content where status = 99 and updatetime between #{startUpdateTime} and #{endUpdateTime}")
    Integer select(@Param("startUpdateTime") int startUpdateTime,@Param("endUpdateTime") int endUpdateTime);

    /**
     * 查询模版单日模版id
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select tmplt from phpcms_content where status = 99 and updatetime between #{startTime} and #{endTime}")
    List<String> selectTmpltIdInTime(@Param("startTime") Long startTime, @Param("endTime") Long endTime);
    /**
     * 查询采集总量
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select count(1) from rawdatas.bidding_raw where intime >= #{startTime} and intime <= #{endTime}")
    long selectCollectCount(@Param("startTime") String startTime,@Param("endTime") String endTime);
    /**
     *查询采集34
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select count(1) from rawdatas.bidding_raw where intime >= #{startTime} and intime <= #{endTime} and status = 34")
    long selectCollect34(@Param("startTime") String startTime,@Param("endTime") String endTime);
    /**
     *查询采集50
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select count(1) from rawdatas.bidding_raw where intime >= #{startTime} and intime <= #{endTime} and status = 50")
    long selectCollect50(@Param("startTime") String startTime,@Param("endTime") String endTime);
    /**
     *查询发布总量
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select count(1) from phpcms_content where updatetime >= #{startTime} and updatetime <= #{endTime} and status = 99")
    long selecRelease(@Param("startTime") String startTime,@Param("endTime") String endTime);
    /**
     *查询人工发布量
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select count(1) from phpcms_content where updatetime >= #{startTime} and updatetime <= #{endTime} and username != 'root' and status = 99")
    long selectReleaseUser(@Param("startTime") String startTime,@Param("endTime") String endTime);
    /**
     *查询项目发布量
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select count(1) from phpcms_content where updatetime >= #{startTime} and updatetime <= #{endTime} and catid = 101 and status = 99")
    long selectReleaseProject(@Param("startTime") String startTime,@Param("endTime") String endTime);

    /**
     * @return a
     * @description 一个pot下近他一个月发布量
     * @author gyx
     * @date 2020-04-01 15:1
     * @parameter * @param null
     * @since
     */
    @Select({
            "<script>" +
                    "select " +
                    "COUNT(1) " +
                    "from  phpcms_content " +
                    "where status = 99 and tmplt in " +
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>"+
                    "#{id}" +
                    "</foreach>"+
                    "and  updatetime> #{startTime} and updatetime &lt; #{endTime}"+
                    "</script>"
    })
    Integer selectPhpcmsCountsByIds(@Param("ids") List<String> ids, @Param("startTime") Long startTime, @Param("endTime") Long endTime);


    @Select("select count(1) as count ,tmplt as tmplt from phpcms_content where updatetime >= #{startTime} and updatetime <= #{endTime}  and status = 99 group by tmplt")
    List<PhpcmsContentDo> selectPhpcmsCounts(@Param("startTime") String startTime, @Param("endTime") String endTime);
    /**
     * @return a
     * @description 一个pot下近他一个月采集量
     * @author gyx
     * @date 2020-04-01 15:1
     * @parameter * @param null
     * @since
     */
    @Select({
            "<script>" +
                    "select " +
                    "COUNT(1) " +
                    "from rawdatas.bidding_raw " +
                    "where ae_template in " +
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>"+
                    "#{id}" +
                    "</foreach>"+
                    "and  intime> #{startTime} and intime &lt; #{endTime}"+
                    "</script>"
    })
    Integer selectBiddingRawByIds(@Param("ids") List<String> ids, @Param("startTime") Long startTime, @Param("endTime") Long endTime);

    @Select("select count(1) as count,ae_template as tmplt from rawdatas.bidding_raw where intime >= #{startTime} and intime <= #{endTime} group by ae_template")
    List<PhpcmsContentDo> selectBiddingRaw(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * @return a
     * @description 单模版时间内采集量
     * @author gyx
     * @date 2020-04-02 21:5
     * @parameter * @param null
     * @since
     */
    @Select("select count(1) from rawdatas.bidding_raw where intime >= #{startTime} and intime <= #{endTime} and ae_template = #{id}")
    Integer selectBiddingById(@Param("startTime") long startTime, @Param("endTime") long endTime,@Param("id") String id);

    /**
     * @return a
     * @description 单模版时间内发布量
     * @author gyx
     * @date 2020-04-02 21:4
     * @parameter * @param null
     * @since
     */
    @Select("select count(1) from phpcms_content where updatetime >= #{startTime} and updatetime <= #{endTime}  and status = 99 and tmplt = #{id}")
    Integer selectPhpcmsById(@Param("startTime") long startTime, @Param("endTime") long endTime,@Param("id") String id);
}
