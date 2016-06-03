package ui;

import render.CharacterSet;
import render.ImageToAsciiImage;

import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import render.Settings;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.NumberFormat;

/**
 * Created by alexthomas on 11/2/15.
 */
public class AsciiRenderScene extends Scene {
    private ZoomableImageView beforeImage, afterImage;
    private ProgressBar progressBar;

    public AsciiRenderScene(@NamedArg("root") Parent root) {
        super(root);
    }

    public AsciiRenderScene(@NamedArg("root") Parent root, @NamedArg("width") double width, @NamedArg("height") double height) {
        super(root, width, height);
    }

    public AsciiRenderScene(@NamedArg("root") Parent root, @NamedArg(value = "fill", defaultValue = "WHITE") Paint fill) {
        super(root, fill);
    }

    public AsciiRenderScene(@NamedArg("root") Parent root, @NamedArg("width") double width, @NamedArg("height") double height, @NamedArg(value = "fill", defaultValue = "WHITE") Paint fill) {
        super(root, width, height, fill);
    }

    public AsciiRenderScene(@NamedArg("root") Parent root, @NamedArg(value = "width", defaultValue = "-1") double width, @NamedArg(value = "height", defaultValue = "-1") double height, @NamedArg("depthBuffer") boolean depthBuffer) {
        super(root, width, height, depthBuffer);
    }

    public AsciiRenderScene(@NamedArg("root") Parent root, @NamedArg(value = "width", defaultValue = "-1") double width, @NamedArg(value = "height", defaultValue = "-1") double height, @NamedArg("depthBuffer") boolean depthBuffer, @NamedArg(value = "antiAliasing", defaultValue = "DISABLED") SceneAntialiasing antiAliasing) {
        super(root, width, height, depthBuffer, antiAliasing);


    }

    public void init(Pane p) {
        ZoomHelper zh = new ZoomHelper();
        beforeImage = new ZoomableImageView(zh,"Before");
        afterImage = new ZoomableImageView(zh,"After");
        HBox hbox = new HBox();
        hbox.getChildren().addAll(beforeImage, afterImage);
        VBox vBox = new VBox(12);
        progressBar = new ProgressBar(0);
        Button selectFileButton = new Button();
        selectFileButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        selectFileButton.textProperty().set("Select File");
        FileChooser fileChooser = new FileChooser();
        GridPane inputGridPane = new GridPane();
        Slider toleranceSlider = new Slider();
        toleranceSlider.maxProperty().set(1);
        toleranceSlider.minProperty().set(0);
        toleranceSlider.setValue(.06);
        toleranceSlider.blockIncrementProperty().set(.01);
        Label toleranceLabel = new Label("Tolerance: .06");
        GridPane.setConstraints(toleranceLabel, 0, 0);
        GridPane.setConstraints(toleranceSlider, 1, 0);
        TextField fileInput = new TextField();
        GridPane.setConstraints(fileInput, 1, 1);
        GridPane.setConstraints(selectFileButton, 0, 1);
        Label rangeLabel = new Label("Character range");
        TextField rangeInput = new TextField("100");
        GridPane.setConstraints(rangeLabel, 0, 2);
        GridPane.setConstraints(rangeInput, 1, 2);
        inputGridPane.setHgap(6);
        inputGridPane.setVgap(6);
        inputGridPane.getChildren().addAll(toleranceSlider, toleranceLabel, fileInput, rangeInput, rangeLabel, selectFileButton);
        toleranceLabel.setMinSize(110, Label.USE_PREF_SIZE);
        selectFileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File f = fileChooser.showOpenDialog(new Stage());
                fileInput.setText(f.getAbsolutePath());
                fileChooser.setInitialDirectory(f.getParentFile());
            }
        });
        toleranceSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                NumberFormat numberFormat = NumberFormat.getInstance();
                numberFormat.setMaximumFractionDigits(3);
                toleranceLabel.setText("Tolerance: " + numberFormat.format(newValue));
            }

        });
        fileInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                File f = new File(newValue);
                if (f.exists() && f.isFile()) {
                    try {
                        beforeImage.setImage(new Image(new FileInputStream(f)));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    beforeImage.setImage(null);
                }
            }
        });

        fileInput.setMinSize(400, 0);
        inputGridPane.setAlignment(Pos.TOP_CENTER);
        Button renderButton = new Button("Render");
        renderButton.setAlignment(Pos.CENTER);
        renderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File f = new File(fileInput.getText());
                if (f.exists() ) {
                    double tolerance;
                    int characterRange;
                    Settings settings;
                    try{
                        characterRange = Integer.parseInt(rangeInput.getText());
                        settings = new Settings(f,toleranceSlider.getValue(),characterRange,false);
                    }catch(Exception ex){
                        settings = new Settings(toleranceSlider.getValue(),f);
                        settings.setCharacterSet(new CharacterSet(rangeInput.getText(),toleranceSlider.getValue()));
                    }
                    ImageToAsciiImage itai = new ImageToAsciiImage(settings);
                    startProgress(itai, progressBar);
                }

            }
        });
        inputGridPane.setMinSize(1000, GridPane.USE_PREF_SIZE);
        progressBar.setMinSize(1000, ProgressBar.USE_PREF_SIZE);
        vBox.getChildren().addAll(inputGridPane, renderButton, progressBar, hbox);
        vBox.setPadding(new Insets(15));
        p.getChildren().add(vBox);


    }


    public void startProgress( ImageToAsciiImage imageToAsciiImage, ProgressBar progressBar) {
        Runnable renderTask = imageToAsciiImage::renderImages;
        Runnable progressTask = () -> {
            do {

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progressBar.setProgress(imageToAsciiImage.getPercentDone());

            } while (imageToAsciiImage.getPercentDone() < 1);

        };
        Runnable updateImageTask =() ->{
            BufferedImage startImage = imageToAsciiImage.getStartImage();
            BufferedImage finalImage = imageToAsciiImage.getFinalImage();
            int width = startImage.getWidth(), height = startImage.getHeight();
            if (imageToAsciiImage.getStartImage() != null)
                beforeImage.setImage(SwingFXUtils.toFXImage(startImage, new WritableImage(width, height)));
            if (imageToAsciiImage.getFinalImage() != null)
                afterImage.setImage(SwingFXUtils.toFXImage(finalImage, new WritableImage(width, height)));
        };
        Runnable updateTask = () -> {
            while(imageToAsciiImage.getStartImage()==null) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            do {
                Platform.runLater(updateImageTask);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (imageToAsciiImage.getPercentDone() < 1);
//            BufferedImage startImage = imageToAscii.getStartImage();
//            BufferedImage finalImage = imageToAscii.getFinalImage();
//            int width = startImage.getWidth(), height = startImage.getHeight();
//            if (imageToAscii.getStartImage() != null)
//                beforeImage.setImage(SwingFXUtils.toFXImage(startImage, new WritableImage(width, height)));
//            if (imageToAscii.getFinalImage() != null)
//                afterImage.setImage(SwingFXUtils.toFXImage(finalImage, new WritableImage(width, height)));

        };

        Thread renderThread = new Thread(renderTask);
        Thread progressThread = new Thread(progressTask);
        Thread updateThread = new Thread(updateTask);
        renderThread.start();
        progressThread.start();
        updateThread.start();

    }
}


