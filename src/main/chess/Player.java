package chess;

/**
 * This class serves as the blueprint for a
 * Player in a chess game.
 *
 * @author Daniel Rocha
 * @version 1.0
 * @see Chess
 */
public class Player {
    private boolean whiteSide;
    private boolean humanPlayer;
    private Long id;

    /**
     * Constructor for the Player class, sets the player's
     * side (white or black) as well as if it's a human
     * player or a computer (the Discord bot)
     *
     * @param whiteSide if the player's side is white
     * @param humanPlayer if the player is human
     */
    public Player(boolean whiteSide, boolean humanPlayer) {
        this.whiteSide=whiteSide;
        this.humanPlayer=humanPlayer;
        if(!humanPlayer) this.id=0L;
    }
    /**
     * Constructor for the Player class, sets the player's
     * side (white or black) as well as if it's a human
     * player or a computer (the Discord bot), as well as an
     * ID (usually this is the user's Discord ID).
     *
     * @param whiteSide if the player's side is white
     * @param humanPlayer if the player is human
     * @param id player's id
     */
    public Player(boolean whiteSide, boolean humanPlayer, long id) {
        this.whiteSide=whiteSide;
        this.humanPlayer=humanPlayer;
        this.id=id;
    }

    /**
     * Gets if the player's side is white or not
     *
     * @return side
     */
    public boolean isWhiteSide() {
        return this.whiteSide;
    }

    /**
     * Gets if the player is human or not
     * (normal player or Discord bot)
     *
     * @return if human
     */
    public boolean isHumanPlayer() {
        return this.humanPlayer;
    }

    /**
     * Gets the player's id (0 if it's a bot)
     *
     * @return id
     */
    public Long getId() {
        return id;
    }
}
