package casino;

import org.jetbrains.annotations.NotNull;

public class Card implements Comparable<Card>{
    private final Suit suit;
    private final int value;
    private boolean exposed;

    /**
     * Basic constructor for a Card
     * 
     * @param suit the card's suit
     * @param value the card's value
     * @param exposed if card is exposed
     */
    public Card(Suit suit, int value, boolean exposed) {
        this.suit=suit;
        this.value=value;
        this.exposed=exposed;
    }

    /**
     * Gets if the card is exposed
     * (facing upwards) or not.
     * 
     * @return if card is exposed
     */
    public boolean isExposed() {
        return exposed;
    }

    /**
     * Sets if the card is exposed
     * (facing upwards) or not.
     * 
     * @param exposed if card will be exposed
     */
    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }

    /**
     * Gets the card's value, from 1
     * (Ace) to 13 (King)
     * 
     * @return card's value
     */
    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(@NotNull Card compareCard) {
        int compareValue=compareCard.getValue();
        return compareValue-this.value;
    }

    @Override
    public String toString() {
        String string = switch (this.suit) {
            case CLUBS -> "♣️";
            case DIAMONDS -> "♦️";
            case HEARTS -> "♥️";
            case SPADES -> "♠️";
        };
        String value = switch (this.value) {
            case 1 -> "A";
            case 11 -> "J";
            case 12 -> "Q";
            case 13 -> "K";
            default -> String.valueOf(this.value);
        };
        return this.exposed ? string+value:"--";
    }
}
