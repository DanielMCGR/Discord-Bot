package chess;

import bot.GamesContainer;
import net.dv8tion.jda.api.entities.TextChannel;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class serves as the blueprint for a chess game
 * using it's functions you can fully play.
 * It's usage is, however, better defined for my
 * Discord Bot project.
 *
 * @author Daniel Rocha
 * @version 1.0
 * @see Board
 * @see Player
 * @see Piece
 * @see GameStatus
 * @see bot.Kebbot
 * @see GamesContainer
 */

public class Chess {
    private Player[] players;
    private Board board;
    private Player currentTurn;
    private GameStatus status;
    private ArrayList<Move> movesPlayed;
    private Boolean[] checks = new Boolean[]{false,false};
    private TextChannel textChannel;
    private Long chessID;
    public String title;
    private int halfMoves=0;
    private int moves=0;

    /**
     * Constructor for creating a new Chess game
     * where 1 player goes against the computer,
     * using a default board.
     *
     * @param p1 the player
     */
    public Chess(Player p1) {
        Player p2 = new Player(!p1.isWhiteSide(), false);
        this.start(p1,p2);
    }
    /**
     * Constructor for creating a new Chess game
     * where 1 player goes against the another player,
     * using a default board.
     *
     * @param p1 the first player
     * @param p2 the second player
     */
    public Chess(Player p1, Player p2) {
        this.start(p1,p2);
    }

    /**
     * Gets the ID of the Discord message to
     * delete it (allowing to then send a new one).
     *
     * @return the message ID
     */
    public Long getChessID() {
        return chessID;
    }

    /**
     * Sets the ID of the Discord message so it can
     * be deleted later (allowing to then send a new one).
     *
     * @param chessID
     */
    public void setChessID(Long chessID) {
        this.chessID = chessID;
    }

    /**
     * Gets the Discord text channel where the game was started,
     * to make sure it only reads commands from that channel.
     *
     * @return the text channel
     */
    public TextChannel getTextChannel() {
        return textChannel;
    }

    /**
     * Sets the Discord text channel where the game was started,
     * to make sure it only reads commands from that channel.
     *
     * @param textChannel the text channel
     */
    public void setTextChannel(TextChannel textChannel) {
        this.textChannel = textChannel;
    }

    /**
     * Gets the list of players
     *
     * @return the list of players
     */
    public Player[] getPlayers() {
        return players;
    }

    /**
     * Sees if both the white and black
     * kings are in check or not.
     *
     * @return if kings are in check
     */
    public Boolean[] getChecks() {
        return checks;
    }

    /**
     * Gets the game board in it's current status.
     *
     * @return the game board
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Gets the player who is playing the
     * current turn.
     *
     * @return current player
     */
    public Player getCurrentTurn() {
        return this.currentTurn;
    }

    /**
     * Changes the current turn to either
     * white or black.
     *
     * @param bool if new turn is white or black
     */
    public void setCurrentTurn(boolean bool) {
        if(players[0].isWhiteSide()==bool) {
            this.currentTurn=players[0];
        } else {
            this.currentTurn=players[1];
        }
    }

    /**
     * Starts the chess game and defines the turns.
     *
     * @param p1 the first player
     * @param p2 the second player
     */
    private void start(Player p1, Player p2) {
        setStatus(GameStatus.ACTIVE);
        movesPlayed = new ArrayList<>();
        players = new Player[2];
        players[0] = p1;
        players[1] = p2;
        this.board = new Board();
        this.currentTurn = p1.isWhiteSide() ? p1:p2;
    }

    /**
     * Gets a random boolean in order to
     * choose a side (white or black) for
     * a player fairly.
     *
     * @return random boolean
     */
    public static boolean randomizeSide() {
        Random rd = new Random();
        return rd.nextBoolean();
    }

    /**
     * Checks if the game is over (the current
     * status is anything but active).
     *
     * @return if game is over
     */
    public boolean isEnd()
    {
        return this.getStatus() != GameStatus.ACTIVE;
    }

    /**
     * Gets the game current status.
     *
     * @return current status
     */
    public GameStatus getStatus()
    {
        return this.status;
    }

    /**
     * Sets the game current status.
     *
     * @param status game status
     */
    public void setStatus(GameStatus status)
    {
        this.status = status;
    }

