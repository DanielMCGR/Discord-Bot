package codeWords;

import bot.GamesContainer;
import net.dv8tion.jda.api.entities.TextChannel;
import utils.Calculator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * This class serves as the blueprint for a CodeWords
 * game. Using it's functions you can fully play.
 * It's usage is, however, better defined for my
 * Discord Bot project.
 *
 * @author Daniel Rocha
 * @version 1.0
 * @see GamesContainer
 * @see bot.Kebbot
 */
public class CodeWords {
    private Card[][] game;
    private ArrayList<String> words;
    private ArrayList<Long> players;
    private ArrayList<Long> teamRed;
    private ArrayList<Long> teamBlue;
    private Long blueSpyID;
    private Long redSpyID;
    private Color turn;
    private TextChannel textChannel;
    private int[] scores;
    private int spyNum;

    /**
     * Default constructor for the class.
     * Randomly sets the board and it's cards.
     */
    public CodeWords() {
        while(words==null) words=WordHandler.getWords();
        game = new Card[5][5];
        setTable();
        this.scores= new int[]{9,8};
        this.turn=Color.RED;
        this.players=new ArrayList<>();
        this.teamBlue=new ArrayList<>();
        this.teamRed=new ArrayList<>();
    }

    /**
     * Sets the blue Spy's id (usually Discord id)
     *
     * @param blueSpyID the spy's id
     */
    public void setBlueSpyID(Long blueSpyID) {
        this.blueSpyID = blueSpyID;
    }

    /**
     * Gets the blueSpy's id (usually Discord id)
     *
     * @return the spy's id
     */
    public Long getBlueSpyID() {
        return blueSpyID;
    }

    /**
     * Gets the red Spy's id (usually Discord id)
     *
     * @return the spy's id
     */
    public Long getRedSpyID() {
        return redSpyID;
    }

    /**
     * Sets the red Spy's id (usually Discord id)
     *
     * @param redSpyID the spy's id
     */
    public void setRedSpyID(Long redSpyID) {
        this.redSpyID = redSpyID;
    }

    /**
     * Gets the Discord text channel where the game was started,
     * to make sure it only reads commands from that channel.
     *
     * @return the text channel
     */
    public TextChannel getTextChannel() {
        return textChannel;
    }

    /**
     * Sets the Discord text channel where the game was started,
     * to make sure it only reads commands from that channel.
     *
     * @param textChannel the text channel
     */
    public void setTextChannel(TextChannel textChannel) {
        this.textChannel = textChannel;
    }

    /**
     * Gets the scores for each team.
     *
     * @return the scores
     */
    public int[] getScores() {
        return scores;
    }

    /**
     * Sets the scores for each team.
     *
     * @param  scores the scores
     */
    public void setScores(int[] scores) {
        this.scores = scores;
    }

    /**
     * Gets the spyNum variable's value (
     * spyNum is the remaining amount of
     * guesses a team has for their turn)
     *
     * @return the number of guesses
     */
    public int getSpyNum() {
        return spyNum;
    }

    /**
     * Sets the spyNum variable's value (
     * spyNum is the remaining amount of
     * guesses a team has for their turn)
     *
     * @param spyNum the number of guesses
     */
    public void setSpyNum(int spyNum) {
        this.spyNum = spyNum;
    }

    /**
     * Gets the current turn's Color.
     *
     * @return the color
     */

    public Color getTurn() {
        return turn;
    }

    /**
     * Sets the current turn's Color.
     *
     * @param turn the color
     */
    public void setTurn(Color turn) {
        this.turn = turn;
    }

    /**
     * Checks if a given player's id is currently
     * on the player list, and if so, returns the
     * player's team as a Color.
     *
     * @param id player's id
     * @return the team color
     */
    public Color getTeam(Long id) {
        for(int i=0;i<teamBlue.size();i++) {
            if((teamBlue.get(i)).equals(id)) return Color.BLUE;
        }
        for(int i=0;i<teamRed.size();i++) {
            if((teamRed.get(i)).equals(id)) return Color.RED;
        }
        return null;
    }

    /**
     * Gets the score of a given guess.
     *
     * @param team the current team
     * @param guess the guessed word
     * @return the score
     */
    public int guessScore(Color team, String guess) {
        Card card = getCard(guess);
        if(card.getValue()==Color.BLACK) return -9999;
        if(card.getValue()==Color.WHITE) return 0;
        if(card.getValue()==team) return 1;
        if(card.getValue()!=team) return -1;
        return 9999;
    }

    /**
     * Gets the current list of players (their id's)
     * that are on team Blue
     *
     * @return the id list
     */
    public ArrayList<Long> getTeamBlue() {
        return teamBlue;
    }

    /**
     * Gets the current list of players (their id's)
     * that are on team Red
     *
     * @return the id list
     */
    public ArrayList<Long> getTeamRed() {
        return teamRed;
    }

