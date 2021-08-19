package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.*;

public class Main extends Application {
    //----------------Konfiguracija programa--------------------------
    int n = 2;  //st razcepov
    boolean graphicsVisible = true;    //vklopi grafiko
    boolean resizeAndZoom = true;      //vklopi zoom in resize
    //running mode: 1. sekvencno 2. paralelno 3. distributed 4.meritve in primerjave
    int runningMode = 4;
    //----------------------------------------------------------------
    double windowHeight = 600;  //sirina okna
    double windowWidth = 800;   //visina okna
    double startx;  //levo ogljisce x
    double starty;  //levo ogljisce y
    double lenght; //dolzina zacetne stranice
    boolean flag = false;       //flag za spremembo zooma
    ExecutorService executorService;

    @Override
    public void start(Stage primaryStage) throws Exception{
        computeLength();
        setStartingPoint();

        if (runningMode == 1) {           //--------------------------------------------------------------------------------
            AnimatedZoomOperator zoomOperator = new AnimatedZoomOperator();
            checkComplexity();
            Fischer f = new Fischer(startx,starty,lenght,0,n,windowWidth,windowHeight,graphicsVisible);
            f.sierpinski(startx,starty,lenght,0,n);

            if (graphicsVisible){
                primaryStage.setTitle("Sequantial Sierpinski triangles");
                Scene s = new Scene(f, windowWidth, windowHeight, Color.rgb(191,255,0));
                primaryStage.setScene(s);
                primaryStage.show();

                s.setOnScroll(new EventHandler<ScrollEvent>() {
                    @Override
                    public void handle(ScrollEvent event) {
                        double zoomFactor = 1.3;
                        double deltaY = event.getDeltaY();
                        //if (event.getDeltaY() <= 0)
                        if (flag){
                            //zoom in
                            zoomFactor = zoomFactor;
                        }else {
                            zoomFactor = 1/zoomFactor;
                        }
                        zoomOperator.zoom(f, zoomFactor, event.getSceneX(), event.getSceneY());
                    }
                });

                s.addEventHandler(MouseEvent.MOUSE_PRESSED,
                        new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                if (!flag){
                                    flag = true;
                                }else {
                                    flag=false;
                                }
                            }
                        });



                ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
                    //System.out.println("Height: " + primaryStage.getHeight() + " Width: " + primaryStage.getWidth());
                    double width = primaryStage.getWidth();
                    double height= primaryStage.getHeight();
                    double factorWidth = width/windowWidth;
                    //System.out.println(factorWidth);
                    windowWidth = width;
                    windowHeight = height;
                    computeLength();
                    setStartingPoint();
                    f.getChildren().clear();
                    f.sierpinski(startx,starty, lenght*factorWidth,0,n);
                };


