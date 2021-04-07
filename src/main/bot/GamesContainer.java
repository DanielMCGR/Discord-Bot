package bot;

import chess.*;
import codeWords.*;
import casino.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * This class stores active games
 * and executes most of their functions.
 *
 * @author Daniel Rocha
 * @version 1.2
 * @see bot.CommandList
 * @see CodeWords
 * @see Chess
 * @see casino.BlackJack
 */
public class GamesContainer {
    ArrayList<Chess> chessGames = new ArrayList<>();
    ArrayList<CodeWords> cwGames = new ArrayList<>();
    ArrayList<BlackJack> bjGames = new ArrayList<>();
    

    //CodeWords Game -+-

    /**
     * Creates a new CodeWords game and adds it to the game list.
     *
     * @return the id of the game
     */
    public int newCW() {
        CodeWords game = new CodeWords();
        cwGames.add(game);
        return cwGames.size()-1;
    }

    /**
     * Checks if a given id exists in the list
     * and if so, returns the corresponding game.
     *
     * @param i the game's id
     * @return CodeWords game
     */
    public CodeWords getCWGame(int i) {
        return cwGames.get(i);
    }

    /**
     * Checks if a given message is considered a value command for the game
     *
     * @param event the GuildMessageReceivedEvent
     * @return if it's valid
     */
    public boolean validCWCommand(GuildMessageReceivedEvent event) {
        String args = event.getMessage().getContentRaw();
        int gameID = getCWGameID(event.getAuthor().getIdLong());
        if(chessGames.size()<1||gameID==-1||!cwGames.get(gameID).getTextChannel().equals(event.getChannel())) return false;
        return (args.startsWith("guess")||args.startsWith("clue"));
    }

