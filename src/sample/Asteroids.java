package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


import java.util.ArrayList;

public class Asteroids extends Application {
    int score;
    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Asteroids");

        BorderPane root = new BorderPane();
        Scene mainScene = new Scene(root);
        primaryStage.setScene(mainScene);

        Canvas canvas = new Canvas(800 , 600);
        GraphicsContext context = canvas.getGraphicsContext2D();
        root.setCenter(canvas);

        Sprite background = new Sprite("file:src/images/space.png");
        background.position.set(400,300);
        background.render(context);
//        context.setFill(Color.BLUE);
//        context.fillRect(0,0,800,600);

        // handle continuous inputs as long as key is pressed
        ArrayList<String> keyPressedList = new ArrayList<>();
        //handle discrete inputs (once per key press)
        ArrayList<String> keyJustPressedList = new ArrayList<String>();


        mainScene.setOnKeyPressed((KeyEvent event) -> {
            String keyName = event.getCode().toString();
            //avoid adding duplicates to list
            if (!keyPressedList.contains(keyName)) {
                keyPressedList.add(keyName);
                keyJustPressedList.add(keyName);
            }

        });

        mainScene.setOnKeyReleased((KeyEvent event) -> {
            String keyName = event.getCode().toString();
            if (keyPressedList.contains(keyName)) {
                keyPressedList.remove(keyName);
            }
        });

        Sprite spaceship = new Sprite("file:src/images/spaceship.png");
        spaceship.position.set(100,300);
        spaceship.velocity.set(50,0);
        spaceship.render(context);

        ArrayList<Sprite> laserList = new ArrayList<Sprite>();
        ArrayList<Sprite> asteroidList = new ArrayList<Sprite>();

        int asteroidCount = 6;
        for (int n = 0; n<asteroidCount; n++) {

            Sprite asteroid = new Sprite("file:src/images/asteroid.png");
            double x = 500*Math.random() + 300; // 300-800
            double y = 400*Math.random() + 100; // 100 - 500
            asteroid.position.set(x,y);
            double angle = 360*Math.random();
            asteroid.velocity.setLength(50);
            asteroid.velocity.setAngle(angle);
            asteroidList.add(asteroid);
        }

        score = 0;
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long l) {
                //process user input
                if (keyPressedList.contains("LEFT")) {
                    spaceship.rotation-=3;
                }
                if (keyPressedList.contains("RIGHT")) {
                    spaceship.rotation+=3;
                }
                if (keyPressedList.contains("UP")) {
                    spaceship.velocity.setLength(150);
                    spaceship.velocity.setAngle(spaceship.rotation);
                } else {
                    spaceship.velocity.setLength(0);
                }
                if (keyJustPressedList.contains("SPACE")) {
                    Sprite laser = new Sprite("file:src/images/laser.png");
                    laser.position.set(spaceship.position.x, spaceship.position.y);
                    laser.velocity.setLength(400);
                    laser.velocity.setAngle(spaceship.rotation);
                    laserList.add(laser);
                }

                //after processing user input, clear justPressedList
                keyJustPressedList.clear();

                //update spaceship
                spaceship.update(1/60.0);
                //update lasers, destory if more than 2 seconds have passed
                for (int n = 0; n<laserList.size(); n++) {
                    Sprite laser = laserList.get(n);
                    laser.update(1/60.0);
                    if (laser.elapsedTime > 2) {
                        laserList.remove(n);
                    }
                }
                for (Sprite asteroid : asteroidList) {
                    asteroid.update(1/60.0);
                }

                //when laser overlaps asteroid, remove both
                for (int laserNum = 0; laserNum < laserList.size(); laserNum++) {
                    Sprite laser = laserList.get(laserNum);
                    for (int asteroidNum = 0; asteroidNum<asteroidList.size(); asteroidNum++) {
                        Sprite asteroid = asteroidList.get(asteroidNum);
                        if (laser.overlaps(asteroid)) {
                            laserList.remove(laserNum);
                            asteroidList.remove(asteroidNum);
                            score+=1;
                        }
                    }
                }

                background.render(context);
                spaceship.render(context);
                for (Sprite laser : laserList) {
                    laser.render(context);
                }
                for (Sprite asteroid : asteroidList) {
                    asteroid.render(context);
                }
                context.setFill(Color.WHITE);
                context.setStroke(Color.GREEN);
                context.setFont(new Font("Arial Black", 48));
                context.setLineWidth(3);

                String text = "Score: " + score;
                int textX = 500;
                int textY = 50;
                context.fillText(text, textX, textY);
                context.strokeText(text, textX, textY);

            }
        };
        gameLoop.start();
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
