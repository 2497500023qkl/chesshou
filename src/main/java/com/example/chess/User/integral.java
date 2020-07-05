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
public class integral {
    private int iid;
    private int uid;
    private int ouid;
    private int integral;
    private Timestamp createDate;
    private Timestamp updateDate;
}
