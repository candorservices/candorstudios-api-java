package net.candorstudios.api.model;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * Represents a user in the Candor Studios API.
 * <p>CandorUser objects represent users interacting with Candor Studios’ platform, whether through Discord or the web. Every user who has engaged with Candor Studios has a corresponding CandorUser object.
 */
@Data
public class CandorUser {

    private final String id;
    private final String username;
    private final String avatarUrl;
    private final CandorFreelancerProfile freelancerProfile;
    private final CandorUserType type;
    private final String bio;
    private final String contactEmail;

    public CandorUser(@NotNull JSONObject json) {
        this.id = json.getString("id");
        this.username = json.getString("username");
        this.avatarUrl = json.optString("avatar_url");
        this.freelancerProfile = json.has("freelancer_profile") ? new CandorFreelancerProfile(json.getJSONObject("freelancer_profile")) : null;
        this.type = CandorUserType.fromCode(json.optInt("type", 1));
        this.bio = json.optString("bio");
        this.contactEmail = json.optString("contact_email");
    }


    /**
     * Represents a freelancer's profile details.
     */
    @Data
    public static class CandorFreelancerProfile {
        private String portfolioUrl;
        private String timezone;
        private float averageRating;

        public CandorFreelancerProfile(@NotNull JSONObject json) {
            this.portfolioUrl = json.optString("portfolio");
            this.timezone = json.optString("timezone");
            this.averageRating = json.getFloat("average_rating");
        }
    }

    public enum CandorUserType {
        NATIVE,
        DISCORD;

        public static CandorUserType fromCode(int code) {
            for (CandorUserType type : values()) {
                if (type.ordinal() == code) {
                    return type;
                }
            }
            return DISCORD;
        }
    }


}
