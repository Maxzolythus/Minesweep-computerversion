import javafx.application.Platform;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * MinesweepGUI.java
 * Created by Maxzolythus Ferguson on 7/8/2016.
 */
public class MinesweepModel extends Observable {
    private Character[][] gameBoard;
    private Character[][] blockBoard; // Represents what is still hidden on the board
    private Character[][] flags;
    public int size;
    private int maxMines;
    private int flagNum = 0;
    private int turnNum = 0;
    private boolean gameOver = false;
    private boolean isSweeping = true;

    private final Character MINE = '*';
    private final Character EMPTY = ' ';
    private final Character HIDDEN = '/';
    private final Character FLAG = '>';

    protected int seconds = 0;
    protected int min = 0;
    private Timer timer = new Timer();
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                    if (seconds < 59) {
                        seconds++;
                    } else {
                        min++;
                        seconds = 0;
                    }

                    setChanged();
                    notifyObservers();
                }
            });
        }
    };

    public MinesweepModel(int size){
        this.size = size;
        if(size == 9){
            maxMines = 10;
        }
        else if(size == 16){
            maxMines = 40;
        }
        else{
            maxMines = 100;
        }

        gameBoard = new Character[size][size];
        blockBoard = new Character[size][size];
        flags = new Character[size][size];

        for(int r = 0;
            r < size;
            r++) {
            for (int c = 0;
                 c < size;
                 c++) {
                blockBoard[r][c] = HIDDEN;
            }
        }

        for(int r = 0;
            r < size;
            r++) {
            for (int c = 0;
                 c < size;
                 c++) {
                flags[r][c] = EMPTY;
            }
        }

        int numOfMines = 0;

        // First pass through the game board populates it with mines and blanks
        while(numOfMines < maxMines) {
            for (int r = 0;
                 r < size;
                 r++) {
                for (int c = 0;
                     c < size;
                     c++) {
                    int random = randInt(0, 10);

                    // Second check is necessary, as during one pass through the numOfMines can be > maxMines
                    if (random <= 2 && numOfMines < maxMines) {
                        gameBoard[r][c] = MINE;
                        numOfMines++;
                    } else {
                        gameBoard[r][c] = EMPTY;
                    }
                }
            }
        }

        // Second pass through, populates the board with numbers.
        for(int r = 0;
            r < size;
            r++) {
            for (int c = 0;
                 c < size;
                 c++) {
                int num = 0;
                if(gameBoard[r][c] == EMPTY) {
                    if (r == 0) {
                        if(gameBoard[r + 1][c] == MINE){ // Look down
                            num++;
                        }

                        if(c > 0){
                            if(gameBoard[r][c - 1] == MINE){ // Look left
                                num++;
                            }

                            if(gameBoard[r + 1][c - 1] == MINE){ // Look down and left
                                num++;
                            }
                        }

                        if(c + 1 < size){
                            if(gameBoard[r][c + 1] == MINE){ // Look right
                                num++;
                            }

                            if(gameBoard[r + 1][c + 1] == MINE){ // Look down and right
                                num++;
                            }
                        }
                    } else if (c == 0) {
                        if(gameBoard[r][c + 1] == MINE){ // Look right
                            num++;
                        }

                        if(r > 0){
                            if(gameBoard[r - 1][c] == MINE){ // Look up
                                num++;
                            }

                            if(gameBoard[r - 1][c + 1] == MINE){ // Look up and right
                                num++;
                            }
                        }

                        if(r + 1 < size){
                            if(gameBoard[r + 1][c] == MINE){ // Look down
                                num++;
                            }

                            if(gameBoard[r + 1][c + 1] == MINE){ // Look down and right
                                num++;
                            }
                        }
                    } else {
                        if(r + 1 == size) {
                            if(gameBoard[r - 1][c] == MINE){ // Look up
                                num++;
                            }

                            if(c + 1 < size) {
                                if(gameBoard[r][c + 1] == MINE){ // Look right
                                    num++;
                                }

                                if (gameBoard[r - 1][c + 1] == MINE) { // Look up and right
                                    num++;
                                }
                            }

                            if(c > 0) {
                                if(gameBoard[r][c - 1] == MINE){ // Look left
                                    num++;
                                }

                                if (gameBoard[r - 1][c - 1] == MINE) { // Look up and left
                                    num++;
                                }
                            }

                        }

                        if(c + 1 == size) {
                            if(r + 1 < size) {
                                if (gameBoard[r + 1][c] == MINE) { // Look down
                                    num++;
                                }

                                if(gameBoard[r + 1][c - 1] == MINE){ // Look down and left
                                    num++;
                                }
                            }

                            if(r > 0) {
                                if (gameBoard[r - 1][c] == MINE) { // Look up
                                    num++;
                                }

                                if (gameBoard[r - 1][c - 1] == MINE) { // Look up and left
                                    num++;
                                }
                            }

                            if(gameBoard[r][c - 1] == MINE){ // Look left
                                num++;
                            }
                        }

                        if(r > 0 && r +1 < size && c > 0 && c +1 < size){ // Then we can look every way
                            if(gameBoard[r + 1][c] == MINE){ // Look down
                                num++;
                            }

                            if(gameBoard[r + 1][c + 1] == MINE){ // Look down and right
                                num++;
                            }

                            if(gameBoard[r + 1][c - 1] == MINE){ // Look down and left
                                num++;
                            }

                            if(gameBoard[r - 1][c] == MINE){ // Look up
                                num++;
                            }

                            if(gameBoard[r - 1][c + 1] == MINE){ // Look up and right
                                num++;
                            }

                            if(gameBoard[r - 1][c - 1] == MINE){ // Look up and left
                                num++;
                            }

                            if(gameBoard[r][c + 1] == MINE){ // Look right
                                num++;
                            }

                            if(gameBoard[r][c - 1] == MINE){ // Look left
                                num++;
                            }

                        }
                    }
                }

                if(num > 0){
                    gameBoard[r][c] = (Integer.toString(num)).charAt(0); // num will never be a double digit
                }

            }
        }
    }

    /**
     * Starts the tread the timer is on
     */
    public void start(){
        timer.scheduleAtFixedRate(task,1000,1000);
    }

    public void stopTimer() { timer.cancel();}

    /**
     * Represents you taking one guess
     * @param row The row number where the guess is taking place
     * @param col The column number where the guess is taking place
     */
    public void guess(int row, int col){
        if(!gameOver) {
            turnNum++;
            if(isSweeping) {
                if (gameBoard[row][col] == MINE) {
                    gameOver = true;
                } else if (Character.isDigit(gameBoard[row][col]) && turnNum != 1) {
                    blockBoard[row][col] = gameBoard[row][col];
                }
                else {
                    if (Character.isDigit(gameBoard[row][col]) && turnNum == 1) {
                        blockBoard[row][col] = gameBoard[row][col];
                    }

                    ArrayList<Point> zone = getZone(row, col);
                    for (int r = 0;
                         r < size;
                         r++) {
                        for (int c = 0;
                             c < size;
                             c++) {
                            if (zone.contains(new Point(r, c)) && flags[r][c] == EMPTY) {
                                blockBoard[r][c] = EMPTY;
                            }
                        }
                    }
                }
            }
            else{
                if(flags[row][col] == EMPTY) {
                    flags[row][col] = FLAG;
                    flagNum++;
                }
                else{
                    flags[row][col] = EMPTY;
                    flagNum--;
                }
            }
        }
    }

    /**
     * Gets the neighbouring blank spaces of a precise point
     * @param row The row number where the neighbors are being retrieved
     * @param col The column number where the neighbors are being retrieved
     * @return An ArrayList of coordinates of the neighbors
     */
    private ArrayList<Point> getNeighbors(int row, int col){
        ArrayList<Point> pointList = new ArrayList<>();

        if (row == 0) {
            getNeighborsUtil(row + 1, col, pointList);

            if(col > 0){
                getNeighborsUtil(row, col - 1, pointList);
            }

            if(col + 1 < size){
                getNeighborsUtil(row, col + 1, pointList);
            }
        } else if (col == 0) {
            getNeighborsUtil(row, col + 1, pointList);

            if(row > 0){
                getNeighborsUtil(row - 1, col, pointList);
            }

            if(row + 1 < size){
                getNeighborsUtil(row + 1, col, pointList);
            }
        } else {
            if(row + 1 == size) {
                getNeighborsUtil(row - 1, col, pointList);

                if(col + 1 < size) {
                    getNeighborsUtil(row, col + 1, pointList);
                }

                if(col > 0) {
                    getNeighborsUtil(row, col - 1, pointList);
                }

            }

            if(col + 1 == size) {
                if(row + 1 < size) {
                    getNeighborsUtil(row + 1, col, pointList);
                }

                if(row > 0) {
                    getNeighborsUtil(row - 1, col, pointList);
                }

                getNeighborsUtil(row, col - 1, pointList);
            }

            if(row > 0 && row +1 < size && col > 0 && col +1 < size){ // Then we can look every way
                getNeighborsUtil(row + 1, col, pointList);

                getNeighborsUtil(row - 1, col, pointList);

                getNeighborsUtil(row, col + 1, pointList);

                getNeighborsUtil(row, col - 1, pointList);

            }
        }

        return pointList;
    }

    /**
     * Gets a neighboring blank space for a given row/col
     * @param row Row to parse through
     * @param col Column to parse through
     * @param pointList List of points to add to
     */
    private void getNeighborsUtil(int row, int col, ArrayList<Point> pointList){
        if(gameBoard[row][col] == EMPTY && flags[row][col] == EMPTY
                && !pointList.contains(new Point(row, col))){
            pointList.add(new Point(row, col));
        }
        else if(Character.isDigit(gameBoard[row][col]) && flags[row][col] == EMPTY
                && !pointList.contains(new Point(row, col))){
            if(hasEmptyNeighbour(row, col)){
                pointList.add(new Point(row, col));
            }
        }
    }

    /**
     * Gets a group of neighbouring blank spaces (aka a zone)
     * @param row The row number where the neighbors start to be retrieved
     * @param col The column number where the neighbors start to be retrieved
     * @return An ArrayList of coordinates of the zone
     */
    private ArrayList<Point> getZone(int row, int col){
        ArrayList<Point> masterList = new ArrayList<>();
        ArrayList<Point> subList = new ArrayList<>();
        masterList.add(new Point(row, col));

        subList = getNeighbors(row, col);
        masterList.addAll(subList);
        while(!subList.isEmpty()){
            for(int i = 0;
                    i < masterList.size();
                    i++) {
                subList.clear();
                subList.addAll(getNeighbors(masterList.get(i).x, masterList.get(i).y));
                for(int v = 0;
                        v < subList.size();
                        v++) {
                    if(!masterList.contains(subList.get(v))) {
                        masterList.add(subList.get(v));
                    }
                }
                subList.clear();
            }
        }

        return masterList;
    }

    /**
     * Checks a given space to see if one of it's neighbours is EMPTY
     * @param row given row
     * @param col given column
     * @return boolean based on if there is and EMPTY (true) or not (false)
     */
    private boolean hasEmptyNeighbour(int row, int col){

        if (row == 0) {
            if(gameBoard[row + 1][col] == EMPTY){
                // Look down
                return true;
            }

            if(col > 0){
                if(gameBoard[row][col - 1] == EMPTY){
                    // Look left
                    return true;
                }
            }

            if(col + 1 < size){
                if(gameBoard[row][col + 1] == EMPTY){
                    // Look right
                    return true;
                }

            }
        } else if (col == 0) {
            if(gameBoard[row][col + 1] == EMPTY){
                // Look right
                return true;
            }

            if(row > 0){
                if(gameBoard[row - 1][col] == EMPTY){
                    // Look up
                    return true;
                }
            }

            if(row + 1 < size){
                if(gameBoard[row + 1][col] == EMPTY){
                    // Look down
                    return true;
                }

            }
        } else {
            if (row + 1 == size) {
                if (gameBoard[row - 1][col] == EMPTY) {
                    // Look up
                    return true;
                }


                if (col + 1 < size) {
                    if (gameBoard[row][col + 1] == EMPTY) {
                        // Look right
                        return true;
                    }
                }

                if (col > 0) {
                    if (gameBoard[row][col - 1] == EMPTY) {
                        // Look left
                        return true;
                    }
                }

            }

            if (col + 1 == size) {
                if (row + 1 < size) {
                    if (gameBoard[row + 1][col] == EMPTY) {
                        // Look down
                        return true;
                    }
                }

                if (row > 0) {
                    if (gameBoard[row - 1][col] == EMPTY) {
                        // Look up
                        return true;
                    }

                }

                if (gameBoard[row][col - 1] == EMPTY) {
                    // Look left
                    return true;
                }
            }

            if (row > 0 && row + 1 < size && col > 0 && col + 1 < size) { // Then we can look every way
                if (gameBoard[row + 1][col] == EMPTY) {
                    // Look down
                    return true;
                }

                if (gameBoard[row + 1][col + 1] == EMPTY) {
                    // Look down and right
                    return true;
                }

                if (gameBoard[row + 1][col - 1] == EMPTY) {
                    // Look down and left
                    return true;
                }

                if (gameBoard[row - 1][col] == EMPTY) {
                    // Look up
                    return true;
                }

                if (gameBoard[row - 1][col - 1] == EMPTY) {
                    // Look up and left
                    return true;
                }

                if (gameBoard[row - 1][col + 1] == EMPTY) {
                    // Look up and right
                    return true;
                }

                if (gameBoard[row][col + 1] == EMPTY) {
                    // Look right
                    return true;
                }

                if (gameBoard[row][col - 1] == EMPTY) {
                    // Look left
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns a pseudo-random number between min and max, inclusive.
     *
     * @param min Minimum value
     * @param max Maximum value.  Must be greater than min.
     * @return Integer between min and max, inclusive.
     */
    public static int randInt(int min, int max) {

        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
     * Shows if it is game over or not
     * @return gameOver
     */
    public boolean isGameOver(){
        return gameOver;
    }

    /**
     * Gets the whole hidden 2D array, blockBoard
     * @return blockBoard
     */
    public Character[][] getHidden(){
        return blockBoard;
    }

    /**
     * Get function for the boolean isSweeping
     * @return isSweeping
     */
    public boolean getSweeping(){return isSweeping;}

    /**
     * Get function for the boolean isSweeping
     * @return isSweeping
     */
    public void setSweeping(){isSweeping = !isSweeping;}

    /**
     * Checks if the game has been won
     * @return true if the game was won and false otherwise
     */
    public boolean hasWon(){
        for (int r = 0;
             r < size;
             r++) {
            for (int c = 0;
                 c < size;
                 c++) {
                if (blockBoard[r][c] == HIDDEN && gameBoard[r][c] != MINE) {
                    return false;
                }

            }
        }
        return true;
    }

    /**
     * Checks the flags array to see if there is a flag in a given space
     * @param row the given row
     * @param col the given column
     * @return true if there is a flag and false otherwise
     */
    public boolean hasFlag(int row, int col){
        return flags[row][col] == FLAG;
    }

    /**
     * Based on placed flags it returns the amount of mines left.
     * @return number of mines left
     */
    public int getRemainingMines(){
        return maxMines - flagNum;
    }

    /**
     * Returns the gameBoard
     * @return gameBoard
     */
    public Character[][] getGameBoard() {
        return gameBoard;
    }
}
