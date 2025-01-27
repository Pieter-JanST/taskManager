package task;

import com.taskmanager.model.Project;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskRepository;
import com.taskmanager.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.sql.Array;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.taskmanager.model.Clock.getOriginalSystemTime;
import static com.taskmanager.model.Clock.getSystemTime;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskTest {

    private TaskRepository taskRepository = new TaskRepository();
    @Test
    public void createvalidTaskWithoutDependers(){
        Project p = new Project("project","project desc", Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES)),getSystemTime(),getOriginalSystemTime());
        List<String> roles = new ArrayList<String>();
        roles.add("Developer");
        Task t = new Task("task desc",60,10L,p,getSystemTime(), roles,new ArrayList<>());
        Assertions.assertEquals("task desc",t.getDescription());
        Assertions.assertEquals(60,t.getEstimatedDuration());
        Assertions.assertEquals(10L,t.getAcceptableDeviation());
        Assertions.assertEquals("AVAILABLE",t.getStatus());
        Assertions.assertEquals(0,t.getDependsOn().size());
        Assertions.assertEquals(p,t.getProject());
        Assertions.assertNull(t.getTimeSpan().getStartTime());
        Assertions.assertNull(t.getTimeSpan().getEndTime());

    }



    @Test
    public void createvalidTaskWithDependers(){
        Project p = new Project("project","project desc", Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES)),getSystemTime(),getOriginalSystemTime());
        List<String> roles = new ArrayList<String>();
        roles.add("Developer");
        Task t = new Task("desc of task",20,5L,p,getSystemTime(), roles,new ArrayList<>());
        taskRepository.addTask(t);
        ArrayList<Task> dependsOn = new ArrayList<Task>();
        dependsOn.add(t);
        Task t2 = new Task("task desc",60,10L,p,getSystemTime(), roles, dependsOn);
        taskRepository.addTask(t2);

        Assertions.assertEquals("task desc",t2.getDescription());
        Assertions.assertEquals(60,t2.getEstimatedDuration());
        Assertions.assertEquals(10L,t2.getAcceptableDeviation());
        Assertions.assertEquals("UNAVAILABLE",t2.getStatus());
        Assertions.assertEquals(p,t.getProject());
        Assertions.assertEquals(1,t2.getDependsOn().size());
        Assertions.assertTrue(t2.getDependsOn().contains(t));
        Assertions.assertNull(t2.getTimeSpan().getStartTime());
        Assertions.assertNull(t2.getTimeSpan().getEndTime());
    }
    @Test
    public void createTaskWithInvalidDesc()
    {
        Project p = new Project("project","project desc", Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES)),getSystemTime(),getOriginalSystemTime());
        List<String> roles = new ArrayList<String>();
        roles.add("Developer");

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    Task t = new Task(" ",60,10L, p, roles);
                });
        Assertions.assertEquals("description cant be empty",exception.getMessage());
    }
    @Test
    public void createTaskWithNullDesc()
    {
        Project p = new Project("project","project desc", Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES)),getSystemTime(),getOriginalSystemTime());
        List<String> roles = new ArrayList<String>();
        roles.add("Developer");

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    Task t = new Task(null,60,10L,p,getSystemTime(), roles,new ArrayList<>());
                });
        Assertions.assertEquals("description cant be empty",exception.getMessage());

    }
    @Test
    public void createTaskWithNullDepend()
    {
        Project p = new Project("project","project desc", Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES)),getSystemTime(),getOriginalSystemTime());
        List<String> roles = new ArrayList<String>();
        roles.add("Developer");

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    Task t = new Task("et",60,10L,p,getSystemTime(), roles,null);
                });
        Assertions.assertEquals("dependencies cant be null",exception.getMessage());

    }


    @Test
    public void createTaskWithInvalidDuration() {
        Project p = new Project("project", "project desc", Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES)),getSystemTime(),getOriginalSystemTime());
        List<String> roles = new ArrayList<String>();
        roles.add("Developer");

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    Task t = new Task("description", -10, 10L, p,getSystemTime(), roles, new ArrayList<>());
                });
        Assertions.assertEquals("estimatedDuration needs to be greater then 0",exception.getMessage());

    }
    @Test
    public void createTaskWithInvalidDeviation() {
        Project p = new Project("project", "project desc", Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES)),getSystemTime(),getOriginalSystemTime());
        List<String> roles = new ArrayList<String>();
        roles.add("Developer");

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    Task t = new Task("description", 10, -1L, p, roles);
                });
        Assertions.assertEquals("acceptableDeviation needs to be greater then 0",exception.getMessage());

    }
    @Test
    public void createTaskWithInvalidNullProject() {
        List<String> roles = new ArrayList<String>();
        roles.add("Developer");

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    Task t = new Task("description", 10, 1L, null, roles);
                });
        Assertions.assertEquals("project cant be empty",exception.getMessage());
    }
    /*@Test
    public void createTaskWithDepenedTaskThatNotBelongsToSameProject() {
        Project p1 = new Project("project", "project desc", Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES)),getSystemTime(),getOriginalSystemTime());
        Project p2 = new Project("project2", "project2 desc", Timestamp.from(Instant.now().plus(10, ChronoUnit.MINUTES)),getSystemTime(),getOriginalSystemTime());
        List<String> roles = new ArrayList<String>();
        roles.add("Developer");


        Task t1 = new Task("desc of task", 20, 5L, p1,getSystemTime(), roles,new ArrayList<>());

        ArrayList<Task> dependsOn = new ArrayList<Task>();
        dependsOn.add(t1);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    Task t2 = new Task("task desc", 60, 10L, p2,getSystemTime(), roles, dependsOn);
                });
        Assertions.assertEquals("depended task does not belong to same project",exception.getMessage());

    }*/

    @Test
    public void EndTaskWithoutStartingIt() {
        Project p = new Project("project", "project desc", Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES)),getSystemTime(),getOriginalSystemTime());
        List<String> roles = new ArrayList<String>();
        roles.add("Developer");
        Task t = new Task("desc of task",20,5L,p,getSystemTime(), roles,new ArrayList<>());

        Exception exception = assertThrows(IllegalStateException.class,
                () -> t.endTask(false));
        Assertions.assertTrue(exception.getMessage().contains("end a task"));

    }
    @Test
    public void replaceDependenciesTest() {
        Project p = new Project("project", "project desc", Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES)),getSystemTime(),getOriginalSystemTime());
        List<String> roles = new ArrayList<String>();
        roles.add("Developer");
        Task t1 = new Task("desc of task",20,5L,p,getSystemTime(), roles, new ArrayList<>());
        Task t2 = new Task("desc of task",20,5L,p,getSystemTime(), roles, new ArrayList<>());

        Assertions.assertEquals(0, t2.getDependsOn().size());
        Assertions.assertEquals("AVAILABLE", t2.getStatus());
        ArrayList<Task> taskList = new ArrayList<>();
        taskList.add(t1);
        t2.replaceDependencies(taskList);
        Assertions.assertEquals(1, t2.getDependsOn().size());
        Assertions.assertEquals("UNAVAILABLE", t2.getStatus());
    }
    @Test
    public void replaceDependenciesWithInvalidRoleTest() {
        Project p = new Project("project", "project desc", Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES)),getSystemTime(),getOriginalSystemTime());
        List<String> roles = new ArrayList<String>();
        roles.add("InvalidRole");
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Task("desc of task",20,5L,p,getSystemTime(), roles, new ArrayList<>()));
        Assertions.assertSame("roles can only contain availableRoles", exception.getMessage());


    }
    @Test
    public void clearDependsOnTest(){
        Project p = new Project("project", "project desc", Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES)),getSystemTime(),getOriginalSystemTime());
        List<String> roles = new ArrayList<String>();
        roles.add("Developer");
        Task t1 = new Task("desc of task",20,5L,p,getSystemTime(), roles, new ArrayList<>());
        Task t2 = new Task("desc of task",20,5L,p,getSystemTime(), roles, new ArrayList<>());

        Assertions.assertEquals(0, t2.getDependsOn().size());
        Assertions.assertEquals("AVAILABLE", t2.getStatus());
        ArrayList<Task> taskList = new ArrayList<>();
        taskList.add(t1);
        t2.replaceDependencies(taskList);
        Assertions.assertEquals(1, t2.getDependsOn().size());
        Assertions.assertEquals("UNAVAILABLE", t2.getStatus());
        t2.clearDependsOn();
        Assertions.assertEquals(0, t2.getDependsOn().size());
        Assertions.assertEquals("AVAILABLE", t2.getStatus());
    }
    @Test
    public void assignUserToTaskTest(){
        Project p = new Project("project", "project desc", Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES)),getSystemTime(),getOriginalSystemTime());
        List<String> roles = new ArrayList<String>();
        roles.add("JavaProgrammer");
        roles.add("PythonProgrammer");
        Task t1 = new Task("desc of task",20,5L,p,getSystemTime(), roles, new ArrayList<>());
        User u = new User("bob","b",new ArrayList<>(List.of("JavaProgrammer")));
        t1.assignUserToTask(u,"JavaProgrammer0");
        Assertions.assertEquals("PENDING", t1.getStatus());
        Assertions.assertEquals(u.getWorkingTask().getDescription(),t1.getDescription());
        Assertions.assertTrue(t1.getNecessaryRoles().containsValue(u));
    }
    @Test
    public void ChangeUserFromPendingTaskTest(){
        Project p = new Project("project", "project desc", Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES)),getSystemTime(),getOriginalSystemTime());
        List<String> roles = new ArrayList<String>();
        roles.add("JavaProgrammer");
        roles.add("PythonProgrammer");
        Task t1 = new Task("desc of task",20,5L,p,getSystemTime(), roles, new ArrayList<>());
        Task t2 = new Task("desc of task",20,5L,p,getSystemTime(), roles, new ArrayList<>());

        User u = new User("bob","b",new ArrayList<>(List.of("JavaProgrammer")));
        t1.assignUserToTask(u,"JavaProgrammer0");
        Assertions.assertEquals("PENDING", t1.getStatus());
        Assertions.assertEquals(u.getWorkingTask().getDescription(),t1.getDescription());
        Assertions.assertTrue(t1.getNecessaryRoles().containsValue(u));
        t2.assignUserToTask(u,"JavaProgrammer0");
        Assertions.assertEquals("PENDING", t2.getStatus());
        Assertions.assertEquals(u.getWorkingTask().getDescription(),t2.getDescription());
        Assertions.assertTrue(t2.getNecessaryRoles().containsValue(u));
        Assertions.assertEquals("AVAILABLE", t1.getStatus());
        Assertions.assertFalse(t1.getNecessaryRoles().containsValue(u));
    }
    @Test
    public void assignUserToTaskWhileUserAlreadyWorkingOnTaskTest(){
        Project p = new Project("project", "project desc", Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES)),getSystemTime(),getOriginalSystemTime());
        List<String> roles = new ArrayList<String>();
        roles.add("JavaProgrammer");
        roles.add("PythonProgrammer");
        Task t1 = new Task("desc of task",20,5L,p,getSystemTime(), roles, new ArrayList<>());
        User u = new User("bob","b",new ArrayList<>(List.of("JavaProgrammer")));
        t1.assignUserToTask(u,"JavaProgrammer0");
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> t1.assignUserToTask(u,"PythonProgrammer1"));

        Assertions.assertSame("user already works on task as other role", exception.getMessage());

        Assertions.assertEquals("PENDING", t1.getStatus());
        Assertions.assertEquals(u.getWorkingTask().getDescription(),t1.getDescription());
        Assertions.assertTrue(t1.getNecessaryRoles().containsValue(u));
    }
    @Test
    public void assignUserToTaskWithInvaldRoleTest(){
        Project p = new Project("project", "project desc", Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES)),getSystemTime(),getOriginalSystemTime());
        List<String> roles = new ArrayList<String>();
        roles.add("JavaProgrammer");
        roles.add("PythonProgrammer");
        Task t1 = new Task("desc of task",20,5L,p,getSystemTime(), roles, new ArrayList<>());
        User u = new User("bob","b",new ArrayList<>(List.of("SysAdmin")));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> t1.assignUserToTask(u,"PythonProgrammer1"));

        Assertions.assertSame("User doesnt have the correct role", exception.getMessage());

        Assertions.assertEquals("AVAILABLE", t1.getStatus());
        Assertions.assertNull(u.getWorkingTask());
        Assertions.assertFalse(t1.getNecessaryRoles().containsValue(u));
    }

    @Test
    public void assignUserToTaskWithInvaldRoleIdTest(){
        Project p = new Project("project", "project desc", Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES)),getSystemTime(),getOriginalSystemTime());
        List<String> roles = new ArrayList<String>();
        roles.add("JavaProgrammer");
        roles.add("PythonProgrammer");
        Task t1 = new Task("desc of task",20,5L,p,getSystemTime(), roles, new ArrayList<>());
        User u = new User("bob","b",new ArrayList<>(List.of("PythonProgrammer")));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> t1.assignUserToTask(u,"PythonProgrammer2"));

        Assertions.assertSame("roleId does not exists", exception.getMessage());


    }
}
