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

public class FailedStateTest {
    Project p = new Project("p","p", Timestamp.from(Instant.now().plus(60*9, ChronoUnit.MINUTES)),getSystemTime(),getOriginalSystemTime());

    private Task createFailedTask(){
        List<String> roles = new ArrayList<>(List.of("JavaProgrammer","PythonProgrammer"));
        User u = new User("ernie","e",roles);
        Task t = new Task("task desc",20, 5.2F,p,getSystemTime(),List.of("JavaProgrammer"),new ArrayList<>());
        t.assignUserToTask(u,"JavaProgrammer0");
        t.endTask(true);
        Assertions.assertEquals("FAILED", t.getStatus());
        return t;
    }
    @Test
    public void endTaskTest(){
        Exception exception = assertThrows(IllegalStateException.class,
                () -> createFailedTask().endTask(false));
        Assertions.assertSame("cant end a task when Failed", exception.getMessage());
    }

    @Test
    public void assignUserToTaskTest(){
        Task t = createFailedTask();
        Exception exception = assertThrows(IllegalStateException.class,
                () -> t.assignUserToTask(new User("bert","r",List.of("JavaProgrammer")),"JavaProgrammer0"));
        Assertions.assertSame("cant assign user to task when Failed", exception.getMessage());

    }

    @Test
    public void replaceDependenciesTest(){
        Task t = new Task("task desc",20, 5.2F,p,List.of("JavaProgrammer"));
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(t);
        Exception exception = assertThrows(IllegalStateException.class,
                () -> createFailedTask().replaceDependencies(tasks));
        Assertions.assertSame("cant update dependencies when Failed", exception.getMessage());
    }
    @Test
    public void replaceTaskTest() {
        Task t1 = createFailedTask();
        Timestamp time = getSystemTime();
        t1.replaceTask("newdesc",5,1,List.of("JavaProgrammer"),time,new ArrayList<>());

        Assertions.assertEquals("newdesc", t1.getDescription());
        Assertions.assertEquals(5, t1.getEstimatedDuration());
        Assertions.assertEquals(1, t1.getAcceptableDeviation());
        Assertions.assertTrue(t1.getNecessaryRoles().containsKey("JavaProgrammer0"));
        assertEquals(0, t1.getDependsOn().size());
    }
    @Test
    public void deleteTaskTest() {
        Task t1 = createFailedTask();

        assertTrue(p.getTasks().contains(t1));
        t1.prepareDelete();
        assertFalse(p.getTasks().contains(t1));
    }
}