    /**
     * Executes a CodeWords command.
     * This contains all commands except
     * the starting one.
     *
     * @param event the GuildMessageReceivedEvent
     */
    public void execCWCommand(GuildMessageReceivedEvent event) {
        int gameID = getCWGameID(event.getAuthor().getIdLong());
        if(gameID==-1) return;
        String args[] = event.getMessage().getContentRaw().split(" ");
        switch (args[0]) {
            case "clue" -> {
                if(args.length!=3) return;
                Long id = event.getAuthor().getIdLong();
                if(cwGames.get(gameID).getTurn()==Color.BLUE) {
                    if(!id.equals(cwGames.get(gameID).getTeamBlue().get(0))) return;
                    int num = 0;
                    try{
                        num=Integer.parseInt(args[2]);
                        if(num>9) num=9;
                        if(num<0) num=0;
                    } catch (Exception e) { return; }
                    event.getChannel().sendMessage("Blue Spymaster gave the clue: "+args[1]+" "+args[2]).queue();
                    cwGames.get(gameID).setSpyNum(num+1);
                } else {
                    if(!id.equals(cwGames.get(gameID).getTeamRed().get(0))) return;
                    int num = 0;
                    try{
                        num=Integer.parseInt(args[2]);
                        if(num>9) num=9;
                        if(num<0) num=0;
                    } catch (Exception e) { return; }
                    event.getChannel().sendMessage("Red Spymaster gave the clue: "+args[1]+" "+args[2]).queue();
                    cwGames.get(gameID).setSpyNum(num+1);
                }
            }
            case "guess" -> {
                if(args.length!=2) return;
                String guessString=args[1];
                Long id = event.getAuthor().getIdLong();
                int spyNum = cwGames.get(gameID).getSpyNum();
                if(cwGames.get(gameID).getTurn() == Color.BLUE) {
                    if(cwGames.get(gameID).getTeam(id)!= Color.BLUE) return;
                    int guess = cwGames.get(gameID).guessScore(Color.BLUE, guessString);
                    if(guess==9999) return;
                    event.getChannel().sendMessage("Blue Team has guessed: "+guessString).queue();
                    if(guess==-9999) {
                        event.getChannel().sendMessage("Blue Team picked the black card. Red team wins!").queue();
                        cwGames.remove(gameID);
                    }
                    if(guess==0) {
                        event.getChannel().sendMessage("That was a white card, no cards removed").queue();
                        cwGames.get(gameID).getCard(guessString).setValue(Color.GREY);
                        spyNum=0;
                    }
                    if(guess==1) {
                        int[] scores = cwGames.get(gameID).getScores();
                        scores[1]=scores[1]-1;
                        cwGames.get(gameID).setScores(scores);
                        event.getChannel().sendMessage("That was a Blue card! Only "+cwGames.get(gameID).getScores()[1]+" cards remaining!").queue();
                        cwGames.get(gameID).getCard(guessString).setValue(Color.BLUE);
                        spyNum--;
                    }
                    if(guess==-1) {
                        int[] scores = cwGames.get(gameID).getScores();
                        scores[0]=scores[0]-1;
                        cwGames.get(gameID).setScores(scores);
                        event.getChannel().sendMessage("Oops... That was a Red card!").queue();
                        cwGames.get(gameID).getCard(guessString).setValue(Color.RED);
                        spyNum=0;
                    }
                } else {
                    if(cwGames.get(gameID).getTeam(id)!= Color.RED) {
                        System.out.println("not red");
                        return;
                    }
                    int guess = cwGames.get(gameID).guessScore(Color.RED, guessString);
                    if(guess==9999) return;
                    event.getChannel().sendMessage("Red Team has guessed: "+guessString).queue();
                    if(guess==-9999) {
                        event.getChannel().sendMessage("Red Team picked the black card. Blue team wins!").queue();
                        cwGames.remove(gameID);
                    }
                    if(guess==0) {
                        event.getChannel().sendMessage("That was a white card, no cards removed").queue();
                        cwGames.get(gameID).getCard(guessString).setValue(Color.GREY);
                        spyNum=0;
                    }
                    if(guess==1) {
                        int[] scores = cwGames.get(gameID).getScores();
                        scores[0]=scores[0]-1;
                        cwGames.get(gameID).setScores(scores);
                        event.getChannel().sendMessage("That was a Red card! Only "+cwGames.get(gameID).getScores()[1]+" cards remaining!").queue();
                        cwGames.get(gameID).getCard(guessString).setValue(Color.RED);
                        spyNum--;
                    }
                    if(guess==-1) {
                        int[] scores = cwGames.get(gameID).getScores();
                        scores[1]=scores[1]-1;
                        cwGames.get(gameID).setScores(scores);
                        event.getChannel().sendMessage("Oops... That was a Blue card!").queue();
                        cwGames.get(gameID).getCard(guessString).setValue(Color.BLUE);
                        spyNum=0;
                    }
                }
                if(cwGames.get(gameID).isGameOver()) {
                    String winner = cwGames.get(gameID).getScores()[0]==0 ? "Red":"Blue";
                    event.getChannel().sendMessage("cwGames.get(gameID) Over! "+winner+" wins!").queue();
                    cwGames.remove(gameID);
                }
                cwGames.get(gameID).setSpyNum(spyNum);
                event.getChannel().sendMessage(cwGames.get(gameID).toString()).queue();
                event.getGuild().getMemberById(cwGames.get(gameID).getTeamBlue().get(0)).getUser().openPrivateChannel().queue((channel) ->
                {
                    channel.editMessageById(cwGames.get(gameID).getBlueSpyID(), cwGames.get(gameID).spyInfo()).queue();
                });
                event.getGuild().getMemberById(cwGames.get(gameID).getTeamRed().get(0)).getUser().openPrivateChannel().queue((channel) ->
                {
                    channel.editMessageById(cwGames.get(gameID).getRedSpyID(), cwGames.get(gameID).spyInfo()).queue();
                });
                if(spyNum>0) return;
                if(cwGames.get(gameID).getTurn()== Color.BLUE) cwGames.get(gameID).setTurn(Color.RED);
                else cwGames.get(gameID).setTurn(Color.BLUE);
                String spy = cwGames.get(gameID).getTurn()== Color.RED ? "Red":"Blue";
                event.getChannel().sendMessage(cwGames.get(gameID).toString());
                event.getChannel().sendMessage(spy+" Spymaster, please select a word and the number of cards that are related (Example: Water 3)").queue();
            }
            default -> { }
        }
    }

