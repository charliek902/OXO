package edu.uob;
import java.util.ArrayList;


public class OXOModel {

    public ArrayList<OXOPlayer> players;
    private int currentPlayerNumber;
    private OXOPlayer winner;
    private boolean gameDrawn;
    private int winThreshold;
    public ArrayList<ArrayList<OXOPlayer>> cells;

    public OXOModel(int numberOfRows, int numberOfColumns, int winThresh) {

        winThreshold = winThresh;
        players = new ArrayList<>(2);
        cells = new ArrayList<>(numberOfRows);
        for (int i = 0; i < numberOfRows; i++) {
            cells.add(new ArrayList<>(numberOfColumns));
            for (int j = 0; j < numberOfColumns; j++) {
                cells.get(i).add(null);
            }
        }
    }

    public int getNumberOfPlayers() {

        return this.players.size();
    }

    // modify addPlayer, will need to be put into an array list...

    public void addPlayer(OXOPlayer player) {
        players.add(player);
    }

    public OXOPlayer getPlayerByNumber(int number) {
        return players.get(number);
    }

    public OXOPlayer getWinner() {
        return winner;
    }

    public void setWinner(OXOPlayer player) {
        winner = player;
    }

    public int getCurrentPlayerNumber() {
        return currentPlayerNumber;
    }

    public void setCurrentPlayerNumber(int playerNumber) {
        currentPlayerNumber = playerNumber;
    }

    public int getNumberOfRows() {
        return cells.size();
    }

    public int getNumberOfColumns() {
        return cells.get(0).size();
    }

    public OXOPlayer getCellOwner(int rowNumber, int colNumber) {
        return cells.get(rowNumber).get(colNumber);
    }


    public void setCellOwner(int rowNumber, int colNumber, OXOPlayer player) {
        cells.get(rowNumber).set(colNumber, player);
    }

    public void setWinThreshold(int winThresh) {
        winThreshold = winThresh;
    }

    public int getWinThreshold() {
        return winThreshold;
    }

    public void setGameDrawn() {
        gameDrawn = true;
    }

    public boolean isGameDrawn() {

        int board_size = cells.size() * cells.get(0).size();

        int count = 0;

        for (ArrayList<OXOPlayer> cell : cells) {
            for (int j = 0; j < cells.get(0).size(); j++) {
                if (cell.get(j) != null) {
                    count += 1;
                }
            }
        }
        return count == board_size;
    }
}
