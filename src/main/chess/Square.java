package chess;

/**
 * This class serves as the blueprint for a
 * Square (position) in a chess game.
 *
 * @author Daniel Rocha
 * @version 1.0
 * @see Piece
 * @see Board
 * @see Chess
 */
public class Square {
    private Piece piece;
    private int x;
    private int y;

    /**
     * Default constructor for this class.
     * Sets the square's x and y values, as well
     * as the piece sitting on the square (null
     * if there is no piece)
     *
     * @param x x value
     * @param y y value
     * @param piece piece
     */
    public Square(int x, int y, Piece piece)
    {
        this.setPiece(piece);
        if(x<0||x>7) this.setX(-1);
        else this.setX(x);
        if(y<0||y>7) this.setY(-9);
        else this.setY(y);
    }

    /**
     * Gets the piece sitting currently on
     * the square (null if no piece)
     *
     * @return piece
     */
    public Piece getPiece()
    { return this.piece; }

    /**
     * Sets the piece on the current square
     *
     * @param p piece
     */
    public void setPiece(Piece p)
    { this.piece = p; }

    /**
     * Gets the x value of the square
     *
     * @return x value
     */
    public int getX()
    { return this.x; }

    /**
     * Sets the x value of the square
     *
     * @param x x value
     */
    public void setX(int x)
    { this.x = x; }

    /**
     * Gets the y value of the square
     *
     * @return y value
     */
    public int getY()
    { return this.y; }

    /**
     * Sets the y value of the square
     *
     * @param y y value
     */
    public void setY(int y)
    { this.y = y; }
}
