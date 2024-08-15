package com.norbertkoziana.Session.Authentication.auth;
import com.norbertkoziana.Session.Authentication.confirmation.Confirmation;
import com.norbertkoziana.Session.Authentication.confirmation.ConfirmationRepository;
import com.norbertkoziana.Session.Authentication.confirmation.ConfirmationService;
import com.norbertkoziana.Session.Authentication.data.TestDataUtil;
import com.norbertkoziana.Session.Authentication.email.ConfirmationEmailService;
import com.norbertkoziana.Session.Authentication.model.RegisterRequest;
import com.norbertkoziana.Session.Authentication.token.ConfirmationTokenGenerator;
import com.norbertkoziana.Session.Authentication.user.User;
import com.norbertkoziana.Session.Authentication.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceUnitTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConfirmationService confirmationService;

    @Mock
    private ConfirmationRepository confirmationRepository;

    @Mock
    private ConfirmationEmailService confirmationEmailService;

    @Mock
    private ConfirmationTokenGenerator confirmationTokenGenerator;

    private AuthServiceImpl underTest;

    @BeforeEach
    void setUp(){
        underTest = new AuthServiceImpl(authenticationManager, passwordEncoder, userRepository, confirmationService,
                confirmationRepository, confirmationEmailService, confirmationTokenGenerator);
    }

    @Test
    void testRegisterMethod() {
        //given
        RegisterRequest registerRequest = RegisterRequest.builder()
                .firstName("ABC")
                .lastName("DEF")
                .email("abcdef@gmail.com")
                .password("123456")
                .build();

        //mock
        String token = TestDataUtil.getTokenA();
        AuthServiceImpl spy = Mockito.spy(underTest);
        Mockito.doReturn(token).when(spy).createAndSaveConfirmation(any(User.class));

        //when
        spy.register(registerRequest);

        //then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertEquals(capturedUser.getFirstName(), registerRequest.getFirstName());
        assertEquals(capturedUser.getLastName(), registerRequest.getLastName());
        assertEquals(capturedUser.getEmail(), registerRequest.getEmail());

        verify(confirmationEmailService).sendConfirmationMail(registerRequest.getEmail(), token);
    }

    @Test
    void testCreateAndSaveConfirmation(){
        //given
        User user = TestDataUtil.getUserA();

        //mock
        String token = TestDataUtil.getTokenA();
        when(confirmationTokenGenerator.getConfirmationToken()).thenReturn(token);

        //when
        underTest.createAndSaveConfirmation(user);

        //then
        ArgumentCaptor<Confirmation> confirmationArgumentCaptor = ArgumentCaptor.forClass(Confirmation.class);
        verify(confirmationRepository).save(confirmationArgumentCaptor.capture());
        Confirmation capturedConfirmation = confirmationArgumentCaptor.getValue();
        assertEquals(capturedConfirmation.getToken(), token);
        assertEquals(capturedConfirmation.getUser(), user);
    }
}