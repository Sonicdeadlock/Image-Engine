import render.ImageToAsciiImage;
import render.Settings;

import java.io.File;

/**
 * Created by Alex on 7/4/2016.
 */
public class Interpreter {
    public static void main(String[] args) {
        //expected arguments image dir,tolerance,characterset
        if(args.length<2){
            throw new IllegalArgumentException("Missing arguments. (Image Directory) and (Tolerance) expected, (Character set) optional");
        }
        double tolerance;
        try {
            tolerance = Double.parseDouble(args[1]);
        }catch (Exception ex){
            throw new IllegalArgumentException("Argument (Tolerance) is expected to be a number");
        }
        if(tolerance>1){
            throw new IllegalArgumentException("Argument (Tolerance) is expected to be less than one");
        }
        File originalImage = new File(args[0]);
        if(!originalImage.exists()){
            throw new IllegalArgumentException("Argument (Image Directory) is expected to exist");
        }
        Settings s = new Settings(tolerance,originalImage);
        if(args.length>=3)
            s.setCharacterSet(new render.CharacterSet(args[2],tolerance));
        ImageToAsciiImage tiai = new ImageToAsciiImage(s);
        tiai.renderImages();

    }
}
