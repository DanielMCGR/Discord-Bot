package codeWords;

/**
 * This class serves as a blueprint for
 * a "card" in a CodeWords game.
 *
 * @author Daniel Rocha
 * @version 1.0
 * @see CodeWords
 * @see Color
 */
public class Card {
    public Color state;
    private Color value;
    public String content;

    /**
     * Default constructor for the class.
     * Sets the hidden color value and the
     * card's content (the card's word).
     *
     * @param value hidden color
     * @param content the word
     */
    public Card(Color value, String content) {
        this.state=Color.WHITE;
        this.value=value;
        this.content=content;
    }

    /**
     * Sets the current state of the card
     *
     * @param state the card's state
     */
    public void setState(Color state) {
        this.state = state;
    }

    /**
     * Gets the current state of the card
     *
     * @return the card's state
     */
    public Color getState() {
        return state;
    }

    /**
     * Sets the card hidden value
     *
     * @param value the hidden value
     */
    public void setValue(Color value) {
        this.value = value;
    }

    /**
     * Gets the card hidden value
     *
     * @return the hidden value
     */
    public Color getValue() {
        return value;
    }

    /**
     * Gets the card's content (it's word)
     *
     * @return the word
     */
    public String getContent() {
        return content;
    }

    /**
     * Overrides the toString for this class,
     * making the card more understandable
     *
     * @return the card as a String
     */
    @Override
    public String toString() {
        String state = "";
        switch (this.state) {
            case WHITE -> { state="❓";}
            case RED -> { state="\uD83D\uDFE5";}
            case BLUE -> { state="\uD83D\uDFE6";}
            case BLACK -> { state="⬛";}
            case GREY -> { state="⬜";}
            }
        return "   "+content+"  "+state+"   ";
    }
}
