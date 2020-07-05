package com.example.chess.Server;

import com.alibaba.fastjson.JSON;
import com.example.chess.Controller.IntegralController;
import com.example.chess.Mapper.integralMapper;
import com.example.chess.Mapper.userMapper;
import com.example.chess.User.integral;
import com.example.chess.User.user;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.Console;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/test")
@Component
@Slf4j
public class receive {
    /**
     * 存放所有在线的客户端
     */
    @Autowired
    public void setHeatMapService(userMapper user){
        receive.user = user;
    }
    @Autowired
    public void setHeatMapService(integralMapper integral){
        receive.integral = integral;
    }
    private static userMapper user;
    private Jedis jedis = new Jedis();
    private static integralMapper integral;
    private static Map<String, Session> clients = new ConcurrentHashMap<>();
    private static Map<String,String> start=new ConcurrentHashMap<>();
    private static Map<String, user> name=new ConcurrentHashMap<>();
    private Gson gson =new Gson();
    private start s = new start();
    String value="";

    @OnOpen
    public void onOpen(Session session) throws IOException {
        log.info("有新的客户端连接了: {}", session.getId());
        //将新用户存入在线的组
        clients.put(session.getId(), session);
        start.put(session.getId(),"空闲");
        s.setStart("id");
        s.setMessage(session.getId());
        clients.get(session.getId()).getBasicRemote().sendText(JSON.toJSON(s).toString());
    }

