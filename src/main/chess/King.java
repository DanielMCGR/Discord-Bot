package chess;

/**
 * This class serves as the blueprint for a King
 * piece in a chess game.
 *
 * @author Daniel Rocha
 * @version 1.0
 * @see Piece
 * @see Chess
 */
public class King extends Piece{
    private boolean moved = false;
    private boolean checked = false;

    /**
     * Gets the character name for this piece
     *
     * @return 'K' (King)
     */
    @Override
    public char getName() {
        return 'K';
    }

    /**
     * Default constructor for this piece, sets
     * whether or not it is on the white side.
     *
     * @param white side of the board
     */
    public King(boolean white)
    { super(white); }

    /**
     * Gets whether or not the King has moved
     * (if it can castle or not)
     *
     * @return if King has moved
     */
    public boolean isMoved()
    { return this.moved; }

    /**
     * Sets the value of the moved variable,
     * indicating whether or not the King has moved.
     *
     * @param moved if the King has moved
     */
    public void setMoved(boolean moved)
    { this.moved = moved; }

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
        if (end.getPiece()!=null&&end.getPiece().isWhite() == this.isWhite()) { return false; }
        int x = Math.abs(start.getX() - end.getX());
        int y = Math.abs(start.getY() - end.getY());
        if (x + y == 1) {
            return !board.inCheck(end, isWhite());
        }
        if (x == 1&&y==1) {
            return !board.inCheck(end, isWhite());
        }
        return this.isValidCastling(board, end);
    }

    /**
     * Checks whether or not the King can castle.
     *
     * @param board the game board
     * @param end the ending square
     * @return if castling is possible
     */
    public boolean isValidCastling(Board board, Square end) {
        try {
            if (board.kingCheck(this.isWhite()) || this.isMoved()) {
                return false;
            }
            int x = this.isWhite()? 0:7;
            if(end.getX()!=x) return false;
            if (end.getY() == 2) {
                if (board.getSquare(x, 0).getPiece() instanceof Rook &&! ((Rook) board.getSquare(x, 0).getPiece()).isMoved()
                        &&board.inCheck(board.getSquare(x, 3), this.isWhite())&&board.inCheck(board.getSquare(x, 2), this.isWhite())
                        &&board.getSquare(x, 3).getPiece()==null&&board.getSquare(x, 2).getPiece()==null) {
                    return true; }
            } if (end.getY() == 6) {
                if (board.getSquare(x, 7).getPiece() instanceof Rook &&! ((Rook) board.getSquare(x, 7).getPiece()).isMoved()
                        &&board.inCheck(board.getSquare(0, 5), this.isWhite())&&board.inCheck(board.getSquare(x, 6), this.isWhite())
                        &&board.getSquare(x, 5).getPiece()==null&&board.getSquare(x, 6).getPiece()==null) {
                    return true; }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