    /**
     * Gets the number of half moves played thus far.
     * If a custom board was used, it accounts for the value
     * stated in the FEN notation.
     *
     * @return number of half moves
     */
    private int getHalfMoves() {
        //creates a "fake move" that can works as the amount of specified half moves
        Move fakeMove=null;
        if(this.halfMoves!=0) {
            Square start = new Square(0,0,new Pawn(true));
            Square end = new Square(7,7,new Pawn(true));
            fakeMove = new Move(start,end);
            movesPlayed.add(0, fakeMove);
        }
        int inc = this.halfMoves!=0 ? this.halfMoves : 0;
        for(int i=movesPlayed.size()-1;i>=0;i--) {
            //if it reaches the first (fake) move, it adds the original halfMove count to the increment
            if(movesPlayed.get(i).equals(fakeMove)) {
                inc+=this.halfMoves;
            }
            if(movesPlayed.get(i).getStart().getPiece() instanceof Pawn && movesPlayed.get(i).getCapturedPiece()!=null) {
                return inc;
            }
            inc++;
            if(inc==50) return inc;
        }
        return inc;
    }

    /**
     * Checks if the third argument in a FEN notation is valid
     * (this represents the possibility to castle - KQkq)
     *
     * @param str the argument
     * @return it the argument is valid
     */
    private boolean canCastleFEN(String str) {
        if(str.length()>4||str.length()==0) return false;
        if(str.length()==1) {
            return (str.charAt(0)!='-'&&str.charAt(0)!='K'&&str.charAt(0)!='k'&&str.charAt(0)!='Q'&&str.charAt(0)!='q');
        }
        for(int i=0;i<str.length();i++) {
            if(str.charAt(i)=='-') return false;
            if(str.charAt(0)!='K'&&str.charAt(0)!='k'&&str.charAt(0)!='Q'&&str.charAt(0)!='q') return false;
        }
        return true;
    }

    /**
     * Sets all Kings and Rooks on the board
     * as if they've moved (and can't castle)
     * and then sets the ones that can castle
     * using the FEN argument provided.
     *
     * @param str FEN argument
     */
    private void setCastlingFEN(String str) {
        King whiteKing = (King) board.instanceOfPiece(new King(true)).get(0).getPiece();
        ArrayList<Square> whiteRooks= board.instanceOfPiece(new Rook(true));
        King blackKing = (King) board.instanceOfPiece(new King(false)).get(0).getPiece();
        ArrayList<Square> blackRooks= board.instanceOfPiece(new Rook(false));
        whiteKing.setMoved(true);
        for(Square sqr:whiteRooks) {
            Rook rook = (Rook)sqr.getPiece();
            rook.setMoved(true);
        }
        blackKing.setMoved(true);
        for(Square sqr:blackRooks) {
            Rook rook = (Rook)sqr.getPiece();
            rook.setMoved(true);
        }
        if(str.charAt(0)=='-') return;
        for(int i=0;i<str.length();i++){
            switch (str.charAt(i)) {
                case 'K' -> {
                    whiteKing.setMoved(false);
                    Rook rook = (Rook) board.board[0][0].getPiece();
                    rook.setMoved(false);
                }
                case 'k' -> {
                    blackKing.setMoved(false);
                    Rook rook = (Rook) board.board[7][0].getPiece();
                    rook.setMoved(false);
                }
                case 'Q' -> {
                    whiteKing.setMoved(false);
                    Rook rook = (Rook) board.board[0][7].getPiece();
                    rook.setMoved(false);
                }
                case 'q' -> {
                    blackKing.setMoved(false);
                    Rook rook = (Rook) board.board[7][7].getPiece();
                    rook.setMoved(false);
                }
            }
        }

    }

