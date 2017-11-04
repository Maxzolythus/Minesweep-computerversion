import java.util.Observable;
import java.util.Observer;

public class MinesweepController extends Observable implements Observer {

    private MinesweepModel model;
    protected int seconds = 0;
    protected int min = 0;

    public MinesweepController(int size){
        model = new MinesweepModel(size);
        model.addObserver(this);
    }

    /**
     * Guesses for a particular row/col on the gameBoard
     * @param row
     * @param col
     */
    public void guess(int row, int col){
        model.guess(row, col);
        setChanged();
        notifyObservers();
    }

    /**
     * Starts the timer in the model
     */
    public void start(){
        model.start();
    }

    public void update(Observable t, Object o){
        min = model.min;
        seconds = model.seconds;

        setChanged();
        notifyObservers();
    }

    /**
     * Checks the flags array to see if there is a flag in a given space
     * @param row the given row
     * @param col the given column
     * @return true if there is a flag and false otherwise
     */
    public boolean hasFlag(int row, int col){
        return model.hasFlag(row, col);
    }

    public boolean hasWon(){
        return model.hasWon();
    }

    public boolean isGameOver(){
        return model.isGameOver();
    }

    /**
     * Gets a single space from the gameBoard
     * @param r The row to access
     * @param c The column to access
     * @return The character @ that point
     */
    public Character getSpace(int r, int c){
        return model.getGameBoard()[r][c];
    }

    public Character[][] getHidden() {
        return model.getHidden();
    }

    public int getRemainingMines() {
        return model.getRemainingMines();
    }

    /**
     * Changes isSweeping to the opposite of what it currently is
     */
    public void toggleSweeping(){model.setSweeping();}

    public boolean getSweeping(){
        return model.getSweeping();
    }
}
