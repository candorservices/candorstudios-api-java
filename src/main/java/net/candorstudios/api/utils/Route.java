package net.candorstudios.api.utils;

public class Route {

    private String url;
    private final Method method;

    public Route(String url, Method method) {
        this.url = url;
        this.method = method;
    }

    public Route setParam(String key, String value) {
        this.url = this.url.replace(":" + key, value);
        return this;
    }

    public String getRequestUrl() {
        return BASE_URL + url;
    }

    @SuppressWarnings("unused")
    public Method getMethod() {
        return method;
    }

    public enum Method {GET, POST}


    public final static String BASE_URL = "https://api.candorstudios.net/api";

    public final static Route VERIFY_LICENSE = new Route("/products/:id/verify-license", Route.Method.POST);
    public final static Route GET_CONFIG = new Route("/configs/:id/retrieve-values", Route.Method.GET);
    public final static Route GET_REVIEWS = new Route("/reviews/freelancer/:id", Route.Method.GET);
}