    /**
     * 客户端关闭
     * @param session session
     */
    @OnClose
    public void onClose(Session session) throws IOException, InterruptedException {
        log.info("有用户断开了, id为:{}", session.getId());
        //将掉线的用户移除在线的组里
        Thread.sleep(1000);
        if(clients.get(session.getId())==null){
            if(name.get(session.getId())==null){
                return;
            }
            name.remove(session.getId());
            return;
        }
        if(start.get(session.getId())=="空闲"){
            clients.remove(session.getId());
            start.remove(session.getId());
            name.remove(session.getId());
        }else if(start.get(session.getId())!="空闲"){
            try {
                if(clients.get(start.get(session.getId()))!=null){
                    clients.remove(session.getId());
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if(start.get(session.getId())!=null&&jedis.get(session.getId())!=null){
                                user u=name.get(session.getId());
                                integral i=new integral();
                                i.setIntegral(Integer.parseInt(jedis.get(session.getId())));
                                i.setUid(u.getUid());
                                i.setOuid(u.getUid());
                                Date d=new Date();
                                Timestamp a=new Timestamp(d.getTime());
                                i.setUpdateDate(a);
                                i.setCreateDate(a);
                                u.setUpdateDate(a);
                                u.setIntegral(u.getIntegral()+Integer.parseInt(jedis.get(session.getId())));
                                jedis.del(session.getId());
                                integral.insertintegral(i);
                                user.updateintegral(u);
                                for(String key : start.keySet()) {
                                    if(start.get(session.getId())==start.get(key)){
                                        start.remove(session.getId());
                                        name.remove(session.getId());
                                    }
                                }
                            }else if(start.get(session.getId())!=null&&jedis.get(session.getId())==null){
                                user u=name.get(session.getId());
                                integral i=new integral();
                                i.setIntegral(-15);
                                i.setUid(u.getUid());
                                i.setOuid(u.getUid());
                                Date d=new Date();
                                Timestamp a=new Timestamp(d.getTime());
                                i.setUpdateDate(a);
                                i.setCreateDate(a);
                                u.setUpdateDate(a);
                                u.setIntegral(u.getIntegral()-15);
                                jedis.del(session.getId());
                                integral.insertintegral(i);
                                user.updateintegral(u);
                                try {
                                    s.setStart("离开");
                                    s.setMessage("对手离开你获胜");
                                    clients.get(start.get(session.getId())).getBasicRemote().sendText(JSON.toJSON(s).toString());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                for(String key : start.keySet()) {
                                    if(start.get(session.getId())==start.get(key)){
                                        start.remove(session.getId());
                                        name.remove(session.getId());
                                    }
                                }
                            }
                        }
                    },60000);
                }
            }catch (Exception e){
                System.out.println("未");
            }
            try {
                if(clients.get(start.get(session.getId()))==null&&jedis.get(session.getId())==null){
                    clients.remove(session.getId());
                    user u=name.get(session.getId());
                    integral i=new integral();
                    i.setIntegral(-10);
                    i.setUid(u.getUid());
                    i.setOuid(name.get(start.get(session.getId())).getUid());
                    Date d=new Date();
                    Timestamp a=new Timestamp(d.getTime());
                    i.setUpdateDate(a);
                    i.setCreateDate(a);
                    u.setUpdateDate(a);
                    u.setIntegral(u.getIntegral()-10);
                    integral.insertintegral(i);
                    user.updateintegral(u);
                    u = name.get(start.get(session.getId()));
                    u.setUpdateDate(a);
                    u.setIntegral(u.getIntegral()-10);
                    i.setUid(u.getUid());
                    i.setOuid(name.get(session.getId()).getUid());
                    integral.insertintegral(i);
                    user.updateintegral(u);
                    for(String key : start.keySet()) {
                        if(start.get(start.get(session.getId()))!=start.get(start.get(key))){
                            start.remove(start.get(session.getId()));
                            name.remove(start.get(session.getId()));
                        }
                        if(start.get(session.getId())==start.get(key)){
                            start.remove(session.getId());
                            name.remove(session.getId());
                        }
                    }
                }else if(clients.get(start.get(session.getId()))==null&&jedis.get(session.getId())!=null){
                    clients.remove(session.getId());
                    user u=name.get(session.getId());
                    integral i=new integral();
                    i.setIntegral(Integer.parseInt(jedis.get(session.getId())));
                    i.setUid(u.getUid());
                    i.setOuid(name.get(start.get(session.getId())).getUid());
                    Date d=new Date();
                    Timestamp a=new Timestamp(d.getTime());
                    i.setUpdateDate(a);
                    i.setCreateDate(a);
                    u.setUpdateDate(a);
                    u.setIntegral(u.getIntegral()+Integer.parseInt(jedis.get(session.getId())));
                    jedis.del(session.getId());
                    integral.insertintegral(i);
                    user.updateintegral(u);
                    for(String key : start.keySet()) {
                        if(start.get(session.getId())==start.get(key)){
                            start.remove(session.getId());
                            name.remove(session.getId());
                        }
                    }
                }
            }catch (Exception e){
                System.out.println("报错");
            }
        }
    }

    /**
     * 发生错误
     * @param throwable e
     */
    @OnError
    public synchronized void onError(Session session, Throwable throwable) throws IOException, InterruptedException {
        throwable.printStackTrace();
        log.info("有用户异常了, id为:{}", session.getId());
        //将掉线的用户移除在线的组里
    }

    /**
     * 收到客户端发来消息
     * @param message  消息对象
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("服务端收到客户端发来的消息: {}", message);
        try {
            this.sendto(gson.fromJson(message,Message.class));
        }catch (IOException e){

        }
    }

    private void sendto(Message message) throws IOException {
        Session session=clients.get(message.getUserId());
        if(message.getStart().equals("0")){
                for(String key : start.keySet()){
                    try {
                    if(name.get(key).getUsername().equals(message.getUser().getUsername())&&start.get(key).equals("空闲")){
                        start.put(session.getId(),"空闲");
                        start.remove(key);
                        name.remove(key);
                        s.setStart("在别的地方上线");
                        clients.get(key).getBasicRemote().sendText(JSON.toJSON(s).toString());
                        clients.get(key).close();
                        clients.remove(key);
                        name.put(message.getUserId(),message.getUser());
                        return;
                    }else if(name.get(key).getUsername().equals(message.getUser().getUsername())){
                        start.remove(start.get(key));
                        start.put(start.get(key),session.getId());
                        start.put(session.getId(),start.get(key));
                        start.remove(key);
                        name.remove(key);
                        s.setStart("在别的地方上线");
                        try {
                            clients.get(key).getBasicRemote().sendText(JSON.toJSON(s).toString());
                            clients.get(key).close();
                            s.setStart("推");
                            clients.get(session.getId()).getBasicRemote().sendText(JSON.toJSON(s).toString());
                        }catch (Exception e){
                            System.out.println("以推出");
                            s.setStart("帮助");
                            clients.get(start.get(session.getId())).getBasicRemote().sendText(JSON.toJSON(s).toString());
                            clients.remove(key);
                        }
                        s.setStart("重连中");
                        clients.get(session.getId()).getBasicRemote().sendText(JSON.toJSON(s).toString());
                        name.put(message.getUserId(),message.getUser());
                        return;
                    }
                    }catch (Exception e){
                        System.out.println("无");
                    }
                }
            name.put(message.getUserId(),message.getUser());
            for(String key : start.keySet()){
                if(start.get(key).equals("空闲")){
                    if(value.equals("")){
                        value = key;
                    }else{
                        start.remove(value);
                        start.put(value,key);
                        s.setStart("开始");
                        s.setColor(false);
                        s.setMessage("游戏开始对战玩家"+name.get(key).getUsername()+"您是红方");
                        clients.get(value).getBasicRemote().sendText(JSON.toJSON(s).toString());
                        start.remove(key);
                        start.put(key,value);
                        start.remove(value);
                        start.put(value,key);
                        s.setColor(true);
                        s.setMessage("游戏开始对战玩家"+name.get(value).getUsername()+"您是黑方");
                        clients.get(key).getBasicRemote().sendText(JSON.toJSON(s).toString());
                        value="";
                    }
                }
                System.out.println(key +":"+ start.get(key));
            }
            if(value.equals(message.getUserId())){
                s.setStart("等待");
                s.setMessage(name.get(message.getUserId()).getUsername()+"你好正在匹配你的对手");
                clients.get(message.getUserId()).getBasicRemote().sendText(JSON.toJSON(s).toString());
            }
        }else if(message.getStart().equals("1")){
                try {
                    clients.get(start.get(message.getUserId())).getBasicRemote().sendText(JSON.toJSON(message).toString());
                }catch (Exception e){
                    System.out.println("失败");
                }
        }else if(message.getStart().equals("重连1")){
            clients.get(start.get(message.getUserId())).getBasicRemote().sendText(JSON.toJSON(message).toString());
        }else if(message.getStart().equals("重连2")){
            clients.get(start.get(message.getUserId())).getBasicRemote().sendText(JSON.toJSON(message).toString());
        }else if(message.getStart().equals("重登")){
            for(String key : start.keySet()) {
                if(name.get(key).getUsername().equals(message.getUser().getUsername())&&!start.get(key).equals("空闲")) {
                    clients.get(key).getBasicRemote().sendText(JSON.toJSON(message).toString());
                }
            }
        }else if(message.getStart().equals("结束")){
            try {
                clients.remove(session.getId());
                if(message.getMessage().equals("false")){
                    user u=name.get(session.getId());
                    integral i=new integral();
                    i.setIntegral(10);
                    i.setUid(u.getUid());
                    i.setOuid(name.get(start.get(session.getId())).getUid());
                    Date d=new Date();
                    Timestamp a=new Timestamp(d.getTime());
                    i.setUpdateDate(a);
                    i.setCreateDate(a);
                    u.setUpdateDate(a);
                    u.setIntegral(u.getIntegral()+10);
                    integral.insertintegral(i);
                    user.updateintegral(u);
                    jedis.set(start.get(session.getId()),"-10");
                }else{
                    user u=name.get(session.getId());
                    integral i=new integral();
                    i.setIntegral(-10);
                    i.setUid(u.getUid());
                    i.setOuid(name.get(start.get(session.getId())).getUid());
                    Date d=new Date();
                    Timestamp a=new Timestamp(d.getTime());
                    i.setUpdateDate(a);
                    i.setCreateDate(a);
                    u.setUpdateDate(a);
                    u.setIntegral(u.getIntegral()-10);
                    integral.insertintegral(i);
                    user.updateintegral(u);
                    jedis.set(start.get(session.getId()),"10");
                }
                start.remove(session.getId());
                name.remove(session.getId());
            }catch (Exception e){
                System.out.println("报错啦");
            }
        }
    }
}
