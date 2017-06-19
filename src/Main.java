import render.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
	    Settings s = new Settings(.14,new File("C:\\development\\Image Engine\\data\\cards\\maxresdefault.jpg"));
//        Settings s = new Settings(.14,new File("C:\\Users\\Alex\\Dropbox\\c4d\\wave.png"));
//	    Settings s = new Settings(.4,new File("H:\\wallpapers\\"));
//	    Settings s = new Settings(.3,new File("/Users/alexthomas/Downloads/A._S._Bradford_House.JPG"));
//        s.setDebug(true);
        s.setCharacterSet(new render.CharacterSet(32,100,s.getTolerance()));
        s.getCharacterSet().saveCharacterTable();
        try{
            BufferedImage bi = ImageIO.read(s.getFile());
            double tolerance = LuminanceCalculator.calculateLuminance(bi)/255;
            System.out.println(tolerance);
//            s.setTolerance(tolerance);
//            ImageToAsciiImage tiai = new ImageToAsciiImage(s);
//            tiai.renderImages();
//            SuperiorAsciiImageRender sair = new SuperiorAsciiImageRender(.01,.99,s);
//            sair.renderImages();

        } catch (IOException e) {
            e.printStackTrace();
        }
//        s.setCharacterSet(new CharacterSet("-/\\||[]() ",s.getTolerance()));

        ImageToAsciiImage tiai = new ImageToAsciiImage(s);
        tiai.renderImages();
        ImageToAsciiText itat = new ImageToAsciiText(s);
        itat.renderText();
    }
}
