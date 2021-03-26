package sample;

public class Rectangle {
    //(x,y) represents top left corner of rectangle

    double x;
    double y;
    double width;
    double height;

    public Rectangle(){
        this.setPosition(0,0);
        this.setSize(1,1);
    }

    public Rectangle (double x, double y, double width, double height) {
        this.setSize(width, height);
        this.setPosition(x, y);
    }

    public void setPosition (double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setSize (double width, double height) {
        this.width = width;
        this.height = height;
    }

    public boolean overlaps (Rectangle other) {
        boolean noOverlap = this.x + this.width < other.x ||
                other.x + other.width< this.x ||
                this.y + this.height < other.y ||
                other.y + other.height < this.y ;
        return !noOverlap;
    }
}
