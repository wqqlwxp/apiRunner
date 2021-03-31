package com.runner.server.controller;

import com.runner.server.dao.entity.bo.MsgResponseObject;
import com.runner.server.dao.entity.bo.PageObject;
import com.runner.server.dao.entity.po.User;
import com.runner.server.service.auth.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private AuthService authService;


    /**
     * @description  查询数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/queryAllUser",produces = "application/json;charset=utf-8")
    public String queryAllUser(@RequestBody PageObject pageObject) throws Exception {
        return authService.query(pageObject);
    }


    /**
     * @description  编辑数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/editUser")
    public MsgResponseObject editUser(@RequestBody PageObject pageObject) throws Exception {
        return authService.edit(pageObject);
    }

    /**
     * @description  删除数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/deleteUser")
    public MsgResponseObject deleteUser(@RequestBody int id) throws Exception {
        return authService.delete(id);
    }


    @ResponseBody
    @RequestMapping(value = "/login")
    public MsgResponseObject login(@RequestBody User user ,HttpServletRequest request, HttpServletResponse response) {
        return  authService.auth(request,user);
    }


    @ResponseBody
    @RequestMapping({"/signOut"})
    public MsgResponseObject signOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return  authService.deleteLoginSession(request);
    }


    @ResponseBody
    @RequestMapping({"/registerUser"})
    public MsgResponseObject registerUser(@RequestBody User user) throws Exception {
       return authService.registerUser(user);
    }


}
