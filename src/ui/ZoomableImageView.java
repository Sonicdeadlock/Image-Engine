package ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;

/**
 * Created by alexthomas on 11/2/15.
 */
public class ZoomableImageView extends VBox {
    private ImageView image;
    private Label lable;
    private ZoomHelper helper;


    private static final double IMAGE_FIT_SIZE=500;
    Rectangle2D viewportRectangle = new Rectangle2D(0,0,IMAGE_FIT_SIZE,IMAGE_FIT_SIZE);


    public ZoomableImageView(ZoomHelper helper){
        super();
        this.helper = helper;
        this.setMaxSize(IMAGE_FIT_SIZE, IMAGE_FIT_SIZE);
        image = new ImageView();
        lable = new Label();
        image.setPreserveRatio(true);
        image.setFitWidth(IMAGE_FIT_SIZE);
        image.setFitHeight(IMAGE_FIT_SIZE);
        //image.setViewport(viewportRectangle);
        this.getChildren().addAll(lable,image);
        lable.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(15));
        this.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                helper.zoomFactorProperty().set(helper.getZoomFactor() * (1 + event.getDeltaY() / 200));
            }
        });
        this.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                helper.setPreviousX(event.getX());
                helper.setPreviousY(event.getY());
            }
        });
        this.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                helper.setCurrent(event.getX(), event.getY());
            }
        });
        helper.dragDeltaXProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                moveZoomBox();
            }
        });
        helper.dragDeltaYProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                moveZoomBox();
            }
        });
        helper.zoomFactorProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double height = image.getFitHeight(),
                        width = image.getFitWidth();
                height /= newValue.doubleValue();
                width /= newValue.doubleValue();
                viewportRectangle = new Rectangle2D(viewportRectangle.getMinX(), viewportRectangle.getMinY(), height, width);
                image.setViewport(viewportRectangle);


            }
        });

    }


    public ZoomableImageView(ZoomHelper helper,Image i){
        this(helper);
        this.setImage(i);
    }

    public ZoomableImageView(ZoomHelper helper,String text){
        this(helper);
        this.setLable(text);
    }

    public ZoomableImageView(ZoomHelper helper,Image i,String text){
        this(helper);
        setImage(i);
        setLable(text);
    }

    public void setImage(Image i){
        image.setImage(i);
        helper.setZoomFactor(1);
    }

    public void setLable(String text){
        lable.setText(text);
    }

    public ZoomHelper getHelper() {
        return helper;
    }

    public void setHelper(ZoomHelper helper) {
        this.helper = helper;
    }

    public void moveZoomBox(){
        viewportRectangle = new Rectangle2D(
                viewportRectangle.getMinX()+helper.getDragDeltaX()/helper.getZoomFactor(),
                viewportRectangle.getMinY()+helper.getDragDeltaY()/helper.getZoomFactor(),
                viewportRectangle.getHeight(),
                viewportRectangle.getWidth());
       image.setViewport(viewportRectangle);
    }
}
