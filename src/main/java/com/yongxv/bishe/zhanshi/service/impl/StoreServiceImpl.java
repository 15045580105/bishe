package com.yongxv.bishe.zhanshi.service.impl;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 14:45 2020-05-02
 * @Modified By :
 */

import com.alibaba.fastjson.JSON;
import com.yongxv.bishe.zhanshi.domain.Goods;
import com.yongxv.bishe.zhanshi.entity.*;
import com.yongxv.bishe.zhanshi.mapper.BisheMapper;
import com.yongxv.bishe.zhanshi.mapper.StoreMapper;
import com.yongxv.bishe.zhanshi.repository.GoodsRepository;
import com.yongxv.bishe.zhanshi.service.StoreService;
import com.yongxv.bishe.zhanshi.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author gyx
 * @date 2020-05-02 14:45
 */
@Service
public class StoreServiceImpl implements StoreService {


    @Autowired
    private StoreMapper storeMapper;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private BisheMapper bisheMapper;


    @Value("${file.path.absolute}")
    private String imagePath;

    @Value("${file.path.relative}")
    private String staticPath;

    @Override
    public Response showStoreService(String storeType, int page, int size) {
        List<UserDto> users = storeMapper.selectStore(storeType, (page-1) * size, size);
        for (int i = 0; i < users.size(); i++) {
            Goods goods = goodsRepository.queryOneByUid(users.get(i).getId() + "");
            if(goods == null || "".equals(goods)) {
                users.get(i).setPicture("");
            }else {
                users.get(i).setPicture(goods.getPicture());
            }
        }
        Integer count = storeMapper.selectStoreCount(storeType);
        Tatal tatal = new Tatal();
        tatal.setTotal(count);
        return Response.success(users, tatal);
    }

    @Override
    public Response showGoods(int uid, int uuid, Integer page, Integer size) {
        String today = DateUtil.getDateTime(DateUtil.getTimePattern(), new Date());
        User user = bisheMapper.selectUserByid(uuid);
        List<Goods> goodsList = goodsRepository.queryByUid(uuid + "", (page-1), size);
        long count = goodsRepository.queryCount(uuid + "");
        if(uid == 0) {
            Tatal tatal = new Tatal();
            tatal.setTotal(Integer.parseInt(count + ""));
            return Response.success(goodsList, tatal);
        }else{
            bisheMapper.addAccess(uid, uuid, user.getUserName(), user.getIntroduction(), user.getArea(), today);
            List<Associated> associatedList = bisheMapper.selectGuaZhu(uid, uuid);
            if (associatedList.size() == 0) {
                Tatal tatal = new Tatal();
                tatal.setTotal(Integer.parseInt(count + ""));
                tatal.setFocus(0);
                return Response.success(goodsList, tatal);
            } else {
                Tatal tatal = new Tatal();
                tatal.setTotal(Integer.parseInt(count + ""));
                tatal.setFocus(1);
                return Response.success(goodsList, tatal);
            }
        }

    }

    @Override
    public Response showGoodsStore(int uid, Integer page, Integer size) {
        List<Goods> goodsList = goodsRepository.queryByUid(uid + "", (page-1), size);
        long count = goodsRepository.queryCount(uid + "");
        Tatal tatal = new Tatal();
        tatal.setTotal(Integer.parseInt(count + ""));
        return Response.success(goodsList, tatal);
    }


    @Override
    public Response delete(String id) {
        goodsRepository.delete(id);
        return Response.success(0);
    }

