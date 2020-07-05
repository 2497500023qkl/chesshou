package com.example.chess.Server;

import com.example.chess.Piece.piece;
import com.example.chess.Piece.rule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {
    private String userId;
    private String message;
    private String start;
    private boolean go;
    private boolean color;
    private com.example.chess.Piece.rule rule;
    private com.example.chess.User.user user;
    private List<com.example.chess.Piece.piece> piece;
}
