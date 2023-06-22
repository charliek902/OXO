package edu.uob;
import java.util.ArrayList;
import edu.uob.OXOMoveException.*;


public class OXOController {
    public OXOModel gameModel;
    OXOPlayer reset;
    int z;
    boolean GameOver;
    boolean Stop_input = false;

    public OXOController(OXOModel model) {
        gameModel = model;
    }

    // this class allows me to compare different positions on the board...
    static private class Board_position {
        int x;
        int y;

        public Board_position(int row, int column){
            this.x = row;
            this.y = column;
        }
    }

    public void handleIncomingCommand(String command) throws OXOMoveException {

        int row;
        int column;

        if(Stop_input){
            return;
        }
        validate_String(command);
        GameOver = false;

        if(command.charAt(0) >= 'A' && command.charAt(0) <= 'Z'){
            char ch = command.charAt(0);
            char lower_case = Character.toLowerCase(ch);
            row = lower_case - 'a';
        }
        else{
            row = command.charAt(0) - 'a';
        }
        column = command.charAt(1) - '1';

        z = gameModel.getCurrentPlayerNumber();
        gameModel.setCellOwner(row, column, gameModel.players.get(z));


        if (isGameWon()) {
            GameOver = true;
            gameModel.setCurrentPlayerNumber(z);
            gameModel.setWinner(gameModel.players.get(z));
            Stop_input = true;
            // got to somehow stop input from users at this point until the board gets reset...

        }
        else {
            z += 1;
            if (z >= gameModel.getNumberOfPlayers()) {
                gameModel.setCurrentPlayerNumber(0);
            } else {
                gameModel.setCurrentPlayerNumber(z);
            }
        }
    }

    private void validate_String(String command) throws OXOMoveException{

        // now validate the string utilising all the exceptions...

        int row, column;
        char ch;

        if(command.length() != 2){
            int x = command.length();
            throw new InvalidIdentifierLengthException(x);
        }

        // converting any uppercase letters into lower case letters...

        if(command.charAt(0) >= 'A' && command.charAt(0) <= 'Z'){
            ch = command.charAt(0);
            ch = Character.toLowerCase(ch);
            row = ch - 'a';
        }
        else{
            ch = command.charAt(0);
            row = command.charAt(0) - 'a';
        }
        column = command.charAt(1) - '1';

        if(ch < 'a' || row > 'z'){
            throw new InvalidIdentifierCharacterException(RowOrColumn.ROW, ch);
        }

        if(column < 0 || column >= gameModel.getNumberOfColumns()){
            throw new OutsideCellRangeException(RowOrColumn.COLUMN, column);
        }
        if(row < 0 || row >= gameModel.getNumberOfRows()){
            throw new OutsideCellRangeException(RowOrColumn.ROW, row);
        }

        if(gameModel.getCellOwner(row, column) != null){
            throw new CellAlreadyTakenException(row, column);
        }
    }


    public void addRow() {

        int k = gameModel.getNumberOfRows();
        if(k >= 9){
            return;
        }
        ArrayList<OXOPlayer> new_row = new ArrayList<>(gameModel.getNumberOfColumns());
        for(int i = 0; i < gameModel.getNumberOfColumns(); i++){
            new_row.add(null);
        }
        gameModel.cells.add(new_row);

    }

    public void removeRow() {

        if(GameOver){
            return;
        }

        // loop over last remove row so that you don't remove any data...
        int i = gameModel.getNumberOfRows() - 1;

        if(i == 0){
            return;
        }

        for(int j = 0; j < gameModel.getNumberOfColumns(); j++){
            if(gameModel.getCellOwner(i, j) != null){
                return;
            }
        }
        gameModel.cells.remove(gameModel.getNumberOfRows() - 1);
    }

    public void addColumn() {

        int k = gameModel.getNumberOfColumns();
        if(k >= 9){
            return;
        }

        for(int i = 0; i < gameModel.getNumberOfRows(); i++){
            gameModel.cells.get(i).add(null);
        }
    }

    public void removeColumn() {

        if(GameOver){
            return;
        }

        int j = gameModel.getNumberOfColumns() - 1;
        if(j == 0){
            return;
        }
        for(int i = 0; i < gameModel.getNumberOfRows(); i++){
            if(gameModel.getCellOwner(i, j) != null){
                return;
            }
        }
        for(int i = 0; i < gameModel.getNumberOfRows(); i++){
            gameModel.cells.get(i).remove(gameModel.getNumberOfColumns() - 1);
        }
    }

    public void increaseWinThreshold() {

        int x = gameModel.getWinThreshold();
        gameModel.setWinThreshold(x + 1);
    }

    public void decreaseWinThreshold() {

        if(GameOver){
            int x = gameModel.getWinThreshold();
            if(x == 3){
                return;
            }
            gameModel.setWinThreshold(x - 1);
        }
    }

    public void reset() {

        for(int row = 0; row < gameModel.cells.size(); row++){
            for(int col = 0; col < gameModel.cells.get(0).size(); col++){
                gameModel.setCellOwner(row, col, reset);
            }
        }
        gameModel.setWinner(reset);
        gameModel.setCurrentPlayerNumber(0);
        Stop_input = false;
        GameOver = false;
    }

