import java.io.File;

public class Main {

    public static void main(String[] args) {
	    Settings s = new Settings(.21,new File("J:\\Norfolk_Southern_,_Kingwood_Texas.jpg"));
        s.setCharacterSet(new CharacterSet("norfolksouthermNORFOLKSOUTHERN ",s.getTolerance()));
        ImageToAsciiImage tiai = new ImageToAsciiImage(s);
        tiai.renderImages();
    }
}
