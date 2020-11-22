package hu.zfall.cleancode.munkaido.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TimeServiceUnitTest {

    private final TimeService target = new TimeService();

    @Test
    public void testPrettyPrintTime() {
        final String prettyPrintedTime = target.prettyPrintTime(7200);
        Assertions.assertEquals("2:00:00", prettyPrintedTime);
    }

}