    public boolean isGameWon() {

        // starting position...
        Board_position position;
        position = new Board_position(0, 0);

        for (int i = 0; i < gameModel.getNumberOfRows(); i++) {
            for (int j = 0; j < gameModel.getNumberOfColumns(); j++) {
                position.x = i;
                position.y = j;

                if(check_horizontal_win(position)){
                    return true;
                }
                else if(check_vertical_win(position)){
                    return true;
                }
                else if(check_Left_to_Right_diagonal_win(position) >= gameModel.getWinThreshold()){
                    return true;
                }
                else if(check_Right_to_Left_diagonal_win(position) >= gameModel.getWinThreshold()){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkIfNull(int i, int j){

        return gameModel.getCellOwner(i, j) == null;
    }


    private boolean check_horizontal_win(Board_position position) {
        if (checkIfNull(position.x, position.y)) {
            return false;
        }

        int count = 1;
        char mainLetter = gameModel.getCellOwner(position.x, position.y).getPlayingLetter();

        for (int j = position.y + 1; j < gameModel.getNumberOfColumns(); j++) {
            if (checkIfNull(position.x, j)) {
                break;
            }
            char compareLetter = gameModel.getCellOwner(position.x, j).getPlayingLetter();
            if (compareLetter == mainLetter) {
                count++;
                if (count >= gameModel.getWinThreshold()) {
                    return true;
                }
            } else {
                break;
            }
        }

        for (int j = position.y - 1; j >= 0; j--) {
            if (checkIfNull(position.x, j)) {
                break;
            }
            char compareLetter = gameModel.getCellOwner(position.x, j).getPlayingLetter();
            if (compareLetter == mainLetter) {
                count++;
                if (count >= gameModel.getWinThreshold()) {
                    return true;
                }
            } else {
                break;
            }
        }

        return false;
    }

    private boolean check_vertical_win(Board_position position) {
        if (checkIfNull(position.x, position.y)) {
            return false;
        }

        int count = 1;
        char mainLetter = gameModel.getCellOwner(position.x, position.y).getPlayingLetter();


        for (int i = position.x + 1; i < gameModel.getNumberOfRows(); i++) {
            if (checkIfNull(i, position.y)) {
                break;
            }
            char compareLetter = gameModel.getCellOwner(i, position.y).getPlayingLetter();
            if (compareLetter == mainLetter) {
                count++;
                if (count >= gameModel.getWinThreshold()) {
                    return true;
                }
            } else {
                break;
            }
        }

        for (int i = position.x - 1; i >= 0; i--) {
            if (checkIfNull(i, position.y)) {
                break;
            }
            char compareLetter = gameModel.getCellOwner(i, position.y).getPlayingLetter();
            if (compareLetter == mainLetter) {
                count++;
                if (count >= gameModel.getWinThreshold()) {
                    return true;
                }
            } else {
                break;
            }
        }

        return false;
    }


    private int check_Left_to_Right_diagonal_win(Board_position position) {

        int count = 1;

        if (checkIfNull(position.x, position.y)) {
            return count;
        }

        char main_letter;
        main_letter = gameModel.getCellOwner(position.x, position.y).getPlayingLetter();

        int startingPoint = position.x;

        for (int i = startingPoint; i < gameModel.getNumberOfRows(); i++) {

            Board_position diagonal_position;
            diagonal_position = new Board_position(gameModel.getNumberOfRows(), gameModel.getNumberOfColumns());
            diagonal_position.x = position.x + 1;
            diagonal_position.y = position.y + 1;
            if (Within_Board_and_not_null(diagonal_position)) {
                int x = position.x + 1;
                int y = position.y + 1;
                if (main_letter == gameModel.getCellOwner(x, y).getPlayingLetter()) {
                    position.x = position.x + 1;
                    position.y = position.y + 1;
                    count += 1;
                } else {
                    return count;
                }
            }
        }
        return count;
    }


    private int check_Right_to_Left_diagonal_win(Board_position position) {

        int count = 1;

        if (checkIfNull(position.x, position.y)) {
            return count;
        }

        char main_letter;
        main_letter = gameModel.getCellOwner(position.x, position.y).getPlayingLetter();


        int startingPoint = position.x;

        for (int i = startingPoint; i  >= 0; i--) {

            Board_position diagonal_position;
            diagonal_position = new Board_position(gameModel.getNumberOfRows(), gameModel.getNumberOfColumns());
            diagonal_position.x = position.x - 1;
            diagonal_position.y = position.y + 1;
            if (Within_Board_and_not_null(diagonal_position)) {
                int x = position.x - 1;
                int y = position.y + 1;
                if (main_letter == gameModel.getCellOwner(x, y).getPlayingLetter()) {
                    count += 1;
                    position.x = position.x - 1;
                    position.y = position.y + 1;
                } else {
                    return count;
                }
            }
        }
        return count;
    }

    private boolean Within_Board_and_not_null(Board_position position){

        if(position.x < 0 || position.y < 0 ){
            return false;
        }
        if(position.x >= gameModel.getNumberOfRows() || position.y >= gameModel.getNumberOfColumns()){
            return false;
        }
        return !checkIfNull(position.x, position.y);
    }

}
