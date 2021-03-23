package chess;

import java.util.ArrayList;

/**
 * This class serves as the blueprint for a board
 * in a chess game.
 *
 * @author Daniel Rocha
 * @version 1.0
 * @see Piece
 * @see Square
 * @see Chess
 */

public class Board {
    Square[][] board = new Square[8][8];

    /**
     * Default constructor for the board
     * (resets the current board to default settings).
     */
    public Board() { this.resetBoard(); }

    /**
     * Given an x and y (vertical and horizontal)
     * values, gets the Square at that position.
     *
     * @param x vertical value
     * @param y horizontal value
     * @return the square at the given position
     * @throws IndexOutOfBoundsException x or y are out of bounds
     */
    public Square getSquare(int x, int y) throws IndexOutOfBoundsException {
        if (!inBounds(x,y)) {
            throw new IndexOutOfBoundsException("Index out of bound");
        }
        return board[x][y];
    }

    /**
     * Checks if the position (x,y) is
     * in bounds of the board matrix.
     *
     * @param x vertical value
     * @param y horizontal value
     * @return if it is in bounds
     */
    public boolean inBounds(int x, int y){
        return x > -1 && x < 8 && y > -1 && y < 8;
    }

    /**
     * Resets the current board, setting
     * all pieces to their default Squares.
     */
    public void resetBoard() {
        //white
        board[0][0] = new Square(0, 0, new Rook(true));
        board[0][1] = new Square(0, 1, new Knight(true));
        board[0][2] = new Square(0, 2, new Bishop(true));
        board[0][3] = new Square(0, 3, new Queen(true));
        board[0][4] = new Square(0, 4, new King(true));
        board[0][5] = new Square(0, 5, new Bishop(true));
        board[0][6] = new Square(0, 6, new Knight(true));
        board[0][7] = new Square(0, 7, new Rook(true));
        for(int i=0;i<8;i++) {
            board[1][i] = new Square(1,i,new Pawn(true));
        }
        // black
        board[7][0] = new Square(7, 0, new Rook(false));
        board[7][1] = new Square(7, 1, new Knight(false));
        board[7][2] = new Square(7, 2, new Bishop(false));
        board[7][3] = new Square(7, 3, new Queen(false));
        board[7][4] = new Square(7, 4, new King(false));
        board[7][5] = new Square(7, 5, new Bishop(false));
        board[7][6] = new Square(7, 6, new Knight(false));
        board[7][7] = new Square(7, 7, new Rook(false));
        for(int i=0;i<8;i++) {
            board[6][i] = new Square(6,i,new Pawn(false));
        }
        // null pieces
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Square(i, j, null);
            }
        }
    }

    /**
     * Gets the integer value (y) for
     * a given position char (a to h).
     *
     * @param c the char to convert
     * @return the char as an int
     */
    public static int charToInt(char c) {
        int i;
        switch (c) {
            case 'a' -> {i=0;}
            case 'b' -> {i=1;}
            case 'c' -> {i=2;}
            case 'd' -> {i=3;}
            case 'e' -> {i=4;}
            case 'f' -> {i=5;}
            case 'g' -> {i=6;}
            case 'h' -> {i=7;}
            default -> throw new IllegalStateException("Unexpected value: " + c);
        }
        return i;
    }

    /**
     * Gets the character value (a to h) for
     * a given y position.
     *
     * @param i the int to convert
     * @return the int as a char
     */
    public static char intToChar(int i) {
        char c;
        switch (i) {
            case 0 -> {c='a';}
            case 1 -> {c='b';}
            case 2 -> {c='c';}
            case 3 -> {c='d';}
            case 4 -> {c='e';}
            case 5 -> {c='f';}
            case 6 -> {c='g';}
            case 7 -> {c='h';}
            default -> throw new IllegalStateException("Unexpected value: " + i);
        }
        return c;
    }

    /**
     * Checks if a given character represents
     * a Chess Piece, except for Pawn (b,n,q,k,r)
     *
     * @param c char to be checked
     * @return if the char represents a piece
     */
    public static boolean isPieceChar(char c) {
        c=Character.toLowerCase(c);
        switch (c) {
            case 'b', 'n', 'q', 'k', 'r' -> { return true; }
            default -> { return false; }
        }
    }

    /**
     * Checks if a given character represents
     * a file in a chess board (a to h)
     *
     * @param c the character
     * @return if the character represents a file
     */
    public static boolean isCoordinateChar(char c) {
        switch (c) {
            case 'a','b','c','d','e','f','g','h' -> {
                return true; } }
        return false;
    }

    /**
     * Gets the position given a string written in
     * algebraic notation (Chess notation).
     *
     * @param s the string in algebraic notation
     * @param white if it's White's current move or not
     * @return the starting position's x and y,
     * the ending position's x and y, and if it's a promotion,
     * the piece to be promoted (as an array)
     * @throws Exception if the given String is not valid
     */
    public int[] getPos(String s, boolean white) throws Exception{
        int endX=-1;
        int endY=-1;
        int startX=-1;
        int startY=-1;
        int capture = 0;
        int promotion = -1;
        for(int i=0;i<s.length();i++) {
            if(s.charAt(i)=='x') capture++; }
        Square endSquare;
        Square startSquare = null;
        if(capture>1) return null;
        if(capture==1) {
            String first = s.split("x")[0];
            String second = s.split("x")[1];
            if(second.length()!=2) return null;
            endX=Character.getNumericValue(second.charAt(1))-1;
            endY=charToInt(second.charAt(0));
            if(first.length()==1){
                if(Character.isLowerCase(first.charAt(0))) {
                    try{
                        startY=charToInt(first.charAt(0));
                        startX = white ? endX-1 : endX+1;
                    } catch (Exception e) {
                        if(!isPieceChar(first.charAt(0))) {
                            return null;
                        }
                        s=first+second;
                        capture=0;
                    }
                } else {
                    if(!isPieceChar(Character.toLowerCase(first.charAt(0)))) {
                        return null;
                    }
                    s=Character.toLowerCase(first.charAt(0))+""+second;
                    capture=0;
                }
            }
            if(first.length()==2){
                if(Character.isLowerCase(first.charAt(0))) {
                    try{
                        startY=charToInt(first.charAt(0));
                        startY=charToInt(first.charAt(1));
                    } catch (Exception e) {
                        if(!isPieceChar(Character.toLowerCase(first.charAt(0)))) {
                            return null;
                        }
                        try {
                            startY=charToInt(first.charAt(1));
                            s=first+second;
                            capture=0;

                        } catch (Exception exception) { }
                    }
                } else {
                    if(!isPieceChar(Character.toLowerCase(first.charAt(0)))) {
                        return null;
                    }
                    s=Character.toLowerCase(first.charAt(0))+""+Character.toLowerCase(first.charAt(1))+""+second;
                    capture=0;
                }
            }
            if(first.length()==3){
                startY=Character.getNumericValue(s.charAt(1));
                startX=charToInt(first.charAt(2));
            }

        } if(capture==0) {
            if (s.length() == 2 && Character.isAlphabetic(s.charAt(0)) && Character.isDigit(s.charAt(1))) {
                endX = Character.getNumericValue(s.charAt(1)) - 1;
                endY = charToInt(s.charAt(0));
                if (white && board[endX - 1][endY].getPiece() != null && board[endX - 1][endY].getPiece() instanceof Pawn && board[endX - 1][endY].getPiece().isWhite()) {
                    startX = endX - 1;
                    startY = endY;
                } else if (white && board[endX - 2][endY].getPiece() != null && board[endX - 2][endY].getPiece() instanceof Pawn && ((Pawn) board[endX - 2][endY].getPiece()).isFirstMove() && board[endX - 2][endY].getPiece().isWhite()) {
                    startX = endX - 2;
                    startY = endY;
                } else if (!white && board[endX + 1][endY].getPiece() != null && board[endX + 1][endY].getPiece() instanceof Pawn && !board[endX + 1][endY].getPiece().isWhite()) {
                    startX = endX + 1;
                    startY = endY;
                } else if (!white && board[endX + 2][endY].getPiece() != null && board[endX + 2][endY].getPiece() instanceof Pawn && ((Pawn) board[endX + 2][endY].getPiece()).isFirstMove() && !board[endX + 2][endY].getPiece().isWhite()) {
                    startX = endX + 2;
                    startY = endY;
                }
            }
            if (s.length() == 3 && Character.isAlphabetic(s.charAt(0)) && Character.isDigit(s.charAt(0)) && Character.isAlphabetic(s.charAt(2))&& isPieceChar(s.charAt(2))) {
                switch (Character.toLowerCase(s.charAt(2))) {
                    case 'q' -> promotion=1;
                    case 'r' -> promotion=2;
                    case 'n' -> promotion=3;
                    case 'b' -> promotion=4;
                    default -> {}
                }
            }
            if (s.length() == 3 && Character.isAlphabetic(s.charAt(0)) && isPieceChar(s.charAt(0)) && Character.isAlphabetic(s.charAt(1)) && Character.isDigit(s.charAt(2))) {
                endX = Character.getNumericValue(s.charAt(2)) - 1;
                endY = charToInt(s.charAt(1));
                endSquare = getSquare(endX, endY);
                if (isPieceChar(s.charAt(0))) {
                    ArrayList<Square> positions = new ArrayList<>();
                    switch (s.charAt(0)) {
                        case 'b' -> {
                            Piece bishop = new Bishop(white);
                            positions.addAll(instanceOfPiece(bishop));
                        }
                        case 'k' -> {
                            Piece king = new King(white);
                            positions.addAll(instanceOfPiece(king));
                        }
                        case 'q' -> {
                            Piece queen = new Queen(white);
                            positions.addAll(instanceOfPiece(queen));
                        }
                        case 'r' -> {
                            Piece rook = new Rook(white);
                            positions.addAll(instanceOfPiece(rook));
                        }
                        case 'n' -> {
                            Piece knight = new Knight(white);
                            positions.addAll(instanceOfPiece(knight));
                        }
                    }
                    int count = 0;
                    if (positions.size() != 0) {
                        for (int i = 0; i < positions.size(); i++) {
                            if (positions.get(i).getPiece().canMove(this, positions.get(i), endSquare)) {
                                count++;
                            }
                        }
                    }
                    if (count == 1) {
                        for (int i = 0; i < positions.size(); i++) {
                            if (positions.get(i).getPiece().canMove(this, positions.get(i), endSquare)) {
                                startSquare = positions.get(i);
                            }
                        }
                    }
                }
            }
            if (s.length() == 4 && Character.isAlphabetic(s.charAt(0)) && isPieceChar(s.charAt(0)) && Character.isAlphabetic(s.charAt(1)) && Character.isAlphabetic(s.charAt(2)) && Character.isDigit(s.charAt(3))) {
                endX = Character.getNumericValue(s.charAt(3)) - 1;
                endY = charToInt(s.charAt(2));
                startY = charToInt(s.charAt(1));
                if (isPieceChar(s.charAt(0))) {
                    ArrayList<Square> positions = new ArrayList<>();
                    ArrayList<Square> placeholder;
                    switch (s.charAt(0)) {
                        case 'b' -> {
                            Piece bishop = new Bishop(white);
                            placeholder = instanceOfPiece(bishop);
                            for (int i = 0; i < placeholder.size(); i++) {
                                if (placeholder.get(i).getY() == startY) positions.add(placeholder.get(i));
                            }
                        }
                        case 'r' -> {
                            Piece rook = new Rook(white);
                            placeholder = instanceOfPiece(rook);
                            for (int i = 0; i < placeholder.size(); i++) {
                                if (placeholder.get(i).getY() == startY) positions.add(placeholder.get(i));
                            }
                        }
                        case 'n' -> {
                            Piece knight = new Knight(white);
                            placeholder = instanceOfPiece(knight);
                            for (int i = 0; i < placeholder.size(); i++) {
                                if (placeholder.get(i).getY() == startY) positions.add(placeholder.get(i));
                            }
                        }
                    }
                    startSquare = positions.get(0);
                }
            }
            if (s.length() == 4 && Character.isAlphabetic(s.charAt(0))&& Character.isDigit(s.charAt(1)) && Character.isAlphabetic(s.charAt(2)) && Character.isDigit(s.charAt(3))) {
                endX = Character.getNumericValue(s.charAt(3)) - 1;
                endY = charToInt(s.charAt(2));
                startX = Character.getNumericValue(s.charAt(1)) - 1;
                startY = charToInt(s.charAt(0));
                startSquare = getSquare(startX, startY);
            }
            if (s.length() == 5 && Character.isAlphabetic(s.charAt(0)) && isPieceChar(s.charAt(0)) && Character.isAlphabetic(s.charAt(1)) && Character.isDigit(s.charAt(2)) && Character.isAlphabetic(s.charAt(3)) && Character.isDigit(s.charAt(4))) {
                endX = Character.getNumericValue(s.charAt(4)) - 1;
                endY = charToInt(s.charAt(3));
                startX = Character.getNumericValue(s.charAt(2)) - 1;
                startY = charToInt(s.charAt(1));
                startSquare = getSquare(startX, startY);
            }
        }
        if(startSquare!=null) {
            startX=startSquare.getX();
            startY=startSquare.getY();
        }
        int[] out;
        if(promotion!=-1) {
            out = new int[]{startX, startY, endX, endY, promotion};
        } else {
            out = new int[]{startX, startY, endX, endY};
        }
        //System.out.println("The move is: "+Arrays.toString(out));
        for(int i=0;i<out.length;i++) {
            if(out[i]==-1) {
                out = null;
                break;
            }
        }
        return out;
    }

    /**
     * Gets all instances of a given piece
     * (the squares where that type of piece
     * and of the same color are).
     *
     * @param piece the piece to be compared
     * @return the piece's positions
     */
    public ArrayList<Square> instanceOfPiece(Piece piece) {
        ArrayList<Square> out = new ArrayList<>();
        for(int i=0;i<8;i++) {
            for(int j=0;j<8;j++) {
                if(board[i][j].getPiece()!=null&&piece.getClass().equals(board[i][j].getPiece().getClass())&&piece.isWhite()==board[i][j].getPiece().isWhite()) {
                    out.add(board[i][j]);
                }
            }
        }
        return out;
    }

    /**
     * Checks if the King for a given team
     * is currently in check (an enemy
     * piece can capture it).
     *
     * @param white the king's side
     * @return if the king is in check
     */
    public boolean kingCheck(boolean white) {
        King king = new King(white);
        try{
            Square sqr = instanceOfPiece(king).get(0);
            return inCheck(sqr, white);
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Checks if a certain square is in check
     * (another piece can attack the given square)
     *
     * @param square the Square to be checked
     * @param white the current player's side
     * @return if it's in check
     */
    public boolean inCheck(Square square, boolean white) {
        try {
            for(int i=0;i<8;i++) {
                for(int j=0;j<8;j++) {
                    if(getSquare(i,j).getPiece()!=null&&getSquare(i,j).getPiece().isWhite()!=white&&(square.getPiece()!=null&&square.getPiece().isWhite()!=white)&&getSquare(i,j).getPiece().canMove(this,getSquare(i,j), square)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error in inCheck()");
            return false;
        }
    }

    /**
     * Checks if doing a move would make the player's
     * king remain in check
     *
     * @param move the move
     * @return if the king remains in check
     */
    public boolean moveChecked(Move move) {
        boolean check = false;
        Piece startBckp = move.getStart().getPiece();
        Piece endBckp = move.getEnd().getPiece();
        move.getStart().setPiece(null);
        move.getEnd().setPiece(startBckp);
        if (this.kingCheck(startBckp.isWhite())) check = true;
        move.getStart().setPiece(startBckp);
        move.getEnd().setPiece(endBckp);
        return check;
    }
    
    /**
     * Checks if one of the enemie's
     * pieces is in enPassant
     * and if it is, returns it's Square.
     *
     * @param white the current player's side
     * @return the piece's square
     */
    public Square enPassant(boolean white) {
        Square square = new Square(0,0, null);
        try {
            for(int i=0;i<8;i++) {
                for(int j=0;j<8;j++) {
                    Piece piece = getSquare(i,j).getPiece();
                    if(piece!=null&&piece instanceof Pawn&&((Pawn) piece).isEnPassant()&&piece.isWhite()==white ) {
                        return getSquare(i,j);
                    }
                }
            }
            return square;
        } catch (Exception e) {
            System.out.println("Error in inCheck()");
            return square;
        }
    }

    /**
     * Gets all the legal moves for both white
     * and black given the current state of the board.
     *
     * @return Array of possible moves
     */
    public ArrayList<Move> possibleMoves() {
        boolean isWhite;
        ArrayList<Move> output = new ArrayList<>();
        ArrayList<Move> white = new ArrayList<>();
        ArrayList<Move> black = new ArrayList<>();
        Piece startPiece;
        Square start;
        Square end;
        try {
            for(int i=0;i<8;i++) {
                for(int j=0;j<8;j++) {
                    start = board[i][j];
                    startPiece = start.getPiece();
                    if (startPiece == null) continue;
                    isWhite = startPiece.isWhite();
                    if(startPiece instanceof Pawn) {
                        if (isWhite) {
                            if(inBounds(i+1,j)) {
                                end = board[i+1][j];
                                if (startPiece.canMove(this, start, end)) {
                                    Move move = new Move(start, end);
                                    if (!this.moveChecked(move)) white.add(move);
                                }
                            }
                            if(inBounds(i+2,j)) {
                                end = board[i+2][j];
                                if (startPiece.canMove(this, start, end)) {
                                    Move move = new Move(start, end);
                                    if (!this.moveChecked(move)) white.add(move);
                                }
                            }
                            if(inBounds(i+1,j+1)) {
                                end = board[i+1][j+1];
                                if (startPiece.canMove(this, start, end)) {
                                    Move move = new Move(start, end);
                                    if (!this.moveChecked(move)) white.add(move);
                                }
                            }
                            if(inBounds(i+1,j-1)) {
                                end = board[i+1][j-1];
                                if (startPiece.canMove(this, start, end)) {
                                    Move move = new Move(start, end);
                                    if (!this.moveChecked(move)) white.add(move);
                                }
                            }
                        }
                        else {
                            if(inBounds(i-1,j)) {
                                end = board[i-1][j];
                                if (startPiece.canMove(this, start, end)) {
                                    Move move = new Move(start, end);
                                    if (!this.moveChecked(move)) black.add(move);
                                }
                            }
                            if(inBounds(i-2,j)) {
                                end = board[i-2][j];
                                if (startPiece.canMove(this, start, end)) {
                                    Move move = new Move(start, end);
                                    if (!this.moveChecked(move)) black.add(move);
                                }
                            }
                            if(inBounds(i-1,j-1)) {
                                end = board[i-1][j-1];
                                if (startPiece.canMove(this, start, end)) {
                                    Move move = new Move(start, end);
                                    if (!this.moveChecked(move)) black.add(move);
                                }
                            }
                            if(inBounds(i-1,j+1)) {
                                end = board[i-1][j+1];
                                if (startPiece.canMove(this, start, end)) {
                                    Move move = new Move(start, end);
                                    if (!this.moveChecked(move)) black.add(move);
                                }
                            }
                        }
                    }
                    if(startPiece instanceof Rook) {
                        for(int k=0;k<8;k++) {
                            end = board[k][j];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    if(isWhite) white.add(move);
                                    else black.add(move);
                                }
                            }
                            end = board[i][k];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    if(isWhite) white.add(move);
                                    else black.add(move);
                                }
                            }
                        }
                    }
                    if(startPiece instanceof Knight) {
                        if(inBounds(i+2,j+1)) {
                            end = board[i+2][j+1];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    if(isWhite) white.add(move);
                                    else black.add(move);
                                }
                            }
                        }
                        if(inBounds(i+2,j-1)) {
                            end = board[i+2][j-1];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    if(isWhite) white.add(move);
                                    else black.add(move);
                                }
                            }
                        }
                        if(inBounds(i-2,j+1)) {
                            end = board[i-2][j+1];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    if(isWhite) white.add(move);
                                    else black.add(move);
                                }
                            }
                        }
                        if(inBounds(i-2,j-1)) {
                            end = board[i-2][j-1];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    if(isWhite) white.add(move);
                                    else black.add(move);
                                }
                            }
                        }
                        if(inBounds(i+2,j+2)) {
                            end = board[i+1][j+2];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    if(isWhite) white.add(move);
                                    else black.add(move);
                                }
                            }
                        }
                        if(inBounds(i-2,j-2)) {
                            end = board[i-1][j-2];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    if(isWhite) white.add(move);
                                    else black.add(move);
                                }
                            }
                        }
                        if(inBounds(i+1,j-2)) {
                            end = board[i+1][j-2];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    if(isWhite) white.add(move);
                                    else black.add(move);
                                }
                            }
                        }
                        if(inBounds(i-1,j-2)) {
                            end = board[i-1][j-2];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    if(isWhite) white.add(move);
                                    else black.add(move);
                                }
                            }
                        }
                    }
                    if(startPiece instanceof Bishop) {
                        int x = start.getX();
                        int y = start.getY();
                        for(int k = 1; x+k<8&&y+k<8;k++) {
                            end = board[i+k][j+k];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    if(isWhite) white.add(move);
                                    else black.add(move);
                                }
                            }
                        }
                        for(int k = 1; x+k<8&&y-k>0;k++) {
                            end = board[i+k][j-k];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    if(isWhite) white.add(move);
                                    else black.add(move);
                                }
                            }
                        }
                        for(int k = 1; x-k>0&&y-k>0;k++) {
                            end = board[i-k][j-k];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    if(isWhite) white.add(move);
                                    else black.add(move);
                                }
                            }
                        }
                        for(int k = 1; x-k>0&&y+k<8;k++) {
                            end = board[i-k][j+k];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    if(isWhite) white.add(move);
                                    else black.add(move);
                                }
                            }
                        }
                    }
                    if(startPiece instanceof Queen) {
                        int x = start.getX();
                        int y = start.getY();
                        for(int k = 1; x+k<8&&y+k<8;k++) {
                            end = board[i+k][j+k];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    if(isWhite) white.add(move);
                                    else black.add(move);
                                }
                            }
                        }
                        for(int k = 1; x+k<8&&y-k>0;k++) {
                            end = board[i+k][j-k];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    if(isWhite) white.add(move);
                                    else black.add(move);
                                }
                            }
                        }
                        for(int k = 1; x-k>0&&y-k>0;k++) {
                            end = board[i-k][j-k];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    if(isWhite) white.add(move);
                                    else black.add(move);
                                }
                            }
                        }
                        for(int k = 1; x-k>0&&y+k<8;k++) {
                            end = board[i-k][j+k];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    if(isWhite) white.add(move);
                                    else black.add(move);
                                }
                            }
                        }
                        for(int k=0;k<8;k++) {
                            end = board[k][j];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    if(isWhite) white.add(move);
                                    else black.add(move);
                                }
                            }
                            end = board[i][k];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    if(isWhite) white.add(move);
                                    else black.add(move);
                                }
                            }
                        }
                    }
                    if(startPiece instanceof King) {
                        if(inBounds(i+1,j)) {
                            end = board[i + 1][j];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    if(isWhite) white.add(move);
                                    else black.add(move);
                                }
                            }
                        }
                        if(inBounds(i+1,j+1)) {
                            end = board[i + 1][j+1];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    if(isWhite) white.add(move);
                                    else black.add(move);
                                }
                            }
                        }
                        if(inBounds(i+1,j-1)) {
                            end = board[i + 1][j-1];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    if(isWhite) white.add(move);
                                    else black.add(move);
                                }
                            }
                        }
                        if(inBounds(i,j+1)) {
                            end = board[i][j+1];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    if(isWhite) white.add(move);
                                    else black.add(move);
                                }
                            }
                        }
                        if(inBounds(i,j-1)) {
                            end = board[i][j-1];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    if(isWhite) white.add(move);
                                    else black.add(move);
                                }
                            }
                        }
                        if(inBounds(i-1,j)) {
                            end = board[i - 1][j];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    if(isWhite) white.add(move);
                                    else black.add(move);
                                }
                            }
                        }
                        if(inBounds(i-1,j+1)) {
                            end = board[i - 1][j+1];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    if(isWhite) white.add(move);
                                    else black.add(move);
                                }
                            }
                        }
                        if(inBounds(i-1,j-1)) {
                            end = board[i - 1][j - 1];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    if (isWhite) white.add(move);
                                    else black.add(move);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
        output.addAll(white);
        output.addAll(black);
        return output;
    }

    /**
     * Gets all the legal moves for the chosen
     * side of the board (white or black),
     * given the current state of the board.
     *
     * @param white side of the board to check
     * @return Array of possible moves
     */
    public ArrayList<Move> possibleMoves(boolean white) {
        ArrayList<Move> output = new ArrayList<>();
        Piece startPiece;
        Square start;
        Square end;
        try {
            for(int i=0;i<8;i++) {
                for(int j=0;j<8;j++) {
                    start = board[i][j];
                    startPiece = start.getPiece();
                    if (startPiece == null||startPiece.isWhite()!=white) continue;
                    if(startPiece instanceof Pawn) {
                        if (white) {
                            if(inBounds(i+1,j)) {
                                end = board[i + 1][j];
                                if (startPiece.canMove(this, start, end)) {
                                    Move move = new Move(start, end);
                                    if (!this.moveChecked(move)) output.add(move);
                                }
                            }
                            if(inBounds(i+2,j)) {
                                end = board[i + 2][j];
                                if (startPiece.canMove(this, start, end)) {
                                    Move move = new Move(start, end);
                                    if (!this.moveChecked(move)) output.add(move);
                                }
                            }
                            if(inBounds(i+1,j+1)) {
                                end = board[i + 1][j+1];
                                if (startPiece.canMove(this, start, end)) {
                                    Move move = new Move(start, end);
                                    if (!this.moveChecked(move)) output.add(move);
                                }
                            }
                            if(inBounds(i+1,j-1)) {
                                end = board[i + 1][j-1];
                                if (startPiece.canMove(this, start, end)) {
                                    Move move = new Move(start, end);
                                    if (!this.moveChecked(move)) output.add(move);
                                }
                            }
                        }
                        else {
                            if(inBounds(i-1,j)) {
                                end = board[i - 1][j];
                                if (startPiece.canMove(this, start, end)) {
                                    Move move = new Move(start, end);
                                    if (!this.moveChecked(move)) output.add(move);
                                }
                            }
                            if(inBounds(i-2,j)) {
                                end = board[i - 2][j];
                                if (startPiece.canMove(this, start, end)) {
                                    Move move = new Move(start, end);
                                    if (!this.moveChecked(move)) output.add(move);
                                }
                            }
                            if(inBounds(i-1,j-1)) {
                                end = board[i - 1][j-1];
                                if (startPiece.canMove(this, start, end)) {
                                    Move move = new Move(start, end);
                                    if (!this.moveChecked(move)) output.add(move);
                                }
                            }
                            if(inBounds(i-1,j+1)) {
                                end = board[i - 1][j+1];
                                if (startPiece.canMove(this, start, end)) {
                                    Move move = new Move(start, end);
                                    if (!this.moveChecked(move)) output.add(move);
                                }
                            }
                        }
                    }
                    if(startPiece instanceof Rook) {
                        for(int k=0;k<8;k++) {
                            end = board[k][j];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    output.add(move);
                                }
                            }
                            end = board[i][k];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    output.add(move);
                                }
                            }
                        }
                    }
                    if(startPiece instanceof Knight) {
                        if(inBounds(i+2,j+1)) {
                            end = board[i+2][j+1];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    output.add(move);
                                }
                            }
                        }
                        if(inBounds(i+2,j-1)) {
                            end = board[i+2][j-1];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    output.add(move);
                                }
                            }
                        }
                        if(inBounds(i-2,j+1)) {
                            end = board[i-2][j+1];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    output.add(move);
                                }
                            }
                        }
                        if(inBounds(i-2,j-1)) {
                            end = board[i-2][j-1];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    output.add(move);
                                }
                            }
                        }
                        if(inBounds(i+2,j+2)) {
                            end = board[i+1][j+2];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    output.add(move);
                                }
                            }
                        }
                        if(inBounds(i-2,j-2)) {
                            end = board[i-1][j-2];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    output.add(move);
                                }
                            }
                        }
                        if(inBounds(i+1,j-2)) {
                            end = board[i+1][j-2];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    output.add(move);
                                }
                            }
                        }
                        if(inBounds(i-1,j-2)) {
                            end = board[i-1][j-2];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    output.add(move);
                                }
                            }
                        }
                    }
                    if(startPiece instanceof Bishop) {
                        int x = start.getX();
                        int y = start.getY();
                        for(int k = 1; x+k<8&&y+k<8;k++) {
                            end = board[i+k][j+k];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    output.add(move);
                                }
                            }
                        }
                        for(int k = 1; x+k<8&&y-k>0;k++) {
                            end = board[i+k][j-k];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    output.add(move);
                                }
                            }
                        }
                        for(int k = 1; x-k>0&&y-k>0;k++) {
                            end = board[i-k][j-k];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    output.add(move);
                                }
                            }
                        }
                        for(int k = 1; x-k>0&&y+k<8;k++) {
                            end = board[i-k][j+k];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) if (white) output.add(move);
                                else output.add(move);
                            }
                        }
                    }
                    if(startPiece instanceof Queen) {
                        int x = start.getX();
                        int y = start.getY();
                        for(int k = 1; x+k<8&&y+k<8;k++) {
                            end = board[i+k][j+k];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    output.add(move);
                                }
                            }
                        }
                        for(int k = 1; x+k<8&&y-k>0;k++) {
                            end = board[i+k][j-k];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    output.add(move);
                                }
                            }
                        }
                        for(int k = 1; x-k>0&&y-k>0;k++) {
                            end = board[i-k][j-k];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    output.add(move);
                                }
                            }
                        }
                        for(int k = 1; x-k>0&&y+k<8;k++) {
                            end = board[i-k][j+k];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    output.add(move);
                                }
                            }
                        }
                        for(int k=0;k<8;k++) {
                            end = board[k][j];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    output.add(move);
                                }
                            }
                            end = board[i][k];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    output.add(move);
                                }
                            }
                        }
                    }
                    if(startPiece instanceof King) {
                        if(inBounds(i+1,j)) {
                            end = board[i + 1][j];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    output.add(move);
                                }
                            }
                        }
                        if(inBounds(i+1,j+1)) {
                            end = board[i + 1][j+1];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    output.add(move);
                                }
                            }
                        }
                        if(inBounds(i+1,j-1)) {
                            end = board[i + 1][j-1];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    output.add(move);
                                }
                            }
                        }
                        if(inBounds(i,j+1)) {
                            end = board[i][j+1];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    output.add(move);
                                }
                            }
                        }
                        if(inBounds(i,j-1)) {
                            end = board[i][j-1];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    output.add(move);
                                }
                            }
                        }
                        if(inBounds(i-1,j)) {
                            end = board[i - 1][j];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    output.add(move);
                                }
                            }
                        }
                        if(inBounds(i-1,j+1)) {
                            end = board[i - 1][j+1];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    output.add(move);
                                }
                            }
                        }
                        if(inBounds(i-1,j-1)) {
                            end = board[i - 1][j - 1];
                            if (startPiece.canMove(this, start, end)) {
                                Move move = new Move(start, end);
                                if (!this.moveChecked(move)) {
                                    output.add(move);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
        return output;
    }

    /**
     * Gets the list of possible moves
     * on a comprehensible and informative
     * string.
     *
     * @return possible moves as a string
     */
    public String pmString() {
        ArrayList<Move> pm = possibleMoves();
        StringBuilder wSB = new StringBuilder();
        StringBuilder bSB = new StringBuilder();
        int wCount = 0;
        int bCount = 0;
        for (Move move : pm)
        {
            if(move.getStart().getPiece().isWhite()){
                wSB.append(move.toString());
                wSB.append(" ");
                wCount++;
            } else {
                bSB.append(move.toString());
                bSB.append(" ");
                bCount++;
            }
        }
        int tCount = wCount+bCount;
        return "There are "+wCount+" possible moves for White and "+bCount+" possible moves for Black, for a total of "+tCount+" moves.\nThe moves for White are: "+wSB.toString()+"\nThe moves for Black are: "+bSB.toString();
}
}
