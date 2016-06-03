import render.CharacterSet;
import render.ImageToAsciiImage;
import render.ImageToAsciiText;
import render.Settings;

import java.io.File;

public class Main {

    public static void main(String[] args) {
//	    Settings s = new Settings(.1,new File("C:\\Users\\VYTKB\\Pictures\\23626.jpg"));
	    Settings s = new Settings(.1,new File("H:\\wallpapers"));
        s.setCharacterSet(new render.CharacterSet("norfolksouthermNORFOLKSOUTHERN ",s.getTolerance()));
//        s.setCharacterSet(new CharacterSet("chair ",s.getTolerance()));
//        ImageToAsciiImage tiai = new ImageToAsciiImage(s);
//        tiai.renderImages();
        ImageToAsciiText itat = new ImageToAsciiText(s);
        itat.renderText();
    }
}
