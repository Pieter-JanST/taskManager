package project;

import com.taskmanager.model.Clock;
import com.taskmanager.model.Project;
import com.taskmanager.model.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static com.taskmanager.model.Clock.getOriginalSystemTime;
import static com.taskmanager.model.Clock.getSystemTime;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProjectTest {
    @Test
    public void createValidProject() {
        Timestamp t = Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES));
        Project p = new Project("project", "project desc", t,getSystemTime(),getOriginalSystemTime());
        Assertions.assertTrue(p.getId()>0);
        Assertions.assertEquals("project",p.getName());
        Assertions.assertEquals("project desc",p.getDescription());
        Assertions.assertEquals(t.getTime()+Clock.getVariationSystemTime().getTime(),p.getDueTime().getTime());
        Assertions.assertNotNull(p.getCreationTime());
        //Assertions.assertEquals(Status.UNAVAILABLE,p.getStatus());
    }
    @Test
    public void createProjectWithInvalidName() {
        Timestamp t = Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    Project p = new Project(" ", "project desc", t);
                });
        Assertions.assertEquals("name cant be empty",exception.getMessage());
    }
    @Test
    public void createProjectWithNullName() {
        Timestamp t = Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    Project p = new Project(null, "project desc", t);
                });
        Assertions.assertEquals("name cant be empty",exception.getMessage());
    }
    @Test
    public void createProjectWithInvalidDesc() {
        Timestamp t = Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    Project p = new Project("project", " ", t);
                });
        Assertions.assertEquals("description cant be empty",exception.getMessage());
    }
    @Test
    public void createProjectWithNullDesc() {
        Timestamp t = Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    Project p = new Project("project", null, t);
                });
        Assertions.assertEquals("description cant be empty",exception.getMessage());
    }
    @Test
    public void createProjectWithNullDueTime() {
        Timestamp t = Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    Project p = new Project("project", "desc", null);
                });
        Assertions.assertEquals("dueTime cant be empty",exception.getMessage());
    }
    @Test
    public void createProjectWithInvalidDueTime() {
        Timestamp t = Timestamp.from(Instant.now().minus(60, ChronoUnit.MINUTES));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    Project p = new Project("project", "desc", t);
                });
        Assertions.assertEquals("dueTime cant be in the past",exception.getMessage());
    }

    @Test
    public void addValidTaskToProject() {
        Timestamp t = Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES));
        ArrayList<String> roles = new ArrayList<>();
        roles.add("Developer");
        Project p = new Project("project", "desc", t,getSystemTime(),getOriginalSystemTime());
        Task task = new Task("task desc",60,10L,p,getSystemTime(),roles,new ArrayList<>());
        Assertions.assertEquals(1,p.getTasks().size());
        Assertions.assertTrue(p.getTasks().contains(task));
        Assertions.assertEquals(task.getDescription(),p.getTaskById(task.getId()).getDescription());
    }

}
