package edu.uob;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import edu.uob.OXOMoveException.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;

class Min_and_Max_tests {
    private OXOModel model;
    private OXOController controller;
    @BeforeEach
    void setup() {
        model = new OXOModel(3, 3, 3);
        model.addPlayer(new OXOPlayer('X'));
        model.addPlayer(new OXOPlayer('O'));
        controller = new OXOController(model);
    }

    // This next method is a utility function that can be used by any of the test methods to _safely_ send a command to the controller
    void sendCommandToController(String command) {
        // Try to send a command to the server - call will timeout if it takes too long (in case the server enters an infinite loop)
        // Note: this is ugly code and includes syntax that you haven't encountered yet
        String timeoutComment = "Controller took too long to respond (probably stuck in an infinite loop)";
        assertTimeoutPreemptively(Duration.ofMillis(1000), ()-> controller.handleIncomingCommand(command), timeoutComment);
    }


    // Test out the minimum and the maximum board sizes...
    // starts at 3x3, even though rows and columns should be reduced to below 1 x 1, after the first player
    // puts in a letter to the board, there should be a draw as soon as a letter is placed down
    // equally as the max of the board is 9 x 9, after columns and rows are added over 9 times,
    // the number of columns and rows should still be 9
    @Test
    void testBasic_Min_Max() throws OXOMoveException {
        // Find out which player is going to make the first move (they should be the eventual winner)
        OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
        // Make a bunch of moves for the two players
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        controller.addRow();
        controller.addColumn();
        controller.addRow();
        controller.addColumn();
        controller.removeColumn();
        controller.removeRow();
        controller.removeColumn();
        controller.removeRow();
        assertEquals(model.getNumberOfColumns(), 1);
        assertEquals(model.getNumberOfRows(), 1);
        sendCommandToController("a1"); // First player
        assert(model.isGameDrawn());
        controller.addRow();
        controller.addColumn();
        controller.addRow();
        controller.addColumn();
        controller.addRow();
        controller.addColumn();
        controller.addRow();
        controller.addColumn();
        controller.addRow();
        controller.addColumn();
        controller.addRow();
        controller.addColumn();
        controller.addRow();
        controller.addColumn();
        controller.addRow();
        controller.addColumn();
        controller.addRow();
        controller.addColumn();
        controller.addRow();
        controller.addColumn();
        controller.addRow();
        controller.addColumn();
        controller.addRow();
        controller.addColumn();
        controller.addRow();
        controller.addColumn();
        controller.addRow();
        controller.addColumn();
        controller.addRow();
        controller.addColumn();
        controller.addRow();
        controller.addColumn();
        assertEquals(model.getNumberOfColumns(), 9);
        assertEquals(model.getNumberOfRows(), 9);

    }
}
