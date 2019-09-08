package com.pleon.chopchop;

import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main extends Application {

    private double xOffset = 0;
    private double yOffset = 0;
    private boolean firstTime;
    private TrayIcon trayIcon;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        SvgImageLoaderFactory.install(); // enable svg wherever other formats are acceptable

        // Parent root = FXMLLoader.load(getClass().getResource("/fxml/scene-main.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/scene-splash.fxml"));
        createTrayIcon(primaryStage);
        firstTime = true;
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("Chop Chop");
        primaryStage.getIcons().add(new Image("/logo.svg"));
        primaryStage.setOpacity(1.0);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setResizable(false);
        primaryStage.toFront();
        Platform.setImplicitExit(false); // for minimize to tray to work correctly
        // primaryStage.setX(0 - 10); // dou to padding and inset in .root{} rule in css we
        // subtract 10
        // primaryStage.setY(0 - 10);

        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT); // for drop shadow to show correctly
        // CircularProgressBar progressBar = (CircularProgressBar) root.lookup
        // ("CircularProgressBar");
        // scene.setOnKeyPressed(event -> {
        //     if (event.getCode() == KeyCode.UP) {
        //         progressBar.increase();
        //     } else if (event.getCode() == KeyCode.DOWN) {
        //         progressBar.decrease();
        //     }
        // });
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createTrayIcon(final Stage stage) throws AWTException {
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            java.awt.Image trayImage = Toolkit.getDefaultToolkit().createImage(
                    getImage("/tray/1.png")
            );

            ActionListener showListener = e -> Platform.runLater(stage::show);

            PopupMenu popup = new PopupMenu();

            MenuItem showItem = new MenuItem("Show");
            showItem.addActionListener(showListener);
            popup.add(showItem);

            MenuItem closeItem = new MenuItem("Exit");
            closeItem.addActionListener(e -> System.exit(0));
            popup.add(closeItem);

            /// ... add other items

            // construct a TrayIcon
            trayIcon = new TrayIcon(trayImage, "Chop chop", popup);
            trayIcon.addActionListener(showListener);

            tray.add(trayIcon);

            trayIcon.displayMessage("Some message.", "Some other message.",
                    TrayIcon.MessageType.INFO);

            java.awt.Image[] trayImages = new java.awt.Image[53];
            for (int i = 0; i < 53; i++) {
                String path = String.format("/tray/%d.png", i + 1);
                trayImages[i] = Toolkit.getDefaultToolkit().createImage(getImage(path));
            }

            Timeline trayAnimation = new Timeline();
            trayAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(50),
                    new EventHandler<>() {
                        int firstFrameDelay = 0;
                        int i = 0;

                        @Override
                        public void handle(ActionEvent event) {
                            trayIcon.setImage(trayImages[i]);
                            if (i == 0 && firstFrameDelay < 100) {
                                firstFrameDelay++;
                                if (firstFrameDelay == 100) {
                                    firstFrameDelay = 0;
                                    i++;
                                }
                            } else {
                                i = (i + 1) % trayImages.length;
                            }
                        }
                    }));
            trayAnimation.setCycleCount(Timeline.INDEFINITE);
            trayAnimation.play();
        }
    }

    private static byte[] getImage(String path) {
        InputStream inputStream = Main.class.getResourceAsStream(path);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int readBytesCount;
        byte[] data = new byte[16384];
        try {
            while ((readBytesCount = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, readBytesCount);
            }
            buffer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toByteArray();
    }

    // private void drawShapes(GraphicsContext gc) {
    //     gc.fillArc(50, 50, 200, 200, 0, 75, ArcType.ROUND);
    //     gc.strokeRoundRect(10, 10, 50, 50, 10, 10);
    // }
    //
    // private void drawText(GraphicsContext gc) {
    //     gc.setFont(new Font("vazir", 16));
    //     gc.setTextAlign(TextAlignment.CENTER);
    //     gc.fillText("سلام Hello", 35, 35);
    // }
}
