package chess;

/**
 * This class is the blueprint for all the pieces in the chess game.
 *
 * @author Daniel Rocha
 * @version 1.0
 * @see Pawn
 * @see Rook
 * @see Bishop
 * @see King
 * @see Queen
 * @see Knight
 */
public abstract class Piece {
    private boolean white;
    private char name;


    /**
     * Default constructor for the Piece class,
     * sets whether the piece is White or Black.
     *
     * @param white if the piece is white or not
     */
    public Piece(boolean white)
    { this.setWhite(white); }

    /**
     * Gets the side (White or Black) where the
     * piece belongs.
     *
     * @return the piece's side
     */
    public boolean isWhite()
    { return this.white; }

    /**
     * Sets the side (White or Black) where the
     * piece belongs.
     *
     * @param white the piece's side
     */
    public void setWhite(boolean white)
    { this.white = white; }

    /**
     * Gets the piece's name (the character
     * that represents the piece).
     *
     * @return the piece's name
     */
    public char getName() {
        return name;
    }

    /**
     * Checks if moving a piece from a starting square
     * to an ending square in a given board is legal.
     *
     * @param board the game board
     * @param start the starting square
     * @param end the ending square
     * @return whether or not the move is legal
     */
    public abstract boolean canMove(Board board,Square start, Square end);
}
