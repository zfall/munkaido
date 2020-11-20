package hu.zfall.cleancode.munkaido.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TimeServiceTest {

    private final TimeService target = new TimeService();

    @Test
    public void testGetCurrentDay() {
        final String currentDay = target.getCurrentDay();
        Assertions.assertEquals(new SimpleDateFormat("yyyyMMdd").format(new Date()), currentDay);
    }

}
