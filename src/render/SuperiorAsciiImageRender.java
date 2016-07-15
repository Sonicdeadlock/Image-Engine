package render;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by vytkb on 6/28/2016.
 */
public class SuperiorAsciiImageRender {
    private double minTolerance;
    private double maxTolerance;
    private double toleranceIncrement=.01;
    private Settings settings;



    public SuperiorAsciiImageRender(double minTolerance, double maxTolerance, Settings settings) {
        this.minTolerance = minTolerance;
        this.maxTolerance = maxTolerance;
        this.settings = settings;
        settings.setDebugRender(false);
    }

    public SuperiorAsciiImageRender(double minTolerance, double maxTolerance, double toleranceIncrement, Settings settings) {

        this.minTolerance = minTolerance;
        this.maxTolerance = maxTolerance;
        this.toleranceIncrement = toleranceIncrement;
        this.settings = settings;
        settings.setDebugRender(false);
    }




    public void renderImages(){
        BufferedImage initialImage=null;
        BufferedWriter dataWriter=null;
        ExecutorService executor = Executors.newFixedThreadPool(12);
        try {
            initialImage = ImageIO.read(settings.getFile());
            dataWriter = new BufferedWriter(new FileWriter(new File("data.tsv")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (dataWriter != null) {
                dataWriter.write("Run #\t");
                dataWriter.write("Image Tolerance\t");
                dataWriter.write("Character Tolerance\t");
                dataWriter.write("Error Count\t");
                dataWriter.write("Character Count\t");
                dataWriter.write("Inaccuracy");
                dataWriter.newLine();
                dataWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage finalImage = new BufferedImage(initialImage.getWidth(),initialImage.getHeight(),BufferedImage.TYPE_INT_RGB);
        int bestRunIndex=-1,bestRunValue=Integer.MAX_VALUE,runIndex=0;
        for (double imageTolerance = minTolerance; imageTolerance < maxTolerance; imageTolerance+=toleranceIncrement) {
            for (double characterTolerance = minTolerance; characterTolerance < maxTolerance; characterTolerance+=toleranceIncrement) {
                settings = new Settings(imageTolerance,settings.getFile());
                settings.setCharacterSet(new CharacterSet(32,100,characterTolerance));
                settings.setDebugRender(false);
                executor.execute(new TestRunner(dataWriter,runIndex,imageTolerance,characterTolerance,settings,initialImage,finalImage));
                runIndex++;
            }
        }
    }

    private static int getCharacterCount(Settings settings, BufferedImage initialImage){
        ImageToAsciiText itat = new ImageToAsciiText(settings);
        String text = itat.processText(initialImage);
        int count = 0;

        for (String line:
             text.split("\n")) {
            for (char c:
                 line.toCharArray()) {
                if(c==' ')
                    count++;
            }
        }
        return count;

    }

    private class TestRunner extends Thread{
        private BufferedWriter dataWriter;
        private int runIndex;
        private double imageTolerance,characterTolerance;
        private DebugImagePartProcessor debugImageProcessor;
        private Settings settings;
        private BufferedImage initialImage;

        public TestRunner(BufferedWriter dataWriter, int runIndex, double imageTolerance, double characterTolerance, Settings settings, BufferedImage initialImage,BufferedImage finalImage) {

            this.dataWriter = dataWriter;
            this.runIndex = runIndex;
            this.imageTolerance = imageTolerance;
            this.characterTolerance = characterTolerance;
            this.debugImageProcessor = new DebugImagePartProcessor(initialImage,finalImage,0,0,initialImage.getWidth(),initialImage.getHeight(),settings);
            this.settings = settings;
            this.initialImage = initialImage;
        }

        @Override
        public void run() {
            System.out.println("Starting: "+runIndex);
            int errorValue = debugImageProcessor.debugImageProcess();
            int charCount = getCharacterCount(settings,initialImage);
            try {
                if (dataWriter != null) {
                    synchronized (dataWriter){
                        dataWriter.write(runIndex+"\t");
                        dataWriter.write(imageTolerance+"\t");
                        dataWriter.write(characterTolerance+"\t");
                        dataWriter.write(errorValue+"\t");
                        dataWriter.write(charCount+"\t");
                        dataWriter.write((1.0*errorValue/charCount)+"\t");
                        dataWriter.newLine();
                        dataWriter.flush();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            super.run();
        }
    }
}
