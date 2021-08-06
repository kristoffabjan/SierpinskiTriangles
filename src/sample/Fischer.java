package sample;


import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Polygon;

public class Fischer extends Group {
    private double startx;
    private double starty;
    private double lenght;
    private double level;
    private double max_level;
    private  GraphicsContext gc;
    public boolean drawShapes;


    public Fischer(double startx, double starty, double lenght, int level, int max_level,
                   double winWidth, double winHeight, boolean drawShapes) {
        this.startx = startx;
        this.starty = starty;
        this.lenght = lenght;
        this.level = level;
        this.max_level = max_level;
        this.drawShapes = drawShapes;
    }

    public void sierpinski(double x, double y, double len, double level, double max_level){
        if (level == max_level){
            if (drawShapes){
                this.getChildren().add(trikotnikPolygon(x,y,len));
            }
        }else{
            sierpinski(x,y,len/2,level +1, max_level);
            sierpinski(x + len/4,y - (int)((Math.sin((double)Math.PI/3)) * (double)(len/2)),len/2,level +1, max_level);
            sierpinski(x + len/2,y,len/2,level +1, max_level);
        }
    }

    public Polygon trikotnikPolygon(double x, double y, double length){
        return new Polygon((double)x,(double)y,
                (double)x+length/2,(double)y - Math.sin(Math.PI/3)*length,
                (double)x+length,(double)y);
    }


    public double getStartx() {
        return startx;
    }

    public void setStartx(int startx) {
        this.startx = startx;
    }

    public double getStarty() {
        return starty;
    }

    public void setStarty(int starty) {
        this.starty = starty;
    }

    public double getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }

    public double getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getMax_level() {
        return max_level;
    }

    public void setMax_level(int max_level) {
        this.max_level = max_level;
    }

    public void setLevel(double level) {
        this.level = level;
    }

    public void setMax_level(double max_level) {
        this.max_level = max_level;
    }

    public GraphicsContext getGc() {
        return gc;
    }

    public void setGc(GraphicsContext gc) {
        this.gc = gc;
    }
}
