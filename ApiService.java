import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ApiService {
    private final String baseUrl;
    private final String chatBotName;
    private final HttpClient httpClient;

    public ApiService(ConfigManager configManager) {
        this.baseUrl = configManager.getServerUrl();
        this.chatBotName = configManager.getChatBotName();
        this.httpClient = HttpClient.newHttpClient();
    }

    public CompletableFuture<String> createPost(BlogPost post) {
        String jsonBody = String.format("{\"title\":\"%s\",\"content\":\"%s\",\"author\":\"%s\"}",
                post.getTitle(), post.getContent(), post.getAuthor());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "?api=blogs"))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(jsonBody))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    switch (response.statusCode()) {
                        case 201: return chatBotName + ": Post created successfully!";
                        case 400: return chatBotName + ": Failed to create post (validation/limit error)";
                        case 500: return chatBotName + ": Server error occurred";
                        default: return chatBotName + ": Unexpected response: " + response.statusCode();
                    }
                });
    }

    public CompletableFuture<List<BlogPost>> getAllPosts() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "?api=blogs"))
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() == 200) {
                        // In a real implementation, parse the JSON response
                        // This is a simplified version
                        return List.of(); // Return empty list for demonstration
                    } else {
                        System.err.println(chatBotName + ": Failed to fetch posts. Status: " + response.statusCode());
                        return List.of();
                    }
                });
    }

    public CompletableFuture<String> updatePost(int id, BlogPost post) {
        String jsonBody = String.format("{\"title\":\"%s\",\"content\":\"%s\",\"author\":\"%s\"}",
                post.getTitle(), post.getContent(), post.getAuthor());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "?api=blogs&action=item&id=" + id))
                .header("Content-Type", "application/json")
                .PUT(BodyPublishers.ofString(jsonBody))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    switch (response.statusCode()) {
                        case 200: return chatBotName + ": Post updated successfully!";
                        case 400: return chatBotName + ": Validation error";
                        case 404: return chatBotName + ": Post not found";
                        default: return chatBotName + ": Unexpected response: " + response.statusCode();
                    }
                });
    }

    public CompletableFuture<String> deletePost(int id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "?api=blogs&action=item&id=" + id))
                .DELETE()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    switch (response.statusCode()) {
                        case 200: return chatBotName + ": Post deleted successfully!";
                        case 404: return chatBotName + ": Post not found";
                        default: return chatBotName + ": Unexpected response: " + response.statusCode();
                    }
                });
    }

    public CompletableFuture<StatsResponse> getStatistics() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "?api=stats"))
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() == 200) {
                        // Parse the actual response in a real implementation
                        return new StatsResponse(0, 100, 100, 0, true);
                    } else {
                        System.err.println(chatBotName + ": Failed to fetch stats. Status: " + response.statusCode());
                        return new StatsResponse(0, 0, 0, 0, false);
                    }
                });
    }
}