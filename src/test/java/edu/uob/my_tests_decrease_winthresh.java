package edu.uob;

import edu.uob.OXOMoveException.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class my_tests_decrease_winthresh {
    private OXOModel model;
    private OXOController controller;

    @BeforeEach
    void setup() {
        model = new OXOModel(4, 4, 3);
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


  // this tests whether the win_threshold will not be able to go below three. it will start off at three
    // and go to four as the first two times when the controller's win threshold is decreased, nothing will happen
    @Test
    void testBasicWin_while_decreasedThreshold() throws OXOMoveException {
        // Find out which player is going to make the first move (they should be the eventual winner)
        OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
        controller.decreaseWinThreshold();
        controller.decreaseWinThreshold();
        controller.decreaseWinThreshold();
        controller.increaseWinThreshold();
        // Make a bunch of moves for the two players
        // The current Win threshold should be 4 before starting the game...
        // If I decrease the win threshold to 3 after the first input, then the decrease
        // will have been implemented during the game and it therefore would be fualty
        // If it is reduced to 3, then the first assertion would pass and the second assertion
        // would fail. However, this does not happen!

        sendCommandToController("a1"); // First player
        controller.decreaseWinThreshold();
        sendCommandToController("b1"); // Second player
        sendCommandToController("a2"); // First player
        sendCommandToController("b2"); // Second player
        sendCommandToController("a3"); // First player
        assertNotEquals(firstMovingPlayer, model.getWinner());
        sendCommandToController("b3"); // Second player
        sendCommandToController("a4"); // First player
        assertEquals(firstMovingPlayer, model.getWinner());
    }
}
