package com.example.demo.controllerTest;

import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc(addFilters=false)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void testRegisterUserEndpoint() throws Exception {
        UserEntity userToRegister = new UserEntity();
        userToRegister.setUserName("John Doe");
        userToRegister.setEmail("john@example.com");
        userToRegister.setPassword("password");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/register")
                        .content(asJsonString(userToRegister))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john@example.com"));
    }

    @Test
    public void testLoginUserEndpoint() throws Exception {
        UserEntity testUser = new UserEntity();
        testUser.setEmail("john@example.com");
        testUser.setPassword("password");
        userRepository.save(testUser);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/login")
                        .param("email", "john@example.com")
                        .param("password", "password"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john@example.com"));
    }

    @Test
    public void testLoginUserWithInvalidCredentials() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/login")
                        .param("email", "unknown@example.com")
                        .param("password", "invalid"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "john@example.com")
    public void testGetAuthenticatedUserEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/auth/me")
                        .header("Authorization", "Bearer your-jwt-token-here")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john@example.com"));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}