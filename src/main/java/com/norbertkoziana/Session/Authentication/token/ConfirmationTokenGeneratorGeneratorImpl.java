package com.norbertkoziana.Session.Authentication.token;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
public class ConfirmationTokenGeneratorGeneratorImpl implements ConfirmationTokenGenerator {
    @Override
    public String getConfirmationToken() {
        return UUID.randomUUID().toString();
    }
}
