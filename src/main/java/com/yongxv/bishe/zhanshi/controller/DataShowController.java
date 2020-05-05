package com.yongxv.bishe.zhanshi.controller;


import com.yongxv.bishe.zhanshi.entity.Response;
import com.yongxv.bishe.zhanshi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/bishe")
public class DataShowController {
    @Autowired
    private UserService userService;


    /**
     * @return a
     * @description 登陆
     * @author gyx
     * @date 2020-05-01 22:4
     * @parameter * @param null
     * @since
     */
    @PostMapping("/user/login")
    public Response login(String account, String passWord) {
        return userService.login(account,passWord);
    }

    /**
     * @return a
     * @description 注册
     * @author gyx
     * @date 2020-05-01 22:3
     * @parameter * @param null
     * @since
     */
    @PostMapping(path = "/user/register")
    public Response register(String account,String passWord,String type,String storeType,String userName,String introduction,String area) {
        Response response = userService.register(account,passWord,type,storeType,userName,introduction,area);
        return response;
    }

    /**
     * @return a
     * @description 根据id查人
     * @author gyx
     * @date 2020-05-02 18:1
     * @parameter * @param null
     * @since
     */
    @PostMapping(path = "/user/select")
    public Response selectUser(int id) {
        Response response = userService.selectUser(id);
        return response;
    }

    /**
     * @return a
     * @description 更新用户信息
     * @author gyx
     * @date 2020-05-02 18:1
     * @parameter * @param null
     * @since
     */
    @PostMapping(path = "/user/update")
    public Response updateUser(int id,String userName,String introduction,String area) {
        Response response = userService.updateInformation(id,userName,introduction,area);
        return response;
    }

    /**
     * @return a
     * @description 修改密码
     * @author gyx
     * @date 2020-05-02 18:15
    * @parameter * @param null
     * @since
     */
    @PostMapping(path = "/password/update")
    public Response updatePassword(int id,String account,String password,String passwordAgain) {
        Response response = userService.updatePassWord(id,account,password,passwordAgain);
        return response;
    }


    /**
     * @return a
     * @description 关注店铺
     * @author gyx
     * @date 2020-05-02 18:1
     * @parameter * @param null
     * @since
     */
    @PostMapping(path = "/add/focus")
    public Response addFocus(int uid,int uuid) {
        Response response = userService.focus(uid,uuid);
        return response;
    }

    /**
     * @return a
     * @description 取消关注
     * @author gyx
     * @date 2020-05-02 18:1
     * @parameter * @param null
     * @since
     */
    @PostMapping(path = "/delete/focus")
    public Response deleteFocus(int uid,int uuid) {
        Response response = userService.deleteFocus(uid,uuid);
        return response;
    }


    /**
     * @return a
     * @description 查询关注店铺
     * @author gyx
     * @date 2020-05-02 18:1
     * @parameter * @param null
     * @since
     */
    @PostMapping(path = "/store/focus")
    public Response storeFocus(int uid,int page,int size) {
        Response response = userService.selectFocusStore(uid,page,size);
        return response;
    }

    /**
     * @return a
     * @description 查询关注店铺的人
     * @author gyx
     * @date 2020-05-02 18:1
     * @parameter * @param null
     * @since
     */
    @PostMapping(path = "/user/focus")
    public Response userFocus(int uuid,int page,int size) {
        Response response = userService.selectFocusUser(uuid,page,size);
        return response;
    }

    /**
     * @return a
     * @description 当天更新店铺
     * @author gyx
     * @date 2020-05-02 18:1
     * @parameter * @param null
     * @since
     */
    @PostMapping(path = "/new/update")
    public Response newUpdate() {
        Response response = userService.selectNewUpdate();
        return response;
    }

    /**
     * @return a
     * @description 查看访问记录
     * @author gyx
     * @date 2020-05-02 18:1
     * @parameter * @param null
     * @since
     */
    @PostMapping(path = "/acess")
    public Response acess(int uid,int page,int size) {
        Response response = userService.selectAcess(uid,page,size);
        return response;
    }

    /**
     * @return a
     * @description 发送警告
     * @author gyx
     * @date 2020-05-02 18:1
     * @parameter * @param null
     * @since
     */
    @PostMapping(path = "/add/Warning")
    public Response addWarning(int uid,int toUser) {
        Response response = userService.addWarning(uid,toUser);
        return response;
    }

    /**
     * @return a
     * @description 禁用用户
     * @author gyx
     * @date 2020-05-02 18:1
     * @parameter * @param null
     * @since
     */
    @PostMapping(path = "/delete/user")
    public Response deleteUser(int id) {
        Response response = userService.deleteUser(id);
        return response;
    }

    /**
     * @return a
     * @description 查询
     * @author gyx
     * @date 2020-05-02 18:2
     * @parameter * @param null
     * @since
     */
    @PostMapping(path = "/select/store")
    public Response selectStore(int page,int size) {
        Response response = userService.selectStore(page,size);
        return response;
    }

    /**
     * @return a
     * @description 查询消费者
     * @author gyx
     * @date 2020-05-02 18:0
     * @parameter * @param null
     * @since
     */
    @PostMapping(path = "/select/consumers")
    public Response selectConsumers(int page,int size) {
        Response response = userService.selectConsumers(page,size);
        return response;
    }

    /**
     * @return a
     * @description 留言
     * @author gyx
     * @date 2020-05-02 19:15
    * @parameter * @param null
     * @since
     */
    @PostMapping(path = "/content")
    public Response content(int uid,int toUser,String content) {
        Response response = userService.content(uid,toUser,content);
        return response;
    }

    /**
     * @return a
     * @description 推送
     * @author gyx
     * @date 2020-05-02 19:1
     * @parameter * @param null
     * @since
     */
    @PostMapping(path = "/content/Store")
    public Response contentStore(int uid,String content) {
        Response response = userService.contentStore(uid,content);
        return response;
    }

    /**
     * @return a
     * @description 推送或留言记录
     * @author gyx
     * @date 2020-05-02 22:0
     * @parameter * @param null
     * @since
     */
    @PostMapping(path = "/push/message")
    public Response pushAndMessage(int id,int page,int size) {
        Response response = userService.pushAndMessage(id,page,size);
        return response;
    }


    /**
     * @return a
     * @description 接收的留言或者推送
     * @author gyx
     * @date 2020-05-02 22:0
     * @parameter * @param null
     * @since
     */
    @PostMapping(path = "/received")
    public Response receivedPushOrMessage(int id,int page,int size) {
        Response response = userService.receivedPushOrMessage(id,page,size);
        return response;
    }

    @PostMapping(path = "/delete/push")
    public Response deletePush(int id,int uid) {
        Response response = userService.deletePushAndMessage(id,uid);
        return response;
    }

}
