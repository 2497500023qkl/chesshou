package com.example.chess.Mapper;

import com.example.chess.User.integral;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface integralMapper {
    @Insert("insert into integral(uid,ouid,integral,update_date,create_date)values(#{uid},#{ouid},#{integral},#{updateDate},#{createDate})")
    void insertintegral(integral i);
    @Select("select * from integral where uid=#{uid}")
    List<integral> selectintegral(int uid);
}
