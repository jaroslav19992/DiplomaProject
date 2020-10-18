package TestMaker.Assets.Animation;

import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.scene.effect.Glow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.ArrayList;

public class LoadingAnimation extends Thread {
    private static final int LOADING_RADIUS = 50;
    private static final int CIRCLE_RADIUS = 6;
    private static final int ANIMATION_DURATION = 1300; //ms
    private static final int CYCLE_COUNT = 1000;
    private static final int GLOBAL_DELAY = 120; //ms
    private static final int NUMBER_OF_CIRCLES = 6;
    private static final String CIRCLES_COLOR = ("c6dbe4");
    private static final String BACKGROUND_COLOR = "rgba(0, 0, 0, 0.28)";
    private static final Double RATE = 1.1;
    private static final int THREAD_SLEEP_TIME = 400;
    private final Pane pane;
    public static ArrayList<PathTransition> animatedElements = new ArrayList<>();
    AnchorPane loadingPane = new AnchorPane();


    public LoadingAnimation(Pane pane) {
        this.pane = pane;
        Platform.runLater(this::createAnimation);
    }

    private void createAnimation() {
        //create animation pane
        loadingPane.setStyle("-fx-background-color : " + BACKGROUND_COLOR);
        loadingPane.setPrefSize(pane.getPrefWidth(), pane.getPrefHeight());

        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setBottomAnchor(pane, 0.0);
        AnchorPane.setTopAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(loadingPane, 0.0);
        AnchorPane.setLeftAnchor(loadingPane, 0.0);
        AnchorPane.setBottomAnchor(loadingPane, 0.0);
        AnchorPane.setTopAnchor(loadingPane, 0.0);

        //add animation pane to parent pane
        pane.getChildren().add(loadingPane);

        double startXPos = pane.getPrefWidth() / 2 + CIRCLE_RADIUS;
        double startYPos = pane.getPrefHeight() / 2 - LOADING_RADIUS / 2.0;

        System.out.println(startXPos);
        System.out.println(startYPos);

        int localDelay = 0;
        //Create Circle and animate them
        for (int i = 0; i < NUMBER_OF_CIRCLES; i++) {
            //create circle
            Circle circle = createCircle(startXPos, startYPos);

            //add circle to group
            loadingPane.getChildren().add(circle);
            //create moving path
            Path path = new Path();
            path.getElements().add(new MoveTo(startXPos, startYPos));
            //set up moving curves
            path.getElements().add(new CubicCurveTo(startXPos + LOADING_RADIUS - CIRCLE_RADIUS, startYPos,
                    startXPos + LOADING_RADIUS - CIRCLE_RADIUS, startYPos + LOADING_RADIUS +
                    CIRCLE_RADIUS * 2, startXPos, startYPos + LOADING_RADIUS + CIRCLE_RADIUS * 2));
            path.getElements().add(new CubicCurveTo(startXPos - LOADING_RADIUS + CIRCLE_RADIUS,
                    startYPos + LOADING_RADIUS + CIRCLE_RADIUS, startXPos - LOADING_RADIUS + CIRCLE_RADIUS,
                    startYPos + CIRCLE_RADIUS * 2, startXPos, startYPos));
            //set up animation properties
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(ANIMATION_DURATION));
            pathTransition.setPath(path);
            pathTransition.setNode(circle);
            pathTransition.setCycleCount(CYCLE_COUNT);
            pathTransition.setDelay(Duration.millis(localDelay));
            pathTransition.setAutoReverse(false);
            pathTransition.setRate(RATE);
            animatedElements.add(pathTransition);
            localDelay += GLOBAL_DELAY;
            //make animation invisible
            loadingPane.toBack();
            loadingPane.setVisible(false);
        }
    }

    private Circle createCircle(double startXPos, double startYPos) {
        Circle circle = new Circle(startXPos, startYPos, CIRCLE_RADIUS);
        circle.setFill(javafx.scene.paint.Paint.valueOf(CIRCLES_COLOR));
        circle.setEffect(new Glow());
        return circle;
    }

    /**
     * Start to play animation
     */
    public void play() {
        System.out.println("Play animation");
        //Platform.runLater used because simple *.toFront cause "not an JavaFX Thread exception"
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                loadingPane.toFront();
                loadingPane.setVisible(true);
                animatedElements.forEach(Animation::play);
            }
        });

    }

    /**
     * Stop to animate on loading pane
     */
    public void stopAnimation() {
        //Platform.runLater used because simple *.toBach cause "not an JavaFX Thread exception"
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                animatedElements.forEach(Animation::stop);
                loadingPane.toBack();
                loadingPane.setVisible(false);
            }
        });

    }

    /**
     * start to play animation on loading pane
     * in loop checking for thread interruption
     * if interrupted control goes to catch block and thread work is stopped
     */
    @Override
    public void run() {
        Thread current = Thread.currentThread();
        play();
        System.out.println("Animation thread: " + this.getName() + " was started");
        try {
            while (!current.isInterrupted()) {
                Thread.sleep(THREAD_SLEEP_TIME);
                System.out.println("Animation thread: " + this.getName() + " is running...");
            }
        } catch (InterruptedException e) {
            System.out.println("Animation thread: " + this.getName() + " was interrupted");
            stopAnimation();
        }
    }

    @Override
    public void interrupt() {
        Platform.runLater(super::interrupt);
    }

    @Override
    public synchronized void start() {
        Platform.runLater(super::start);
    }
}
