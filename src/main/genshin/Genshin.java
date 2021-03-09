package genshin;

/**
 * This class is used to help players of the video game
 * Genshin Impact, by automating calculations. *
 *
 * @author Daniel Rocha
 * @version 1.0
 * @see genshin.DayInfo
 */

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Genshin {

    static boolean exit;
    static final SimpleDateFormat FORM = new SimpleDateFormat("HH:mm, dd/MM/yy");
    static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yy");
    static final SimpleDateFormat HOUR_FORM = new SimpleDateFormat("HH:mm");
    static final File DATA = new File("Data.txt");

  //the main function that allows the usage of this class are removed since it is only used here by the Discord Bot
  public static void main(String[] args) { }

    /**
     * Sets the specified starting date with the given values
     * and populates the 60 days after
     *
     * @param starter the starting date for populating
     * @param file the file where data is stored
     * @param values values for DayInfo (primo, xp and lvl (we already know the date))
     */
    public static void SetDay(Date starter, File file, String[] values) {
        try {
            int starterLine = GetDayLine(starter, file);
            String[] array = TextFileHandler.GetFileAsArray(file);
            for(int i=0;i<3;i++) {
                array[starterLine+1+i]=values[i];
            }
            TextFileHandler.SetFileFromArray(array, file);
            Populate(starter, 60, DATA);
            } catch (Exception e) {
                return;
            }
    }

    /**
     * Gets the line number of the specified date in the file
     *
     * @param starter the date to search for
     * @param file the file where data is stored
     * @return line number of specified date
     */
    //note: the first 8 lines (0 to 7) of the text file are "locked". first line of every date is blank
    public static int GetDayLine(Date starter, File file) {
        Calendar starterCal = Calendar.getInstance();
        starterCal.setTime(starter);
        Calendar stringCal = Calendar.getInstance();
        Date stringDate = new Date();
        int pos = 9;
        try {
            String[] array = TextFileHandler.GetFileAsArray(file);
            for (int i = 9; i < (array.length - 8); i++) {
                if (array[i].length()!=13) {
                    continue;
                }
                stringDate.setTime(Long.parseLong(array[i]));
                stringCal.setTime(stringDate);
                if (DateCheck(stringCal, starterCal)) {
                    pos = i;
                    i = array.length - 8;
                    return pos;
                }
            }
        } catch (Exception e) {}
        return pos;
    }

    /**
     * Gets the day the player will have a certain amount of primogems
     *
     * @param primos the amount of gems
     * @return returns the closest date (as a complete String)
     */
    public static String getPrimoDay(int primos) {
        Calendar now = Calendar.getInstance();
        int starter = GetDayLine(now.getTime(), DATA)+1;
        try {
            String[] file = TextFileHandler.GetFileAsArray(DATA);
            Long date = Long.parseLong(file[starter]);
            starter+=5;
            int pos = 0;
            while(starter+5<file.length) {
                int firstInt = Math.abs(Integer.parseInt(file[starter])-primos);
                int secondInt = Math.abs(Integer.parseInt(file[starter-5])-primos);
                if(firstInt<=secondInt){
                    date = Long.parseLong(file[starter-1]);
                    pos = starter-1;

                }
                starter+=5;
            }
            Date primoDate = new Date();
            primoDate.setTime(date);
            if(Integer.parseInt(file[GetDayLine(primoDate, DATA)+1])<primos&&pos+5<file.length) {
                date = Long.parseLong(file[pos+5]);
                primos = Integer.parseInt(file[pos+6]);
            } else {
                primos = Integer.parseInt(file[pos+1]);
            }
            System.out.println(starter);
            primoDate.setTime(date);
            return "You will get "+primos+" primogems on "+DATE_FORMAT.format(primoDate)+" (This is the closest value, rounded up, that I could get!";
        } catch (Exception e) {
            return e.toString();
        }
    }

    /**
     * Checks if two dates are the same date
     * @param calOne the first date to check
     * @param calTwo the second date to check
     * @return whether or not they are the same
     */
    public static boolean DateCheck(Calendar calOne, Calendar calTwo) {
        return (calOne.get(Calendar.DAY_OF_MONTH)==calTwo.get(Calendar.DAY_OF_MONTH)&&calOne.get(Calendar.MONTH)==calTwo.get(Calendar.MONTH)&&calOne.get(Calendar.YEAR)==calTwo.get(Calendar.YEAR));
    }

    /**
     * Populates the text file with information for a certain amount of days
     * starting from a specified starter date.
     * <p>
     * Note: the first 8 lines (0 to 7) of the text file are "locked". first line of every date is blank
     *
     * @param starter the starting date
     * @param days the amount of days after the starting date
     * @param file the file where data is stored
     */
    public static void Populate(Date starter, int days, File file){
        Long.toString(starter.getTime());
        try{
            String[] array = TextFileHandler.GetFileAsArray(file);
            int pos = GetDayLine(starter, file)-1;
            int nextPos = pos+5;
            String[] newArray = new String[(days*5)+nextPos];
            for(int i=0;i<nextPos;i++) {
                newArray[i]=array[i];
            }
            DayInfo starterInfo = new DayInfo();
            starterInfo.Set(starter,Integer.parseInt(array[pos+2]),Integer.parseInt(array[pos+3]),Integer.parseInt(array[pos+4]));
            DayInfo[] dayArray= new DayInfo[days+1];
            dayArray[0] = starterInfo;
            for(int i=0;i<days;i++) {
                dayArray[i+1] = dayArray[i];
                dayArray[i+1].AddDay();
                dayArray[i+1].AddPrimo(10+10+10+10+20+90);
                dayArray[i+1].AddXP(250+250+250+250+500+900);
                newArray[(5*i)+nextPos]=" ";
                newArray[(5*i)+nextPos+1]=Long.toString(dayArray[i+1].date.getTime());
                newArray[(5*i)+nextPos+2]=Integer.toString(dayArray[i+1].primo);
                newArray[(5*i)+nextPos+3]=Integer.toString(dayArray[i+1].xp);
                newArray[(5*i)+nextPos+4]=Integer.toString(dayArray[i+1].lvl);
            }
            TextFileHandler.SetFileFromArray(newArray, DATA);
        } catch (Exception e) {
            System.out.println(e);
            return;
        }
    }

    /**
     * tells the player when he will get 160 resin based on how many he has
     * (if the value entered is below 160) or how long it will take to have the
     * entered amount of resin (if the entered value is 160 or above)
     *
     * @param resin the amount of resin the player has/wants
     * @return returns when the player will get 160 resin (as a full sentence)
     */
    public static String getFullResinTime(int resin){
        Calendar now = Calendar.getInstance();
        int firstDay = now.get(Calendar.DAY_OF_YEAR);
        if(resin>159) {
            now.add(Calendar.MINUTE, resin*8);
            return("You will get " + resin +" resin at "+ FORM.format(now.getTime()));
        }
        resin=160-resin;
        now.add(Calendar.MINUTE, resin*8);
        int secondDay = now.get(Calendar.DAY_OF_YEAR);
        String day = "";
        if(secondDay-firstDay==1) {
            day = " tomorrow";
        } else {
            day = " today";
        }
        return("You will get full resin (160) at "+ HOUR_FORM.format(now.getTime())+day);
    }
}
