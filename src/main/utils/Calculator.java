package utils;

/**
 * This class is used for basic math applications
 * to use in the Discord Bot
 *
 * @author Daniel Rocha
 * @version 1.0
 * @see bot
 */
public class Calculator {

    public static void main(String[] args) { }

    /**
     * Gives a random value between the specified range
     *
     * @param min minimum value
     * @param max maximum value
     * @return the random value
     */
    public static int GetRandom(int min, int max) {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    /**
     * Used for the whatIs command in Discord Bot,
     * does basic calculations
     *
     * @param args the arguments provided by the user to the Bot
     * @return the solution to the equation
     * @throws Exception
     * @see bot
     */
    public static double whatIs(String args) throws Exception{
        try {
            double solution = 0;
            String[] splitArgs = args.split(" ");
            if(splitArgs.length!=3) { throw new Exception("Invalid Arguments");}
            double firstArg = Double.parseDouble(splitArgs[0]);
            if(splitArgs[1].length()!=1) {throw new Exception("Invalid Arguments");}
            char operator = splitArgs[1].charAt(0);
            double secondArg = Double.parseDouble(splitArgs[2]);
            switch (operator) {
                case '-' -> solution = firstArg-secondArg;
                case '+' -> solution = firstArg+secondArg;
                case '*','x','X' -> solution = firstArg*secondArg;
                case '/' -> solution = firstArg/secondArg;
                case '^' -> solution = Math.pow(firstArg,secondArg);
            }
            return solution;
        } catch (Exception e) {
            throw e;
        }
    }
}
