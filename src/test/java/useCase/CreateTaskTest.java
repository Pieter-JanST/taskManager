package useCase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.net.CookieManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

//TODO: WERKT NOG NIET
public class CreateTaskTest {

    private final String url = "http://localhost:8080";

    /**
     * Log in a user with predefined projects and tasks
     * @throws IOException
     * @throws InterruptedException
     */
    public HttpClient logInUser() throws IOException, InterruptedException {
        URI uri = URI.create(url+"/user/loginUser?username=user&password=t");
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
     * Step 2: Test if a user is shown the form to create a task.
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void createTaskFormTest() throws IOException, InterruptedException {
        HttpClient client = logInUser();

        URI uri = URI.create(url+"/task/taskForm?projectid=2");
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

        Assertions.assertTrue(response.body().contains("Create a new Task"));
    }

    /**
     * Step 3&4: Test if a user can enter details for a task and that it is created correctly.
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void enterDetailsTest() throws IOException, InterruptedException {
        HttpClient client = logInUser();

        URI uri = URI.create(url+"/task/createTask");

        String testDisc = "NewTask";
        String testEst = "1";
        String testDev = "1";
        String testDep = "";
        String testRole = "JavaProgrammer";

        // Build the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("project=13"+"&desc="+testDisc+"&estimated="+testEst+"&deviation="+testDev+"&rolesNecessary="+testRole+"&dependsOn="+testDep))
                .build();


        // Send the HTTP request and get the response
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI test = URI.create(url+"/project/showProject?projectid=13");
        HttpRequest testRequest = HttpRequest
                .newBuilder()
                .uri(test)
                .header("Content-Type", "application/json")
                .GET()
                .build();
        var testResponse = client.send(testRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(302,response.statusCode());
        Assertions.assertTrue(response.headers().toString().contains("succesmessage=task%20has%20been%20added"));
        Assertions.assertTrue(testResponse.body().contains("NewTask"));
    }

    /**
     * Step 3a: Test if a user can leave the form without creating a task.
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void stopFormTest() throws IOException, InterruptedException {
        HttpClient client = logInUser();

        String testDisc = "NoTask";
        String testEst = "1";
        String testDev = "1";
        String testDep = "-1";
        String testRole = "JavaProgrammer";

        //Go to form
        URI uri = URI.create(url+"/task/taskForm?projectid=13");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //Leave Form
        URI test = URI.create(url+"/project/showProject?projectid=13");
        HttpRequest testRequest = HttpRequest
                .newBuilder()
                .uri(test)
                .header("Content-Type", "application/json")
                .GET()
                .build();
        var testResponse = client.send(testRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertFalse(testResponse.body().contains(testDisc));


    }

    /**
     * Step4a: Test if a user can enter wrong details for a task.
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void enterWrongDetailsTest() throws IOException, InterruptedException {
        HttpClient client = logInUser();

        URI uri = URI.create(url+"/task/createTask");

        String testDisc = "illegalTask";
        String testEst = "illegal";
        String testDev = "1";
        String testDep = "-1";
        String testRole = "JavaProgrammer";

        // Build the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("projectid=2"+"&desc="+testDisc+"&estimated="+testEst+"&deviation="+testDev+"&rolesNecessary="+testRole+"&dependsOn="+testDep))
                .build();


        // Send the HTTP request and get the response
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI test = URI.create(url+"/project/showProject?projectid=13");
        HttpRequest testRequest = HttpRequest
                .newBuilder()
                .uri(test)
                .header("Content-Type", "application/json")
                .GET()
                .build();
        var testResponse = client.send(testRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200,response.statusCode());
        Assertions.assertFalse(response.headers().toString().contains("succesmessage=task%20has%20been%20added"));
        Assertions.assertFalse(testResponse.body().contains(testDisc));
    }
}
