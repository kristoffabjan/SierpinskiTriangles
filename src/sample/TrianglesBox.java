package sample;

import javafx.scene.Group;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.locks.ReentrantLock;

public class TrianglesBox extends RecursiveAction {
    private double x;
    private double y;
    private double len;
    private double level;
    private double max_level;
    private boolean drawShapes;
    Group root = new Group();

    public TrianglesBox(double startx, double starty, double lenght, double level, double max_level,
                        boolean drawShapes, Group root){
        this.x = startx;
        this.y = starty;
        this.len = lenght;
        this.level = level;
        this.max_level = max_level;
        this.drawShapes = drawShapes;
        this.getGroup();
    }


    @Override
    protected void compute () {
        if (level == max_level){
            if (drawShapes){
                //System.out.println("naredili trikotnik " + x + " " + y + " len " + len  + " z nitjo " + Thread.currentThread().getName() );
                //System.out.println(drawPolygon(x,y,len));
                this.root.getChildren().add(drawPolygon(x,y,len));
                //System.out.println(this.root.getChildren());
                //System.out.println(drawPolygon(x,y,len).hashCode());
                //this.queue.add(drawPolygon(x,y,len));
                //this.list.add(drawPolygon(x,y,len));
                //System.out.println(list.size());
            }
        } else {
            TrianglesBox t1 = new TrianglesBox(x,y,len/2,level+1,max_level,drawShapes,root);
            //t1.fork();
            TrianglesBox t2 = new TrianglesBox(x + len/4,y - (int)((Math.sin((double)Math.PI/3)) * (double)(len/2)),
                    len/2,level+1,max_level,drawShapes,root);
            //t2.fork();
            TrianglesBox t3 = new TrianglesBox(x+len/2,y,len/2,level+1,max_level,drawShapes,root);
            //t3.fork();
            invokeAll(t1,t2,t3);
        }
    }

    public Polygon drawPolygon(double x, double y, double length){
        return new Polygon((double)x,(double)y,
                (double)x+length/2,(double)y - Math.sin(Math.PI/3)*length,
                (double)x+length,(double)y);
    }

    //for list

    public Group getGroup() {
        return this.root;
    }
}