    /**
     * Sets the board according to a given
     * string using the FEN notation and indicates
     * it the given FEN string is valid or not.
     *
     * @param str the FEN string
     * @return if the string is valid
     */
    public boolean setFEN(String str) {
        ArrayList<Character> complete= new ArrayList<>();
        String[] split = str.split(" ");
        if(split.length!=6) return false;
        str = split[0];
        String turn = split[1];
        if(turn.length()!=1) return false;
        String castles = split[2];
        if(!canCastleFEN(castles)) return false;
        String enPassant = split[3];
        if(enPassant.length()==1&&enPassant.charAt(0)!='-') return false;
        if(enPassant.length()!=2||!Board.isCoordinateChar(enPassant.charAt(0))||!Character.isDigit(enPassant.charAt(1))) return false;
        int halfMoves;
        int moves;
        char side;
        try {
            halfMoves = Integer.getInteger(split[4]);
            if (halfMoves < 0 || halfMoves > 50) {
                throw new IllegalArgumentException("The given halfMoves amount is not valid");
            }
            moves = Integer.parseInt(split[5]);
            if (moves<1) {
                throw new IllegalArgumentException("The given moves amount is not valid");
            }
            side = Character.toLowerCase(turn.charAt(0));
            if(side!='w'&&side!='b') return false;
        } catch (Exception e) {
            return false;
        }
        for(int i=0;i<str.length();i++) {
            char a = str.charAt(i);
            if(a=='/') {
                continue;
            }
            if(Character.isDigit(a)) {
                for(int j=0;j<Character.getNumericValue(a);j++) {
                    complete.add('0');
                }
            } else complete.add(a);
        }
        if(complete.size()!=64) return false;
        boolean white = side == 'w';
        int inc = white ? 1:-1;
        Piece eP = board.getSquare(Board.charToInt(enPassant.charAt(0))+inc,Character.getNumericValue(enPassant.charAt(1))).getPiece();
        if(eP instanceof Pawn) {
            ((Pawn) eP).setEnPassant(true);
        } else {
            return false;
        }
        int count = 0;
        for(int i=7;i>=0;i--) {
            for(int j=0;j<8;j++) {
                switch (complete.get(count)) {
                    case '0' -> {board.board[i][j]=new Square(i,j,null);}
                    case 'P' -> {board.board[i][j]=new Square(i,j,new Pawn(true));}
                    case 'p' -> {board.board[i][j]=new Square(i,j,new Pawn(false));}
                    case 'R' -> {board.board[i][j]=new Square(i,j,new Rook(true));}
                    case 'r' -> {board.board[i][j]=new Square(i,j,new Rook(false));}
                    case 'B' -> {board.board[i][j]=new Square(i,j,new Bishop(true));}
                    case 'b' -> {board.board[i][j]=new Square(i,j,new Bishop(false));}
                    case 'N' -> {board.board[i][j]=new Square(i,j,new Knight(true));}
                    case 'n' -> {board.board[i][j]=new Square(i,j,new Knight(false));}
                    case 'K' -> {board.board[i][j]=new Square(i,j,new King(true));}
                    case 'k' -> {board.board[i][j]=new Square(i,j,new King(false));}
                    case 'Q' -> {board.board[i][j]=new Square(i,j,new Queen(true));}
                    case 'q' -> {board.board[i][j]=new Square(i,j,new Queen(false));}
                }
                count++;
            }
        }
        setCastlingFEN(castles);
        this.setCurrentTurn(white);
        this.halfMoves=halfMoves;
        this.moves=moves;
        return true;
    }