    public int getCWGameID(Long id) {
        if(cwGames.size()<1) return -1;
        for(int i=0;i<cwGames.size();i++) {
            if(cwGames.get(i).getPlayers().contains(id)) return i;
        }
        return -1;
    }


    //Chess Game -+-

    /**
     * Creates a new Chess game against the bot
     * and adds it to the game list.
     * This uses the default chess board.
     *
     * @param p1 the player
     * @return the id of the game
     */
    public int newChess(Player p1) {
        Chess game = new Chess(p1);
        chessGames.add(game);
        return chessGames.size()-1;
    }
    /**
     * Creates a new Chess game against the bot
     * and adds it to the game list.
     * This uses a custom chess board written
     * in FEN notation.
     *
     * @param p1 the player
     * @param fen the fen string
     * @return the id of the game
     */
    public int newChess(Player p1, String fen) {
        Chess game = new Chess(p1);
        if(game.setFEN(fen)) {
            chessGames.add(game);
            return chessGames.size()-1;
        } else {
            return -1;
        }
    }
    /**
     * Creates a new Chess game against another
     * player and adds it to the game list.
     * This uses the default chess board.
     *
     * @param p1 the first player
     * @param p2 the second player
     * @return the id of the game
     */
    public int newChess(Player p1, Player p2) {
        Chess game = new Chess(p1,p2);
        chessGames.add(game);
        return chessGames.size()-1;
    }
    /**
     * Creates a new Chess game against another
     * player and adds it to the game list.
     * This uses a custom chess board written
     * in FEN notation.
     *
     * @param p1 the player
     * @param p2 the second player
     * @param fen the fen string
     * @return the id of the game
     */
    public int newChess(Player p1, Player p2, String fen) {
        Chess game = new Chess(p1,p2);
        if(game.setFEN(fen)) {
            chessGames.add(game);
            return chessGames.size()-1;
        } else {
            return -1;
        }
    }

    /**
     * Checks if a given message is considered a value command for the game
     *
     * @param event the GuildMessageReceivedEvent
     * @return if it's valid
     */
    public boolean validChessCommand(GuildMessageReceivedEvent event) {
        String args = event.getMessage().getContentRaw();
        int gameID = getChessGameID(event.getAuthor().getIdLong());
        if(chessGames.size()<1||gameID==-1||!chessGames.get(gameID).getTextChannel().equals(event.getChannel())) return false;
        return (args.startsWith("move")||args.startsWith("board_info"));
    }

