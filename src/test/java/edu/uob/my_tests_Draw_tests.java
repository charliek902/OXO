package edu.uob;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import edu.uob.OXOController;
import edu.uob.OXOModel;
import edu.uob.OXOMoveException;
import edu.uob.OXOMoveException.*;
import edu.uob.OXOPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;

class my_tests_Draw_tests {
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


    // Test out basic draw detection, whether a row and/or column can be removed once data
    // is inside, and testing whether after a stalemate, someone can enlarge the columns and rows and
    // keep playing until a player has won
    @Test
    void testBasicDraw_then_win() throws OXOMoveException {
        // Find out which player is going to make the first move (they should be the eventual winner)
        OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
        OXOPlayer secondMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber() + 1);

        // Make a bunch of moves for the two players
        sendCommandToController("a1"); // First player
        sendCommandToController("a2"); // Second player
        sendCommandToController("b1"); // First player
        sendCommandToController("b2"); // Second player
        sendCommandToController("c2"); // First player
        sendCommandToController("c1"); // Second player
        sendCommandToController("a3"); // First player
        sendCommandToController("b3"); // Second player
        sendCommandToController("c3"); // First player
        assert(model.isGameDrawn());
        controller.addRow();
        controller.addColumn();
        sendCommandToController("d1"); // Second player
        sendCommandToController("d4"); // First player
        sendCommandToController("b4"); // Second player
        assertEquals(secondMovingPlayer, model.getWinner());

    }
}