    /**
     * Gets the current game in valid FEN notation
     *
     * @return FEN notation
     */
    public String getFEN() {
        boolean turn = currentTurn.isWhiteSide();
        Square[][] board = this.getBoard().board;
        StringBuffer sb = new StringBuffer();
        int count = 0;
        for(int i=7;i>=0;i--) {
            if(count>0) {
                sb.append(count);
                count=0;
            }
            if(i<7)  sb.append("/");
            for(int j=0;j<8;j++) {
                Piece piece = board[i][j].getPiece();
                if(piece==null) count++;
                else {
                    boolean white=piece.isWhite();
                    if(count>0) {
                        sb.append(count);
                        count=0;
                    }
                    switch (board[i][j].getPiece().getName()) {
                        case 'p' -> {
                            if(white) sb.append('P');
                            else sb.append('p');
                        }
                        case 'Q' -> {
                            if(white) sb.append('Q');
                            else sb.append('q');
                        }
                        case 'K' -> {
                            if(white) sb.append('K');
                            else sb.append('k');
                        }
                        case 'B' -> {
                            if(white) sb.append('B');
                            else sb.append('b');
                        }
                        case 'R' -> {
                            if(white) sb.append('R');
                            else sb.append('r');
                        }
                        case 'N' -> {
                            if(white) sb.append('N');
                            else sb.append('n');
                        } } } } }
        String white = turn ? " w " : " b " ;
        sb.append(white);
        boolean[] whiteCastles = new boolean[]{false, false, false};
        boolean[] blackCastles = new boolean[]{false, false, false};
        if(board[0][4].getPiece() instanceof King&&!((King) board[0][4].getPiece()).isMoved()) whiteCastles[0]=true;
        if(board[7][4].getPiece() instanceof King&&!((King) board[7][4].getPiece()).isMoved()) blackCastles[0]=true;
        if(board[0][0].getPiece() instanceof Rook&&!((Rook) board[0][0].getPiece()).isMoved()) whiteCastles[1]=true;
        if(board[0][7].getPiece() instanceof Rook&&!((Rook) board[0][7].getPiece()).isMoved()) whiteCastles[2]=true;
        if(board[7][0].getPiece() instanceof Rook&&!((Rook) board[7][0].getPiece()).isMoved()) blackCastles[1]=true;
        if(board[7][7].getPiece() instanceof Rook&&!((Rook) board[7][7].getPiece()).isMoved()) blackCastles[2]=true;
        if(whiteCastles[0]) {
            if(whiteCastles[2]) sb.append("K");
            if(whiteCastles[1]) sb.append("Q");
        }
        if(blackCastles[0]) {
            if(blackCastles[2]) sb.append("k");
            if(blackCastles[1]) sb.append("q");
        }
        if(!blackCastles[0]&&!whiteCastles[0]) sb.append("-");
        sb.append(" ");
        Square square = this.board.enPassant(!turn);
        Piece piece = square.getPiece();
        if(piece!=null) {
            char y = Board.intToChar(square.getY());
            int x;
            if(piece.isWhite()) {
                x=square.getX()-1;
            } else {
                x=square.getX()+1;
            }
            sb.append(y);
            sb.append(x);
        } else {
            sb.append("-");
        }
        sb.append(" ");
        sb.append(getHalfMoves());
        sb.append(" ");
        //-1 accounts for the extra move in getHalfMoves
        int size = this.moves!=0 ? (this.moves+movesPlayed.size()-1):movesPlayed.size();
        int moves = (size/2)+1;
        sb.append(moves);
        return sb.toString();
   }

    /**
     * Checks if the given player can make a move
     * based on the specified parameters (the coordinates
     * for the starting and ending position, and, if it's
     * a pawn promotion, what is the piece the pawn is
     * going to promote into).
     *
     * @param player the player
     * @param startX starting position x value
     * @param startY starting position y value
     * @param endX ending position x value
     * @param endY ending position x value
     * @param promotion the piece to promote a pawn into
     * @return if the move is possible
     * @throws Exception if the move is not possible
     */
    public boolean playerMove(Player player, int startX, int startY, int endX, int endY, Piece promotion) throws Exception {
        Square startBox = board.getSquare(startX, startY);
        Square endBox = board.getSquare(endX, endY);
        Move move = new Move(player, startBox, endBox);
        if(move.getStart().getPiece() instanceof Pawn&&((endX==0)||endX==7)) {
            if(promotion instanceof Queen||promotion instanceof Rook||promotion instanceof Bishop||promotion instanceof Knight) {
                move.setPromotion();
            }
            else return false;
        } else {
            promotion=null;
        }
        return this.makeMove(move, player, promotion);
    }