    /**
     * Sets both teams (from the list of players,
     * it randomly divides the list in half and
     * each half corresponds to a team)
     *
     * @return the id list
     */
    public void setTeams() {
        ArrayList<Long> players = this.players;
        Collections.shuffle(players);
        if(players.size()%2==0) {
            for(int i=0;i<players.size()/2;i++) {
                teamRed.add(players.get(i));
            }
            for(int i=players.size()/2;i<players.size();i++) {
                teamBlue.add(players.get(i));
            }
        } else {
            for(int i=0;i<players.size()/2;i++) {
                teamRed.add(players.get(i));
            }
            for(int i=players.size()/2;i<players.size();i++) {
                teamBlue.add(players.get(i));
            }
        }
    }

    /**
     * Gets the current list of players (their id's)
     * that are on both teams.
     *
     * @return the id list
     */
    public ArrayList<Long> getPlayers() {
        return players;
    }

    /**
     * Adds a player to the player list (given the player's id)
     *
     * @param id the player's id
     */
    public void addPlayers(Long id) {
        if(players.contains(id)) return;
        players.add(id);
    }

    /**
     * Checks if there is a card with content corresponding
     * to the given one, and if so, returns that card.
     *
     * @return the id list
     */
    public Card getCard(String cardContent) {
        cardContent=cardContent.toLowerCase();
        for(int i=0;i<5;i++) {
            for(int j=0;j<5;j++) {
                if(cardContent.equals(game[i][j].content.toLowerCase())) return game[i][j];
            }
        }
        return null;
    }

    /**
     * Checks if the game is over (a team reached 0 cards left)
     *
     * @return if game is over
     */
    public boolean isGameOver() {
        return (scores[0]==0||scores[1]==0);
    }

    /**
     * Sets the current table (the game), creating 25 blank cards with
     * randomized words and then randomly turns 9 red,
     * 8 blue and 1 black, thus making the game playable.
     */
    private void setTable() {
        for(int i=0;i<5;i++) {
            for(int j=0;j<5;j++) {
                int value = ((i*5)+(j));
                game[i][j]=new Card(Color.WHITE,words.get(value));
            }
        }
        int count=0;
        while(count<9) {
            int randomI = Calculator.GetRandom(0,4);
            int randomJ = Calculator.GetRandom(0,4);
            if(game[randomI][randomJ].getValue()==Color.WHITE) {
                game[randomI][randomJ].setValue(Color.RED);
                count++;
            }
        }
        count=0;
        while(count<8) {
            int randomI = Calculator.GetRandom(0,4);
            int randomJ = Calculator.GetRandom(0,4);
            if(game[randomI][randomJ].getValue()==Color.WHITE) {
                game[randomI][randomJ].setValue(Color.BLUE);
                count++;
            }
        }
        boolean clear = false;
        while(!clear) {
            int randomI = Calculator.GetRandom(0,4);
            int randomJ = Calculator.GetRandom(0,4);
            if(game[randomI][randomJ].getValue()==Color.WHITE) {
                game[randomI][randomJ].setValue(Color.BLACK);
                clear=true;
            }
        }
    }

    /**
     * Gets necessary information for both spies,
     * the list of cards and which ones are
     * red, blue and black.
     *
     * @return the info for the spy
     */
    public String spyInfo() {
        String out="";
        Card black = null;
        ArrayList<Card> red = new ArrayList<>();
        ArrayList<Card> blue = new ArrayList<>();
        for(int i=0;i<5;i++) {
            for(int j=0;j<5;j++) {
                if(game[i][j].getValue()==Color.BLACK) black = game[i][j];
                if(game[i][j].getValue()==Color.RED) red.add(game[i][j]);
                if(game[i][j].getValue()==Color.BLUE) blue.add(game[i][j]);
            }
        }
        String redList = "";
        for(int i=0;i<red.size();i++) {
            if(i!=red.size()-1) {
                redList+=red.get(i)+", ";
            } else {
                redList+=red.get(i);
            }
        }
        String blueList = "";
        for(int i=0;i<blue.size();i++) {
            if(i!=blue.size()-1) {
                blueList+=blue.get(i)+", ";
            } else {
                blueList+=blue.get(i);
            }
        }
        out+="The Black card is: "+black.content+".\n"+
                "The Red cards are: "+redList+".\n"+
                "The Blue cards are: "+blueList+".\n";

        return out;
    }

    /**
     * Overrides the toString method for easier understanding.
     *
     * @return the game as a String
     */
    @Override
    public String toString() {
        String board = "";
        for(int i=0;i<5;i++) {
            board+= Arrays.toString(game[i]) +"\n";
        }
        return "This CodeWord game was brought to you by Kebakko\n" +
                "Team Red has "+scores[0]+" cards left and Team Blue has "+scores[1]+" cards left.\n"+
                board;
    }
}
