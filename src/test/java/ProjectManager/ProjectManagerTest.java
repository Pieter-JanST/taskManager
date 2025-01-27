package ProjectManager;

import com.taskmanager.controller.UserService;
import com.taskmanager.model.*;
import com.taskmanager.model.roles.ProjectManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProjectManagerTest {
    UserService userService = new UserService();
    ProjectManager p = new ProjectManager(new ProjectRepository(),new TaskRepository(),new ActionRepository(),userService);
    @Test
    public void addProjectTest()
    {
        userService.addUser("b","b");

        //with T in dueTime
        long id1 = p.addProject("projectname1","projectdesc1","2029-01-01T12:00",userService.getUsers().get(0).getId());
        //without T in dueTime
        long id2 = p.addProject("projectname2","projectdesc2","2029-01-01 12:00:00",userService.getUsers().get(0).getId());

        Assertions.assertEquals("projectname1",p.getProjectById(id1).getName());
        Assertions.assertEquals("projectdesc1",p.getProjectById(id1).getDescription());

        Assertions.assertEquals("projectname2",p.getProjectById(id2).getName());
        Assertions.assertEquals("projectdesc2",p.getProjectById(id2).getDescription());
    }
    @Test
    public void addTaskTest(){
        userService.addUser("b","b");

        //with T in dueTime
        long pId = p.addProject("projectname1","projectdesc","2029-01-01T12:00",userService.getUsers().get(0).getId());
        long tId = p.addTask(pId,"taskdesc",20,5.5F, List.of("JavaProgrammer"),new LinkedList<>(),userService.getUsers().get(0).getId());

        Assertions.assertEquals("taskdesc",p.getTask(pId,tId).getDescription());
        Assertions.assertEquals("AVAILABLE",p.getTask(pId,tId).getStatus());
        Assertions.assertEquals(20,p.getTask(pId,tId).getEstimatedDuration());
        Assertions.assertEquals(5.5F,p.getTask(pId,tId).getAcceptableDeviation());
        Assertions.assertTrue(p.getTask(pId,tId).getNecessaryRoles().containsKey("JavaProgrammer0"));
        Assertions.assertEquals(0, p.getTask(pId, tId).getDependsOn().size());
    }
    @Test
    public void getNonExistentTask(){
        userService.addUser("b","b");

        //with T in dueTime
        long pId = p.addProject("projectname1","projectdesc","2029-01-01T12:00",userService.getUsers().get(0).getId());

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    p.getTask(pId,-1L);
                });
        Assertions.assertEquals("task not found",exception.getMessage());
    }
    @Test
    public void getNonExistentProject(){
        userService.addUser("b","b");

        //with T in dueTime
        long pId = p.addProject("projectname1","projectdesc","2029-01-01T12:00",userService.getUsers().get(0).getId());

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    p.getProjectById(-1L);
                });
        Assertions.assertEquals("Project with this id was not found inside this project manager's projects",exception.getMessage());
    }

    @Test
    public void replaceTaskTest(){
        userService.addUser("b","b");

        //with T in dueTime
        long pId = p.addProject("projectname1","projectdesc","2029-01-01T12:00",userService.getUsers().get(0).getId());
        long tId = p.addTask(pId,"taskdesc",20,5.5F, List.of("JavaProgrammer"),new LinkedList<>(),userService.getUsers().get(0).getId());

        p.replaceTask(pId,tId,10,1.0F,"newdesc",List.of("PythonProgrammer"),new LinkedList<>(),userService.getUsers().get(0).getId());

        Assertions.assertEquals("newdesc",p.getTask(pId,tId).getDescription());
        Assertions.assertEquals("AVAILABLE",p.getTask(pId,tId).getStatus());
        Assertions.assertEquals(10,p.getTask(pId,tId).getEstimatedDuration());
        Assertions.assertEquals(1.0F,p.getTask(pId,tId).getAcceptableDeviation());
        Assertions.assertTrue(p.getTask(pId,tId).getNecessaryRoles().containsKey("PythonProgrammer0"));
        Assertions.assertEquals(0, p.getTask(pId, tId).getDependsOn().size());

    }
    @Test
    public void updateTaskDependenciesTest(){
        Long userId = userService.addUser("b","b");

        //with T in dueTime
        long pId = p.addProject("projectname1","projectdesc","2029-01-01T12:00",userService.getUsers().get(0).getId());
        long tId = p.addTask(pId,"taskdesc",20,5.5F, List.of("JavaProgrammer"),new LinkedList<>(),userService.getUsers().get(0).getId());
        long tId2 = p.addTask(pId,"taskdesc",20,5.5F, List.of("JavaProgrammer"),new LinkedList<>(),userService.getUsers().get(0).getId());

        p.updateTaskDependencies(tId,List.of(tId2), userId);
        Assertions.assertSame(p.getTask(pId, tId).getDependsOn().get(0).getId(), p.getTask(pId, tId2).getId());
        Assertions.assertEquals("UNAVAILABLE",p.getTask(pId,tId).getStatus());
    }
    @Test
    public void getAvailableTasksTest(){
        userService.addUser("b","b");

        //with T in dueTime
        long pId = p.addProject("projectname1","projectdesc","2029-01-01T12:00",userService.getUsers().get(0).getId());
        long tId = p.addTask(pId,"taskdesc",20,5.5F, List.of("JavaProgrammer"),new LinkedList<>(),userService.getUsers().get(0).getId());
        Assertions.assertTrue(p.getAvailableTasks(pId).contains(tId));


    }
    @Test
    public void clearDependenciesTest(){
        long userid = userService.addUser("b","b");

        //with T in dueTime
        long pId = p.addProject("projectname1","projectdesc","2029-01-01T12:00",userService.getUsers().get(0).getId());
        long tId = p.addTask(pId,"taskdesc",20,5.5F, List.of("JavaProgrammer"),new LinkedList<>(),userService.getUsers().get(0).getId());
        long tId2 = p.addTask(pId, "taskdesc", 20, 5.5F, List.of("JavaProgrammer"), List.of(tId),userService.getUsers().get(0).getId());
        //Assertions.assertTrue(p.getTask(pId,tId2).getDependsOn().contains(p.getTask(pId,tId)));
        Assertions.assertSame(p.getTask(pId, tId2).getDependsOn().get(0).getId(), p.getTask(pId, tId).getId());
        List<Long> dependsOn = new LinkedList<>();
        p.updateTaskDependencies(tId2, dependsOn , userid);
        Assertions.assertEquals(0, p.getTask(pId, tId2).getDependsOn().size());
    }
    @Test
    public void deleteTaskTest(){
        userService.addUser("b","b");

        long pId = p.addProject("projectname1","projectdesc","2029-01-01T12:00",userService.getUsers().get(0).getId());
        long tId = p.addTask(pId,"taskdesc",20,5.5F, List.of("JavaProgrammer"),new LinkedList<>(),userService.getUsers().get(0).getId());
        long tId2 = p.addTask(pId, "taskdesc", 20, 5.5F, List.of("JavaProgrammer"), List.of(tId),userService.getUsers().get(0).getId());

        Assertions.assertSame(p.getProjectById(pId).getTasks().get(0).getId(), p.getTask(pId, tId).getId());
        Assertions.assertSame(p.getProjectById(pId).getTasks().get(0).getId(), p.getTask(pId, tId).getId());

        Assertions.assertSame(p.getTask(pId, tId2).getDependsOn().get(0).getId(),p.getTask(pId, tId).getId());
        p.deleteTask(tId);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> p.getTask(pId, tId)
        );
        Assertions.assertSame("task not found", exception.getMessage());
    }
}
