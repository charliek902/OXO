package edu.uob;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import edu.uob.OXOMoveException.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;

class my_tests_Win_and_keep_adding {
    private OXOModel model;
    private OXOController controller;

    // Make a new "standard" (3x3) board before running each test case (i.e. this method runs before every `@Test` method)
    // In order to test boards of different sizes, winning thresholds or number of players, create a separate test file (without this method in it !)
    @BeforeEach
    void setup() {
        model = new OXOModel(3, 3, 3);
        model.addPlayer(new OXOPlayer('X'));
        model.addPlayer(new OXOPlayer('O'));
        controller = new OXOController(model);
    }

    // This next method is a utility function that can be used by any of the test methods to _safely_ send a command to the controller
    void sendCommandToController(String command) {
        // Try to send a command to the server - call will time out if it takes too long (in case the server enters an infinite loop)
        // Note: this is ugly code and includes syntax that you haven't encountered yet
        String timeoutComment = "Controller took too long to respond (probably stuck in an infinite loop)";
        assertTimeoutPreemptively(Duration.ofMillis(1000), ()-> controller.handleIncomingCommand(command), timeoutComment);
    }

  // this is quite a difficult one to test
    // this test will test whether after a win a user can add columns and rows but not remove them...
    // I will also test whether a user can add any input after a game has been won (they should not be able to!)
    // I will also test whether the reset button is working...
    @Test
    void testWin_then_add_columns_and_rows_and_test_cant_remove__andor_columns_rows() throws OXOMoveException {
        // Find out which player is going to make the first move (they should be the eventual winner)
        OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
        // Make a bunch of moves for the two players
        sendCommandToController("a1"); // First player
        sendCommandToController("b1"); // Second player
        sendCommandToController("b2"); // First player
        sendCommandToController("a2"); // Second player
        sendCommandToController("c1"); // First player
        sendCommandToController("c2"); // Second player
        sendCommandToController("b3"); // First player
        sendCommandToController("a3"); // Second player
        sendCommandToController("c3"); // First player
        assertEquals(firstMovingPlayer, model.getWinner());
        controller.addColumn();
        controller.addRow();
        controller.addColumn();
        controller.addRow();
        assertEquals(model.getNumberOfRows(), 5);
        assertEquals(model.getNumberOfColumns(), 5);
        controller.removeColumn();
        controller.removeRow();
        assertEquals(model.getNumberOfRows(), 5);
        assertEquals(model.getNumberOfColumns(), 5);
        controller.addColumn();
        controller.addColumn();
        assertEquals(model.getNumberOfRows(), 5);
        assertEquals(model.getNumberOfColumns(), 7);
        controller.removeColumn();
        controller.removeRow();
        assertEquals(model.getNumberOfRows(), 5);
        assertEquals(model.getNumberOfColumns(), 7);
        // below test shows that there has been no change to rowNumber 4 and column number 4
        sendCommandToController("d4"); // second player
        assertEquals(model.getCellOwner(4 , 4 ), null);
        // now I will reset the board. The past inputted data should not null be on the board...
        controller.reset();
        assertEquals(model.getCellOwner(1 , 1 ), null);
    }
}
