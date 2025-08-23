package com.Suhayr.Finance.Tracker.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private long expirationIn;

    public LoginResponse(String token, long expirationIn) {
        this.expirationIn = expirationIn;
        this.token = token;
    }
}
