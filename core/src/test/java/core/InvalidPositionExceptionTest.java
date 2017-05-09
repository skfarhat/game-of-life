package core;

import core.exceptions.InvalidPositionException;
import org.junit.Test;

import static org.junit.Assert.*;

public class InvalidPositionExceptionTest {


    @Test
    public void testDefaultException() {
        try {
            throw new InvalidPositionException();
        }
        catch (InvalidPositionException exc) {
            assertTrue(true);// pass
        }
    }

    @Test
    public void testExceptionWithMessage() {
        String message = Utils.randomString();
        try {
            throw new InvalidPositionException(message);
        }
        catch (InvalidPositionException exc) {
            assertEquals(message, exc.getMessage());
            return;
        }
    }
}