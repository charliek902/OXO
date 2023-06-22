package edu.uob;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import edu.uob.OXOMoveException.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;

// this class tests whether the model responds to a win threshold going up to 4.
// It also tests diagonally as opposed to the controller tests which tested vertically...

public class my_tests_horizontal_winthresh_5 {

    private OXOModel model;
    private OXOController controller;
    @BeforeEach
    void setup() {
        model = new OXOModel(5, 5, 5);
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


    // Test out basic win detection
    @Test
    void testBasic_Horizontal_Win() throws OXOMoveException {
        // Find out which player is going to make the first move (they should be the eventual winner)
        OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
        // Make a bunch of moves for the two players
        sendCommandToController("a1"); // First player
        sendCommandToController("b1"); // Second player
        sendCommandToController("a2"); // First player
        sendCommandToController("b2"); // Second player
        sendCommandToController("a3"); // First player
        sendCommandToController("b3"); // Second player
        sendCommandToController("a4"); // First player
        sendCommandToController("b4"); // Second player
        sendCommandToController("a5"); // First player
        assertEquals(firstMovingPlayer, model.getWinner());
    }


}
