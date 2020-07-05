package com.example.chess.Piece;

import com.google.gson.internal.$Gson$Types;

public class rule {
    private piece piece;
    private String hang;
    private String grid;
    public com.example.chess.Piece.piece getPiece() {
        return piece;
    }

    public void setPiece(com.example.chess.Piece.piece piece) {
        this.piece = piece;
    }

    public String getHang() {
        return hang;
    }

    public void setHang(String hang) {
        this.hang = hang;
    }

    public String getGrid() {
        return grid;
    }

    public void setGrid(String grid) {
        this.grid = grid;
    }

    public rule(com.example.chess.Piece.piece piece, String hang, String grid) {
        this.piece = piece;
        this.hang = hang;
        this.grid = grid;
    }
}
