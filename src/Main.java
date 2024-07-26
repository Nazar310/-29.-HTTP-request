import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ApiClient apiClient = new ApiClient();

        try {
            List<Post> posts = apiClient.getPosts();
            posts.forEach(System.out::println);
        } catch (IOException e) {
            System.err.println("Не вдалось отримати пост: " + e.getMessage());
            e.printStackTrace();
        }

        Post newPost = new Post(101, 0, "Заголовок", "Це тіло поста.");
        try {
            Post createdPost = apiClient.createPost(newPost);
            System.out.println("Створити пост: " + createdPost);
        } catch (IOException e) {
            System.err.println("Не вдалось створити пост: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
