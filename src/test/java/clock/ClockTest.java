package clock;

import com.taskmanager.model.Clock;
import com.taskmanager.model.Task;
import com.taskmanager.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClockTest {
    //TODO: clock tests werken nie als andere tests ook runnen ?
    @Test
    public void getSystemTime() {
        Timestamp t = Timestamp.from(Instant.now());
        //klein verschil want kan niet excact op hetzelfde moment starten
        Assertions.assertTrue((t.getTime()-Clock.getSystemTime().getTime())<100);
    }

    @Test
    public void setValidSystemTime() {
        Timestamp t2 = Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES));
        Clock.setSystemTime(t2);
        Assertions.assertEquals(t2.getTime(),Clock.getSystemTime().getTime());
    }
    @Test
    public void setInvalidValidSystemTime() {
        Timestamp t1 = Clock.getSystemTime();
        Timestamp t2 = Timestamp.from(Instant.now().minus(60, ChronoUnit.MINUTES));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    Clock.setSystemTime(t2);
                });
        Assertions.assertEquals("systemTime cant be set in the past",exception.getMessage());

        //klein verschil want kan niet excact op hetzelfde moment starten
        Assertions.assertTrue((t1.getTime()-Clock.getSystemTime().getTime())<100);
    }
}
