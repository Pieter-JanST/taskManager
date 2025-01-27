package useCase;

import com.taskmanager.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.CookieManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class EndTaskTest {
    private final String url = "http://localhost:8080";

    /**
     * Log in a user with predefined projects and tasks
     * @throws IOException
     * @throws InterruptedException
     */
    public HttpClient logInUser(String username,String pass) throws IOException, InterruptedException {
        URI uri = URI.create(url+"/user/loginUser?username="+username+"&password="+pass);
        HttpClient client = HttpClient.newBuilder()
                .cookieHandler(new CookieManager())
                .build();

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return client;
    }

    /**
     * Log in a user that is a Developer
     * @throws IOException
     * @throws InterruptedException
     */
    public HttpClient logInDev() throws IOException, InterruptedException {
        URI uri = URI.create(url+"/user/loginUser?username=userD&password=d");
        HttpClient client = HttpClient.newBuilder()
                .cookieHandler(new CookieManager())
                .build();

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return client;
    }


    private long getIdFromTaskDesc(HttpClient client,String taskname ) throws IOException, InterruptedException {
        //get the id from the newly created task
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create(url+"/project/showProject?projectid=13"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
        var response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        List<String> s = new java.util.ArrayList<>(List.of(response2.body().split("<tr>")));
        s.remove(0);
        s.remove(0);
        for (String td: s) {
            td = td.replace("</td>","");
            String[] string = td.split("<td>");
            if (string[2].trim().equals(taskname)) {
                return Long.parseLong(string[1].trim());
            }
        }
        return -1;
    }

    /**
     * Create a task and assign the user
     * @throws IOException
     * @throws InterruptedException
     */
    //String testDisc
    public HttpClient makeAndStartTask(String taskname, String dev,String username,String pass) throws IOException, InterruptedException {
        HttpClient client = null;
        client = logInUser(username,pass);

        ///task/startTask?taskid=${task.getId()}&projectid=${task.getProject().getId()}
        ///task/addUserToTask?taskId=${task.getId()}&projectId=${task.getProject().getId()}&roleId=${role}

        //Create a task
        URI uri = URI.create(url+"/task/createTask");

        String testEst = "1";
        String testDev = "1";
        String testDep = "";//-1
        String testRole = "Developer";

        ///task/addUserToTask?taskId=15&projectId=2&roleId=Developer0
        // Build the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("project=13"+"&desc="+ taskname +"&estimated="+testEst+"&deviation="+testDev+"&rolesNecessary="+testRole+"&dependsOn="+testDep))
                .build();

        // Send the HTTP request and get the response
        var response1 = client.send(request, HttpResponse.BodyHandlers.ofString());

        //Add a user to the task and start the task
        uri = URI.create(url+"/task/addUserToTask");
        long id = getIdFromTaskDesc(client,taskname);
        // Build the HTTP request
         request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("taskId="+id+"&projectId=13"+"&roleId=Developer0"))
                .build();

        var response3 = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response3.body());
        return client;

    }

    /**
     * Step 2: Test if a user is the list of tasks which are executing
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void listOfTasksTest() throws IOException, InterruptedException {
        String desc = "NewTestTask"+Instant.now();
        HttpClient client = makeAndStartTask(desc, "dev","EndTaskTestUser1","n");

        URI uri = URI.create(url+"/task/endTaskOverview");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200,response.statusCode());

        Assertions.assertTrue(response.body().contains(desc));
        Assertions.assertTrue(response.body().contains("EXECUTING"));

        //set task to failed so the user is back to available
        long id = getIdFromTaskDesc(client,desc);
        uri = URI.create(url+"/task/endTask?taskid="+id+"&projectid=13&failed=True");
        request = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());


    }

    /**
     * Step 3&4: Test if a user can select the task to end and view its details.
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void selectTaskTest() throws IOException, InterruptedException {
        String desc = "NewTestTask2"+ Instant.now();
        HttpClient client = makeAndStartTask(desc, "","EndTaskTestUser2","n");
        long id = getIdFromTaskDesc(client,desc);

        URI uri = URI.create(url+"/task/endTaskPage?taskid="+id+"&projectid=13");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200,response.statusCode());
        Assertions.assertTrue(response.body().contains(desc));
        Assertions.assertTrue(response.body().contains("Time"));
    }

    /**
     * Step 5&6: test if a user can end a task finished of failed
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void endtaskTest() throws IOException, InterruptedException {
        String desc = "NewTestTask3"+ Instant.now();
        HttpClient client = makeAndStartTask(desc, "","EndTaskTestUser3","n");
        long id = getIdFromTaskDesc(client,desc);

        //end task 15 as finished
        URI uri = URI.create(url+"/task/endTask?taskid="+id+"&projectid=13&failed=False");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //System.out.println(response.body());
        //System.out.println(response.headers());

        Assertions.assertEquals(302,response.statusCode());
        Assertions.assertTrue(response.headers().toString().contains("succesfully"));

        //end task 16 as failed
        String desc2 = "NewTestTask4"+ Instant.now();

        client = makeAndStartTask(desc2, "dev","EndTaskTestUser4","n");
        long id2 = getIdFromTaskDesc(client,desc2);

        uri = URI.create(url+"/task/endTask?taskid="+id2+"&projectid=13&failed=True");
        request = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(302,response.statusCode());
        Assertions.assertTrue(response.headers().toString().contains("succesfully"));

        //check if the tasks are finished or failed
        uri = URI.create(url+"/task/taskOverview");
        request = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200,response.statusCode());
        Assertions.assertTrue(response.body().contains("FAILED"));
        Assertions.assertTrue(response.body().contains("FINISHED"));
    }

    /**
     * Step 3-5a: able to cancel updating the status.
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void cancelEndTest() throws IOException, InterruptedException {
        HttpClient client = logInUser("EndTaskTestUser4","n");
        URI uri = URI.create(url+"/task/addUserToTask");
        // Build the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("taskId=19"+"&projectId=15"+"&roleId=PythonProgrammer0"))
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //go to end task page but don't submit anything and go back to task overview
        uri = URI.create(url+"/task/endTaskPage?taskid=19&projectid=15");
        request = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        uri = URI.create(url+"/task/endTaskOverview");
        request = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //Assertions.assertTrue(response.body().contains("19"));
    }

    /**
     * Step 6a: Test invalid data
     * @throws IOException
     * @throws InterruptedException
     */
    //TODO welke data kan hier invalid zijn ?
    @Test
    public void invalidDataTest(){

    }
}
