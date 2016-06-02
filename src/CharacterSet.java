import java.util.ArrayList;

/**
 * Created by VYTKB on 6/1/2016.
 */
public class CharacterSet {
    private char[] characterTable ;
    private boolean[] setValues;
    private double tolerance;
    public CharacterSet(int characterRangeStart,int characterRangeLength,double tolerance){
        this(new char[0],tolerance);
        for (int i = 0; i < characterRangeLength; i++) {
           addCharacter((char)i);
        }


    }

    public CharacterSet(String s,double tolerance){
        this(s.toCharArray(),tolerance);
    }

    public CharacterSet(char[] charArray,double tolerance){
        this.tolerance = tolerance;
        characterTable = new char[(int)Math.pow(2,(Settings.getCharImageSize()*Settings.getCharImageSize())/(Settings.getPRECISION()*Settings.getPRECISION()))];
        setValues= new boolean[(int)Math.pow(2,(Settings.getCharImageSize()*Settings.getCharImageSize())/(Settings.getPRECISION()*Settings.getPRECISION()))];
        for(char c : charArray){
           addCharacter(c);

        }
    }

    public void addCharacter(char c){
        int value = BitwiseCalculator.calculateBitwize(c,tolerance);
        characterTable[value]=c;
        setValues[value]=true;
    }


    public char getMatch(int value){
        int subVal=-1;
        int difference=0;
        do{
            subVal=value;
            ArrayList<Integer> masks = MaskGenerator.getMasks((Settings.getCharImageSize()*Settings.getCharImageSize())/(Settings.getPRECISION()*Settings.getPRECISION()),difference); //get masks to alter the bitwise value
            for (int mask : masks){
                subVal=value ^ mask;
                if(setValues[subVal]){
                    if(!setValues[value]){
                        setValues[value]=true;
                        characterTable[value]=characterTable[subVal];
                        return characterTable[subVal];
                    }
                    return characterTable[subVal];
                }
            }
            difference++;
        }while(!setValues[subVal]);
        return characterTable[subVal];
    }
}