    /**
     * Executes a Chess command.
     * This contains most commands
     * besides the starting one.
     *
     * @param event the GuildMessageReceivedEvent
     */
    public void execChessCommand(GuildMessageReceivedEvent event) {
        int gameID = getChessGameID(event.getAuthor().getIdLong());
        String args[] = event.getMessage().getContentRaw().split(" ");
        switch (args[0]) {
            case "move" -> {
                if(gameID==-1) return;
                try{
                    Chess game = getChessGame(gameID);
                    Board board = game.getBoard();
                    Long id = event.getAuthor().getIdLong();
                    if(!id.equals(game.getCurrentTurn().getId())) return;
                    boolean white = game.getCurrentTurn().isWhiteSide();
                    int[] pos = board.getPos(args[1], white);
                    Piece piece = null;
                    if(pos.length==5) {
                        switch (pos[4]) {
                            case 1 -> piece = new Queen(white);
                            case 2 -> piece = new Rook(white);
                            case 3 -> piece = new Knight(white);
                            case 4 -> piece = new Bishop(white);
                            default -> {}
                        }
                    }
                    if(game.playerMove(game.getCurrentTurn(),pos[0],pos[1],pos[2],pos[3], piece)) {
                        ImageHandler.updateIMGBoard(board);
                        event.getChannel().retrieveMessageById(game.getChessID()).complete().delete().queue();
                        File file = new File(ImageHandler.alboard);
                        EmbedBuilder result= new EmbedBuilder();
                        result.setImage("attachment://alboard.png");
                        result.setTitle("Chess Game");
                        result.setAuthor(game.title);
                        String turn = "";
                        if(game.getChecks()[0]) {
                            turn+="White is in Check.\n";
                        }
                        if(game.getChecks()[1]) {
                            turn+="Black is in Check.\n";
                        }
                        String values = Kebbot.client.getEvalScore(game.getFEN(), 1000);
                        turn += "It's ";
                        turn += game.getCurrentTurn().isWhiteSide() ? "White":"Black";
                        turn += "'s turn. ";
                        turn += game.getCurrentTurn().isWhiteSide() ? "Black":"White";
                        result.setFooter(turn+"'s move was: "+args[1]+".\nValue of Board: "+values+".");
                        final int finalGameID=gameID;
                        event.getChannel().sendMessage(result.build())
                                .addFile(file, "alboard.png")
                                .queue((message) -> {
                                    getChessGame(finalGameID).setChessID(message.getIdLong());
                                });
                        event.getMessage().delete().queue();
                        if(game.isEnd()) {
                            String out;
                            switch (game.getStatus()) {
                                case FORFEIT -> {out="Game Over by Forfeit";}
                                case BLACK_WIN -> {out="Game is Over. Black has won!";}
                                case STALEMATE -> {out="Game is Over. There was a stalemate";}
                                case WHITE_WIN -> {out="Game is Over. White has won!";}
                                default -> {out="Error";}
                            }
                            event.getChannel().sendMessage(out).queue();
                            game=null;
                            getChessGame(gameID).setChessID(null);
                        }
                    }
                    if(!game.getPlayers()[1].isHumanPlayer()) {
                        String botMove = Kebbot.client.getBestMove(game.getFEN(), 2000);
                        white = game.getCurrentTurn().isWhiteSide();
                        pos = board.getPos(botMove, white);
                        piece = null;
                        if(pos.length==5) {
                            switch (pos[4]) {
                                case 1 -> piece = new Queen(white);
                                case 2 -> piece = new Rook(white);
                                case 3 -> piece = new Knight(white);
                                case 4 -> piece = new Bishop(white);
                                default -> {}
                            }
                        }
                        if(game.playerMove(game.getCurrentTurn(),pos[0],pos[1],pos[2],pos[3], piece)) {
                            ImageHandler.updateIMGBoard(board);
                            event.getChannel().retrieveMessageById(game.getChessID()).complete().delete().queue();
                            File file = new File(ImageHandler.alboard);
                            EmbedBuilder result = new EmbedBuilder();
                            result.setImage("attachment://alboard.png");
                            result.setTitle("Chess Game");
                            result.setAuthor(game.title);
                            String turn = "";
                            if (game.getChecks()[0]) {
                                turn += "White is in Check.\n";
                            }
                            if (game.getChecks()[1]) {
                                turn += "Black is in Check.\n";
                            }
                            String values = Kebbot.client.getEvalScore(game.getFEN(), 1000);
                            turn += "It's ";
                            turn += game.getCurrentTurn().isWhiteSide() ? "White" : "Black";
                            turn += "'s turn. ";
                            turn += game.getCurrentTurn().isWhiteSide() ? "Black" : "White";
                            result.setFooter(turn+"'s move was: "+args[1]+".\nValue of Board: "+values+".");
                            final int finalGameID = gameID;
                            event.getChannel().sendMessage(result.build())
                                    .addFile(file, "alboard.png")
                                    .queue((message) -> {
                                        getChessGame(finalGameID).setChessID(message.getIdLong());
                                    });
                        }
                    }
                    else throw new Exception("That move is not valid");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error here boyo");
                    event.getMessage().delete().queue();
                    event.getChannel().sendMessage("The move "+args[1]+" is not valid // There has been an error").queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
                }
                System.out.println("move command");
            }
            case "board_info" -> {
                Chess game=null;
                Board board = new Board();
                if(args.length==1) {
                    if(gameID==-1) return;
                    game = chessGames.get(gameID);
                    board = game.getBoard();
                }
                if(args.length==2) {
                    try {
                        gameID=Integer.parseInt(args[1]);
                        game = chessGames.get(gameID);
                        board = game.getBoard();
                    } catch (Exception e) {
                        return;
                    }
                }
                if(args.length!=2) {
                    board = new Board();
                    if(game!=null&&!game.setFEN(event.getMessage().getContentRaw().split(" ",2)[1])) return;
                }
                try{
                    String values = Kebbot.client.getEvalScore(game.getFEN(), 1000);
                    ImageHandler.updateIMGBoard(board);
                    File file = new File(ImageHandler.alboard);
                    EmbedBuilder result= new EmbedBuilder();
                    result.setTitle("Chess board");
                    result.setImage("attachment://alboard.png");
                    event.getChannel().sendMessage(result.build())
                            .addFile(file, "alboard.png")
                            .queue();
                    event.getChannel().sendMessage(values).queue();
                } catch (Exception e) {
                    System.out.println("Error here boyo");
                }
                System.out.println("board_info command");
            }
            default -> { }
        }
    }

