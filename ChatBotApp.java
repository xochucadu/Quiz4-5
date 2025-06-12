import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class ChatBotApp {
    private final ApiService apiService;
    private final String chatBotName;
    private final Scanner scanner;

    public ChatBotApp(ConfigManager configManager) {
        this.apiService = new ApiService(configManager);
        this.chatBotName = configManager.getChatBotName();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println(chatBotName + ": Hello! How can I help you today?");
        printMenu();

        while (true) {
            System.out.print("\nYour command: ");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "create":
                    createPost();
                    break;
                case "view":
                    viewPosts();
                    break;
                case "update":
                    updatePost();
                    break;
                case "delete":
                    deletePost();
                    break;
                case "stats":
                    viewStats();
                    break;
                case "help":
                    printMenu();
                    break;
                case "exit":
                    System.out.println(chatBotName + ": Goodbye!");
                    return;
                default:
                    System.out.println(chatBotName + ": Unknown command. Type 'help' to see available commands.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\nAvailable commands:");
        System.out.println("create  - Create a new blog post");
        System.out.println("view    - View all blog posts");
        System.out.println("update  - Update a blog post");
        System.out.println("delete  - Delete a blog post");
        System.out.println("stats   - View website statistics");
        System.out.println("help    - Show this help menu");
        System.out.println("exit    - Exit the application");
    }

    private void createPost() {
        System.out.println("\n" + chatBotName + ": Let's create a new blog post!");
        System.out.print("Enter post title: ");
        String title = scanner.nextLine();

        System.out.print("Enter post content: ");
        String content = scanner.nextLine();

        System.out.print("Enter your name (author): ");
        String author = scanner.nextLine();

        BlogPost post = new BlogPost(title, content, author);
        CompletableFuture<String> result = apiService.createPost(post);
        result.thenAccept(System.out::println);
    }

    private void viewPosts() {
        System.out.println("\n" + chatBotName + ": Fetching all blog posts...");
        CompletableFuture<List<BlogPost>> postsFuture = apiService.getAllPosts();
        postsFuture.thenAccept(posts -> {
            if (posts.isEmpty()) {
                System.out.println(chatBotName + ": No posts found.");
            } else {
                System.out.println(chatBotName + ": Here are all the blog posts:");
                posts.forEach(post -> {
                    System.out.println(post);
                    System.out.println("---------------------");
                });
            }
        });
    }

    private void updatePost() {
        System.out.println("\n" + chatBotName + ": Let's update a blog post!");
        System.out.print("Enter post ID to update: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter new title: ");
        String title = scanner.nextLine();

        System.out.print("Enter new content: ");
        String content = scanner.nextLine();

        System.out.print("Enter new author name: ");
        String author = scanner.nextLine();

        BlogPost post = new BlogPost(title, content, author);
        CompletableFuture<String> result = apiService.updatePost(id, post);
        result.thenAccept(System.out::println);
    }

    private void deletePost() {
        System.out.println("\n" + chatBotName + ": Let's delete a blog post!");
        System.out.print("Enter post ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());

        CompletableFuture<String> result = apiService.deletePost(id);
        result.thenAccept(System.out::println);
    }

    private void viewStats() {
        System.out.println("\n" + chatBotName + ": Fetching website statistics...");
        CompletableFuture<StatsResponse> statsFuture = apiService.getStatistics();
        statsFuture.thenAccept(stats -> {
            System.out.println(chatBotName + ": " + stats);
        });
    }

    public static void main(String[] args) {
        ConfigManager configManager = new ConfigManager();
        ChatBotApp chatBot = new ChatBotApp(configManager);
        chatBot.start();
    }
}