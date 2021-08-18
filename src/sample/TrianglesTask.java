package sample;

import javafx.scene.shape.Polygon;

import java.util.Vector;
import java.util.concurrent.RecursiveTask;

public class TrianglesTask extends RecursiveTask<Vector<Polygon>> {
    private double x;
    private double y;
    private double len;
    private double level;
    private double max_level;
    private boolean drawShapes;

    public TrianglesTask(double x, double y, double len, double level, double max_level, boolean drawShapes) {
        this.x = x;
        this.y = y;
        this.len = len;
        this.level = level;
        this.max_level = max_level;
        this.drawShapes = drawShapes;
    }



    @Override
    protected Vector<Polygon> compute() {
        Vector<Polygon> vector = new Vector<>();
        if (level == max_level){
            if (drawShapes){
                vector.add(drawPolygon(x,y,len));
            }
        } else {
            TrianglesTask t1 = new TrianglesTask(x,y, len/2, level+1, max_level, drawShapes);
            TrianglesTask t2 = new TrianglesTask(x + len/4,y - (int)((Math.sin((double)Math.PI/3)) * (double)(len/2)),
                    len/2,level+1,max_level,drawShapes);
            TrianglesTask t3 = new TrianglesTask(x+len/2,y,len/2,level+1,max_level,drawShapes);
            t1.fork();
            t2.fork();
            t3.fork();

            vector.addAll(t1.join());
            vector.addAll(t2.join());
            vector.addAll(t3.join());
        }
        return vector;
    }

    public Polygon drawPolygon(double x, double y, double length){
        return new Polygon((double)x,(double)y,
                (double)x+length/2,(double)y - Math.sin(Math.PI/3)*length,
                (double)x+length,(double)y);
    }

    //GETTERS N SETTERS

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getLen() {
        return len;
    }

    public void setLen(double len) {
        this.len = len;
    }

    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
        this.level = level;
    }

    public double getMax_level() {
        return max_level;
    }

    public void setMax_level(double max_level) {
        this.max_level = max_level;
    }

    public boolean isDrawShapes() {
        return drawShapes;
    }

    public void setDrawShapes(boolean drawShapes) {
        this.drawShapes = drawShapes;
    }
}
