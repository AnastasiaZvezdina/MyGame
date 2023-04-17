package com.example.mygame;

public class Blocks {
    private boolean Visible;
    public int row, column, height, wigth;

    public Blocks(int row, int column, int height, int wigth) {
        Visible = true;
        this.row = row;
        this.column = column;
        this.height = height;
        this.wigth = wigth;
    }

    public void setInvisible(){
        Visible=false;
    }

    public boolean getVisiblity(){
        return Visible;
    }
}
