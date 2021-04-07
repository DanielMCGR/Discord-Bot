package casino;

import utils.Calculator;
import java.util.Arrays;

/**
 * This class serves as the
 * blueprint for the EuroMillions
 * lottery game.
 *
 * @author Daniel Rocha
 * @version 1.0
 */
public class EuroMillions {
    int[] numbers = new int[5];
    int[] stars = new int[2];

    /**
     * Default constructor for the
     * class, randomizes the numbers
     * and stars
     */
    public EuroMillions() {
        randomize();
    }

    /**
     * Randomly picks 5 numbers and
     * 2 stars from the valid
     * values (1 to 50 and 1 to 12)
     */
    public void randomize() {
        boolean unique = false;
        while(!unique) {
            for(int i = 0;i<numbers.length;i++) {
                numbers[i]=Calculator.getRandom(1,50);
            }
            Arrays.sort(numbers);
            for(int i = 0;i<stars.length;i++) {
                stars[i]=Calculator.getRandom(1,12);
            }
            Arrays.sort(stars);
            if(!hasDupes(numbers)&&!hasDupes(stars)) unique=true;
        }
    }

    /**
     * Checks if a given "guess" (wager)
     * won any of the prizes (according
     * to EuroMillions rules), returning
     * the amount or 0 if there is no prize
     * 
     * @param guess the player's guess
     * @return the prize
     */
    public double hasPrize(int[] guess) {
        double value=-1;
        int numbers=0;
        int stars=0;
        for(int i=0;i<5;i++) {
            for(int j=0;j<5;j++) {
                if(guess[i]==this.numbers[j]) {
                    numbers++;
                } } }
        for(int i=5;i<7;i++) {
            for(int j=0;j<2;j++) {
                if(guess[i]==this.stars[j]) {
                    stars++;
                } } }
        if(numbers==0) value=0;
        if(numbers==1&&stars==2) value=5.83;
        if(numbers==2) {
            if(stars==0) value=4.55;
            if(stars==1) value=5.83;
            if(stars==2) value=13.34;
        }
        if(numbers==3) {
            if(stars==0) value=11.19;
            if(stars==1) value=12.51;
            if(stars==2) value=58.33;
        }
        if(numbers==4) {
            if(stars==0) value=47.69;
            if(stars==1) value=135.81;
            if(stars==2) value=1700.74;
        }
        if(numbers==5) {
            if(stars==0) value=30031.53;
            if(stars==1) value=1027964.67;
            if(stars==2) value=144755907;
        }
        return value;
    }

    /**
     * Checks if a player's guess
     * is valid (the guessed values
     * are within valid range)
     * 
     * @param guess the player's guess
     * @return if guess is valid
     */
    public static boolean validGuess(int[] guess) {
        if(guess.length!=7) return false;
        if(hasDupes(guess)) return false;
        for(int i=0;i<5;i++) {
            if(guess[i]<1||guess[i]>50) return false;
        }
        for(int i=5;i<7;i++) {
            if(guess[i]<1||guess[i]>12) return false;
        }
        return true;
    }

    /**
     * Checks if an int array has duplicates
     * 
     * @param array the array to check
     * @return if array has duplicates
     */
    private static boolean hasDupes(int[] array) {
        for (int i=0;i<array.length;i++) {
            for (int j=0;j<array.length;j++) {
                if (i!=j && array[i] == array[j]) return true;
            } }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("The numbers are: ");
        for(int i=0;i<numbers.length;i++) {
            sb.append(numbers[i]);
            if(i==numbers.length-2) {
                sb.append(" and ");
            } else if(i<numbers.length-2) {
                sb.append(", ");
            }
        }
        sb.append(". The stars are: ");
        sb.append(stars[0]);
        sb.append(" and ");
        sb.append(stars[1]);
        sb.append(".");
        return sb.toString();
    }
}
