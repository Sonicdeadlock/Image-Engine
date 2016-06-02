import java.util.ArrayList;

/**
 * Created by VYTKB on 6/1/2016.
 */
public class MaskGenerator {
    /**
     *
     * @param origMask the mask that is being added on to (used for recursive calls)
     * @param masks the list of masks that is being added to
     * @param maskLength the length of the mask
     * @param quantityChanges the number of values that will be true in the mask
     * @param index the index of the mask that is being altered (used for recursive calls)
     */
    private static void generateMasks(boolean[] origMask, ArrayList<Integer> masks, int maskLength, int quantityChanges, int index){
        boolean[] amask,bmask;
        int countTrue = 0;
        if(origMask==null){//means this is the 0 recursive call and two starting masks need to be generated
            amask = new boolean[maskLength];
            bmask = new boolean[maskLength];
        }else{//otherwise count the number of changes in the starting mask and clone it
            for (boolean anOrigMask : origMask) {//count the changes
                if (anOrigMask) {
                    countTrue++;
                }
            }
            amask = origMask.clone();//clone the mask twice
            bmask = origMask.clone();
        }

        if(countTrue==quantityChanges){//if the number of changes has been reached
            for (int i=index;i<amask.length;i++){//set the remaining values to false
                amask[i]=false;
            }
            masks.add(boolArrayToInt(amask));//add the value to the list. This ends the recursive loop
        }else if(maskLength-index==quantityChanges-countTrue){//if the only way to fill the number of changes is to set all the values to true
            for (int i=index;i<amask.length;i++){//set the remaining values to true
                amask[i]=true;
            }
            masks.add(boolArrayToInt(amask));//add the value to the list. This ends the recursive loop
        }else{
            amask[index]=true;//create the two possibilities
            bmask[index]=false;
            index++;//increment the index
            if(index==maskLength){//if the masks are full
                masks.add(boolArrayToInt(amask));//add the masks to the list
                masks.add(boolArrayToInt(bmask));
            }
            else{//the masks are not full
                generateMasks(amask,masks,maskLength,quantityChanges,index);//make the next recursive calls
                generateMasks(bmask,masks,maskLength,quantityChanges,index);
            }
        }
    }
    public static ArrayList<Integer> getMasks(int maskLength,int quantityChanges){
        ArrayList<Integer> masks = new ArrayList<Integer>();
        generateMasks(null, masks, maskLength, quantityChanges, 0);
        return  masks;
    }
    private static int boolArrayToInt(boolean[] array){
        int value =0;
        for(int i=0;i<array.length;i++){
            if(array[i])
                value = value | 1 << i;
        }
        return value;
    }
}
