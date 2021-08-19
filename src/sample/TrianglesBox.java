package sample;

import javafx.scene.shape.Polygon;
import java.util.Vector;
import java.util.concurrent.RecursiveAction;

public class TrianglesBox extends RecursiveAction {
    private double x;
    private double y;
    private double len;
    private double level;
    private double max_level;
    private boolean drawShapes;
    Vector<Polygon> triangles = new Vector<>();

    public TrianglesBox(double startx, double starty, double lenght, double level, double max_level,
                        boolean drawShapes){
        this.x = startx;
        this.y = starty;
        this.len = lenght;
        this.level = level;
        this.max_level = max_level;
        this.drawShapes = drawShapes;
    }


    @Override
    protected void compute () {
        if (level == max_level){
            if (drawShapes){
                //TODO thread do work
                this.triangles.add(drawPolygon(x,y,len));
                //this.root.getChildren().add(drawPolygon(x,y,len));
            }
            //speed
//            int sum = 0;
//            for (int i = 0; i < 1000000; i++) {
//                sum += i;
//            }
        } else {
            TrianglesBox t1 = new TrianglesBox(x,y,len/2,level+1,max_level,drawShapes);
            //t1.fork();
            TrianglesBox t2 = new TrianglesBox(x + len/4,y - (int)((Math.sin((double)Math.PI/3)) * (double)(len/2)),
                    len/2,level+1,max_level,drawShapes);
            //t2.fork();
            TrianglesBox t3 = new TrianglesBox(x+len/2,y,len/2,level+1,max_level,drawShapes);
            //t3.fork();
            invokeAll(t1,t2,t3);
        }
    }

    public Polygon drawPolygon(double x, double y, double length){
        return new Polygon((double)x,(double)y,
                (double)x+length/2,(double)y - Math.sin(Math.PI/3)*length,
                (double)x+length,(double)y);
    }
}
