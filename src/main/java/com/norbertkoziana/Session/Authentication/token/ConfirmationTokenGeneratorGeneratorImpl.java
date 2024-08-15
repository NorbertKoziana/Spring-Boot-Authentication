package com.norbertkoziana.Session.Authentication.token;
import java.util.UUID;
public class ConfirmationTokenGeneratorGeneratorImpl implements ConfirmationTokenGenerator {
    @Override
    public String getConfirmationToken() {
        return UUID.randomUUID().toString();
    }
}