    /**
     * Checks if a given id exists in the list
     * and if so, returns the corresponding game.
     *
     * @param i the game's id
     * @return Chess game
     */
    public Chess getChessGame(int i) {
        return chessGames.get(i);
    }

    /**
     * Given a player's ID checks if that
     * player is currently in an active game
     * and if so returns that game's id (
     * returns -1 if the player is not in a game).
     *
     * @param id the player's id
     * @return the game's id
     */
    public int getChessGameID(Long id) {
        if(chessGames.size()<1) return -1;
        for(int i=0;i<chessGames.size();i++) {
            Long id1 = chessGames.get(i).getPlayers()[0].getId();
            Long id2 = chessGames.get(i).getPlayers()[1].getId();
            if(id1!=null&&id1.equals(id)) return i;
            if(id2!=null&&id2.equals(id)) return i;
        }
        return -1;
    }


    //BlackJack Game -+-

    /**
     * Creates a new BlackJack game
     * 
     * @param playerID the player's ID
     * @param wager the player's wager
     * @return the game ID
     */
    public int newBJ(long playerID, int wager) {
        BlackJack game = new BlackJack(playerID);
        game.setWager(wager);
        bjGames.add(game);
        return bjGames.size()-1;
    }

    /**
     * Checks if a command is a valid
     * BlackJack command
     * 
     * @param event the MessageReceivedEvent
     * @return if command is valid
     */
    public boolean validBJCommand(MessageReceivedEvent event) {
        String args = event.getMessage().getContentRaw();
        int gameID = getBJGameID(event.getAuthor().getIdLong());
        if(bjGames.size()<1||gameID==-1||!bjGames.get(gameID).getMessageChannel().equals(event.getChannel())) return false;
        return (args.startsWith("hit")||args.startsWith("stay"));
    }

    /**
     * Executes a BlackJack command.
     * This contains most commands
     * besides the starting one.
     *
     * @param event the GuildMessageReceivedEvent
     */
    public void execBJCommand(MessageReceivedEvent event) {
        int gameID = getBJGameID(event.getAuthor().getIdLong());
        if(gameID==-1) return;
        String[] args = event.getMessage().getContentRaw().split(" ");
        BlackJack game = getBJGame(gameID);
        switch (args[0]) {
            case "hit" -> {
                game.playerHit();
            }
            case "stay" -> {
                game.dealerHit();
            }
        }
        String gameOver = game.gameOver();
        event.getChannel().sendMessage("This is the current state of the game:\n"+game.toString()).queue();
        if(!gameOver.equals("")) {
            event.getChannel().sendMessage(gameOver).queue();
        }
    }

    /**
     * Given a game ID returns
     * that game.
     * 
     * @param i game ID
     * @return the game
     */
    public BlackJack getBJGame(int i) {
        return bjGames.get(i);
    }

    /**
     * Given a player's ID checks
     * if that player is currently
     * on a game, and if so, gets
     * that game's ID
     * 
     * @param id the player's ID
     * @return the game
     */
    public int getBJGameID(long id) {
        for(int i=0;i<bjGames.size();i++) {
            Long pID = bjGames.get(i).getPlayerID();
            if(pID.equals(id)) return i;
        }
        return -1;
    }
}
