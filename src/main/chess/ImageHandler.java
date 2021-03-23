package chess;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This class handles the image creation
 * process for the Chess Game, creating
 * an image showing the current state of the board
 * that can be later used in the Discord Bot.
 *
 * @author Daniel Rocha
 * @version 1.0
 * @see bot.Kebbot
 * @see Chess
 */

public class ImageHandler {
    //these are the images for the original board, the altered board and all the pieces
    public static String board = ("../BOARD.png");
    public static String alboard = ("../ALBOARD.png");
    public static String bb = ("../bb.png");
    public static String bk = ("../bk.png");
    public static String bn = ("../bn.png");
    public static String bq = ("../bq.png");
    public static String br = ("../br.png");
    public static String bp = ("../bp.png");
    public static String wb = ("../wb.png");
    public static String wk = ("../wk.png");
    public static String wn = ("../wn.png");
    public static String wq = ("../wq.png");
    public static String wr = ("../wr.png");
    public static String wp = ("../wp.png");

    /**
     * Updates the current altered board image
     * based on the given board
     *
     * @param board the new board
     * @throws Exception if it couldn't alter the board
     */
    public static void updateIMGBoard(Board board) throws Exception {
        BufferedImage bgImage = readImage(ImageHandler.board);
        BufferedImage fgImage;
        Graphics2D g = bgImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(bgImage, 0, 0, null);
        for(int i=0;i<8;i++) {
            for(int j=0;j<8;j++) {
                if(board.getSquare(i,j).getPiece()==null) continue;
                fgImage = readImage(getPieceImage(board.getSquare(i,j).getPiece()));
                g.drawImage(fgImage, j*150, 1050-(i*150), null);
            }
        }
        g.dispose();
        if (g != null){
            writeImage(bgImage, alboard, "PNG");
        }else
            throw new Exception("Couldn't alter image");
    }

    /**
     * Gets the file location for the image
     * corresponding to a given Piece (gets
     * the image with the same piece type
     * and also the same color)
     *
     * @param piece the piece to check
     * @return the file location
     */
    public static String getPieceImage(Piece piece) {
        if(piece instanceof Pawn) {
            return piece.isWhite() ? wp : bp;
        }
        if(piece instanceof King) {
            return piece.isWhite() ? wk : bk;
        }
        if(piece instanceof Queen) {
            return piece.isWhite() ? wq : bq;
        }
        if(piece instanceof Bishop) {
            return piece.isWhite() ? wb : bb;
        }
        if(piece instanceof Rook) {
            return piece.isWhite() ? wr : br;
        }
        if(piece instanceof Knight) {
            return piece.isWhite() ? wn : bn;
        }
        return ("");
    }

    /**
     * Tries to read an image and return it.
     *
     * @param fileLocation the image location
     * @return the image
     */
    public static BufferedImage readImage(String fileLocation) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(fileLocation));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    /**
     * Tries to write an image on a given file location,
     * using the given file extension.
     *
     * @param img the image
     * @param fileLocation the file
     * @param extension the file extension
     */
    public static void writeImage(BufferedImage img, String fileLocation, String extension) {
        try {
            BufferedImage bi = img;
            File outputFile = new File(fileLocation);
            ImageIO.write(bi, extension, outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
