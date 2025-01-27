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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PendingStateTest {
    private final Project p = new Project("p","p", Timestamp.from(Instant.now().plus(60*9, ChronoUnit.MINUTES)),getSystemTime(),getOriginalSystemTime());
    private final List<String> roles = new ArrayList<>(List.of("JavaProgrammer","PythonProgrammer"));
    private final User u = new User("ernie","e",roles);
    @Test
    public void endTaskTest(){
        Task t1 = new Task("task desc",20, 5.2F,p,getSystemTime(),roles, new ArrayList<>());
        t1.assignUserToTask(u,"JavaProgrammer0");
        Assertions.assertEquals("PENDING", t1.getStatus());
        Exception exception = assertThrows(IllegalStateException.class,
                () -> t1.endTask(false));
        Assertions.assertSame("cant end a task when Pending", exception.getMessage());
    }

    @Test
    public void removeUserFromTaskTest(){
        Task t1 = new Task("task desc",20, 5.2F,p,getSystemTime(),roles, new ArrayList<>());
        Task t2 = new Task("task desc2",20, 5.2F,p,getSystemTime(),roles,new ArrayList<>());

        t1.assignUserToTask(u,"JavaProgrammer0");
        Assertions.assertEquals("PENDING", t1.getStatus());

        t2.assignUserToTask(u,"JavaProgrammer0");
        Assertions.assertEquals(t2.getDescription(),u.getWorkingTask().getDescription());
        Assertions.assertEquals("AVAILABLE", t1.getStatus());
    }

    @Test
    public void assignUserToTaskTest(){
        Task t1 = new Task("task desc",20, 5.2F,p,getSystemTime(),roles, new ArrayList<>());
        Task t2 = new Task("task desc",20, 5.2F,p,getSystemTime(),roles, new ArrayList<>());

        t1.assignUserToTask(u,"JavaProgrammer0");
        Assertions.assertEquals("PENDING", t1.getStatus());
        t1.assignUserToTask(new User("bert","b",List.of("PythonProgrammer")),"PythonProgrammer1");

        Assertions.assertEquals("EXECUTING", t1.getStatus());
    }

    @Test
    public void replaceDependenciesTest(){
        Task t1 = new Task("task desc",20, 5.2F,p,getSystemTime(),roles, new ArrayList<>());
        Task t2 = new Task("task desc",20, 5.2F,p,getSystemTime(),roles, new ArrayList<>());

        t1.assignUserToTask(u,"JavaProgrammer0");
        Assertions.assertEquals("PENDING", t1.getStatus());

        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(t2);

        Exception exception = assertThrows(IllegalStateException.class,
                () -> t1.replaceDependencies(tasks));
        Assertions.assertSame("cant update dependencies when Pending", exception.getMessage());
    }
    @Test
    public void replaceTaskTest() {
        Task t1 = new Task("task desc",20, 5.2F,p,getSystemTime(),roles, new ArrayList<>());
        t1.assignUserToTask(u,"JavaProgrammer0");
        Timestamp time = getSystemTime();
        Exception exception = assertThrows(IllegalStateException.class,
                () -> t1.replaceTask("newdesc",5,1,List.of("JavaProgrammer"),time,new ArrayList<>())
        );
        Assertions.assertSame("cant replace task when Pending", exception.getMessage());
    }

}
