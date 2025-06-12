public class StatsResponse {
    private int totalPosts;
    private int maxPosts;
    private int remainingPosts;
    private int percentageUsed;
    private boolean canAddMore;

    public StatsResponse(int totalPosts, int maxPosts, int remainingPosts,
                         int percentageUsed, boolean canAddMore) {
        this.totalPosts = totalPosts;
        this.maxPosts = maxPosts;
        this.remainingPosts = remainingPosts;
        this.percentageUsed = percentageUsed;
        this.canAddMore = canAddMore;
    }

    @Override
    public String toString() {
        return String.format(
                "Website Statistics:\n" +
                        "Total Posts: %d\n" +
                        "Max Posts Allowed: %d\n" +
                        "Remaining Posts: %d\n" +
                        "Percentage Used: %d%%\n" +
                        "Can Add More: %s\n",
                totalPosts, maxPosts, remainingPosts, percentageUsed, canAddMore
        );
    }
}