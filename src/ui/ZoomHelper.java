package ui;

import javafx.beans.property.SimpleDoubleProperty;

/**
 * Created by alexthomas on 11/9/15.
 */
public class ZoomHelper {
    private SimpleDoubleProperty zoomFactor = new SimpleDoubleProperty(1),
                                 dragDeltaX = new SimpleDoubleProperty(),
                                 dragDeltaY = new SimpleDoubleProperty(),
                                 previousX = new SimpleDoubleProperty(),
                                 previousY = new SimpleDoubleProperty();



    public double getZoomFactor() {
        return zoomFactor.get();
    }

    public SimpleDoubleProperty zoomFactorProperty() {
        return zoomFactor;
    }

    public double getDragDeltaX() {
        return dragDeltaX.get();
    }

    public SimpleDoubleProperty dragDeltaXProperty() {
        return dragDeltaX;
    }

    public double getDragDeltaY() {
        return dragDeltaY.get();
    }

    public SimpleDoubleProperty dragDeltaYProperty() {
        return dragDeltaY;
    }

    public void setDragDeltaX(double dragDeltaX) {
        this.dragDeltaX.set(dragDeltaX);
    }

    public void setDragDeltaY(double dragDeltaY) {
        this.dragDeltaY.set(dragDeltaY);
    }

    public void setZoomFactor(double zoomFactor) {
        this.zoomFactor.set(zoomFactor);
    }

    public double getPreviousX() {
        return previousX.get();
    }

    public SimpleDoubleProperty previousXProperty() {
        return previousX;
    }

    public void setPreviousX(double previousX) {
        this.previousX.set(previousX);
    }

    public double getPreviousY() {
        return previousY.get();
    }

    public SimpleDoubleProperty previousYProperty() {
        return previousY;
    }

    public void setPreviousY(double previousY) {
        this.previousY.set(previousY);
    }

    public void setCurrent(double x,double y){
        setDragDeltaX((getPreviousX() - x));
        setDragDeltaY((getPreviousY() - y));
        setPreviousX(x);
        setPreviousY(y);
    }
}
