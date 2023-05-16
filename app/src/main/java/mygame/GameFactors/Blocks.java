package mygame.GameFactors;

public class Blocks {
    private boolean visible;
    public int  height, wigth, row, line;

    public Blocks(int row, int line, int height, int wigth) {
        visible = true;
        this.row = row;
        this.line = line;
        this.height = height;
        this.wigth = wigth;
    }

    public void setInvisible(){
        visible=false;
    }

    public boolean getVisiblity(){
        return visible;
    }
}
