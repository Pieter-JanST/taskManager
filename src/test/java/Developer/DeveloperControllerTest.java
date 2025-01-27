package Developer;

import com.taskmanager.controller.DevController;
import com.taskmanager.controller.PMController;
import com.taskmanager.controller.UserService;
import com.taskmanager.model.*;
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

public class DeveloperControllerTest {
    TaskRepository taskRepository = new TaskRepository();

    ProjectRepository projectRepository = new ProjectRepository();
    UserService userService = new UserService();
    ActionRepository actionRepository = new ActionRepository();
    private Task CreateTask(String taskdesc){
        Project p = new Project("project","project desc", Timestamp.from(Instant.now().plus(60, ChronoUnit.MINUTES)),getSystemTime(),getOriginalSystemTime());
        List<String> roles = new ArrayList<String>();
        roles.add("JavaProgrammer");
        return new Task(taskdesc,60,10L,p,getSystemTime(), roles,new ArrayList<>());
    }

    @Test
    public void getTasksTest() {
        Task t = CreateTask("testTask100");
        taskRepository.addTask(t);
        DevController devController = new DevController(taskRepository,userService,actionRepository,projectRepository);
        Assertions.assertTrue(devController.getTasks().contains(t));
    }
    @Test
    public void getTaskByIdTest(){
        Task t = CreateTask("testTask99");
        taskRepository.addTask(t);
        DevController devController = new DevController(taskRepository,userService,actionRepository,projectRepository);
        Assertions.assertEquals(t.getId(),devController.getTaskById(t.getId()).getId());
    }
    @Test
    public void addUserToTaskTest() {
        User u = new User("user","u",List.of("JavaProgrammer"));
        userService.addUser(u);
        Task t = CreateTask("testTask101");
        taskRepository.addTask(t);
        DevController devController = new DevController(taskRepository,userService,actionRepository,projectRepository);
        devController.addUserToTask(t.getId(),u.getId());//,"JavaProgrammer0"
        Assertions.assertEquals(t,u.getWorkingTask());
        Assertions.assertTrue(t.getNecessaryRoles().containsValue(u));
    }
    @Test
    public void getWorkingTasksTest() {
        User u = new User("user","u",List.of("JavaProgrammer"));
        userService.addUser(u);
        Task t = CreateTask("testTask101");
        taskRepository.addTask(t);
        DevController devController = new DevController(taskRepository,userService,actionRepository,projectRepository);
        devController.addUserToTask(t.getId(),u.getId());//,"JavaProgrammer0"
        Assertions.assertTrue(devController.getWorkingTasks(u.getId()).contains(t));
    }
    @Test
    public void endTaskTest() {
        User u = new User("user","u",List.of("JavaProgrammer"));
        userService.addUser(u);
        Task t = CreateTask("testTask101");
        taskRepository.addTask(t);
        DevController devController = new DevController(taskRepository,userService,actionRepository,projectRepository);
        devController.addUserToTask(t.getId(),u.getId());//,"JavaProgrammer0"
        devController.endTask(t.getId(),false,u.getId());
        Assertions.assertEquals("FINISHED",t.getStatus());

    }
}
