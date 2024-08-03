package com.norbertkoziana.Session.Authentication.oauth2;
import com.norbertkoziana.Session.Authentication.user.Role;
import com.norbertkoziana.Session.Authentication.user.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
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

        //To test banning certain users; will remove later
        if(user.getEmail().equals("norbi29014@gmail.com"))
            throw new OAuth2AuthenticationException(new OAuth2Error("ACCESS_DENIED", "Your account is locked!", ""));

        return user;
    }
}

//TODO

//if user email already in database, then tell user to login using email and password

//if user enabled == false throw exception (also read user enabled field correctly) (allow to login only users with confirmed emails)

//if you want to ban user, create his account in database (with random data but correct email and locked = true), then he will be forced to
//login using password and email, and then the field locked = true will be checked; this way we dont have to implement seperate blacklist
//and we are querying users only once instead of 2 times

//https://spring.io/guides/tutorials/spring-boot-oauth2#_social_login_custom_error