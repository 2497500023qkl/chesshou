package com.example.chess.Controller;

import com.alibaba.fastjson.JSON;
import com.example.chess.Mapper.userMapper;
import com.example.chess.Server.start;
import com.example.chess.User.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RestController
@CrossOrigin(origins = "*",maxAge = 3600)
public class UserController {
    start s = new start();
    private Jedis jedis = new Jedis();
    @Autowired
    userMapper usermapper;
    @RequestMapping("/insertuser")
    public String insertuser(HttpServletRequest request){
        user u=new user();
        Date d=new Date();
        Timestamp a=new Timestamp(d.getTime());
        u.setCreateDate(a);
        u.setUpdateDate(a);
        if(request.getParameter("username").equals("")){
            s.setStart("失败");
            s.setMessage("用户名为空");
            return JSON.toJSON(s).toString();
        }else if(request.getParameter("password").equals("")){
            s.setStart("失败");
            s.setMessage("密码为空");
            return JSON.toJSON(s).toString();
        }
        u.setState(0);
        u.setUsername(request.getParameter("username"));
        u.setPassword(request.getParameter("password"));
        try {
            usermapper.insertuser(u);
            s.setStart("成功");
            s.setMessage("创建帐号成功");
            return JSON.toJSON(s).toString();
        }catch (Exception e){
            s.setStart("失败");
            s.setMessage("参数错误");
            return JSON.toJSON(s).toString();
        }
    }
    @RequestMapping("/updateuser")
    public String updateuser(HttpServletRequest request){
        user u=new user();
        Date d=new Date();
        Timestamp a=new Timestamp(d.getTime());
        u.setUpdateDate(a);
        if(request.getParameter("username").equals("")){
            s.setStart("失败");
            s.setMessage("用户名为空");
            return JSON.toJSON(s).toString();
        }else if(request.getParameter("password").equals("")){
            s.setStart("失败");
            s.setMessage("密码为空");
            return JSON.toJSON(s).toString();
        }else if(request.getParameter("newpassword").equals("")){
            s.setStart("失败");
            s.setMessage("修改后的密码为空");
            return JSON.toJSON(s).toString();
        }
        u.setUsername(request.getParameter("username"));
        u.setPassword(request.getParameter("password"));
        try {
            user user=usermapper.selectusername(u);
            if(user==null){
                s.setStart("失败");
                s.setMessage("帐号或密码错误");
                return JSON.toJSON(s).toString();
            }else{
                usermapper.updatepass(u,request.getParameter("newpassword"));
                s.setStart("成功");
                s.setMessage("修改密码成功");
                return JSON.toJSON(s).toString();
            }
        }catch (Exception e){
            System.out.println(e);
            s.setStart("失败");
            s.setMessage("参数错误");
            return JSON.toJSON(s).toString();
        }
    }
    @RequestMapping("/selectuser")
    public String selectuser(HttpServletRequest request){
        user u=new user();
        Date d=new Date();
        Timestamp a=new Timestamp(d.getTime());
        u.setUpdateDate(a);
        try {
            if(request.getParameter("token").equals(null)){
            }else{
                if(request.getParameter("token").equals(jedis.get(request.getParameter("user")))){
                    user user=usermapper.selectuser(request.getParameter("user"));
                    user.setPassword("");
                    return JSON.toJSON(user).toString();
                }else{
                    s.setStart("过期");
                    s.setMessage("token过期");
                    return JSON.toJSON(s).toString();
                }
            }
        }catch (Exception e){

        }
        if(request.getParameter("username").equals("")){
            s.setStart("失败");
            s.setMessage("用户名为空");
            return JSON.toJSON(s).toString();
        }else if(request.getParameter("password").equals("")){
            s.setStart("失败");
            s.setMessage("密码为空");
            return JSON.toJSON(s).toString();
        }
        u.setUsername(request.getParameter("username"));
        u.setPassword(request.getParameter("password"));
        try {
            System.out.println(request.getParameter("username"));
            System.out.println(request.getParameter("password"));
            user user=usermapper.selectusername(u);
            if(user==null){
                s.setStart("失败");
                s.setMessage("用户名或密码错误");
                return JSON.toJSON(s).toString();
            }else{
                String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
                Random random1=new Random();
                StringBuffer sb=new StringBuffer();
                for (int i = 0; i < 12; i++) {
                    int number=random1.nextInt(str.length());
                    char charAt = str.charAt(number);
                    sb.append(charAt);
                }
                StringBuffer str1 = sb;
                jedis.set(user.getUsername(),str1.toString()+user.getUsername());
                user.setPassword(str1.toString()+user.getUsername());
                jedis.expire(user.getUsername(),7200);
                return JSON.toJSON(user).toString();
            }
        }catch (Exception e){
            System.out.println(e);
            s.setStart("失败");
            s.setMessage("参数错误");
            return JSON.toJSON(s).toString();
        }
    }
}
