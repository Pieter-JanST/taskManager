package user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;


public class UserUITest {

    private final String url = "http://localhost:8080";
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

}
