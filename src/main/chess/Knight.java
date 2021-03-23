package chess;

/**
 * This class serves as the blueprint for a Knight
 * piece in a chess game.
 *
 * @author Daniel Rocha
 * @version 1.0
 * @see Piece
 * @see Chess
 */
public class Knight extends Piece {

    /**
     * Gets the character name for this piece
     *
     * @return 'N' (kNight)
     */
    @Override
    public char getName() {
        return 'N';
    }

    /**
     * Default constructor for this piece, sets
     * whether or not it is on the white side.
     *
     * @param white side of the board
     */
    public Knight(boolean white)
    { super(white); }

    /**
     * Checks if the piece can move from the start square
     * to the end square according to chess rules.
     *
     * @param board the game board
     * @param start the starting square
     * @param end the ending square
     * @return if the move is possible
     */
    @Override
    public boolean canMove(Board board, Square start, Square end)
    {
        if (end.getPiece()!=null&&end.getPiece().isWhite() == this.isWhite()) {
            return false;
        }
        int x = Math.abs(start.getX() - end.getX());
        int y = Math.abs(start.getY() - end.getY());
        return x * y == 2;
    }
}
