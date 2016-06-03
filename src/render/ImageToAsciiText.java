package render;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class ImageToAsciiText {
    private Settings settings;

    public ImageToAsciiText(Settings settings) {
        this.settings = settings;
    }

    private  String processText(BufferedImage bi){
        StringBuilder sb = new StringBuilder(bi.getHeight()*bi.getWidth()/Settings.getCharImageSize()+bi.getWidth()/Settings.getCharImageSize());
        for(int y=0;y<bi.getHeight();y+=Settings.getCharImageSize()){
            for(int x=0;x<bi.getWidth();x+=Settings.getCharImageSize()){
                try{
                    int value;
                    int blockX = x,
                            blockY = y,
                            blockWidth = Settings.getCharImageSize(),
                            blockHeight = Settings.getCharImageSize();
                    if(x>=Settings.getCharImageSize()){
                        blockX-=Settings.getCharImageSize();
                        blockWidth+=Settings.getCharImageSize();
                    }
                    if(x+Settings.getCharImageSize()<bi.getWidth()){
                        blockWidth+=Settings.getCharImageSize();
                    }
                    if(y>Settings.getCharImageSize()){
                        blockY-=Settings.getCharImageSize();
                        blockHeight+=Settings.getCharImageSize();
                    }
                    if(y+Settings.getCharImageSize()+1<bi.getHeight()){
                        blockHeight+=Settings.getCharImageSize();
                    }
                    double averageLuminance = LuminanceCalculator.calculateLuminance(bi.getSubimage(blockX,blockY,blockWidth,blockHeight));
                    value = BitwiseCalculator.calculateBitwize(bi.getSubimage(x, y, Settings.getCharImageSize(), Settings.getCharImageSize()),averageLuminance,settings);
                    char c = settings.getCharacterSet().getMatch(value);
                    if(value!=0)
                        sb.append(c);
                    else
                        sb.append(" ");
                }catch(Exception ex){
                    //ex.printStackTrace();
                }

            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public void renderText(){
        ArrayList<File> files = new ArrayList<File>();
        File new_file=null;
        if(settings.getFile().isDirectory()){
            ImageToAsciiImage.listFilesForFolder(settings.getFile(), files);
            new_file=new File(settings.getFile().getPath()+"_ascii");
            new_file.mkdir();
        }
        else
            files.add(settings.getFile());

        for(File f : files){
            try {

                BufferedImage bi = ImageIO.read(f);
                if(bi==null)
                    continue;
                String result = processText(bi);

                if(settings.getFile().isDirectory()){
                    int dotIndex = f.getName().indexOf(".");
                    FileWriter fw = new FileWriter(new File(new_file.getPath()+"/"+f.getName().substring(0, dotIndex)+"_Ascii.txt"));
                    fw.write(result);
                    fw.flush();
                    fw.close();
                }
                else{
                    int dotIndex = f.getPath().indexOf(".");
                    FileWriter fw = new FileWriter(new File(f.getPath().substring(0, dotIndex)+"_Ascii.txt"));
                    fw.write(result);
                    fw.flush();
                    fw.close();
                }


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private String removeEdgeSpaces(String start){
        char[][] grid;
        String[] lines = start.split("\n");
        grid = new char[lines.length][lines[0].length()];
        for (int i = 0; i < lines.length; i++) {
            grid[i] = lines[i].toCharArray();
        }

        for (int x = 0; x < grid[0].length; x++) {
            boolean isAllBlank = true;
            for (int y = 0; y < grid.length; y++) {
                if(grid[y][x]!=' '){
                    isAllBlank =false;
                }
            }

        }

        lines = new String[grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            lines[i]= new String(grid[i]);
        }
        StringBuilder sb = new StringBuilder(lines.length*lines[0].length());
        for (String line:
             lines) {
            sb.append(line);
            sb.append("\n");
        }
        return sb.toString();
    }

}
