package com.example.chess.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class user {
    private int uid;
    private String username;
    private String password;
    private int integral;
    private int state;
    private Timestamp createDate;
    private Timestamp updateDate;
}
