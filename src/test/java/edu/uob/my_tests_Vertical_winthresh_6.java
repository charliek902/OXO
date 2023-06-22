package edu.uob;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import edu.uob.OXOMoveException.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;
public class my_tests_Vertical_winthresh_6 {


    private OXOModel model;
    private OXOController controller;
    @BeforeEach
    void setup() {
        model = new OXOModel(6, 6, 6);
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
    void testBasicVerticalWin() throws OXOMoveException {
        // Find out which player is going to make the first move (they should be the eventual winner)
        OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
        // Make a bunch of moves for the two players
        sendCommandToController("a1"); // First player
        sendCommandToController("a2"); // Second player
        sendCommandToController("b1"); // First player
        sendCommandToController("b2"); // Second player
        sendCommandToController("c1"); // First player
        sendCommandToController("c2"); // First player
        sendCommandToController("d1"); // Second player
        sendCommandToController("d2"); // First player
        sendCommandToController("e1"); // Second player
        sendCommandToController("e2"); // First player
        sendCommandToController("f1"); // First player


        // a1, a2, a3 should be a win for the first player (since players alternate between moves)
        // Let's check to see whether the first moving player is indeed the winner
        String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't";
        assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
    }
}


