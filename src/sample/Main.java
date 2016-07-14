package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

public class Main extends Application {

    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    static final int ANT_COUNT = 100;

    static ArrayList<Ant> ants = new ArrayList<>();

    static long lastTimeStamp = 0;

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
            if (ant.isRed) {
                context.setFill(Color.RED);
            }
            else {
                context.setFill(Color.BLACK);
            }
            context.fillOval(ant.x, ant.y, 5, 5);
        }
    }

    static Ant aggravateAnt(Ant ant) {
        ArrayList<Ant> aggrAnts = ants.stream()
                .filter(antA ->
                     (Math.abs(ant.x - antA.x) <= 20) && (Math.abs(ant.y - antA.y) <= 20)
                )
                .collect(Collectors.toCollection(ArrayList<Ant>::new));

        if (aggrAnts.size() > 1) {
            ant.isRed = true;
        }
        else {
            ant.isRed = false;
        }
        return ant;
    }

    static Ant moveAnt(Ant ant) {
        ant.x += (Math.random() * 2) - 1; // Math.random returns between 0 and 1, but we want -1 to 1 for both directions
        ant.y += (Math.random() * 2) - 1;
        try {
            Thread.sleep(1);                 // used to slow ants
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ant;
    }

    static void moveAnts() {
        ants = ants.parallelStream()            // <-- parallelism speeds ants
                .map(Main::moveAnt)
                .map(Main::aggravateAnt)
                .collect(Collectors.toCollection(ArrayList<Ant>::new));
    }

    static int fps(long currentTimeStamp) {      // frames per second
        double diff = currentTimeStamp - lastTimeStamp;
        double diffSeconds = diff / 1000000000;
        return (int) (1/diffSeconds);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Ants");
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.show();

        Canvas canvas = (Canvas) primaryStage.getScene().lookup("#canvas");
        GraphicsContext context = canvas.getGraphicsContext2D();
        Label fpsLabel = (Label) primaryStage.getScene().lookup("#fps");

        createAnts();

        AnimationTimer timer = new AnimationTimer() {    // anonymous class
            @Override
            public void handle(long now) {
                moveAnts();
                drawAnts(context);
                fpsLabel.setText(String.valueOf(fps(now)));
                lastTimeStamp = now;
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
