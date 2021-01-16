package main;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.api.request.LoginRequest;
import main.controller.ApiAuthController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApiAuthTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ApiAuthController authController;

  @Test
  public void loginTest() throws Exception {
    mockMvc.perform(post("/api/auth/login")
        .content(objectMapper.writeValueAsString(
            new LoginRequest("vadik@mail.com", "password")))
        .contentType(MediaType.APPLICATION_JSON))
    .andExpect(status().isOk())
    .andExpect(authenticated());

    mockMvc.perform(post("/api/auth/login")
        .content(objectMapper.writeValueAsString(
            new LoginRequest("ne_vadik@mail.com", "wrong_password")))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(unauthenticated());
  }

  @Test @WithUserDetails("vadik@mail.com")
  public void logoutTest() throws Exception {
    mockMvc.perform(get("/api/auth/logout"))
        .andExpect(status().isOk())
        .andExpect(unauthenticated());
  }

  @Test @WithUserDetails("vadik@mail.com")
  public void checkTest() throws Exception {
    mockMvc.perform(get("/api/auth/check"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.user.email").value("vadik@mail.com"));
  }

  @Test
  public void badCheckTest() throws Exception {
    mockMvc.perform(get("/api/auth/check"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.result").value(false));
  }

  /*

  /register
  /captcha
  /restore

   */
}
