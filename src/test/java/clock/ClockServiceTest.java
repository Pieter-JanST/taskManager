package clock;

import com.taskmanager.controller.PMController;
import com.taskmanager.controller.RoleController;
import com.taskmanager.controller.UserService;
import com.taskmanager.model.ActionRepository;
import com.taskmanager.model.Clock;
import com.taskmanager.model.ProjectRepository;
import com.taskmanager.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClockServiceTest {
    RoleController roleController = new RoleController(new UserService(),new ActionRepository(),new ProjectRepository(),new TaskRepository());

    @Test
    public void setValidSystemTime() {
        Timestamp t1 = Timestamp.from(Instant.now());
        Timestamp t2 = Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES));

        //PMController pmController = new PMController();
        roleController.advanceTime(t2);

        Assertions.assertEquals(t2.getTime(),Clock.getSystemTime().getTime());
        //Assertions.assertTrue((t1.getTime()-Clock.getOriginalSystemTime().getTime())<100);
    }
    @Test
    public void setInvalidValidSystemTime() {
        Timestamp t1 = Timestamp.from(Instant.now());
        Timestamp t2 = Timestamp.from(Instant.now().minus(60, ChronoUnit.MINUTES));
        //PMController pmController = new PMController();

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    roleController.advanceTime(t2);
                });
        Assertions.assertEquals("systemTime cant be set in the past",exception.getMessage());

        //klein verschil want kan niet excact op hetzelfde moment starten
        Assertions.assertTrue((t1.getTime()-Clock.getSystemTime().getTime())<100);
        //Assertions.assertTrue((t1.getTime()-Clock.getOriginalSystemTime().getTime())<100);
    }

}
