package render;

import java.awt.*;
import java.io.File;


public class Settings {
    private static final int PRECISION =2;
    private static final int CHAR_IMAGE_SIZE=8;
    private double tolerance;
    private static final Font FONT = new Font("Courier New", Font.PLAIN,11);
    private int charRange = 150;
    private boolean useColor=false;
    private File file;
    private CharacterSet characterSet;

    public Settings(File file, double tolerance, int charRange, boolean useColor) {
        this.file = file;
        this.tolerance = tolerance;
        this.charRange = charRange;
        this.useColor = useColor;
        generateCharacterSet();
    }

    public Settings(double tolerance, File file) {
        this.tolerance = tolerance;
        this.file = file;
        generateCharacterSet();
    }

    public Settings(double tolerance, boolean useColor, File file) {
        this.tolerance = tolerance;
        this.useColor = useColor;
        this.file = file;
        generateCharacterSet();
    }

    public static int getPRECISION() {
        return PRECISION;
    }

    public static int getCharImageSize() {
        return CHAR_IMAGE_SIZE;
    }

    public double getTolerance() {
        return tolerance;
    }

    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    public static Font getFONT() {
        return FONT;
    }

    public int getCharRange() {
        return charRange;
    }

    public void setCharRange(int charRange) {
        this.charRange = charRange;
    }

    public boolean isUseColor() {
        return useColor;
    }

    public void setUseColor(boolean useColor) {
        this.useColor = useColor;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    private void generateCharacterSet(){
      this.characterSet = new CharacterSet(32,charRange,this.getTolerance());
    }

    public CharacterSet getCharacterSet() {
        return characterSet;
    }

    public void setCharacterSet(CharacterSet characterSet) {
        this.characterSet = characterSet;
    }
}