                primaryStage.widthProperty().addListener(stageSizeListener);
                primaryStage.heightProperty().addListener(stageSizeListener);
            }
        }
        else if (runningMode == 2){     //--------------------------------------------------------------------------------
            checkComplexity();
            TrianglesTask task = new TrianglesTask(startx, starty, lenght, 0, n, graphicsVisible);
            ForkJoinPool pool = new ForkJoinPool();
            Vector<Polygon> triangles = pool.invoke(task);

            if (graphicsVisible) {
                AnimatedZoomOperator zoomOperator = new AnimatedZoomOperator();
                Group root = new Group();
                //draw triangles to group
                for (int i = 0; i < triangles.size(); i++) {
                    root.getChildren().add(triangles.get(i));
                }
                System.out.println(root.getChildren().size() + " size of drawn");
                Scene s = new Scene(root, windowWidth, windowHeight, Color.rgb(191, 255, 0));
                primaryStage.setTitle("Parallel Sierpinski triangles");
                primaryStage.setScene(s);
                primaryStage.show();

                s.setOnScroll(new EventHandler<ScrollEvent>() {
                    @Override
                    public void handle(ScrollEvent event) {
                        double zoomFactor = 1.3;
                        double deltaY = event.getDeltaY();
                        //if (event.getDeltaY() <= 0)
                        if (flag){
                            //zoom in
                            zoomFactor = zoomFactor;
                        }else {
                            zoomFactor = 1/zoomFactor;
                        }
                        zoomOperator.zoom(root, zoomFactor, event.getSceneX(), event.getSceneY());
                    }
                });

                s.addEventHandler(MouseEvent.MOUSE_PRESSED,
                        new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                if (!flag){
                                    flag = true;
                                }else {
                                    flag=false;
                                }
                            }
                        });



                ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
                    double width = primaryStage.getWidth();
                    double height= primaryStage.getHeight();
                    double factorWidth = width/windowWidth;
                    //System.out.println(factorWidth);
                    windowWidth = width;
                    windowHeight = height;
                    computeLength();
                    setStartingPoint();
                    root.getChildren().clear();
                    triangles.clear();
                    //TODO MAKE ANOTHER CALCULATION
                    TrianglesTask t = new TrianglesTask(startx, starty, lenght, 0, n, graphicsVisible);
                    Vector<Polygon> tri = pool.invoke(t);
                    for (int i = 0; i < tri.size(); i++) {
                        root.getChildren().add(tri.get(i));
                    }
                    double stage_height = (lenght * Math.sqrt(3.0)) / 2;
                    primaryStage.setMinWidth(lenght + 60.0);
                    primaryStage.setMinHeight(stage_height + 60.0);
                };


                primaryStage.widthProperty().addListener(stageSizeListener);
                primaryStage.heightProperty().addListener(stageSizeListener);
            }
            //parallel mode end
        }
        else if (runningMode == 4){       //--------------------------------------------------------------------------------
            graphicsVisible = false;
            int cores = Runtime.getRuntime().availableProcessors();
            ForkJoinPool pool = new ForkJoinPool(cores);
            for (int i = 5; i < 22; i++) {
                System.out.println("--------------------------------------------------------------------------");

                //SEQ TIMING
//                executorService.submit(new Runnable() {
//                    @Override
//                    public void run() {
//                        long startSeq = System.currentTimeMillis();
//                        Fischer f = new Fischer(startx,starty,lenght,0,finalI,windowWidth,windowHeight,graphicsVisible);
//                        f.sierpinski(startx,starty,lenght,0, finalI);
//                        System.out.println("Sekvencna za " + finalI + " razcepov z nitjo: "+Thread.currentThread().getName()+
//                                " čas: " + ( System.currentTimeMillis() - startSeq));
//                    }
//                });
//                executorService.shutdown();

                long startSeq = System.currentTimeMillis();
                Fischer f = new Fischer(startx,starty,lenght,0,i,windowWidth,windowHeight,graphicsVisible);
                f.sierpinski(startx,starty,lenght,0, i);
                System.out.println("Sekvencna za " + i + " razcepov z nitjo: "+Thread.currentThread().getName()+
                        " čas: " + ( System.currentTimeMillis() - startSeq));
                //SEQ TIMING END

                //PARALLEL TIMING
//                long start_parallel = System.currentTimeMillis();
//                TrianglesTask task = new TrianglesTask(startx, starty, lenght, 0, i, graphicsVisible);
//                Vector<Polygon> triangles_test = pool.invoke(task);
//                System.out.println("Trajanje paralelne verzije za " + i + " razcepov: " + (System.currentTimeMillis() - start_parallel));
                //END PARALLEL TIMING

                //PARALLEL TIMING
                long start_parallel = System.currentTimeMillis();
                TrianglesBox task = new TrianglesBox(startx, starty, lenght, 0, i,graphicsVisible);
                pool.invoke(task);
                System.out.println("Trajanje paralelne verzije TBox za " + i + " razcepov: " + (System.currentTimeMillis() - start_parallel));
                //END PARALLEL TIMING
            }
        }else {// code block
            System.out.println("Enter correct mode");
        }
    }

    public void setStartingPoint(){
        startx = (this.windowWidth - this.lenght)/2;
         double newY = this.windowHeight - (this.windowHeight - (int)((double)lenght*Math.sqrt(3.0)/2.0)) / 2  ;
        starty = newY - (newY/25);
        //(height - visina trikotnika)/2
    }

    public void computeLength(){
        double max_tri_height = windowHeight - (windowHeight/10);
        lenght = max_tri_height/(Math.sin(Math.PI/3));

        if (windowWidth <= lenght){
            windowWidth = lenght + 50 ;
        }
    }

    public void checkComplexity(){
        if (graphicsVisible && n>11){
            n = 11;
            System.out.println("Parameter n popravljen na " + n);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
