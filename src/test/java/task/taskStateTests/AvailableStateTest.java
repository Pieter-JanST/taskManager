package task.taskStateTests;

import com.taskmanager.model.Project;
import com.taskmanager.model.Task;
import com.taskmanager.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.taskmanager.model.Clock.getOriginalSystemTime;
import static com.taskmanager.model.Clock.getSystemTime;
import static org.junit.jupiter.api.Assertions.*;

public class AvailableStateTest {
    private final List<String> roles = new ArrayList<>(List.of("JavaProgrammer","PythonProgrammer"));
    Project p = new Project("p","p", Timestamp.from(Instant.now().plus(60*9, ChronoUnit.MINUTES)),getSystemTime(),getOriginalSystemTime());

    private Task createAvailAbleTask(){
        User u = new User("ernie","e",roles);
        Timestamp time =getSystemTime();
        Task t = new Task("task desc",20, 5.2F,p,time,roles, new ArrayList<>());
        Assertions.assertEquals("AVAILABLE", t.getStatus());
        return t;
    }
    @Test
    public void endTaskTest(){
        Exception exception = assertThrows(IllegalStateException.class,
                () -> createAvailAbleTask().endTask(false));
        Assertions.assertSame("cant end a task when Available", exception.getMessage());
    }
    @Test
    public void assignUserToTaskTest(){
        Task t = createAvailAbleTask();
        t.assignUserToTask(new User("ernie","e",roles),"JavaProgrammer0");
        Assertions.assertEquals("PENDING", t.getStatus());
    }

    @Test
    public void replaceDependenciesTest() {
        Task t1 = createAvailAbleTask();
        Task t2 = createAvailAbleTask();
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(t2);
        t1.replaceDependencies(tasks);
        Assertions.assertEquals("UNAVAILABLE", t1.getStatus());
        Assertions.assertEquals("AVAILABLE", t2.getStatus());
        Assertions.assertTrue(t1.getDependsOn().contains(t2));
    }
    @Test
    public void replaceTaskTest() {
        Task t1 = createAvailAbleTask();
        Timestamp time = getSystemTime();
        t1.replaceTask("newdesc",5,1,List.of("JavaProgrammer"), time, new ArrayList<>());

        Assertions.assertEquals("newdesc", t1.getDescription());
        Assertions.assertEquals(5, t1.getEstimatedDuration());
        Assertions.assertEquals(1, t1.getAcceptableDeviation());
        Assertions.assertTrue(t1.getNecessaryRoles().containsKey("JavaProgrammer0"));
        Assertions.assertEquals(time, t1.getTimeSpan().getTsSystemTime());
        assertEquals(0, t1.getDependsOn().size());
    }

    @Test
    public void deleteTaskTest() {
        Task t1 = createAvailAbleTask();
        Task t2 = createAvailAbleTask();
        ArrayList<Task> a = new ArrayList<>();
        a.add(t1);
        t2.setDependsOn(a);

        assertTrue(p.getTasks().contains(t1));
        assertTrue(t2.getDependsOn().contains(t1));
        t1.prepareDelete();
        assertFalse(p.getTasks().contains(t1));
        assertFalse(t2.getDependsOn().contains(t1));

        assertEquals(0, t1.getDependsOn().size());
    }
}
