package chess;

/**
 * This class serves as the blueprint for a Bishop
 * piece in a chess game.
 *
 * @author Daniel Rocha
 * @version 1.0
 * @see Piece
 * @see Chess
 */

public class Bishop extends Piece {

    /**
     * Gets the character name for this piece
     *
     * @return 'B' (Bishop)
     */
    @Override
    public char getName() {
        return 'B';
    }

    /**
     * Default constructor for this piece, sets
     * whether or not it is on the white side.
     *
     * @param white side of the board
     */
    public Bishop(boolean white)
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
        try {
            if (end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
                return false;
            }
            int trueX = start.getX() - end.getX();
            int trueY = start.getY() - end.getY();
            int x = Math.abs(start.getX() - end.getX());
            int y = Math.abs(start.getY() - end.getY());
            if (x == 0 || y == 0) return false;
            if (x / y != 1 || x%y!=0) return false;
            if (trueX > 1 && trueY > 1) {
                for (int i = 1; i < x; i++) {
                    if (board.getSquare(start.getX()-i, start.getY()-i).getPiece()!=null) {
                        return false; } } }
            if (trueX < -1 && trueY < -1) {
                for (int i = 1; i < x; i++) {
                    if (board.getSquare(start.getX()+i, start.getY()+i).getPiece()!=null) {
                        return false; } } }
            if (trueX < -1 && trueY > 1) {
                for (int i = 1; i < x; i++) {
                    if (board.getSquare(start.getX()+i, start.getY()-i).getPiece()!=null) {
                        return false; } } }
            if (trueX > 1 && trueY < -1) {
                for (int i = 1; i < x; i++) {
                    if (board.getSquare(start.getX()-i, start.getY()+i).getPiece()!=null) {
                        return false; } } }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
