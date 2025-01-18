package net.candorstudios.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.candorstudios.api.model.CandorRemoteConfig;
import net.candorstudios.api.model.CandorReview;
import net.candorstudios.api.utils.Requester;
import net.candorstudios.api.utils.Response;
import net.candorstudios.api.utils.Route;
import org.json.JSONArray;
import org.json.JSONObject;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Main class for accessing the Candor Studios API.
 * <br>To instantiate this class, you'll need to provide your public API key - which can be found on the <a href="https://candorstudios.net/freelancers/api-dashboard">API Dashboard</a> page.
 *
 * @author George
 * @version 1.0.0
 */
@Getter
@RequiredArgsConstructor
public class CandorApi {

    private final String publicApiKey;

    private final String CANDOR_STUDIOS_LICENSE_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA80AWjlgESmTlrqOfG7Rtyd30+IHryCkDf+phuFQuDI2AhEa1DnJ9xsz4gPYNOWzLxDz6cBBlpZ+4KgzyO/uKDJDjPgp4TTrTncdEuLgXr5zCKpuXU29pABncZrnZYxvFohB2BYRaWD+BGvSCCZfihcFcUZ67Kwy0rWeEFhew8w8uWc8Vg3nYtY6TYD09jK7eUD2dckcybzGbgypFsV5fZ4kScqelBBD9xqv3oWc8/wJJffPAdQeRrkQTaQMKM95bHOgpSckrRS5xc/NLlQ+DFPyXqZ4guvnkgS8UgKK4/7GGVQ4AdC11Z8y6hx5++1owBXAbsViCcQBY35g9hQyB9QIDAQAB";

    /**
     * Retrieves a remote config's values.
     *
     * @param id The ID of the config to retrieve.
     * @return A response containing the config's values.
     */
    @SuppressWarnings("unused")
    public Response<CandorRemoteConfig> retrieveConfig(String id) {
        Response<CandorRemoteConfig> response = new Response<>();
        new Thread(() -> {
            try {
                JSONObject res = new JSONObject(new Requester(Route.GET_CONFIG.setParam("id", id), null, this)
                        .invoke());

                response.complete(new CandorRemoteConfig(res));
            } catch (Response.CandorResponseError e) {
                response.completeError(e.getError());
            }
        }).start();
        return response;
    }


    /**
     * Verifies a license key using the API.
     * <p>This endpoint uses RSA signatures to verify the license key, ensuring that it is valid and not tampered with, which this method handles for you.
     * <p>You should be retrieving the licenseKey from a local config file or from remote configs so that the client can enter it. Then, run this method before
     * allowing the user to access the product (shutdown if it fails).
     *
     * @param licenseKey The license key to verify.
     * @param productId  The product ID to verify the license key for.
     * @return A response containing a boolean indicating whether the license key is valid or not. If the request fails, the response object will contain an error instead.
     */
    @SuppressWarnings("unused")
    public Response<Boolean> verifyLicense(String licenseKey, String productId) {
        Response<Boolean> response = new Response<>();
        new Thread(() -> {
            try {
                JSONObject res = new JSONObject(new Requester(Route.VERIFY_LICENSE.setParam("id", productId), new JSONObject().put("license", licenseKey), this)
                        .invoke());

                String license = res.getString("license");
                long timestamp = res.getLong("timestamp");
                boolean valid = res.getBoolean("valid");
                String signature = res.getString("signature");
                String product = res.getString("product");

                byte[] publicKeyBytes = Base64.getDecoder().decode(CANDOR_STUDIOS_LICENSE_PUBLIC_KEY);

                String licenseData = "license=" + license + "&valid=" + valid + "&timestamp=" + timestamp + "&product=" + product;

                // Verify the signature using the public key and java.security.Signature
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
                PublicKey publicKey = keyFactory.generatePublic(keySpec);

                Signature sig = Signature.getInstance("SHA256withRSA");
                sig.initVerify(publicKey);
                sig.update(licenseData.getBytes());

                if (!sig.verify(Base64.getDecoder().decode(signature))) {
                    response.complete(false);
                    return;
                }
                response.complete(valid);
            } catch (Response.CandorResponseError e) {
                response.completeError(e.getError());
            } catch (InvalidKeySpecException | NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
                response.completeError(new Response.CandorError(500, "An error occurred while verifying the license key."));
                e.printStackTrace();
            }
        }).start();
        return response;
    }

    /**
     * Retrieves reviews for a specific user.
     * @param yourId The ID of the user to retrieve reviews for.
     * @return A response containing a list of reviews for the user.
     */
    public Response<List<CandorReview>> retrieveReviews(String yourId) {
        Response<List<CandorReview>> response = new Response<>();
        new Thread(() -> {
            try {
                JSONArray res = new JSONArray(new Requester(Route.GET_REVIEWS.setParam("id", yourId), null, this)
                        .invoke());

                List<CandorReview> reviews = new ArrayList<>();
                for (int i = 0; i < res.length(); i++) {
                    reviews.add(new CandorReview(res.getJSONObject(i)));
                }

                response.complete(reviews);
            } catch (Response.CandorResponseError e) {
                response.completeError(e.getError());
            }
        }).start();
        return response;
    }


}
