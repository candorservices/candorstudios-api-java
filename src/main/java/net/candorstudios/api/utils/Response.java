package net.candorstudios.api.utils;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Represents a response from the Candor Studios API and allows for asynchronous or synchronous handling of the response.
 *
 * @author George
 * @since 1.0.0
 */
public class Response<T> {

    private final CompletableFuture<T> responseFuture = new CompletableFuture<>();
    private final CompletableFuture<CandorError> errorFuture = new CompletableFuture<>();
    private boolean throwOnError = false;

    public Response() {}

    public T awaitCompleted() {
        return responseFuture.join();
    }

    public CandorError awaitError() {
        return errorFuture.join();
    }

    public CandorError getError() {
        return errorFuture.getNow(null);
    }

    public T getResponse() {
        return responseFuture.getNow(null);
    }

    /**
     * Tells the class to throw a runtime exception if an error is received.
     */
    public Response<T> throwOnError() {
        throwOnError = true;
        return this;
    }

    public Response<T> complete(T response) {
        responseFuture.complete(response);
        return this;
    }

    public Response<T> completeError(CandorError error) {
        errorFuture.complete(error);
        if (throwOnError) {
            throw new CandorResponseError(error);
        }
        return this;
    }

    public Response<T> onCompletion(Consumer<T> consumer) {
        responseFuture.thenAccept(consumer);
        return this;
    }

    public Response<T> onError(Consumer<CandorError> consumer) {
        errorFuture.thenAccept(consumer);
        return this;
    }

    public Response<T> completeAsync(Supplier<T> response) {
        CompletableFuture.supplyAsync(response).thenAccept(this::complete);
        return this;
    }

    @Getter
    public static class CandorResponseError extends RuntimeException {
        private final CandorError error;

        public CandorResponseError(CandorError error) {
            super(error.getMessage());
            this.error = error;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class CandorError extends Throwable {
        private final int code;
        private final String message;

        public CandorError(@Nullable JSONObject err) {
            if (err != null) {
                code = err.optInt("code", -1);
                message = err.optString("message", "Unknown error");
            } else {
                code = -1;
                message = "Unknown error";
            }
        }
    }
}