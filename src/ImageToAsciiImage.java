import javax.annotation.processing.Processor;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class ImageToAsciiImage {
    private Settings settings;
    private long totalPixes;
    private long pixesRendered;
    private BufferedImage startImage,finalImage;
    private ImagePartProcessor[] currentThreads;

    public ImageToAsciiImage(Settings settings){
        this.settings = settings;
    }

    public void renderImages(){
        totalPixes=0;
        pixesRendered=0;
        ArrayList<File> files = new ArrayList<File>();
        File new_file=null;
        if(settings.getFile().isDirectory()){
            listFilesForFolder(settings.getFile(), files);
            new_file=new File(settings.getFile().getPath()+"_ascii");
            new_file.mkdir();
        }
        else
            files.add(settings.getFile());
        for (File f:files){
            try {
                BufferedImage bi = ImageIO.read(f);
                if(bi !=null){
                    totalPixes+=bi.getHeight()*bi.getWidth();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for(int i=0;i<files.size();i++){
            try {
                File f = files.get(i);
                BufferedImage bi = ImageIO.read(f);
                if(bi==null)
                    continue;
                BufferedImage finalImage = processImage(bi);

                if(settings.getFile().isDirectory()){
                    int dotIndex = f.getName().indexOf(".");
                    ImageIO.write(finalImage, "png", new File(new_file.getPath()+"/"+f.getName().substring(0, dotIndex)+"_Ascii.png"));
                    System.out.println(new_file.getPath()+"/"+f.getName().substring(0, dotIndex)+"_Ascii.png");
                }
                else{
                    int dotIndex = f.getPath().indexOf(".");
                    ImageIO.write(finalImage, "png", new File(f.getPath().substring(0, dotIndex)+"_Ascii.png"));}
                //System.out.println(f.getName().substring(0, dotIndex)+"_Ascii.png"+" done");

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        this.pixesRendered = totalPixes;
    }
    private BufferedImage processImage(BufferedImage bi){
        BufferedImage finalImage = new BufferedImage(bi.getWidth(),bi.getHeight(),BufferedImage.TYPE_INT_RGB);
        startImage = bi;
        this.finalImage = finalImage;
        int threadCount=1;//should be square
        int sectionLengthCount = (int)Math.sqrt(threadCount);
        int width = finalImage.getWidth()/sectionLengthCount;
        int height = finalImage.getHeight()/sectionLengthCount;
        currentThreads = new ImagePartProcessor[threadCount];
        for(int x=0;x<sectionLengthCount;x++){
            for(int y=0;y<sectionLengthCount;y++){
                currentThreads[x*sectionLengthCount+y]= new ImagePartProcessor(bi,finalImage,x*width,y*height,width,height,settings);
                currentThreads[x*sectionLengthCount+y].start();
            }
        }
        for(ImagePartProcessor p : currentThreads){
            try {
                p.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        pixesRendered+=finalImage.getWidth()*finalImage.getHeight();
        return finalImage;
    }

    public void listFilesForFolder(final File folder,ArrayList<File> files) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry,files);
            } else {
                files.add(fileEntry);
            }
        }
    }


    public long getPixesRendered(){
        long temp = pixesRendered;
        for(ImagePartProcessor p : currentThreads){
            temp+=p.getPixesRendered();
        }
        return temp;

    }

    public double getPercentDone(){
        if(totalPixes==0)
            return 0;
        return (double)getPixesRendered()/totalPixes*1;
    }

}
