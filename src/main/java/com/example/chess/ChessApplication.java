package com.example.chess;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.BitSet;
import java.util.TimeZone;

@SpringBootApplication
@MapperScan("com.example.chess.Mapper")
public class ChessApplication {
	public static void main(String[] args){
		SpringApplication.run(ChessApplication.class, args);
	}

}
