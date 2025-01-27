package timestamp;

import com.taskmanager.model.TimeSpan;
import com.taskmanager.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class TimeStampTest {
    @Test
    public void validTimeSpanTest() {
        Date date = new Date();
        TimeSpan t = new TimeSpan(new Timestamp(date.getTime()));
        Assertions.assertNull(t.getStartTime());
        Assertions.assertNull(t.getEndTime());

        t.startTime();
        Assertions.assertNotNull(t.getStartTime());
        t.endTime();
        Assertions.assertNotNull(t.getEndTime());

    }
}
