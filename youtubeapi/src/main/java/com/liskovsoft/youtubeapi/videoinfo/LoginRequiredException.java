package com.liskovsoft.youtubeapi.videoinfo;

/** A player API sign-in restriction that must not be retried as a network failure. */
public class LoginRequiredException extends RuntimeException {
    public LoginRequiredException(String reason) {
        super(reason);
    }
}