    /**
     * Complements the playerMove method
     * in order to check if a move
     * is possible or not.
     *
     * @param move the move
     * @param player the player to move
     * @param promotion the piece to promote a pawn into
     * @return if the move is possible
     */
    private boolean makeMove(Move move, Player player, Piece promotion) {
        Piece sourcePiece = move.getStart().getPiece();
        //check if there's a piece on starting square
        if (sourcePiece == null) {
            return false;
        }
        //checks if it's the player's current turn
        if (player != currentTurn) {
            return false;
        }
        //checks if the piece on starting square is of correct color
        if (sourcePiece.isWhite() != player.isWhiteSide()) {
            return false;
        }
        //checks if moving the piece would cause the king to remain in check
        if(board.kingCheck(currentTurn.isWhiteSide())&&board.moveChecked(move)) {
            return false;
        }
        //checks if the move is valid according to the piece's rules
        if (!sourcePiece.canMove(board, move.getStart(), move.getEnd())) {
            return false;
        }
        //checks if a pawn of the player's color moved twice last round and wasn't captured,
        //thus ending it's en passant status
        Piece enPass = board.enPassant(currentTurn.isWhiteSide()).getPiece();
        if(sourcePiece instanceof Pawn&&enPass!=null) {
            if(enPass instanceof Pawn) ((Pawn) enPass).setEnPassant(false);
        }
        //checks if there is a pawn that wants to capture another pawn using enPassant
        if(sourcePiece instanceof Pawn&& ((Pawn) sourcePiece).capturesEP()) {
            Move enPassantMove = movesPlayed.get( movesPlayed.size()-1);
            move.setCapturedPiece(enPassantMove.getEnd().getPiece());
            enPassantMove.getEnd().setPiece(null);
            move.setEnPassantMove();
            ((Pawn) sourcePiece).resetCEP();
        }
        //checks if there's a capture and defines the move accordingly
        Piece destPiece = move.getEnd().getPiece();
        if (destPiece != null) {
            move.setCapturedPiece(destPiece);
        }
        // checks if the move is a castling move and moves the rooks in advance
        if (sourcePiece instanceof King && ((King) sourcePiece).isValidCastling(this.board, move.getEnd())) {
            int x = move.getStart().getX();
            boolean castleLong = move.getEnd().getY()==2;
            if(castleLong) {
                try {
                    Square startBox = board.getSquare(x, 0);
                    Square endBox = board.getSquare(x, 3);
                    Move castleMove = new Move(player, startBox, endBox);
                    Piece rook = castleMove.getStart().getPiece();
                    ((Rook) rook).setMoved(true);
                    castleMove.getEnd().setPiece(rook);
                    castleMove.getStart().setPiece(null);
                } catch (Exception ignored) { }
            } else {
                try {
                    Square startBox = board.getSquare(x, 7);
                    Square endBox = board.getSquare(x, 5);
                    Move castleMove = new Move(player, startBox, endBox);
                    Piece rook = castleMove.getStart().getPiece();
                    ((Rook) rook).setMoved(true);
                    castleMove.getEnd().setPiece(rook);
                    castleMove.getStart().setPiece(null);
                } catch (Exception ignored) { }
            }
            ((King) sourcePiece).setMoved(true);
            move.setCastlingMove();
        }
        //checks if it's the pawn's first move and if it is, sets it as enPassant
        if(sourcePiece instanceof Pawn&&((Pawn) sourcePiece).isFirstMove()) {
            ((Pawn) sourcePiece).notFirstMove();
            ((Pawn) sourcePiece).setEnPassant(true);
        }
        //checks if there is a promotion and applies it
        if(promotion!=null) {
            move.getStart().setPiece(promotion);
            move.setPromotion();
        }
        //stores the move in an array
        movesPlayed.add(move);
        //moves the starting piece to it's destination
        move.getEnd().setPiece(move.getStart().getPiece());
        move.getStart().setPiece(null);
        //check if other king is in check
        if(board.kingCheck(!currentTurn.isWhiteSide())) {
            if(currentTurn.isWhiteSide()) checks[1]=true;
            if(!currentTurn.isWhiteSide()) checks[0]=true;
        }
        //checks if the other king has no possible moves, and if so, ends the game accordingly
        int i = currentTurn.isWhiteSide() ? 0:1;
        checks[i]=false;
        if (board.possibleMoves(!currentTurn.isWhiteSide()).size()==0) {
            i = currentTurn.isWhiteSide() ? 1:0;
            if(!checks[i]) {
                this.setStatus(GameStatus.STALEMATE);
            }
            if (currentTurn.isWhiteSide()) {
                this.setStatus(GameStatus.WHITE_WIN);
            }
            else {
                this.setStatus(GameStatus.BLACK_WIN);
            }
        }
        //changes the current turn to the opposite player
        if (this.currentTurn.isWhiteSide()) {
            this.currentTurn = players[1];
        } else {
            this.currentTurn = players[0];
        }
        return true;
    }
}
