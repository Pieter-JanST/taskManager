package useCase;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.CookieManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;

public class UndoRedoTest {
    private final String url = "http://localhost:8080";

    /**
     * Log in a user with predefined projects and tasks
     * @throws IOException
     * @throws InterruptedException
     */
    public HttpClient logInUser(String userName, String pass) throws IOException, InterruptedException {
        URI uri = URI.create(url+"/user/loginUser?username="+userName+"&password="+pass);
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


    public HttpClient createTask(HttpClient client,String taskName ) throws IOException, InterruptedException {

        URI uri = URI.create(url+"/task/createTask");

        String testDisc = taskName;
        String testEst = "1";
        String testDev = "1";
        String testDep = "";//-1
        String testRole = "JavaProgrammer";

        // Build the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("project=13"+"&desc="+testDisc+"&estimated="+testEst+"&deviation="+testDev+"&rolesNecessary="+testRole+"&dependsOn="+testDep))
                .build();


        // Send the HTTP request and get the response
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return client;
    }

    public HttpClient undoTaskRequest(HttpClient client ) throws IOException, InterruptedException {

        URI uri = URI.create(url+"/task/undo");

        // Build the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        // Send the HTTP request and get the response
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return client;
    }

    @Test
    public void actionPageTest() throws IOException, InterruptedException {
        HttpClient client = logInUser("user","t");

        URI uri = URI.create(url+"/action/actionPage");
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

        Assertions.assertTrue(response.body().contains("Action Overview"));
    }

    @Test
    public void UndoAction() throws IOException, InterruptedException {
        HttpClient client = logInUser("user","t");
        String taskName = "Task"+Timestamp.from(Instant.now());
        createTask(client, taskName);

        URI uri = URI.create(url+"/action/actionPage");
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
        Assertions.assertTrue(response.body().contains("added Task "+taskName));



        // Build the HTTP request
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create(url+"/action/undo"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        // Send the HTTP request and get the response
        var response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(response2.statusCode(),302);


        HttpRequest testRequest = HttpRequest
                .newBuilder()
                .uri(URI.create(url+"/task/taskOverview"))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        var testResponse = client.send(testRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertFalse(testResponse.body().contains(taskName));
    }

    @Test
    public void redoAction() throws IOException, InterruptedException {
        HttpClient client = logInUser("user","t");
        String taskName = "Task"+Timestamp.from(Instant.now());
        createTask(client, taskName);

        URI uri = URI.create(url+"/action/actionPage");
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
        Assertions.assertTrue(response.body().contains("added Task "+taskName));



        // Build the HTTP request
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create(url+"/action/undo"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        // Send the HTTP request and get the response
        var response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(response2.statusCode(),302);


        HttpRequest testRequest = HttpRequest
                .newBuilder()
                .uri(URI.create(url+"/task/taskOverview"))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        var testResponse = client.send(testRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertFalse(testResponse.body().contains(taskName));

        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(URI.create(url+"/action/redo"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
        var response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(response3.statusCode(),302);

        HttpRequest request4 = HttpRequest
                .newBuilder()
                .uri(URI.create(url+"/task/taskOverview"))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        var response4 = client.send(testRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertTrue(response4.body().contains(taskName));

    }
}

