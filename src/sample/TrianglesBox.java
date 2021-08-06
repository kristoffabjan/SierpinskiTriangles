package sample;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RecursiveAction;

public class TrianglesBox extends RecursiveAction  {
    private double x;
    private double y;
    private double len;
    private double level;
    private double max_level;
    private boolean drawShapes;
    BlockingQueue<Polygon> queue;

    public TrianglesBox(double startx, double starty, double lenght, double level, double max_level,
                        boolean drawShapes, BlockingQueue<Polygon> queue){
        this.x = startx;
        this.y = starty;
        this.len = lenght;
        this.level = level;
        this.max_level = max_level;
        this.drawShapes = drawShapes;
        this.queue = queue;
    }


    @Override
    protected void compute () {
        if (level == max_level){
            if (drawShapes){
                //System.out.println("naredili trikotnik " + x + " " + y + " len " + len  + " z nitjo " + Thread.currentThread().getName() );
                this.queue.add(drawPolygon(x,y,len));
            }
        } else {
            TrianglesBox t1 = new TrianglesBox(x,y,len/2,level+1,max_level,drawShapes,queue);
            //t1.fork();
            TrianglesBox t2 = new TrianglesBox(x + len/4,y - (int)((Math.sin((double)Math.PI/3)) * (double)(len/2)),
                    len/2,level+1,max_level,drawShapes,queue);
            //t2.fork();
            TrianglesBox t3 = new TrianglesBox(x+len/2,y,len/2,level+1,max_level,drawShapes,queue);
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
