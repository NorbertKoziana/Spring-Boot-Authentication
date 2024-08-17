package com.norbertkoziana.Session.Authentication.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.norbertkoziana.Session.Authentication.data.TestDataUtil;
import com.norbertkoziana.Session.Authentication.email.ChangePasswordEmailService;
import com.norbertkoziana.Session.Authentication.email.ConfirmationEmailService;
import com.norbertkoziana.Session.Authentication.model.LoginRequest;
import com.norbertkoziana.Session.Authentication.model.RegisterRequest;
import com.norbertkoziana.Session.Authentication.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CompleteLocalAuthenticationTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private ConfirmationEmailService confirmationEmailService;

    @MockBean
    private ChangePasswordEmailService changePasswordEmailService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    void ShouldNotLogInIntoAccountThatDoesNotExist() throws Exception {
        //given
        LoginRequest loginRequest = TestDataUtil.getLoginRequestA();
        String loginRequestJSON = objectMapper.writeValueAsString(loginRequest);

        //when and then
        mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJSON)
                        .with(csrf())
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void ShouldNotLogInIntoAccountThatDidNotConfirmEmailAddressTest() throws Exception {
        //given
        User user = TestDataUtil.getUserA();

        LoginRequest loginRequest = TestDataUtil.getLoginRequestA();
        String loginRequestJSON = objectMapper.writeValueAsString(loginRequest);

        RegisterRequest registerRequest = TestDataUtil.getRegisterRequestA();
        String registerRequestJSON = objectMapper.writeValueAsString(registerRequest);

        //when and then
        mockMvc.perform(
                post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequestJSON)
                        .with(csrf())
        ).andExpect(status().isCreated());

        mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJSON)
                        .with(csrf())
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void ShouldLogInIntoAccountAfterCorrectRegistration() throws Exception {
        //given
        LoginRequest loginRequest = TestDataUtil.getLoginRequestA();
        String loginRequestJSON = objectMapper.writeValueAsString(loginRequest);

        RegisterRequest registerRequest = TestDataUtil.getRegisterRequestA();
        String registerRequestJSON = objectMapper.writeValueAsString(registerRequest);

        //when and then
        mockMvc.perform(
                post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequestJSON)
                        .with(csrf())
        ).andExpect(status().isCreated());

        //Catch sent token instead of sending email
        ArgumentCaptor<String> tokenArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(confirmationEmailService).sendConfirmationMail(any(), tokenArgumentCaptor.capture());
        String capturedToken = tokenArgumentCaptor.getValue();

        mockMvc.perform(
                get("/auth/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("token", capturedToken)
        ).andExpect(status().isOk())
        .andExpect(content().string("Your account is now active"));

        mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJSON)
                        .with(csrf())
        ).andExpect(status().isOk()
        ).andExpect(authenticated().withAuthenticationName(registerRequest.getEmail()));
    }

}
