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

public class FinishedStateTest {
    Project p = new Project("p","p", Timestamp.from(Instant.now().plus(60*9, ChronoUnit.MINUTES)),getSystemTime(),getOriginalSystemTime());

    private Task createFinishedTask(){
        List<String> roles = new ArrayList<>(List.of("JavaProgrammer","PythonProgrammer"));
        User u = new User("ernie","e",roles);
        Task t = new Task("task desc",20, 5.2F,p,getSystemTime(),List.of("JavaProgrammer"),new ArrayList<>());
        t.assignUserToTask(u,"JavaProgrammer0");
        t.endTask(false);
        Assertions.assertEquals("FINISHED", t.getStatus());
        return t;
    }
    @Test
    public void endTaskTest(){
        Exception exception = assertThrows(IllegalStateException.class,
                () -> createFinishedTask().endTask(false));
        Assertions.assertSame("cant end a task when Finished", exception.getMessage());
    }

    @Test
    public void assignUserToTaskTest(){
        Task t = createFinishedTask();
        Exception exception = assertThrows(IllegalStateException.class,
                () -> t.assignUserToTask(new User("bert","r",List.of("JavaProgrammer")),"JavaProgrammer0"));
        Assertions.assertSame("cant assign user to task when Finished", exception.getMessage());

    }

    @Test
    public void replaceDependenciesTest(){
        Project p = new Project("p","p", Timestamp.from(Instant.now().plus(60*9, ChronoUnit.MINUTES)),getSystemTime(),getOriginalSystemTime());

        Task t = new Task("task desc",20, 5.2F,p,getSystemTime(),List.of("JavaProgrammer"),new ArrayList<>());
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(t);
        Exception exception = assertThrows(IllegalStateException.class,
                () -> createFinishedTask().replaceDependencies(tasks));
        Assertions.assertSame("cant update dependencies when Finished", exception.getMessage());
}
    @Test
    public void replaceTaskTest() {
        Timestamp time = getSystemTime();
        Exception exception = assertThrows(IllegalStateException.class,
                () -> createFinishedTask().replaceTask("newdesc",5,1,List.of("JavaProgrammer"),time,new ArrayList<>())
        );
        Assertions.assertSame("cant replace task when Finished", exception.getMessage());
    }
    @Test
    public void deleteTaskTest() {
        Task t1 = createFinishedTask();

        assertTrue(p.getTasks().contains(t1));
        t1.prepareDelete();
        assertFalse(p.getTasks().contains(t1));
    }
}
