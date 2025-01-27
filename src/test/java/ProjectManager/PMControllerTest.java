package ProjectManager;

import com.taskmanager.controller.UserService;
import com.taskmanager.model.*;
import com.taskmanager.model.taskState.*;
import com.taskmanager.controller.DevController;
import com.taskmanager.controller.PMController;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PMControllerTest {
    private final ProjectRepository projectRepository = new ProjectRepository();
    private final TaskRepository taskRepository = new TaskRepository();
    private final ActionRepository actionRepository = new ActionRepository();
    private final UserService userService = new UserService();

    private final PMController pmController = new PMController(projectRepository,taskRepository,actionRepository,userService);


    //--------------------Project--------------------
    @Test
    public void addValidProject() {
        userService.addUser(new User("u","u"));
        long userid = userService.getUsers().get(0).getId();
        Timestamp t = Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES));
        long id = pmController.addProject("project", "project desc", t.toString(),userid);
        Assertions.assertSame(((Project) pmController.getProjectsList().toArray()[0]).getId(), pmController.getProjectsById(id).getId());
    }
    @Test
    public void getProjectWithNonExistingId() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    pmController.getProjectsById(-1L);
                });
        Assertions.assertTrue(exception.getMessage().contains("not found"));
    }
    @Test
    public void getAllProjects() {
        userService.addUser(new User("u","u"));
        long userid = userService.getUsers().get(0).getId();
        Timestamp t = Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES));
        pmController.addProject("project", "project desc", t.toString(),userid);
        Assertions.assertTrue(pmController.getProjectsList().size()>0);
    }
    @Test
    public void getAllTasksFromProjects() {
        userService.addUser(new User("u","u"));
        long userid = userService.getUsers().get(0).getId();
        Timestamp t = Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES));

        pmController.addProject("project", "project desc", t.toString(),userid);
        Project[] list = pmController.getProjectsList().toArray(new Project[0]);
        Task task = new Task("test",60,10L,list[0], List.of("JavaProgrammer"));
        Assertions.assertSame(pmController.getTasksFromProject(list[0].getId()).get(0).getId(), task.getId());
        Assertions.assertTrue(pmController.getTasksFromProject(list[0].getId()).size()>0);
    }
    @Test
    public void getAllTasksFromProjectsWithNonExistingId() {
        userService.addUser(new User("u","u"));
        long userid = userService.getUsers().get(0).getId();
        Timestamp t = Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES));

        pmController.addProject("project", "project desc", t.toString(),userid);
        Project[] list = pmController.getProjectsList().toArray(new Project[0]);
        Task task = new Task("test",60,10L,list[0], List.of("JavaProgrammer"));
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    pmController.getTasksFromProject(1000);
                });
        Assertions.assertTrue(exception.getMessage().contains("not found"));
    }

    //--------------------Task--------------------
    @Test
    public void addValidTaskWithParams() {
        userService.addUser(new User("u","u"));
        long userid = userService.getUsers().get(0).getId();
        Timestamp t = Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES));

        long projectid = pmController.addProject("project","project desc", t.toString(),userid);
        long taskid = pmController.addTask(projectid,"task desc",60,1L,List.of("JavaProgrammer"),new LinkedList<>(),userid);

        Assertions.assertEquals("task desc",pmController.getTask(projectid,taskid).getDescription());
        Assertions.assertEquals(60,pmController.getTask(projectid,taskid).getEstimatedDuration());
        Assertions.assertEquals(1L,pmController.getTask(projectid,taskid).getAcceptableDeviation());
        Assertions.assertSame(pmController.getTasksFromProject(projectid).get(0).getId(), pmController.getTask(projectid, taskid).getId());
    }


    /*@Test
    public void startTaskTrough() {
        PMController pmController = new PMController();
        DevController devController = new DevController();
        Timestamp t = Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES));
        long projectid = pmController.addProject("project","project desc", t.toString());
        //TaskService taskService = new TaskService();
        long taskid = pmController.addTask(pmController.getProjectsById(projectid),"task desc",60,1L,List.of("JavaProgrammer"),new LinkedList<>());
        Task task = pmController.getTask(projectid, taskid);
        task.setState(new AvailableState());
        devController.startTask(taskid);
        Assertions.assertEquals("EXECUTING",pmController.getTask(projectid,taskid).getStatus());
        Assertions.assertNotNull(pmController.getTask(projectid,taskid).getTimeSpan().getStartTime());
    }*/
    /*@Test
    public void endTaskTrough() {
        PMController pmController = new PMController();
        DevController devController = new DevController();
        Timestamp t = Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES));
        long projectid = pmController.addProject("project","project desc", t.toString());
        long taskid = pmController.addTask(pmController.getProjectsById(projectid),"task desc",60,1L,List.of("JavaProgrammer"),new LinkedList<>());
        Task task = pmController.getTask(projectid, taskid);
        task.setState(new AvailableState());
        devController.startTask(taskid);
        devController.endTask(taskid, false);
        Assertions.assertEquals("FINISHED",pmController.getTask(projectid,taskid).getStatus());
        Assertions.assertNotNull(pmController.getTask(projectid,taskid).getTimeSpan().getStartTime());
        Assertions.assertNotNull(pmController.getTask(projectid,taskid).getTimeSpan().getEndTime());
    }*/
    @Test
    public void replaceTaskWithValidParam() {
        userService.addUser(new User("u","u"));
        long userid = userService.getUsers().get(0).getId();

        Timestamp t = Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES));
        long projectid = pmController.addProject("project","project desc", t.toString(),userid);

        long taskid = pmController.addTask((projectid),"task desc",60,1L,List.of("JavaProgrammer"),new LinkedList<>(),userid);

        Assertions.assertEquals("task desc",pmController.getTask(projectid,taskid).getDescription());
        Assertions.assertEquals(60,pmController.getTask(projectid,taskid).getEstimatedDuration());
        Assertions.assertEquals(1L,pmController.getTask(projectid,taskid).getAcceptableDeviation());
        Assertions.assertSame(pmController.getTasksFromProject(projectid).get(0).getId(), pmController.getTask(projectid, taskid).getId());

        pmController.replaceTask(projectid,taskid,100,6L,"new desc",List.of("JavaProgrammer"),new LinkedList<>(),userid);

        Assertions.assertEquals("new desc",pmController.getTask(projectid,taskid).getDescription());
        Assertions.assertEquals(100,pmController.getTask(projectid,taskid).getEstimatedDuration());
        Assertions.assertEquals(6L,pmController.getTask(projectid,taskid).getAcceptableDeviation());
        Assertions.assertSame(pmController.getTasksFromProject(projectid).get(0).getId(), pmController.getTask(projectid, taskid).getId());

    }
    @Test
    public void replaceTaskWithInValidDesc() {
        userService.addUser(new User("u","u"));
        long userid = userService.getUsers().get(0).getId();

        Timestamp t = Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES));
        long projectid = pmController.addProject("project","project desc", t.toString(),userid);

        long taskid = pmController.addTask((projectid),"task desc",60,1L,List.of("JavaProgrammer"),new LinkedList<>(),userid);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    pmController.replaceTask(projectid,taskid,100,6L," ",List.of("JavaProgrammer"),new LinkedList<>(),userid);
                });
        Assertions.assertEquals("description cant be empty",exception.getMessage());

        Assertions.assertEquals("task desc",pmController.getTask(projectid,taskid).getDescription());
        Assertions.assertEquals(60,pmController.getTask(projectid,taskid).getEstimatedDuration());
        Assertions.assertEquals(1L,pmController.getTask(projectid,taskid).getAcceptableDeviation());
        Assertions.assertSame(pmController.getTasksFromProject(projectid).get(0).getId(), pmController.getTask(projectid, taskid).getId());
    }

    @Test
    public void replaceTaskWithNullDesc() {
        userService.addUser(new User("u","u"));
        long userid = userService.getUsers().get(0).getId();

        Timestamp t = Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES));
        long projectid = pmController.addProject("project","project desc", t.toString(),userid);

        long taskid = pmController.addTask((projectid),"task desc",60,1L,List.of("JavaProgrammer"),new LinkedList<>(),userid);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    pmController.replaceTask(projectid,taskid,100,6L,null,List.of("JavaProgrammer"),new LinkedList<>(),userid);
                });
        Assertions.assertEquals("description cant be empty",exception.getMessage());

        Assertions.assertEquals("task desc",pmController.getTask(projectid,taskid).getDescription());
        Assertions.assertEquals(60,pmController.getTask(projectid,taskid).getEstimatedDuration());
        Assertions.assertEquals(1L,pmController.getTask(projectid,taskid).getAcceptableDeviation());
        Assertions.assertSame(pmController.getTasksFromProject(projectid).get(0).getId(), pmController.getTask(projectid, taskid).getId());
    }

    @Test
    public void replaceTaskWithInValidDuration() {
        userService.addUser(new User("u","u"));
        long userid = userService.getUsers().get(0).getId();

        Timestamp t = Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES));
        long projectid = pmController.addProject("project","project desc", t.toString(),userid);

        long taskid = pmController.addTask((projectid),"task desc",60,1L,List.of("JavaProgrammer"),new LinkedList<>(),userid);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    pmController.replaceTask(projectid,taskid,-10,6L,"test",List.of("JavaProgrammer"),new LinkedList<>(),userid);
                });
        Assertions.assertEquals("estimatedDuration needs to be greater then 0",exception.getMessage());

        Assertions.assertEquals("task desc",pmController.getTask(projectid,taskid).getDescription());
        Assertions.assertEquals(60,pmController.getTask(projectid,taskid).getEstimatedDuration());
        Assertions.assertEquals(1L,pmController.getTask(projectid,taskid).getAcceptableDeviation());
        Assertions.assertSame(pmController.getTasksFromProject(projectid).get(0).getId(), pmController.getTask(projectid, taskid).getId());
    }
    @Test
    public void replaceTaskWithInValidDeviation() {
        userService.addUser(new User("u","u"));
        long userid = userService.getUsers().get(0).getId();

        Timestamp t = Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES));
        long projectid = pmController.addProject("project","project desc", t.toString(),userid);

        long taskid = pmController.addTask((projectid),"task desc",60,1L,List.of("JavaProgrammer"),new LinkedList<>(),userid);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    pmController.replaceTask(projectid,taskid,10,-6L,"test",List.of("JavaProgrammer"),new LinkedList<>(),userid);
                });
        Assertions.assertEquals("acceptableDeviation needs to be greater then 0",exception.getMessage());

        Assertions.assertEquals("task desc",pmController.getTask(projectid,taskid).getDescription());
        Assertions.assertEquals(60,pmController.getTask(projectid,taskid).getEstimatedDuration());
        Assertions.assertEquals(1L,pmController.getTask(projectid,taskid).getAcceptableDeviation());
        Assertions.assertSame(pmController.getTasksFromProject(projectid).get(0).getId(), pmController.getTask(projectid, taskid).getId());
    }
    @Test
    public void updateTaskDependenciesTest() {
        userService.addUser(new User("u","u"));
        long userid = userService.getUsers().get(0).getId();

        Timestamp t = Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES));
        long projectid = pmController.addProject("project","project desc", t.toString(),userid);
        long taskid1 = pmController.addTask((projectid),"task desc",60,1L,List.of("JavaProgrammer"),new LinkedList<>(),userid);
        long taskid2 = pmController.addTask((projectid),"task2 desc",60,1L,List.of("JavaProgrammer"),new LinkedList<>(),userid);

        Assertions.assertEquals(0,pmController.getTask(projectid,taskid1).getDependsOn().size());
        Assertions.assertEquals("AVAILABLE",pmController.getTask(projectid,taskid1).getStatus());

        pmController.updateTaskDependencies(taskid1,List.of(taskid2), userid);

        Assertions.assertSame(pmController.getTask(projectid, taskid1).getDependsOn().get(0).getId(), pmController.getTask(projectid, taskid2).getId());
        Assertions.assertEquals("UNAVAILABLE",pmController.getTask(projectid,taskid1).getStatus());
    }
    @Test
    public void getAvailableTasksTest() {
        userService.addUser(new User("u","u"));
        long userid = userService.getUsers().get(0).getId();

        Timestamp t = Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES));
        long projectid = pmController.addProject("project","project desc", t.toString(),userid);
        long taskid1 = pmController.addTask((projectid),"task desc",60,1L,List.of("JavaProgrammer"),new LinkedList<>(),userid);
        long taskid2 = pmController.addTask((projectid),"task2 desc",60,1L,List.of("JavaProgrammer"),new LinkedList<>(),userid);

        Assertions.assertTrue(pmController.getAvailableTasks(projectid,taskid1).contains(pmController.getTask(projectid,taskid2).getId()));
    }
    @Test
    public void clearDependenciesTest() {
        userService.addUser(new User("u","u"));
        long userid = userService.getUsers().get(0).getId();

        Timestamp t = Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES));
        long projectid = pmController.addProject("project","project desc", t.toString(),userid);
        long taskid1 = pmController.addTask((projectid),"task desc",60,1L,List.of("JavaProgrammer"),new LinkedList<>(),userid);
        long taskid2 = pmController.addTask((projectid),"task2 desc",60,1L,List.of("JavaProgrammer"),new LinkedList<>(),userid);
        pmController.updateTaskDependencies(taskid1,List.of(taskid2), userid);
        Assertions.assertSame(pmController.getTask(projectid, taskid1).getDependsOn().get(0).getId(), pmController.getTask(projectid, taskid2).getId());
        Assertions.assertEquals("UNAVAILABLE",pmController.getTask(projectid,taskid1).getStatus());

        List<Long> dependsOn = new LinkedList<>();
        pmController.updateTaskDependencies(taskid1, dependsOn , userid);
        Assertions.assertEquals(0,pmController.getTask(projectid,taskid1).getDependsOn().size());
        Assertions.assertEquals("AVAILABLE",pmController.getTask(projectid,taskid1).getStatus());
    }
    @Test
    public void deleteTaskTest() {
        userService.addUser(new User("u","u"));
        long userid = userService.getUsers().get(0).getId();

        Timestamp t = Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES));
        long projectid = pmController.addProject("project","project desc", t.toString(),userid);
        long taskid1 = pmController.addTask((projectid),"task desc",60,1L,List.of("JavaProgrammer"),new LinkedList<>(),userid);
        long taskid2 = pmController.addTask((projectid),"task2 desc",60,1L,List.of("JavaProgrammer"),List.of(taskid1),userid);

        Assertions.assertSame(pmController.getProjectsById(projectid).getTasks().get(0).getId(), pmController.getTask(projectid, taskid1).getId());
        Assertions.assertSame(pmController.getTask(projectid, taskid2).getDependsOn().get(0).getId(), pmController.getTask(projectid, taskid1).getId());
        pmController.deleteTask(taskid1);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> pmController.getTask(projectid, taskid1)
        );
        Assertions.assertSame("task not found", exception.getMessage());
    }

    //--------------------Clock--------------------
    /* TODO: move
    @Test
    public void advanceTime() {
        Timestamp t2 = Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES));
        PMController pmController = new PMController();
        pmController.advanceTime(t2);
        Assertions.assertEquals(t2.getTime(), Clock.getSystemTime().getTime());
    }
    @Test
    public void setInvalidValidSystemTime() {
        Timestamp t1 = Timestamp.from(Instant.now());
        Timestamp t2 = Timestamp.from(Instant.now().minus(60, ChronoUnit.MINUTES));
        PMController pmController = new PMController();
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    pmController.advanceTime(t2);
                });
        Assertions.assertEquals("systemTime cant be set in the past",exception.getMessage());
        //klein verschil want kan niet excact op hetzelfde moment starten
        Assertions.assertTrue((t1.getTime()-Clock.getSystemTime().getTime())<100);
    }
     */

}