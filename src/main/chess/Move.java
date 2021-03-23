package chess;

/**
 * This class is a blueprint for
 * a move in Chess.
 *
 * @author Daniel Rocha
 * @version 1.0
 * @see bot.Kebbot
 * @see Chess
 * @see Square
 */
public class Move {
    private Player player;
    private Square start;
    private Square end;
    private Piece pieceMoved;
    private Piece capturedPiece;
    private boolean castlingMove = false;
    private boolean isEnPassantMove = false;
    private boolean isPromotion = false;

    /**
     * Constructor for the Move class. Sets who's doing
     * the move, and the initial and ending squares.
     *
     * @param player player moving the piece
     * @param start starting square
     * @param end ending square
     */
    public Move(Player player, Square start, Square end) {
        this.player = player;
        this.start = start;
        this.end = end;
        this.pieceMoved = start.getPiece();
    }
    /**
     * Constructor for the Move class. Sets the
     * move's initial and ending squares.
     *
     * @param start starting square
     * @param end ending square
     */
    public Move(Square start, Square end) {
        this.start = start;
        this.end = end;
        this.pieceMoved = start.getPiece();
    }

    /**
     * Sets the move as a promotion move.
     */
    public void setPromotion() {
        isPromotion = true;
    }

    /**
     * Checks if the move is a promotion move
     *
     * @return if it's a promotion
     */
    public boolean isPromotion() {
        return isPromotion;
    }

    /**
     * Checks if the move is enPassant move.
     *
     * @return if it's enPassant
     */
    public boolean isEnPassantMove() {
        return isEnPassantMove;
    }

    /**
     * Sets a move as enPassant move.
     */
    public void setEnPassantMove() {
        isEnPassantMove = true;
    }

    /**
     * Checks if a move is a castling move.
     *
     * @return if it's castling
     */
    public boolean isCastlingMove()
    {
        return this.castlingMove;
    }

    /**
     * Sets the move as a castling move.
     */
    public void setCastlingMove()
    {
        this.castlingMove = true;
    }

    /**
     * Gets the starting square for this move.
     *
     * @return starting square
     */
    public Square getStart() {
        return this.start;
    }

    /**
     * Gets the ending square for this move.
     *
     * @return ending square
     */
    public Square getEnd() {
        return this.end;
    }

    /**
     * If there was a capture, records which piece was captured.
     *
     * @param destPiece captured piece
     */
    public void setCapturedPiece(Piece destPiece) {
        this.capturedPiece =end.getPiece();
        end.setPiece(null);
    }

    /**
     * Gets the captured piece resultant of this move.
     *
     * @return the captured piece
     */
    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    /**
     * Overrides the toString method for easier understanding.
     *
     * @return the Move as a String
     */
    @Override
    public String toString() {
        String out = "";
        char piece = pieceMoved.getName();
        if(piece!='p') out+=piece;
        if(piece!='p'&&piece!='Q'&&piece!='K') out+=Board.intToChar(start.getY());
        if(piece=='p'&&end.getPiece()!=null) out+=Board.intToChar(start.getY());
        if(end.getPiece()!=null) out+= 'x';
        out+= Board.intToChar(end.getY())+""+(end.getX()+1);
        return '{' + out + '}';
    }
}
