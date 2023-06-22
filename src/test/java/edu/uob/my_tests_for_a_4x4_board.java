package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class my_tests_for_a_4x4_board {
    private OXOModel model;
    private OXOController controller;

    @BeforeEach
    void setup() {
        model = new OXOModel(4, 4, 3);
        model.addPlayer(new OXOPlayer('X'));
        model.addPlayer(new OXOPlayer('O'));
        model.addPlayer(new OXOPlayer('A'));
        model.addPlayer(new OXOPlayer('B'));
        controller = new OXOController(model);
    }

    // This next method is a utility function that can be used by any of the test methods to _safely_ send a command to the controller
    void sendCommandToController(String command) {
        // Try to send a command to the server - call will time out if it takes too long (in case the server enters an infinite loop)
        // Note: this is ugly code and includes syntax that you haven't encountered yet
        String timeoutComment = "Controller took too long to respond (probably stuck in an infinite loop)";
        assertTimeoutPreemptively(Duration.ofMillis(1000), ()-> controller.handleIncomingCommand(command), timeoutComment);
    }

    // so with this board I decided to do one big test where I would add multiple players
    // and test for whether the right diagonal was working...
    // importantly with this test, I decided to put the number of the win threshold of a certain letter
    // in a row/column/ left_right diagonal. For example, the letter X would appear 3 times in the
    // 1 column, however, those X's would not be all next to each other and therefore would not be the
    // winner. This stops code which simply counts the number of a certain letter in a row or column from
    // passing and only allows code which allows letters which are all consecutively next to each other...
    // (very mean!)

    @Test
    void testBasicDraw_then_win() throws OXOMoveException {
        // Find out which player is going to make the first move (they should be the eventual winner)
        OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
        OXOPlayer secondMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber() + 1);
        OXOPlayer thirdMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber() + 2);
        OXOPlayer fourthMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber() + 3);


        // Make a bunch of moves for the two players
        sendCommandToController("a1"); // First player
        sendCommandToController("a3"); // Second player
        sendCommandToController("a4"); // Third player
        sendCommandToController("a2"); // Fourth player

        sendCommandToController("c1"); // First player
        sendCommandToController("b4"); // Second player
        sendCommandToController("b3"); // Third player
        sendCommandToController("d2"); // Fourth player

        sendCommandToController("d1"); // First player
        sendCommandToController("d3"); // Second player
        sendCommandToController("d4"); // Third player
        sendCommandToController("b1"); // Fourth player

        sendCommandToController("c3"); // First player
        // if the coder simply just counts the number of X's in a column rather than
        // code whether they are consecutive, the below code will fail
        assertNotEquals(firstMovingPlayer, model.getWinner());
        sendCommandToController("c4"); // Second player
        sendCommandToController("c2"); // Third player
        assertEquals(thirdMovingPlayer, model.getWinner());
    }
}
