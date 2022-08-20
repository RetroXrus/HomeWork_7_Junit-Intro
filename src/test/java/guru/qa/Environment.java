package guru.qa;

public enum Environment {
    COM("https://vk.com"),
    RU("https://vk.ru");

    private final String url;

    Environment(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
