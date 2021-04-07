package casino;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> deck;

    /**
     * Creates a new deck, consisting
     * of a given amount of 52 card multiples
     * (thus allowing to have multiple decks
     * in one)
     * 
     * @param amount amount of decks
     */
    public Deck(int amount) {
        ArrayList<Card> deck = new ArrayList<>(0);
        int inc = 0;
        while(inc!=amount) {
            for (int i = 0; i < 13; i++) {
                Card Card = new Card(Suit.CLUBS, i + 1, false);
                deck.add(Card);
            }
            for (int i = 0; i < 13; i++) {
                Card Card = new Card(Suit.DIAMONDS, i + 1, false);
                deck.add(Card);
            }
            for (int i = 0; i < 13; i++) {
                Card Card = new Card(Suit.HEARTS, i + 1, false);
                deck.add(Card);
            }
            for (int i = 0; i < 13; i++) {
                Card Card = new Card(Suit.SPADES, i + 1, false);
                deck.add(Card);
            }
            inc++;
        }
        this.deck=deck;
    }

    /**
     * Removes a card from the deck
     * (usually into another card array)
     * 
     * @return the removed card
     */
    public Card takeCard() {
        Card card = deck.get(0);
        deck.remove(0);
        card.setExposed(true);
        return card;
    }

    /**
     * Randomly shuffles the deck. 
     */
    public void randomShuffle() {
        Collections.shuffle(deck);
    }
}
