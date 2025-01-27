package task;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskServiceTest {
/*
    @Test
    public void addValidTaskToServiceWithParams() {

        ProjectService projectService = new ProjectService();
        long projectid = projectService.addProject("project","project desc", "2029-01-01 12:00");

        TaskService taskService = new TaskService();
        long taskid = taskService.addTask(projectid,"task desc",60,1L,new LinkedList<>());

        Assertions.assertEquals("task desc",taskService.getTask(projectid,taskid).getDescription());
        Assertions.assertEquals(60,taskService.getTask(projectid,taskid).getEstimatedDuration());
        Assertions.assertEquals(1L,taskService.getTask(projectid,taskid).getAcceptableDeviation());
        Assertions.assertTrue(taskService.getTasksFromProject(projectid).contains(taskService.getTask(projectid,taskid)));
    }

    @Test
    public void updateTaskStatusFromTaskService() {

        ProjectService projectService = new ProjectService();
        long projectid = projectService.addProject("project","project desc", "2029-01-01 12:00");

        TaskService taskService = new TaskService();
        long taskid = taskService.addTask(projectid,"task desc",60,1L,new LinkedList<>());
        taskService.updateTaskStatus(projectid,taskid,"EXECUTING");

        Assertions.assertEquals(Status.EXECUTING,taskService.getTask(projectid,taskid).getStatus());
    }
    @Test
    public void startTaskTroughTaskService() {

        ProjectService projectService = new ProjectService();
        long projectid = projectService.addProject("project","project desc", "2029-01-01 12:00");

        TaskService taskService = new TaskService();
        long taskid = taskService.addTask(projectid,"task desc",60,1L,new LinkedList<>());
        taskService.startTask(projectid,taskid);

        Assertions.assertEquals(Status.EXECUTING,taskService.getTask(projectid,taskid).getStatus());
        Assertions.assertNotNull(taskService.getTask(projectid,taskid).getTimeSpan().getStartTime());

    }
    @Test
    public void endTaskTroughTaskService() {

        ProjectService projectService = new ProjectService();
        long projectid = projectService.addProject("project","project desc", "2029-01-01 12:00");

        TaskService taskService = new TaskService();
        long taskid = taskService.addTask(projectid,"task desc",60,1L,new LinkedList<>());
        taskService.startTask(projectid,taskid);
        taskService.endTask(projectid,taskid);
        Assertions.assertEquals(Status.FINISHED,taskService.getTask(projectid,taskid).getStatus());
        Assertions.assertNotNull(taskService.getTask(projectid,taskid).getTimeSpan().getStartTime());
        Assertions.assertNotNull(taskService.getTask(projectid,taskid).getTimeSpan().getEndTime());

    }
    @Test
    public void replaceTaskWithValidParam() {

        ProjectService projectService = new ProjectService();
        long projectid = projectService.addProject("project","project desc", "2029-01-01 12:00");

        TaskService taskService = new TaskService();
        long taskid = taskService.addTask(projectid,"task desc",60,1L,new LinkedList<>());

        Assertions.assertEquals("task desc",taskService.getTask(projectid,taskid).getDescription());
        Assertions.assertEquals(60,taskService.getTask(projectid,taskid).getEstimatedDuration());
        Assertions.assertEquals(1L,taskService.getTask(projectid,taskid).getAcceptableDeviation());
        Assertions.assertTrue(taskService.getTasksFromProject(projectid).contains(taskService.getTask(projectid,taskid)));

        taskService.replaceTask(projectid,taskid,100,6L,"new desc");

        Assertions.assertEquals("new desc",taskService.getTask(projectid,taskid).getDescription());
        Assertions.assertEquals(100,taskService.getTask(projectid,taskid).getEstimatedDuration());
        Assertions.assertEquals(6L,taskService.getTask(projectid,taskid).getAcceptableDeviation());
        Assertions.assertTrue(taskService.getTasksFromProject(projectid).contains(taskService.getTask(projectid,taskid)));

    }
    @Test
    public void replaceTaskWithInValidDesc() {
        ProjectService projectService = new ProjectService();
        long projectid = projectService.addProject("project","project desc", "2029-01-01 12:00");

        TaskService taskService = new TaskService();
        long taskid = taskService.addTask(projectid,"task desc",60,1L,new LinkedList<>());

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    taskService.replaceTask(projectid,taskid,100,6L," ");
                });
        Assertions.assertEquals("description cant be empty",exception.getMessage());

        Assertions.assertEquals("task desc",taskService.getTask(projectid,taskid).getDescription());
        Assertions.assertEquals(60,taskService.getTask(projectid,taskid).getEstimatedDuration());
        Assertions.assertEquals(1L,taskService.getTask(projectid,taskid).getAcceptableDeviation());
        Assertions.assertTrue(taskService.getTasksFromProject(projectid).contains(taskService.getTask(projectid,taskid)));
    }

    @Test
    public void replaceTaskWithNullDesc() {

        ProjectService projectService = new ProjectService();
        long projectid = projectService.addProject("project","project desc", "2029-01-01 12:00");

        TaskService taskService = new TaskService();
        long taskid = taskService.addTask(projectid,"task desc",60,1L,new LinkedList<>());

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    taskService.replaceTask(projectid,taskid,100,6L,null);
                });
        Assertions.assertEquals("description cant be empty",exception.getMessage());

        Assertions.assertEquals("task desc",taskService.getTask(projectid,taskid).getDescription());
        Assertions.assertEquals(60,taskService.getTask(projectid,taskid).getEstimatedDuration());
        Assertions.assertEquals(1L,taskService.getTask(projectid,taskid).getAcceptableDeviation());
        Assertions.assertTrue(taskService.getTasksFromProject(projectid).contains(taskService.getTask(projectid,taskid)));
    }

    @Test
    public void replaceTaskWithInValidDuration() {

        ProjectService projectService = new ProjectService();
        long projectid = projectService.addProject("project","project desc", "2029-01-01 12:00");

        TaskService taskService = new TaskService();
        long taskid = taskService.addTask(projectid,"task desc",60,1L,new LinkedList<>());

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    taskService.replaceTask(projectid,taskid,-10,6L,"test");
                });
        Assertions.assertEquals("estimatedDuration needs to be greather then 0",exception.getMessage());

        Assertions.assertEquals("task desc",taskService.getTask(projectid,taskid).getDescription());
        Assertions.assertEquals(60,taskService.getTask(projectid,taskid).getEstimatedDuration());
        Assertions.assertEquals(1L,taskService.getTask(projectid,taskid).getAcceptableDeviation());
        Assertions.assertTrue(taskService.getTasksFromProject(projectid).contains(taskService.getTask(projectid,taskid)));
    }
    @Test
    public void replaceTaskWithInValidDeviation() {

        ProjectService projectService = new ProjectService();
        long projectid = projectService.addProject("project","project desc", "2029-01-01 12:00");

        TaskService taskService = new TaskService();
        long taskid = taskService.addTask(projectid,"task desc",60,1L,new LinkedList<>());

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    taskService.replaceTask(projectid,taskid,10,-6L,"test");
                });
        Assertions.assertEquals("acceptableDeviation needs to be greather then 0",exception.getMessage());

        Assertions.assertEquals("task desc",taskService.getTask(projectid,taskid).getDescription());
        Assertions.assertEquals(60,taskService.getTask(projectid,taskid).getEstimatedDuration());
        Assertions.assertEquals(1L,taskService.getTask(projectid,taskid).getAcceptableDeviation());
        Assertions.assertTrue(taskService.getTasksFromProject(projectid).contains(taskService.getTask(projectid,taskid)));
    }*/
}
