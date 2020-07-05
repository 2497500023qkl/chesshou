package com.example.chess.Controller;

import com.example.chess.Mapper.integralMapper;
import com.example.chess.Mapper.userMapper;
import com.example.chess.User.integral;
import com.example.chess.User.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;

@RestController
@CrossOrigin(origins = "*",maxAge = 3600)
public class IntegralController {

    @Autowired
    integralMapper integralMapper;
    @Autowired
    userMapper usermapper;

    @RequestMapping("/selectintegral")
    public String selectintegral(HttpServletRequest request){
        integral i=new integral();
        if(request.getParameter("username").equals("")){
            return "请输入你的用户名";
        }
        user u= usermapper.selectuser(request.getParameter("username"));
        i.setUid(u.getUid());
        integralMapper.selectintegral(i.getUid());
        return "成功";
    }
}
