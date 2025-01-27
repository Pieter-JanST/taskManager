package task.taskStateTests;
import com.taskmanager.model.Project;
import com.taskmanager.model.Task;
import com.taskmanager.model.User;

import static com.taskmanager.model.Clock.getOriginalSystemTime;
import static com.taskmanager.model.Clock.getSystemTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ExecutingStateTest {

    private Task createExecutingTask(){
        Project p = new Project("p","p", Timestamp.from(Instant.now().plus(60*9, ChronoUnit.MINUTES)),getSystemTime(),getOriginalSystemTime());
        List<String> roles = new ArrayList<>(List.of("JavaProgrammer","PythonProgrammer"));
        User u = new User("ernie","e",roles);
        Task t = new Task("task desc",20, 5.2F,p,getSystemTime(),List.of("JavaProgrammer"),new ArrayList<>());
        t.assignUserToTask(u,"JavaProgrammer0");
        Assertions.assertEquals("EXECUTING", t.getStatus());
        return t;
    }
    @Test
    public void endTaskTest(){
        Task t1 = createExecutingTask();
        t1.endTask(false);
        Assertions.assertEquals("FINISHED", t1.getStatus());
        Task t2 = createExecutingTask();
        t2.endTask(true);
        Assertions.assertEquals("FAILED", t2.getStatus());

    }

    @Test
    public void assignUserToTaskTest(){
        Exception exception = assertThrows(IllegalStateException.class,
                () -> createExecutingTask().assignUserToTask(new User("bert","r",List.of("JavaProgrammer")),"JavaProgrammer0"));
        Assertions.assertSame("cant assign user to task when Executing", exception.getMessage());

    }

    @Test
    public void replaceDependenciesTest(){
        Project p = new Project("p","p", Timestamp.from(Instant.now().plus(60*9, ChronoUnit.MINUTES)),getSystemTime(),getOriginalSystemTime());

        Task t = new Task("task desc",20, 5.2F,p,List.of("JavaProgrammer"));
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(t);
        Exception exception = assertThrows(IllegalStateException.class,
                () -> createExecutingTask().replaceDependencies(tasks));
        Assertions.assertSame("cant update dependencies when Executing", exception.getMessage());
    }
    @Test
    public void replaceTaskTest() {
        Task t1 = createExecutingTask();
        Timestamp time = getSystemTime();
        Exception exception = assertThrows(IllegalStateException.class,
                () -> t1.replaceTask("newdesc",5,1,List.of("JavaProgrammer"),time,new ArrayList<>())
        );
        Assertions.assertSame("cant replace task when Executing", exception.getMessage());

    }
}
