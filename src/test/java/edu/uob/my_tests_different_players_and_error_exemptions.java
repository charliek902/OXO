package edu.uob;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import edu.uob.OXOMoveException.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;

class my_tests_different_players_and_error_exemptions {
    private OXOModel model;
    private OXOController controller;

// this file tests whether adding more than 2 players still passes the tests. It also tests whether
    // different errors will get thrown

    @BeforeEach
    void setup() {
        model = new OXOModel(3, 3, 3);
        model.addPlayer(new OXOPlayer('A'));
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

    // Test out basic win detection
    @Test
    void Another_testBasicWin() throws OXOMoveException {
        // Find out which player is going to make the first move (they should be the eventual winner)
        OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
        // Make a bunch of moves for the two players
        sendCommandToController("a1"); // First player
        sendCommandToController("b1"); // Second player
        sendCommandToController("c1"); // Third player
        sendCommandToController("a2"); // First player
        sendCommandToController("b2"); // Second player
        sendCommandToController("c2"); // Third player
        sendCommandToController("a3"); // First player


        // a1, a2, a3 should be a win for the first player (since players alternate between moves)
        // Let's check to see whether the first moving player is indeed the winner
        String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't";
        assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
    }

    // Example of how to test for the throwing of exceptions
    @Test
    void testInvalidIdentifierException_more_than2() throws OXOMoveException {
        // Check that the controller throws a suitable exception when it gets an invalid command
        String failedTestComment = "Controller failed to throw an InvalidIdentifierLengthException for command `abc123`";
        // The next lines is a bit ugly, but it is the easiest way to test exceptions (soz)
        assertThrows(InvalidIdentifierLengthException.class, ()-> sendCommandToController("abc123"), failedTestComment);
    }

    @Test
    void testInvalidIdentifierException_more_than2_1() throws OXOMoveException {
        // Check that the controller throws a suitable exception when it gets an invalid command
        String failedTestComment = "Controller failed to throw an InvalidIdentifierLengthException for command `a23`";
        // The next lines is a bit ugly, but it is the easiest way to test exceptions (soz)
        assertThrows(InvalidIdentifierLengthException.class, ()-> sendCommandToController("a23"), failedTestComment);
    }

    @Test
    void testInvalidIdentifierException_length_of1() throws OXOMoveException {
        // Check that the controller throws a suitable exception when it gets an invalid command
        String failedTestComment = "Controller failed to throw an InvalidIdentifierLengthException for command `1`";
        // The next lines is a bit ugly, but it is the easiest way to test exceptions (soz)
        assertThrows(InvalidIdentifierLengthException.class, ()-> sendCommandToController("1"), failedTestComment);
    }

    @Test
    void testInvalidOutsideCellRangeException() throws OXOMoveException {
        String failedTestComment = "Controller failed to throw an InvalidOutsideCellRangeException for command `i3`";
        assertThrows(OutsideCellRangeException.class, ()-> sendCommandToController("i3"), failedTestComment);
    }

    @Test
    void testInvalidOutsideCellRangeException_1() throws OXOMoveException {
        String failedTestComment = "Controller failed to throw an InvalidOutsideCellRangeException for command `d3`";
        assertThrows(OutsideCellRangeException.class, ()-> sendCommandToController("d3"), failedTestComment);
    }

    @Test
    void testInvalidOutsideCellRangeException_2() throws OXOMoveException {
        String failedTestComment = "Controller failed to throw an InvalidOutsideCellRangeException for command `D3`";
        assertThrows(OutsideCellRangeException.class, ()-> sendCommandToController("D3"), failedTestComment);
    }

    @Test
    void testInvalidIdentifierCharacterException_3() throws OXOMoveException {
        String failedTestComment = "Controller failed to throw an InvalidIdentifierCharacterException for command `a0`";
        assertThrows(OutsideCellRangeException.class, ()-> sendCommandToController("a0"), failedTestComment);
    }

    @Test
    void testInvalidIdentifierCharacterException_4() throws OXOMoveException {
        String failedTestComment = "Controller failed to throw an InvalidIdentifierCharacterException for command `c0`";
        assertThrows(OutsideCellRangeException.class, ()-> sendCommandToController("c0"), failedTestComment);
    }


    @Test
    void testInvalidIdentifierCharacterException() throws OXOMoveException {
        String failedTestComment = "Controller failed to throw an InvalidIdentifierCharacterException for command `?1`";
        assertThrows(InvalidIdentifierCharacterException.class, ()-> sendCommandToController("?1"), failedTestComment);
    }

    @Test
    void testInvalidIdentifierCharacterException_1() throws OXOMoveException {
        String failedTestComment = "Controller failed to throw an InvalidIdentifierCharacterException for command `.>`";
        assertThrows(InvalidIdentifierCharacterException.class, ()-> sendCommandToController(".>"), failedTestComment);
    }

    @Test
    void testInvalidIdentifierCharacterException_2() throws OXOMoveException {
        String failedTestComment = "Controller failed to throw an InvalidIdentifierCharacterException for command `12`";
        assertThrows(InvalidIdentifierCharacterException.class, ()-> sendCommandToController("12"), failedTestComment);
    }




























}
