package casino;

import genshin.TextFileHandler;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * This classes serves as the basis for
 * server economy, handling requests for
 * the wallet feature
 *
 * @author Daniel Rocha
 * @version 1.0
 * @see BlackJack
 * @see EuroMillions
 * @see bot.CommandList
 */
public class Currency {
    private static final File CURRENCY = new File("C:/Users/Kebakk/IdeaProjects/Kebbot/src/main/java/casino/currency.txt");

    /**
     * Adds a player to the database (txt file)
     * using the default amount of 500€
     * 
     * @param id the player's id
     */
    public static void addPlayer(long id) {
        try {
            double currency = 500.00;
            ArrayList<String> lines = new ArrayList<>(Arrays.asList(TextFileHandler.getFileAsArray(CURRENCY)));
            lines.add(Long.toString(id));
            lines.add(String.valueOf(currency));
            String[] linesArray = new String[lines.size()];
            TextFileHandler.setFileFromArray(lines.toArray(linesArray),CURRENCY);
        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * Gets a player's current currency,
     * and if the player is not on the 
     * database, adds him.
     * 
     * @param id the player's ID
     * @return the player's currency
     */
    public static double getCurrency(long id) {
        try {
            if(!TextFileHandler.fileContains(Long.toString(id),CURRENCY)) {
                addPlayer(id);
            }
            int line = TextFileHandler.lineOfString(Long.toString(id),CURRENCY);
            double val = Double.parseDouble(Objects.requireNonNull(TextFileHandler.ReadLine(line+1, CURRENCY)));
            return val;
        } catch (Exception e) {
            e.printStackTrace();
            return Integer.MIN_VALUE; }
    }

    /**
     * Sets a player's current currency,
     * and if the player is not on the 
     * database, adds him.
     *
     * @param id the player's id
     * @param currency the currency to be set
     */
    public static void setCurrency(long id, double currency) {
        try {
            if(!TextFileHandler.fileContains(Long.toString(id),CURRENCY)) {
                addPlayer(id);
            }
            int line = TextFileHandler.lineOfString(Long.toString(id),CURRENCY);
            TextFileHandler.setLine(line+1,String.valueOf(currency),CURRENCY);
        } catch (Exception ignored) { }
    }

    /**
     * Removes a given amount from
     * the player's current currency
     *
     * @param id the player's ID
     * @param currency the currency to remove
     */
    public static void removeCurrency(long id, double currency) {
        double i = getCurrency(id)-currency;
        setCurrency(id,i);
    }

    /**
     * Adds a given amount from
     * the player's current currency
     *
     * @param id the player's ID
     * @param currency the currency to add
     */
    public static void addCurrency(long id, double currency) {
        double i = getCurrency(id)+currency;
        setCurrency(id,i);
    }
}
