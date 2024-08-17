package com.norbertkoziana.Session.Authentication.auth;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.norbertkoziana.Session.Authentication.data.TestDataUtil;
import com.norbertkoziana.Session.Authentication.mapper.Mapper;
import com.norbertkoziana.Session.Authentication.model.LoginRequest;
import com.norbertkoziana.Session.Authentication.model.UserDto;
import com.norbertkoziana.Session.Authentication.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testLoginSuccess() throws Exception {
        //given
        LoginRequest loginRequest = TestDataUtil.getLoginRequestA();
        String loginRequestJSON = objectMapper.writeValueAsString(loginRequest);

        //when and then
        mockMvc.perform(
                    post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJSON)
                        .with(csrf())
        ).andExpect(status().isOk());
    }

    @Test
    void testLoginFailsOnBadCsrf() throws Exception {
        //given
        LoginRequest loginRequest = TestDataUtil.getLoginRequestA();
        String loginRequestJSON = objectMapper.writeValueAsString(loginRequest);

        //when and then
        mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJSON)
                        .with(csrf().useInvalidToken())
        ).andExpect(status().isForbidden());
    }

    @Test
    void testLoginFailsOnBadCredentials() throws Exception {
        //given
        LoginRequest loginRequest = TestDataUtil.getLoginRequestA();
        String loginRequestJSON = objectMapper.writeValueAsString(loginRequest);

        //mock
        doThrow(BadCredentialsException.class)
                .when(authService).login(any(LoginRequest.class), any(HttpServletRequest.class), any(HttpServletResponse.class));

        //when and then
        mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJSON)
                        .with(csrf())
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testLoginFailsWhenUserAlreadyAuthenticated() throws Exception {
        //given
        LoginRequest loginRequest = TestDataUtil.getLoginRequestA();
        String loginRequestJSON = objectMapper.writeValueAsString(loginRequest);

        //when and then
        mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJSON)
                        .with(csrf())
        ).andExpect(status().isForbidden());
    }
}