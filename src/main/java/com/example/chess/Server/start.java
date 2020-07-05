package com.example.chess.Server;

import com.example.chess.Piece.piece;
import com.example.chess.User.user;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class start {
    private String start;
    private boolean color;
    private String message;
}