    @Override
    public Response updateGoods(String id, String name, String introduction, String price) {
        String today = DateUtil.getDateTime(DateUtil.getTimePattern(), new Date());
        Goods goods = new Goods();
        goods.setId(id);
        goods.setName(name);
        goods.setIntroduction(introduction);
        goods.setUpdateTime(today);
        if (StringUtils.isBlank(price)) {
            return Response.error(1, "请输入价格");
        }
        if (isNumeric(price)) {
            if (Integer.parseInt(price) >= 0 && Integer.parseInt(price) <= 20000) {
                goods.setPrice(Long.parseLong(price));
            } else {
                return Response.error(1, "价格不可小于0或者大于20000");
            }
        } else {
            return Response.error(1, "价格只能是数字");
        }
        goodsRepository.update(goods);
        Goods goods1 = goodsRepository.queryById(id);
        User user = new User();
        user.setId(Integer.parseInt(goods1.getUid()));
        user.setUpdateTime(today);
        bisheMapper.updateUpdateTime(user);
        return Response.success(0);
    }

    @Override
    public Response addGoods(String uid, String name, String introduction, String type, String price, MultipartFile file) {
        if (file == null || "".equals(file)) {
            return Response.error(1, "请插入图片");
        }
        User user = bisheMapper.selectUserByid(Integer.parseInt(uid));
        String today = DateUtil.getDateTime(DateUtil.getTimePattern(), new Date());
        Goods goods = new Goods();
        String url = "http://47.94.201.4:9002" + uploadImg(file);
        goods.setUid(uid);
        goods.setPicture(url);
        if (StringUtils.isBlank(price)) {
            return Response.error(1, "请输入价格");
        }
        if (isNumeric(price)) {
            if (Integer.parseInt(price) >= 0 && Integer.parseInt(price) <= 20000) {
                goods.setPrice(Integer.parseInt(price));
            } else {
                return Response.error(1, "价格不可小于0或者大于20000");
            }
        } else {
            return Response.error(1, "价格只能是数字");
        }
        goods.setIntroduction(introduction);
        goods.setType(type);
        goods.setName(name);
        goods.setStoreType(user.getStoreType());
        goods.setCreateTime(today);
        goods.setUpdateTime(today);
        goodsRepository.save(goods);
        user.setUpdateTime(today);
        bisheMapper.updateUpdateTime(user);
        return Response.success(0, "添加商品成功");
    }

    @Override
    public Response selectGoodsById(String id) {
        Goods goods = goodsRepository.queryById(id);
        return Response.success(goods);
    }

    @Override
    public Response selectStoreNew(int page, int size) {
        List<UserDto> users = storeMapper.selectNewStore((page-1) * size, size);
        for (int i = 0; i < users.size(); i++) {
            Goods goods = goodsRepository.queryOneByUid(users.get(i).getId() + "");
            if(goods == null || "".equals(goods)) {
                users.get(i).setPicture("");
            }else {
                users.get(i).setPicture(goods.getPicture());
            }
        }
        return Response.success(users);
    }

    @Override
    public Response selectStoreHot(int page, int size) {
        List<UserDto> users = storeMapper.selectHot((page-1) * size, size);
        Integer count = bisheMapper.selectStore();
        for (int i = 0; i < users.size(); i++) {
            Goods goods = goodsRepository.queryOneByUid(users.get(i).getId() + "");
            if(goods == null || "".equals(goods)) {
                users.get(i).setPicture("");
            }else {
                users.get(i).setPicture(goods.getPicture());
            }
        }
        Tatal tatal = new Tatal();
        tatal.setTotal(count);
        return Response.success(users,tatal);
    }

    @Override
    public Response selectGoods(int page,int size){
        List<Goods> goodsList = goodsRepository.queryGoods((page-1), size);
        long count = goodsRepository.queryAllCount();
        Tatal tatal = new Tatal();
        tatal.setTotal(Integer.parseInt(count + ""));
        return Response.success(goodsList, tatal);
    }



    private String uploadImg(MultipartFile file) {
        String arrUrl = upload(file, imagePath, staticPath);
        return arrUrl;
    }


    private static String upload(MultipartFile file, String absolutepath, String relativepath) {
        if (file.isEmpty()) {
            return "";
        }
        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        fileName = UUID.randomUUID() + suffixName; // 新文件名
        File dest = new File(absolutepath + "/" + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return relativepath + fileName;
    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


}
