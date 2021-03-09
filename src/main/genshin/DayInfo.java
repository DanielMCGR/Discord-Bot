package genshin;
import java.io.File;
import java.util.*;

/**
 * This class is a blueprint for use in the Genshin class
 * so it is easier to calculate the level, xp and amount
 * of primogems a player will have on a certain date
 *
 * @author Daniel Rocha
 * @version 1.0
 * @see Genshin
 */

public class DayInfo {
    private static Calendar cal = Calendar.getInstance();
    public static Date date;
    public static int primo;
    public static int xp;
    public static int lvl;
    public static final Map<Integer, Integer> neededXP = getLevelMap();

    //the main method is only for testing, doesn't really serve any purpose since this class is meant to be called by other classes
    public static void main(String[] args) {
        try {
            File root = new File(Thread.currentThread().getContextClassLoader().getResource("").toURI());
            System.out.println(root.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(getLevelMap().toString());
    }

    /**
     * Defines the values of the variables: date, primo, xp, lvl
     *
     * @param date the day to set the values
     * @param primo the amount of primogems on the date
     * @param xp the amount of xp on the date
     * @param lvl the player level on the date
     */
    public static void Set(Date date, int primo, int xp, int lvl){
        DayInfo.date=date;
        DayInfo.primo=primo;
        DayInfo.xp=xp;
        DayInfo.lvl=lvl;
    }

    /**
     * Adds the specified amount to the variable primo
     *
     * @param amount the amount to add to primo
     */
    public static void AddPrimo(int amount) {
        DayInfo.primo+=amount;
    }

    /**
     * Adds the specified amount to the variable xp
     *
     * @param amount the amount to add to xp
     */
    public static void AddXP(int amount) {
        if( DayInfo.xp+amount>neededXP.get(DayInfo.lvl)) {
            DayInfo.xp= DayInfo.xp+amount-neededXP.get( DayInfo.lvl);
            DayInfo.lvl+=1;
        } else {
            DayInfo.xp+=amount;
        }
    }

    /**
     * Moves the date value by one day
     */
    public static void AddDay() {
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        date=cal.getTime();
    }

    /**
     * Prints on the console the values of the variables: date, primo, xp, lvl
     */
    public static void Properties(){
        System.out.println(DayInfo.date);
        System.out.println(DayInfo.primo);
        System.out.println(DayInfo.xp);
        System.out.println(DayInfo.lvl);
    }

    /**
     * Returns a Map to be used by final variable neededXP
     *
     * @return the Map with all levels (keys) and the amount of xp needed for the next one (value)
     */
    private static Map<Integer, Integer> getLevelMap() {
        Map<Integer, Integer> levels = new HashMap<>();
        File file = new File("AdventureRankXP.txt");
        try {
            String[] fileAsArray = TextFileHandler.GetFileAsArray(file);
            if(fileAsArray.length%2!=0) throw new Exception("The selected file does not work, please fix it (File Path: "+file.getAbsolutePath());
            int key=0;
            int value;
            for(int i=0;i<fileAsArray.length;i++) {
                if(i%2==0) {
                    key = Integer.parseInt(fileAsArray[i]);
                } else {
                    value = Integer.parseInt(fileAsArray[i]);
                    levels.put(key,value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return levels;
    }

}
