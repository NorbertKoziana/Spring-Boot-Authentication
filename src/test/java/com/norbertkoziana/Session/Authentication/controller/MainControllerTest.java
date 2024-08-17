package com.norbertkoziana.Session.Authentication.controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {

    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    void ShouldAccessPublicEndpoint() throws Exception {
        mockMvc.perform(
                get("/")
        ).andExpect(status().isOk()
        ).andExpect(content().string("Hello World!"));
    }

    @Test
    void ShouldNotAccessSecuredEndpointWithoutLoggingIn() throws Exception {
        mockMvc.perform(
                get("/private")
        ).andExpect(redirectedUrl("http://localhost/auth/login"));;
    }

    @Test
    @WithMockUser
    void ShouldAccessSecuredEndpointAfterLoggingIn() throws Exception {
        mockMvc.perform(
                get("/private")
        ).andExpect(status().isOk()
        ).andExpect(content().string("Hello logged in users!"));
    }

    @Test
    @WithMockUser(authorities = {"User"})
    void ShouldNotAccessAdminEndpointWithUserAuthority() throws Exception {
        mockMvc.perform(
                get("/admin")
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"Admin"})
    void ShouldAccessAdminEndpointWithAdminAuthority() throws Exception {
        mockMvc.perform(
                get("/admin")
        ).andExpect(status().isOk()
        ).andExpect(content().string("Hello logged in admins!"));
    }
}