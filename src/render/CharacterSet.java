package render;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by VYTKB on 6/1/2016.
 */
public class CharacterSet {
    private char[] characterTable ;
    private boolean[] setValues;
    private double tolerance;
    private static final double CHARACTER_TOLERANCE = .16;
    public CharacterSet(int characterRangeStart,int characterRangeLength,double tolerance){
        this(new char[0],tolerance);

        for (int i = 0; i < characterRangeLength; i++) {
           addCharacter((char)(i+characterRangeStart));
        }
        addCharacter(' ');

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
        addCharacter(' ');
    }

    public void saveCharacterTable(){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("charactertable.tsv")));
            bw.write("int\t");
            bw.write("char\t");
            bw.write("bin\t");
            bw.write("unicode");
            bw.newLine();
            for (int i = 0; i < setValues.length; i++) {
                if(setValues[i]){
                    bw.write(i+"\t");
                    if(characterTable[i]!='\\' && characterTable[i]!='/')
                        bw.write(characterTable[i]+"\t");
                    else
                        bw.write("\\\t");
                    bw.write(Integer.toBinaryString(i)+"\t");
                    bw.write(""+(int)characterTable[i]);
                    bw.newLine();
                }
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addCharacter(char c){
        if(isValidCharacter(c)){
            int value = BitwiseCalculator.calculateBitwize(c,CHARACTER_TOLERANCE);
            if(setValues[value] && characterTable[value]!=c){
                setValues[value] = false;
                char[] characters = new char[2];
                characters[0] = c;
                characters[1] = characterTable[value];
                assignCharacters(characters,CHARACTER_TOLERANCE-.001);
            }else{
                characterTable[value]=c;
                setValues[value]=true;
            }
        }
    }

    private boolean isValidCharacter(char c){
        if(c=='\u0083' ||c=='\u0082' || c=='\u0081' || c=='\u0080' ){//white space /r rendered as box
            return false;
        }
        if((int)c==127){//delete character
            return false;
        }

        return true;
    }

    private void assignCharacters(char[] characters,double tolerance){
        if(tolerance<=0){
            char c = characters[0];
            int value = BitwiseCalculator.calculateBitwize(c,CHARACTER_TOLERANCE);
            characterTable[value] = c;
            setValues[value] = true;
        }
        else{
            int[] bitwiseValues = new int[characters.length];
            for (int i = 0; i < characters.length; i++) {
                bitwiseValues[i] = BitwiseCalculator.calculateBitwize(characters[i],tolerance);
            }
            ArrayList<Character> charactersWithDuplicateValues = new ArrayList<>();
            for (int k = 0; k < bitwiseValues.length; k++) {
                for (int j = k; j < bitwiseValues.length; j++) {
                    if(setValues[bitwiseValues[k]] || (k!=j && bitwiseValues[k]==bitwiseValues[j])){
                        if(charactersWithDuplicateValues.indexOf(characters[k])==-1){
                            charactersWithDuplicateValues.add(characters[k]);
                        }
                    }
                }
            }
            for (int o = 0; o < characters.length; o++) {
                if(charactersWithDuplicateValues.indexOf(characters[o])==-1){
                    setValues[bitwiseValues[o]]=true;
                    characterTable[bitwiseValues[o]] = characters[o];
                }
            }
            if(charactersWithDuplicateValues.size()>0){
                char[] remainingCharacters = new char[charactersWithDuplicateValues.size()];
                for (int p = 0; p < charactersWithDuplicateValues.size(); p++) {
                    remainingCharacters[p] = charactersWithDuplicateValues.get(p);
                }
                assignCharacters(remainingCharacters,tolerance-.001);
            }
        }

    }


    public char getMatch(int value){
        int subVal;
        int difference=0;
        do{
            subVal=value;
            ArrayList<Integer> masks = MaskGenerator.getMasks((Settings.getCharImageSize()*Settings.getCharImageSize())/(Settings.getPRECISION()*Settings.getPRECISION()),difference); //get masks to alter the bitwise value
            for (int mask : masks){
                subVal=value ^ mask;
                if(setValues[value ^ mask]){
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

    public int getCharacterIndex(char c){
        for (int i = 0; i < characterTable.length; i++) {
            if(c==characterTable[i])
                return i;
        }
        return -1;
    }
}
