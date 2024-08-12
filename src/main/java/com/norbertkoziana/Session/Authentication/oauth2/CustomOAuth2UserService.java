package com.norbertkoziana.Session.Authentication.oauth2;
import com.norbertkoziana.Session.Authentication.user.Role;
import com.norbertkoziana.Session.Authentication.user.User;
import com.norbertkoziana.Session.Authentication.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User =  super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String email = oAuth2User.getAttribute("email");

        User user = userRepository.findByEmail(email)
                .orElseGet(
                        () -> {
                            return userRepository.save(readUserAttributesByProvider(oAuth2User, registrationId));
                        }
                );

        if(!user.getEnabled()) //when user registered locally, but didn't confirm his email overwrite account
            userRepository.save(readUserAttributesByProvider(oAuth2User, registrationId));

        if(user.getLocked())
            throw new OAuth2AuthenticationException(new OAuth2Error("ACCESS_DENIED", "Your account is locked!", ""));

        return user;
    }

    private User readUserAttributesByProvider(OAuth2User oAuth2User, String registrationId){
        if(registrationId.equals("google")){
            return User.builder()
                    .firstName(oAuth2User.getAttribute("given_name"))
                    .lastName(oAuth2User.getAttribute("family_name"))
                    .email(oAuth2User.getAttribute("email"))
                    .locked(false)
                    .enabled(true)
                    .role(Role.User)
                    .build();
        } else if (registrationId.equals("facebook")) {
            return User.builder()
                    .firstName(oAuth2User.getAttribute("first_name"))
                    .lastName(oAuth2User.getAttribute("last_name"))
                    .email(oAuth2User.getAttribute("email"))
                    .locked(false)
                    .enabled(true)
                    .role(Role.User)
                    .build();
        }

        throw new IllegalStateException("Unrecognized provider");
    }
}