package chess;

/**
 * This class serves as the blueprint for a Rook
 * piece in a chess game.
 *
 * @author Daniel Rocha
 * @version 1.0
 * @see Piece
 * @see Chess
 */
public class Rook extends Piece {
    private boolean moved = false;

    /**
     * Gets the character name for this piece
     *
     * @return 'R' (Rook)
     */
    @Override
    public char getName() {
        return 'R';
    }

    /**
     * Default constructor for this piece, sets
     * whether or not it is on the white side.
     *
     * @param white side of the board
     */
    public Rook(boolean white)
    { super(white); }

    /**
     * Gets whether or not the Rook has moved
     * (if the King can castle to it's side or not)
     *
     * @return if Rook has moved
     */
    public boolean isMoved()
    { return this.moved; }

    /**
     * Sets the value of the moved variable,
     * indicating whether or not the Rook has moved.
     *
     * @param moved if the Rook has moved
     */
    public void setMoved(boolean moved) {
        this.moved = moved;
    }

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
        try {
            if (end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
                return false;
            }
            int x = start.getX() - end.getX();
            int y = start.getY() - end.getY();
            if (x == 0 && y != 0) {
                if (y > 1) {
                    for (int i = 1; i < y; i++) {
                        if (board.getSquare(start.getX(), start.getY() - i).getPiece() != null) {
                            return false;
                        } }
                }
                if (y < -1) {
                    for (int i = 1; i < Math.abs(y); i++) {
                        if (board.getSquare(start.getX(), start.getY() + i).getPiece() != null) {
                            return false;
                        } }
                }
                moved = true;
                return true;
            }
            if (y == 0 && x != 0) {
                if (x > 1) {
                    for (int i = 1; i < x; i++) {
                        if (board.getSquare(start.getX() - i, start.getY()).getPiece() != null) {
                            return false;
                        }
                    }
                } if (x < -1) {
                    for (int i = 1; i < Math.abs(x); i++) {
                        if (board.getSquare(start.getX() + i, start.getY()).getPiece() != null) {
                            return false;
                        } }
                }
                moved =true;
                return true;
            }
            return false;
        }
        catch (Exception e) {
            return false;
        }
    }
}
