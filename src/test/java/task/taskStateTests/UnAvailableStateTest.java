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
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UnAvailableStateTest {
    private final Project p = new Project("p","p", Timestamp.from(Instant.now().plus(60*9, ChronoUnit.MINUTES)),getSystemTime(),getOriginalSystemTime());
    private final List<String> roles = new ArrayList<>(List.of("JavaProgrammer","PythonProgrammer"));
    private final User u = new User("ernie","e",roles);
    @Test
    public void endTaskTest(){
        Task t1 = new Task("task desc",20, 5.2F,p,roles);
        Task t2 = new Task("task desc",20, 5.2F,p,getSystemTime(),roles,new ArrayList<>(List.of(t1)));

        Assertions.assertEquals("UNAVAILABLE", t2.getStatus());
        Exception exception = assertThrows(IllegalStateException.class,
                () -> t2.endTask(false));
        Assertions.assertSame("Can't end a task when it is Unavailable", exception.getMessage());
    }
    @Test
    public void canSwitchToAvailableTest(){
        Task t1 = new Task("task desc",20, 5.2F,p,getSystemTime(),List.of("JavaProgrammer"),new ArrayList<>());
        Task t2 = new Task("task desc",20, 5.2F,p,getSystemTime(),roles,new ArrayList<>(List.of(t1)));

        Assertions.assertEquals("UNAVAILABLE", t2.getStatus());
        t1.assignUserToTask(u,"JavaProgrammer0");
        t1.endTask(false);
        Assertions.assertEquals("AVAILABLE", t2.getStatus());
    }

    @Test
    public void removeUserFromTaskTest(){
        Task t1 = new Task("task desc",20, 5.2F,p,roles);
        Task t2 = new Task("task desc",20, 5.2F,p,roles,new ArrayList<>(List.of(t1)));

        Assertions.assertEquals("UNAVAILABLE", t2.getStatus());
        Exception exception = assertThrows(IllegalStateException.class,
                () -> t2.assignUserToTask(u,"JavaProgrammer1"));
        Assertions.assertSame("cant assign user to task when Unavailable", exception.getMessage());

    }

    @Test
    public void assignUserToTaskTest(){
        Task t1 = new Task("task desc",20, 5.2F,p,roles);
        Task t2 = new Task("task desc",20, 5.2F,p,roles,new ArrayList<>(List.of(t1)));
        Assertions.assertEquals("UNAVAILABLE", t2.getStatus());
        Exception exception = assertThrows(IllegalStateException.class,
                () -> t2.assignUserToTask(u,"JavaProgrammer1"));
        Assertions.assertSame("cant assign user to task when Unavailable", exception.getMessage());

    }

    @Test
    public void replaceDependenciesTest(){
        Task t1 = new Task("task desc",20, 5.2F,p,roles);
        Task t2 = new Task("task desc",20, 5.2F,p,roles,new ArrayList<>(List.of(t1)));
        Task t3 = new Task("task desc",20, 5.2F,p,roles);
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(t3);
        Assertions.assertEquals("UNAVAILABLE", t2.getStatus());
        Assertions.assertTrue( t2.getDependsOn().contains(t1));
        t2.replaceDependencies(tasks);
        Assertions.assertTrue(t2.getDependsOn().contains(t3));
    }
    @Test
    public void replaceTaskTest() {
        Task t1 = new Task("task desc",20, 5.2F,p,roles);
        Timestamp time = getSystemTime();
        t1.replaceTask("newdesc",5,1,List.of("JavaProgrammer"), time, new ArrayList<>());

        Assertions.assertEquals("newdesc", t1.getDescription());
        Assertions.assertEquals(5, t1.getEstimatedDuration());
        Assertions.assertEquals(1, t1.getAcceptableDeviation());
        Assertions.assertTrue(t1.getNecessaryRoles().containsKey("JavaProgrammer0"));
        assertEquals(0, t1.getDependsOn().size());
    }
    @Test
    public void deleteTaskTest() {
        Task t1 = new Task("task desc",20, 5.2F,p,roles);

        assertTrue(p.getTasks().contains(t1));
        t1.prepareDelete();
        assertFalse(p.getTasks().contains(t1));
    }
}
