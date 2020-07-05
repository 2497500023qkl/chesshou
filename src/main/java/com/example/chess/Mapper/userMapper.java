package com.example.chess.Mapper;

import com.example.chess.User.user;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface userMapper {
    @Select("select * from createuser where username=#{username}")
    user selectuser(String username);
    @Update("update createuser set password=#{newpassword},update_date=#{user.updateDate} where username=#{user.username} and password=#{user.password}")
    void updatepass(user user,String newpassword);
    @Insert("insert into createuser(username,password,state,update_date,create_date)values(#{username},#{password},#{state},#{updateDate},#{createDate})")
    void insertuser(user user);
    @Select("select * from createuser where username=#{username} and password=#{password}")
    user selectusername(user user);
    @Update("update createuser set integral=#{integral},update_date=#{updateDate} where username=#{username}")
    void updateintegral(user user);

}
