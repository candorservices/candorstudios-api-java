package net.candorstudios.api.model;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * Represents a review in the Candor Studios API. These are submitted at the end of orders.
 * <p>Through the API, you can retrieve your own reviews to display on your website or in your application.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @see CandorUser
 * @author George
 */
@Data
public class CandorReview {

    private final int rating;
    private final String review;
    private final CandorUser reviewer;
    private final CandorUser reviewed;

    public CandorReview(@NotNull JSONObject object) {
        this.rating = object.getInt("rating");
        this.review = object.getString("review");
        this.reviewer = new CandorUser(object.getJSONObject("reviewer"));
        this.reviewed = new CandorUser(object.getJSONObject("reviewed"));
    }

}
