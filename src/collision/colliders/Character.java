package collision.colliders;

import collision.Collider;
import collision.CollisionDetails;
import collision.DynamicCollider;
import collision.spatial.*;
import collision.spatial.Point;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Alex on 8/21/2016.
 */
public class Character extends DynamicCollider {
    private char character;
    private Font font;
    private static final Color RENDER_FOREGROUND_COLOR = Color.WHITE,RENDER_BACKGROUND_COLOR = Color.BLACK;
    public Character(int x, int y, Direction direction,char character) {
        super(x, y, direction);
        this.character = character;
    }

    public Character(int x,int y,char character){
        super(x,y,Direction.NONE);
        this.character = character;
    }

    public Character(int x, int y, Direction direction,char character,Font font) {
        this(x,y,direction,character);
        this.font = font;
    }

    public Character(int x,int y,char character,Font font){
        this(x,y,Direction.NONE,character);
        this.character = character;
        this.font = font;
    }

    @Override
    protected void renderTakenPoints() {
        BufferedImage bi = getCharacterRenderedOnImage();
        this.localTakenPoints = new boolean[bi.getWidth()][bi.getHeight()];
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                localTakenPoints[x][y] = bi.getRGB(x,y)==RENDER_FOREGROUND_COLOR.getRGB();
            }
        }
        removeSelfUnnecessaryEdges();
    }

    private BufferedImage getCharacterRenderedOnImage(){
        int size = font.getSize();
        BufferedImage bi = new BufferedImage(size,size,BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.getGraphics();
        g.setColor(RENDER_BACKGROUND_COLOR);
        g.fillRect(0,0,size,size);
        g.setColor(RENDER_FOREGROUND_COLOR);
        g.drawString(character+"",0,size);
        return bi;
    }

    private void removeSelfUnnecessaryEdges(){
        this.localTakenPoints = removeUnnecessaryXEdges(removeUnnecessaryYEdges(this.localTakenPoints));
    }

    private boolean[][] removeUnnecessaryYEdges(boolean[][] array){
        boolean hasTrue = false;
        for (boolean[] row:
             array) {
            if(row[row.length-1]){
                hasTrue=true;
            }
        }
        if(hasTrue){
            return array;
        }else{
            boolean[][] temp = new boolean[array.length][array[0].length-1];
            for (int x = 0; x < array.length; x++) {
                System.arraycopy(array[x], 0, temp[x], 0, array[0].length - 1);
            }
            return removeUnnecessaryYEdges(temp);
        }
    }

    private boolean[][] removeUnnecessaryXEdges(boolean[][] array){
        boolean hasTrue = false;
        for (boolean[] row : array) {
            for (int y = 0; y < array[0].length; y++) {
                if (row[y]) {
                    hasTrue = true;
                }
            }
        }
        if(hasTrue){
            return array;
        }else{
            boolean[][] temp = new boolean[array.length-1][array[0].length];
            System.arraycopy(array, 1, temp, 0, array.length - 1);
            return removeUnnecessaryXEdges(temp);
        }
    }

    @Override
    public CollisionDetails hasCollision(Collider c) {
        for (int x = 0; x < localTakenPoints.length; x++) {
            for (int y = 0; y < localTakenPoints[0].length; y++) {
                if(c.hasCollisionAtPoint(new Point(x,y))){
                    Vector v;
                    if(c instanceof DynamicCollider){
                        v = Vector.subtract(this.getVelocity(),((DynamicCollider) c).getVelocity());
                    }else{
                        v = this.getVelocity();
                    }
                    ArrayList<Collider> list = new ArrayList<>();
                    list.add(this);
                    list.add(c);
                    CollisionDetails details = new CollisionDetails(v,list);
                    return details;
                }
            }
        }
        return null;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public char getCharacter() {
        return character;
    }
}
