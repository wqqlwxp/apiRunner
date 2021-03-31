package com.runner.server.service.auth;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.runner.server.dao.entity.bo.MsgResponseObject;
import com.runner.server.dao.entity.bo.PageObject;
import com.runner.server.dao.entity.bo.ReqCaseData;
import com.runner.server.dao.entity.bo.ReqRequest;
import com.runner.server.dao.entity.po.TestCase;
import com.runner.server.dao.entity.po.TestPlan;
import com.runner.server.dao.entity.po.User;
import com.runner.server.dao.mapper.CaseMapper;
import com.runner.server.dao.mapper.TestPlanMapper;
import com.runner.server.dao.mapper.UserMapper;
import com.runner.server.service.serviceImpl.CipherService;
import com.runner.server.service.utils.CacheUtil;
import com.runner.server.service.utils.DateUtil;
import com.runner.server.service.utils.LogUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.jws.soap.SOAPBinding;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class AuthService {

    @Resource
    public UserMapper userMapper;

    @Resource
    private CipherService cipherService;

    private MsgResponseObject msgReponseObject=new MsgResponseObject() ;


    /**
     * @description 查询数据
     * @author 星空梦语
     * @date 2020/4/11 11:37
     */
    public String query(PageObject pageObject) throws InvocationTargetException, IllegalAccessException {
        User user =new User();
        BeanUtils.populate(user, pageObject.getData());
        int offset=(pageObject.getOffset()-1)*pageObject.getLimit();
        List<User> list=userMapper.queryPageData(offset,pageObject.getLimit(), user);
        int total=getUserCache().size();
        if(StringUtils.isNotBlank(user.getUserAccount()) ){
            total=list.size();
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total",total);
        map.put("rows",list);
        return JSON.toJSONString(map);
    }


    /**
     * @description 新增或修改数据
     * @author 星空梦语
     * @date 2020/4/11 12:14
     */
    public MsgResponseObject edit(PageObject pageObject) throws Exception {
        User user=new User();
        BeanUtils.populate(user, pageObject.getData());
        msgReponseObject.setCode(201);
        if(StringUtils.isBlank(user.getUserAccount()) ||StringUtils.isBlank(user.getUserPassword())  ){
            msgReponseObject.setMsg("用户账户或密码不能为空，请确认");
            return  msgReponseObject;
        }
        String pwd=cipherService.encrypt("testPlateform","login",user.getUserPassword());
        user.setUserPassword(pwd);
        if(user.getId()!=null){
            Example example=new Example(TestPlan.class);
            example.createCriteria().andEqualTo("id",user.getId());
            msgReponseObject.setMsg("修改失败");
            if(userMapper.updateByExampleSelective(user,example)>0){
                msgReponseObject.setMsg("修改成功");
                msgReponseObject.setCode(200);
                updateUserCache();
            }
        }else{
            user.setCreateTime(DateUtil.formatToDayByDate3(new Date()));
            if(userMapper.insertSelective(user)>0){
                msgReponseObject.setMsg("新增成功");
                msgReponseObject.setCode(200);
            }else{
                msgReponseObject.setMsg("新增失败");
            }
        }
        return  msgReponseObject;
    }

    
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/2/23 上午10:10
     */
    public MsgResponseObject delete(int id) throws Exception {
        Example example=new Example(User.class);
        example.createCriteria().andEqualTo("id",id);
        msgReponseObject.setMsg("删除失败");
        if(userMapper.deleteByExample(example)>0){
            msgReponseObject.setMsg("删除成功");
            msgReponseObject.setCode(200);
            updateUserCache();
        }
        return msgReponseObject;
    }



    /**
     * @description  获取缓存
     * @author 星空梦语
     * @date 2020/4/19 11:37
     */
    public List<User> getUserCache(){
        return (List<User>) CacheUtil.getCacheObject("user");
    }


    /**
     * @description  更新缓存
     * @author 星空梦语
     * @date 2020/4/19 11:41
     */
    public void updateUserCache(){
        LogUtil.info("开始更新用户数据缓存");
        List<User> userList= userMapper.queryAll();
        CacheUtil.setCacheObject("user",userList,0L);
    }



    /**
     * @description  用户注册
     * @author 星空梦语
     * @date 2021/2/25 下午3:39
     */
    public MsgResponseObject registerUser(User user) throws Exception {
        user.setStatus("0");
        user.setPermission("");
        msgReponseObject.setCode(201);
        if(StringUtils.isBlank(user.getUserAccount()) || StringUtils.isBlank(user.getUserPassword())){
            msgReponseObject.setMsg("账户信息不能为空，请重新输入");
            return  msgReponseObject;
        }
        User u=new User();
        u.setUserAccount(user.getUserAccount());
        List<User> data=userMapper.queryPageData(0,1,u);
        if(data.size()>0){
            msgReponseObject.setMsg("用户已注册，请直接登录");
            return  msgReponseObject;
        }
        user.setCreateTime(DateUtil.formatToDayByDate3(new Date()));

        String pwd=cipherService.encrypt("testPlateform","login",user.getUserPassword());
        user.setUserPassword(pwd);
        int flag=userMapper.insertSelective(user);
        if(flag>0){
            msgReponseObject.setCode(200);
            msgReponseObject.setMsg("用户注册成功");
        }else{
            msgReponseObject.setMsg("用户注册失败");
        }
        return  msgReponseObject;
    }


    /**
     * @description 登录
     * @author 星空梦语
     * @date 2021/2/25 下午12:42
     */
    public MsgResponseObject login(User user) throws Exception {
        msgReponseObject.setCode(200);
        msgReponseObject.setMsg("登录成功");
        if(StringUtils.isBlank(user.getUserAccount()) || StringUtils.isBlank(user.getUserPassword())){
            msgReponseObject.setCode(201);
            msgReponseObject.setMsg("用户账号或密码不能为空");
            return msgReponseObject;
        }
        User user1=new User();
        user1.setUserAccount(user.getUserAccount());
        user1.setStatus("0");
        String pwd=cipherService.encrypt("testPlateform","login",user.getUserPassword());
        user1.setUserPassword(pwd);
        List<User> data=userMapper.queryPageData(0,1,user1);
        if(data.size()==0){
            msgReponseObject.setCode(201);
            msgReponseObject.setMsg("用户账号或密码错误，请重新输入");
            return  msgReponseObject;
        }
        user1=data.get(0);
        Map map=new HashMap();
        map.put("nickName",user1.getNickName());
        map.put("userPermission",user1.getPermission());
        msgReponseObject.setObject(map);
        return  msgReponseObject;
    }



    /**
     * @description  认证
     * @author 星空梦语
     * @date 2021/2/25 下午3:40
     */
    public MsgResponseObject auth(HttpServletRequest request,User user){
        msgReponseObject.setCode(201);
        try {
            MsgResponseObject msg=login(user);
            if(msg.getCode()==200){
                setAuthSession(request);
                return  msg;
            }
        } catch (Exception e) {
            e.printStackTrace();
            msgReponseObject.setMsg("登录失败");
        }
        return  msgReponseObject;
    }



    /**
     * @description  设置登录成功后session会话
     * @author 星空梦语
     * @date 2021/2/25 下午3:40
     */
    public void setAuthSession(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        session.setAttribute("auth","true");
        session.setMaxInactiveInterval(6*60*60); // 无操作会话最大保持时间6小时
    }




    /**
     * @description 清除会话数据
     * @author 星空梦语
     * @date 2021/3/2 上午11:22
     */
    public MsgResponseObject deleteLoginSession(HttpServletRequest request){
        msgReponseObject.setCode(200);
        msgReponseObject.setMsg("退出登录成功");
        HttpSession session = request.getSession();
        session.invalidate();
        return msgReponseObject;
    }



}
