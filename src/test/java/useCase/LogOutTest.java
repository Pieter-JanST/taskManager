package useCase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class LogOutTest {
    private final String url = "http://localhost:8080";
    /**
     * Step1&2: Test if a logged in user is able to log out, and is shown a confirmation message.
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void LogOutUserTest() throws IOException, InterruptedException {

        //Log in the user

        URI uriLogIn = URI.create(url+"/user/loginUser?username=user&password=t");
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest requestLogIn = HttpRequest
                .newBuilder()
                .uri(uriLogIn)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
        var responseLogIn = client.send(requestLogIn, HttpResponse.BodyHandlers.ofString());


        URI uri = URI.create(url + "/user/logoutUser");
        HttpRequest requestLogOut = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
        var responseLogOut = client.send(requestLogOut, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestWebPage = HttpRequest
                .newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        var responseWeb = client.send(requestWebPage, HttpResponse.BodyHandlers.ofString());


        Assertions.assertEquals(200, responseLogOut.statusCode());
        Assertions.assertTrue(responseLogOut.body().contains("href=\"/user/loginPage\"")); //User has the option to log in again
        Assertions.assertTrue(responseWeb.body().contains("alert alert-success")); //User get the success message
    }
}
