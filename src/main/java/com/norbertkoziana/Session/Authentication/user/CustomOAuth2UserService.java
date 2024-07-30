package com.norbertkoziana.Session.Authentication.user;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User =  super.loadUser(userRequest);

        User user = null;
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        if(registrationId.equals("google")){
            user = User.builder()
                    .firstName(oAuth2User.getAttribute("given_name"))
                    .lastName(oAuth2User.getAttribute("family_name"))
                    .email(oAuth2User.getAttribute("email"))
                    .locked(false)
                    .enabled(true)
                    .role(Role.User)
                    .build();
        } else if (registrationId.equals("facebook")) {
            user = User.builder()
                    .firstName(oAuth2User.getAttribute("first_name"))
                    .lastName(oAuth2User.getAttribute("last_name"))
                    .email(oAuth2User.getAttribute("email"))
                    .locked(false)
                    .enabled(true)
                    .role(Role.User)
                    .build();
        }else{
            throw new IllegalStateException("Unrecognized provider");
        }

        return user;
    }
}
