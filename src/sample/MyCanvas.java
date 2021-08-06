package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class MyCanvas extends Canvas {
     GraphicsContext gc;


    public MyCanvas(double winWidth, double winHeight) {
        super(winWidth,winHeight);
        this.gc = this.getGraphicsContext2D();
    }
}
