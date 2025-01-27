package useCase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;


public class LogInTest {

    private final String url = "http://localhost:8080";
    /**
     * Test if  the user is shown the home page.
     */
    @Test
    public void homeTest() throws IOException, InterruptedException {

        URI uri = URI.create(url);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .header("accept", "application/json")
                .GET()
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200,response.statusCode());
        Assertions.assertTrue(response.body().toString().contains("<title>Home</title>"));


    }

    /**
     * Step1: Test if user is shown the login page if requested.
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void loginPageTest() throws IOException, InterruptedException {
        URI uri = URI.create(url+"/user/loginPage");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .header("accept", "application/json")
                .GET()
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200,response.statusCode());
        Assertions.assertTrue(response.body().toString().contains("<title>Login</title>"));
    }

    /**
     * Step 2&3: Test if user is able to log in with correct credentials.
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void LoginUserTest() throws IOException, InterruptedException {
        URI uri = URI.create(url+"/user/loginUser?username=user&password=t");
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(302,response.statusCode());
        Assertions.assertTrue(response.headers().toString().contains("location=[/?succesmessage=succesfully%20logged%20in]"));
    }

    /**
     * Step 3a: Test if the user can't log in when presenting wrong credentials, and is shown an error message.
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void LoginUserFailTest() throws IOException, InterruptedException {
        URI uri = URI.create(url+"/user/loginUser?username=false&password=false");
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestWebPage = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        var responseWeb = client.send(requestWebPage, HttpResponse.BodyHandlers.ofString());


        Assertions.assertEquals(200,response.statusCode());
        Assertions.assertFalse(response.headers().toString().contains("location=[/?succesmessage=succesfully%20logged%20in]"));
        Assertions.assertTrue(responseWeb.body().contains("alert alert-danger"));
    }

}