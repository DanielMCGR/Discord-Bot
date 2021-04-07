package casino;

import net.dv8tion.jda.api.entities.MessageChannel;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class serves as the basis
 * for a playable BlackJack game.
 *
 * @author Daniel Rocha
 * @version 1.0
 * @see Card
 * @see Deck
 */
public class BlackJack {
    private final long playerID;
    private Deck deck;
    private ArrayList<Card> playerHand = new ArrayList<>();
    private ArrayList<Card> dealerHand = new ArrayList<>();
    private MessageChannel messageChannel;
    private int condition = 0;
    private int wager = 0;

    /**
     * Default constructor for the class,
     * creates a set of 4 decks and
     * shuffles it, setting both hands.
     * 
     * @param playerID the player's ID
     */
    public BlackJack(long playerID) {
        this.playerID=playerID;
        deck = new Deck(4);
        deck.randomShuffle();
        setHands();
    }

    /**
     * Gets the wager that the player
     * bet on the game.
     * 
     * @return the waged value
     */
    public int getWager() {
        return wager;
    }

    /**
     * Sets the wager that the player
     * will bet on the game.
     * 
     * @param wager the waged value
     */
    public void setWager(int wager) {
        this.wager = wager;
    }

    /**
     * Gets the playing player's ID
     * 
     * @return the player's ID
     */
    public long getPlayerID() {
        return playerID;
    }

    /**
     * Sets the message channel for
     * easier reading of future commands
     * 
     * @param messageChannel the message channel
     */
    public void setMessageChannel(MessageChannel messageChannel) {
        this.messageChannel = messageChannel;
    }

    /**
     * Gets the message channel for
     * easier reading of future commands
     * 
     * @return the message channel
     */
    public MessageChannel getMessageChannel() {
        return messageChannel;
    }

    /**
     * Sets both hands (the dealer has one
     * of his cards hidden and the remaining
     * ones are all visible)
     */
    private void setHands() {
        dealerHand.add(deck.takeCard());
        dealerHand.add(deck.takeCard());
        playerHand.add(deck.takeCard());
        playerHand.add(deck.takeCard());
        Collections.sort(dealerHand);
        Collections.sort(playerHand);
        dealerHand.get(0).setExposed(false);
    }

    /**
     * Gets the value of a given hand
     * (numbers are their value, ace is
     * 1 or 11 and remaining ones are 10)
     * 
     * @param hand the hand to be valued
     * @return the hand's value
     */
    private int getHandValue(ArrayList<Card> hand) {
        int value=0;
        for(Card card:hand) {
            if(card.getValue()==1) {
                if(value+11<22) {
                    value+=11;
                } else {
                    value+=1;
                }
            } else {
                value+= Math.min(card.getValue(), 10);
            }
        }
        return value;
    }

    /**
     * Checks if the game has a blackjack
     * (instant win) on the first round
     * 
     * @return if there is a blackjack
     */
    public String firstOver() {
        String over = "";
        int playerScore=getHandValue(playerHand);
        int dealerScore=getHandValue(dealerHand);
        if(playerScore==21) {
            over = "You have won! (Blackjack)";
            int newWager = wager*3;
            Currency.addCurrency(playerID,newWager/2);
        }
        if(dealerScore==21) {
            over = "You have lost! (House Blackjack)";
            Currency.removeCurrency(playerID,wager);
        }
        return over;
    }

    /**
     * Checks if the game is over (there was
     * a tie, someone busted or there is a
     * higher value)
     *
     * @return if there is a blackjack
     */
    public String gameOver() {
        switch (condition) {
            case 1 -> {
                Currency.addCurrency(playerID,wager);
                return "You win! (Higher Value)";
            }
            case 2 -> {
                Currency.removeCurrency(playerID,wager);
                return "The House wins! (Higher Value)";
            }
            case 3 -> {
                return "There was a tie! (No one wins)";
            }
        }
        String over = "";
        int playerScore=getHandValue(playerHand);
        int dealerScore=getHandValue(dealerHand);
        if(playerScore>=21) {
            Currency.removeCurrency(playerID,wager);
            over = "The House wins! (You busted)";
        }
        if(dealerScore>=21) {
            Currency.addCurrency(playerID,wager);
            over = "You have won! (The dealer busted)";
        }
        if(!over.equals("")) dealerHand.get(0).setExposed(true);
        return over;
    }

    /**
     * If the dealer's hand values less than
     * 17 adds a card from the deck, and then
     * checks for winning conditions
     */
    public void dealerHit() {
        dealerHand.get(0).setExposed(true);
        while(getHandValue(dealerHand)<17) {
            dealerHand.add(deck.takeCard());
        }
        if(getHandValue(dealerHand)==getHandValue(playerHand)) condition=3;
        if(getHandValue(dealerHand)>getHandValue(playerHand)) condition=2;
        if(getHandValue(dealerHand)<getHandValue(playerHand)) condition=1;
    }

    /**
     * Takes a card from the deck and
     * adds it to the player's hand
     */
    public void playerHit() {
        playerHand.add(deck.takeCard());
    }

    @Override
    public String toString() {
        String dealerValue = dealerHand.get(0).isExposed() ? "  (Value:" +getHandValue(dealerHand)+ ")":"";
        return "Dealer's hand: " + dealerHand + dealerValue + "\n" +
                "Player's hand: " + playerHand + "  (Value:" + getHandValue(playerHand) + ")";
    }
}
