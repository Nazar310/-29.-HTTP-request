import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ApiClient {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Post> getPosts() throws IOException {
        URL url = new URL(BASE_URL + "/posts");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int statusCode = connection.getResponseCode();
        if (statusCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Запит GET не вдалося виконати. Код статусу: " + statusCode);
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            return objectMapper.readValue(content.toString(), new TypeReference<List<Post>>() {});
        } finally {
            connection.disconnect();
        }
    }

    public Post createPost(Post post) throws IOException {
        URL url = new URL(BASE_URL + "/posts");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        String jsonInputString = objectMapper.writeValueAsString(post);
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int statusCode = connection.getResponseCode();
        if (statusCode != HttpURLConnection.HTTP_CREATED) {
            throw new IOException("Помилка запиту POST з кодом статусу: " + statusCode);
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine.trim());
            }
            return objectMapper.readValue(response.toString(), Post.class);
        } finally {
            connection.disconnect();
        }
    }
}
