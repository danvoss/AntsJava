package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

public class Main extends Application {

    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    static final int ANT_COUNT = 100;

    static ArrayList<Ant> ants = new ArrayList<>();

    static void createAnts() {
        for (int i = 0; i < ANT_COUNT; i++) {
            Random r = new Random();
            Ant a = new Ant(r.nextInt(WIDTH), r.nextInt(HEIGHT)); // r.nextInt gives a random # between 0 and "max" (the args)
            ants.add(a);
        }
    }

    static void drawAnts(GraphicsContext context) {
        context.clearRect(0, 0, WIDTH, HEIGHT);
        for (Ant ant : ants) {
            context.setFill(Color.BLACK);
            context.fillOval(ant.x, ant.y, 5, 5);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Ants");
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.show();

        Canvas canvas = (Canvas) primaryStage.getScene().lookup("#canvas");
        GraphicsContext context = canvas.getGraphicsContext2D();

        createAnts();
        drawAnts(context);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
