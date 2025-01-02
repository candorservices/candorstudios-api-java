package net.candorstudios.api.utils;

import net.candorstudios.api.CandorApi;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * This class is used to make requests to the Candor API.
 *
 * @version 1.0.0
 * @author Seailz
 */
public class Requester {

    private final Route route;
    private final JSONObject body;
    private final CandorApi candor;

    public Requester(Route route, JSONObject body, CandorApi candor) {
        this.route = route;
        this.body = body;
        this.candor = candor;
    }

    public String invoke() throws Response.CandorResponseError {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(route.getRequestUrl()))
                .header("Content-Type", "application/json")
                .header("Authorization", candor.getPublicApiKey());

        if (body != null) {
            requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body.toString()));
        } else {
            requestBuilder.GET();
        }

        HttpRequest request = requestBuilder.build();

        HttpResponse<String> res;
        try {
            res = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new Response.CandorResponseError(new Response.CandorError(-1, "An error occurred while sending the request."));
        }

        String body = res.body();
        int code = res.statusCode();

        if (code != 200) {
            throw new Response.CandorResponseError(new Response.CandorError(new JSONObject(body)));
        } else {
            return body;
        }
    }


}
