package chess;

/**
 * Lists all the possible statuses for
 * a chess game
 *
 * @author Daniel Rocha
 * @version 1.0
 * @see Chess
 */
public enum GameStatus {
    ACTIVE,
    BLACK_WIN,
    WHITE_WIN,
    FORFEIT,
    STALEMATE,
    RESIGNATION
}
