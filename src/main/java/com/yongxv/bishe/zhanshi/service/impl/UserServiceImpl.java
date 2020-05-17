package com.yongxv.bishe.zhanshi.service.impl;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 09:46 2020-03-20
 * @Modified By :
 */

import com.alibaba.fastjson.JSON;
import com.yongxv.bishe.zhanshi.domain.Goods;
import com.yongxv.bishe.zhanshi.entity.*;
import com.yongxv.bishe.zhanshi.mapper.BisheMapper;
import com.yongxv.bishe.zhanshi.mapper.StoreMapper;
import com.yongxv.bishe.zhanshi.repository.GoodsRepository;
import com.yongxv.bishe.zhanshi.service.UserService;
import com.yongxv.bishe.zhanshi.utils.DateUtil;
import com.yongxv.bishe.zhanshi.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author gyx
 * @date 2020-03-20 09:46
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private BisheMapper bisheMapper;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private StoreMapper storeMapper;

    @Override
    public Response login(String account, String passWord,String role){
        if(StringUtils.isBlank(account) || StringUtils.isBlank(passWord)){
            return Response.error(1,"请输入账号或者密码");
        }else{
            User user = bisheMapper.selectUser(account);
            if(user == null || "".equals(user)){
                return Response.error(1,"用户不存在");
            }else{
                if(user.getPassword().equals(passWord)){
                    if(role.equals(user.getType())){
                        return Response.success(user,"登陆成功");
                    }else {
                        return Response.error(1, "角色选择错误请重新选择");
                    }
                }else {
                    return Response.error(1,"账号或密码错误");
                }
            }
        }
    }


    @Override
    public Response register(String account,String passWord,String type,String storeType,String userName,String introduction,String area){
        if(StringUtils.isBlank(account) || StringUtils.isBlank(passWord)){
            return Response.error(1,"账号或密码不能为空");
        }
        if(account.length() < 6 || passWord.length() < 6){
            return Response.error(1,"账号或和密码长度必需大于5位");
        }
        if(account.length() >15 || passWord.length() > 15){
            return Response.error(1,"账号或和密码长度必需小于16位");
        }
        if(!isNumeric(account)){
            return Response.error(1,"账号只能由纯数字组成");
        }
        User user = bisheMapper.selectUser(account);
        if(user == null || "".equals(user)){
            User user1 = new User();
            String today = DateUtil.getDateTime(DateUtil.getTimePattern(), new Date());
            user1.setAccount(account);
            user1.setPassword(passWord);
            user1.setType(type);
            if(type.equals("2")){
                user1.setStoreType(storeType);
            }else{
                user1.setStoreType("");
            }
            user1.setUserName(userName);
            user1.setIntroduction(introduction);
            user1.setArea(area);
            user1.setCreateTime(today);
            bisheMapper.addUser(user1);
            return Response.success(0,"注册成功");
        }else{
            return Response.error(1,"此账号已存在");
        }

    }

    @Override
    public Response addAdmin(String account,String passWord,String userName,String introduction,String area){
        if(StringUtils.isBlank(account) || StringUtils.isBlank(passWord)){
            return Response.error(1,"账号或密码不能为空");
        }
        if(account.length() < 6 || passWord.length() < 6){
            return Response.error(1,"账号或和密码长度必需大于5位");
        }
        if(account.length() >15 || passWord.length() > 15){
            return Response.error(1,"账号或和密码长度必需小于16位");
        }
        if(!isNumeric(account)){
            return Response.error(1,"账号只能由纯数字组成");
        }
        User user = bisheMapper.selectUser(account);
        if(user == null || "".equals(user)){
            User user1 = new User();
            String today = DateUtil.getDateTime(DateUtil.getTimePattern(), new Date());
            user1.setAccount(account);
            user1.setPassword(passWord);
            user1.setUserName(userName);
            user1.setStoreType("");
            user1.setIntroduction(introduction);
            user1.setArea(area);
            user1.setCreateTime(today);
            bisheMapper.addAdmin(user1);
            return Response.success(0,"添加成功");
        }else{
            return Response.error(1,"此账号已存在");
        }
    }


    @Override
    public Response updateInformation(int id,String userName,String introduction,String area){
        User user = new User();
        user.setId(id);
        user.setUserName(userName);
        user.setIntroduction(introduction);
        user.setArea(area);
        bisheMapper.updateInformation(user);
        User user1 = bisheMapper.selectUserByid(id);
        return Response.success(user1);
    }


    @Override
    public Response updatePassWord(int id,String account,String password,String passwordAgain){
        User user = bisheMapper.selectUserByid(id);
        if(StringUtils.isBlank(password) || StringUtils.isBlank(passwordAgain)){
            return Response.error(1,"密码不可为空");
        }
        if(!user.getAccount().equals(account)){
            return Response.error(1,"账号输入不正确");
        }else if(!password.equals(passwordAgain)){
            return Response.error(1,"两次密码输入不相同");
        }else if(password.length() < 6){
            return Response.error(1,"密码必须大于5位");
        }else if(password.length() > 15){
            return Response.error(1,"密码必须小于16位");
        }else {
            User user1 = new User();
            user1.setId(id);
            user1.setPassword(password);
            bisheMapper.updatePassword(user1);
            return Response.success("","修改成功");
        }

    }

    @Override
    public Response selectUser(int id){
        User user  = bisheMapper.selectUserByid(id);
        return Response.success(user);
    }

    @Override
    public Response focus(int uid,int uuid){
        String today = DateUtil.getDateTime(DateUtil.getTimePattern(), new Date());
        User user  = bisheMapper.selectUserByid(uuid);
        List<Associated> associatedList = new ArrayList<>();
        associatedList = bisheMapper.selectGuaZhu(uid,uuid);
        if(associatedList.size() != 0){
            return Response.error(1,"你已进行关注");
        }else{
            bisheMapper.addFocus(uid,uuid,today);
            bisheMapper.updateFocusCount((Integer.parseInt(user.getFocusCount())+1)+"",uuid);
            return Response.success(1,"关注成功");
        }

    }

    @Override
    public Response deleteFocus(int uid,int uuid){
        List<Associated> associatedList = new ArrayList<>();
        associatedList = bisheMapper.selectGuaZhu(uid,uuid);
        User user  = bisheMapper.selectUserByid(uuid);
        if(associatedList.size() != 0){
            bisheMapper.deleteFocus(uid,uuid);
            bisheMapper.updateFocusCount((Integer.parseInt(user.getFocusCount())-1)+"",uuid);
            return Response.success(0,"取消关注成功");
        }else{
            return Response.error(1,"未进行关注");
        }

    }

    @Override
    public Response selectFocusStore(int uid,int page,int size){
        List<Associated> associatedList = bisheMapper.selectFocus(uid,page*size,size);
        List<User> users = new ArrayList<>();
        for (int i = 0; i <associatedList.size() ; i++) {
            User user  = bisheMapper.selectUserByid(associatedList.get(i).getUuid());
            users.add(user);
        }
        Integer count = bisheMapper.selectFocusCount(uid);
        Tatal tatal = new Tatal();
        tatal.setTotal(count);
        return Response.success(users,tatal);
    }

    @Override
    public Response selectFocusUser(int uuid,int page,int size){
        List<Associated> associatedList = bisheMapper.selectFocusUser(uuid,(page-1)*size,size);
        List<User> users = new ArrayList<>();
        for (int i = 0; i <associatedList.size() ; i++) {
            User user  = bisheMapper.selectUserByid(associatedList.get(i).getUid());
            users.add(user);
        }
        Integer count = bisheMapper.selectFocusUserCount(uuid);
        Tatal tatal = new Tatal();
        tatal.setTotal(count);
        return Response.success(users,tatal);
    }

    @Override
    public Response selectNewUpdate(int page,int size){
        String day = DateUtils.getDayAgoOrAfterString(-3);
        List<UserDto> users = bisheMapper.selectStoreByUpdatetime(day,(page-1)*size,size);
        Integer count = bisheMapper.selectStoreByUpdatetimeCount(day);
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
    public Response selectAcess(int uid,int page,int size){
        List<Access> accessList = bisheMapper.selectAccess(uid,(page-1)*size,size);
        Integer count = bisheMapper.selectAccessCount(uid);
        Tatal tatal = new Tatal();
        tatal.setTotal(count);
        return Response.success(accessList,tatal);
    }

    @Override
    public Response addWarning(int uid,int toUser){
        String content = "您有数据不符合标准请尽快修改，以免封号";
        String today = DateUtil.getDateTime(DateUtil.getTimePattern(), new Date());
        bisheMapper.addWarning(uid,toUser,content,today);
        return Response.success(0);
    }
    @Override
    public Response deleteUser(int id){
        User user = bisheMapper.selectUserByid(id);
        if(user.getType().equals("2")){
            goodsRepository.delete(id+"");
            storeMapper.deleteAccessDian(id);
            storeMapper.deleteAssociatedDian(id);
            storeMapper.deleteContentDian(id);
        }
        if(user.getType().equals("1")){
            storeMapper.deleteAccessRen(id);
            storeMapper.deleteContentRen(id);
            List<Associated> associatedList = storeMapper.selectFocusAll(id);
            for (int i = 0; i <associatedList.size() ; i++) {
                User user1 = bisheMapper.selectUserByid(associatedList.get(i).getUuid());
                bisheMapper.updateFocusCount((Integer.parseInt(user1.getFocusCount())-1)+"",associatedList.get(i).getUuid());
            }
            storeMapper.deleteAssociatedRen(id);
        }
        bisheMapper.deleteUser(id);
        return Response.success(0);
    }

    @Override
    public Response selectStore(int page,int size){
        List<User> userList = bisheMapper.selectStores((page-1)*size,size);
        Integer count = bisheMapper.selectStore();
        Tatal tatal = new Tatal();
        tatal.setTotal(count);
        return Response.success(userList,tatal);
    }

    @Override
    public Response selectConsumers(int page,int size){
        List<User> userList = bisheMapper.selectConsumer((page-1)*size,size);
        Integer count = bisheMapper.selectConsumers();
        Tatal tatal = new Tatal();
        tatal.setTotal(count);
        return Response.success(userList,tatal);
    }



    @Override
    public Response content(int uid,int toUser,String content){
        String today = DateUtil.getDateTime(DateUtil.getTimePattern(), new Date());
        bisheMapper.insertContent(uid,toUser,content,today);
        return Response.success(0);
    }
    @Override
    public Response contentStore(int uid,String content){
        String today = DateUtil.getDateTime(DateUtil.getTimePattern(), new Date());
        List<Associated> associatedList = bisheMapper.selectFocusUserAll(uid);
        for (int i = 0; i <associatedList.size() ; i++) {
            bisheMapper.insertContent(uid,associatedList.get(i).getUid(),content,today);
        }
        return Response.success(0);
    }


    @Override
    public Response pushAndMessage(int id,int page,int size){
        User user = bisheMapper.selectUserByid(id);
        if(user.getType().equals("1")){
            List<Content> contentList =  bisheMapper.message(id,page*size,size);
            Integer count = bisheMapper.messageCount(id);
            Tatal tatal = new Tatal();
            tatal.setTotal(count);
            return Response.success(contentList,tatal);
        }else{
            List<Content> contentList =  bisheMapper.push(id,page*size,size);
            Integer count = bisheMapper.pushCount(id);
            Tatal tatal = new Tatal();
            tatal.setTotal(count);
            return Response.success(contentList,tatal);
        }

    }
    @Override
    public Response receivedPushOrMessage(int id,int page,int size){
        User user = bisheMapper.selectUserByid(id);
        if(user.getType().equals("1")){
            List<Content> contentList =  bisheMapper.receivedPush(id,(page-1)*size,size);
            Integer count = bisheMapper.receivedPushCount(id);
            Tatal tatal = new Tatal();
            tatal.setTotal(count);
            return Response.success(contentList,tatal);
        }else{
            List<Content> contentList =  bisheMapper.receivedMessage(id,(page-1)*size,size);
            Integer count = bisheMapper.receivedMessageCount(id);
            Tatal tatal = new Tatal();
            tatal.setTotal(count);
            return Response.success(contentList,tatal);
        }
    }


    @Override
    public Response deletePushAndMessage(int id,int uid){
        User user = bisheMapper.selectUserByid(uid);
        if(user.getType().equals("1")){
            bisheMapper.updateCustomer(id);
        }else {
            bisheMapper.updateShopState(id);
        }
        return Response.success(0);
    }

    @Override
    public Response selectUserBypage(int id,int page,int size){
        List<User> list = bisheMapper.selectUserByPage(id,(page-1)*size,size);
        Integer count = bisheMapper.selectUserCount(id);
        Tatal tatal = new Tatal();
        tatal.setTotal(count);
        return Response.success(list,tatal);
    }

    @Override
    public Response deleteAccess(int id){
        bisheMapper.deleteAccess(id);
        return Response.success(0,"删除访问记录成功");
    }



    /**
     * @return a
     * @description 计算比例
     * @author gyx
     * @date 2020-03-23 18:5
     * @parameter * @param null
     * @since
     */
    private String efficient(long total, long count) {
        if(total==0||count==0){
            return "0";
        }
        double s = total;
        double c = count;
        double f = c / s;
        return String.format("%.4f", f);
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
