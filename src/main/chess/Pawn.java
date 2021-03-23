package chess;

/**
 * This class serves as the blueprint for a Pawn
 * piece in a chess game.
 *
 * @author Daniel Rocha
 * @version 1.0
 * @see Piece
 * @see Chess
 */
public class Pawn extends Piece {
    private boolean firstMove;
    private boolean enPassant;
    private boolean captureEP;

    /**
     * Gets the character name for this piece
     *
     * @return 'p' (pawn)
     */
    @Override
    public char getName() {
        return 'p';
    }

    /**
     * Default constructor for this piece, sets
     * whether or not it is on the white side.
     * It also sets the variables firstMove (as true),
     * enPassant (as false) and captureEP (as false).
     *
     * @param white side of the board
     */
    public Pawn(boolean white) {
        super(white);
        firstMove=true;
        enPassant = false;
        captureEP = false;
    }

    /**
     * Checks if the piece is in en passant (
     * the piece has moved 2 squares and this
     * was the player's last move).
     *
     * @return if piece is en passant
     */
    public boolean isEnPassant() {
        return enPassant;
    }

    /**
     * Sets the piece in enPassant (if the move was
     * the first pawn move and it advance 2 squares)
     *
     * @param enPassant whether or not piece is en passant
     */
    public void setEnPassant(boolean enPassant) {
        this.enPassant = enPassant;
    }

    /**
     * Checks if the pawn has moved or not
     *
     * @return if the pawn has moved
     */
    public boolean isFirstMove(){
        return this.firstMove;
    }

    /**
     * Sets the pawn's first move variable
     * to false (after it's first move).
     */
    public void notFirstMove(){ firstMove=false; }

    /**
     * Resets the captureEP variable
     * (gives it a false value).
     */
    public void resetCEP() {
        this.captureEP = false;
    }

    /**
     * Checks whether or not this pawn
     * can (and will) capture another pawn
     * that is in en Passant.
     *
     * @return if the capture will happen
     */
    public boolean capturesEP() {
        return captureEP;
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
        boolean white = this.isWhite();
        int trueX = (start.getX() - end.getX());
        int y = Math.abs(start.getY() - end.getY());
        int x = white ? -1:1;
        //checks for pawn captures
        if (end.getPiece()!=null) {
            if(end.getPiece().isWhite()==white||trueX!=x) return false;
            return y == 1;
        }
        //checks for en passant
        if(y!=0) {
            if(y!=1) return false;
            int startY = start.getY();
            try{
                Piece piece = board.getSquare(start.getX(),startY+1).getPiece();
                if(piece instanceof Pawn&&((Pawn) piece).isEnPassant()&&piece.isWhite()!=white) {
                    captureEP =true;
                    return true;
                }
                throw new Exception("test");
            } catch (Exception e) {
                try{
                    Piece piece = board.getSquare(start.getX(),startY-1).getPiece();
                    if(piece instanceof Pawn&&((Pawn) piece).isEnPassant()&&piece.isWhite()!=white) {
                        captureEP =true;
                        return true;
                    }
                } catch (Exception exception) { return false; }
            return false;
            }
        }
        //checks the first move (can move 2 squares)
        if(firstMove&&trueX==(2*x)) { return true; }
        //checks normal moves (1 forward)
        return trueX == x;
    }

    public boolean Promote(Board board, Square start, Square end) {
        if (end.getPiece().isWhite() == this.isWhite()) {
            return false;
        }
        int x = end.getX();
        if(x==7&&isWhite()) return true;
        if(x==0&&!isWhite()) return true;
        return false;
    }
}
