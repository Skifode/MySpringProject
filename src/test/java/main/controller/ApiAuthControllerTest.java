package main.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.api.request.ChangePasswordRequest;
import main.api.request.LoginRequest;
import main.api.request.RegisterRequest;
import main.api.request.RestorePasswordRequest;
import main.api.response.CaptchaResponse;
import main.api.response.LoginResponse;
import main.api.response.ResultErrorsResponse;
import main.api.response.UserLoginResponse;
import main.service.CaptchaService;
import main.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ApiAuthControllerTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CaptchaService captchaService;

  @MockBean
  private UserService userService;

  @Test @WithUserDetails("vadik@mail.com")
  void check() throws Exception {

    when(userService.getLoginResponse("vadik@mail.com"))
        .thenReturn(LoginResponse.builder().result(true)
            .userLoginResponse(
                UserLoginResponse
                    .builder()
                    .name("success")
                    .build())
            .build());

    mockMvc.perform(get("/api/auth/check"))
        .andExpect(status().isOk())
        .andExpect(authenticated())
        .andExpect(jsonPath("$.user.name").value("success"));
  }

  @Test @WithUserDetails("vadik@mail.com")
  void logout() throws Exception {
    mockMvc.perform(get("/api/auth/logout"))
        .andExpect(status().isOk())
        .andExpect(unauthenticated());
  }

  @Test
  void login() throws Exception {
    LoginRequest request = new LoginRequest("email", "password");

    when(userService.getAuth(request))
        .thenReturn(
            LoginResponse.builder()
                .userLoginResponse(
                    UserLoginResponse
                        .builder()
                        .name("success")
                        .build())
                .build());

    mockMvc.perform(post("/api/auth/login")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.user.name").value("success"));

  }

  @Test
  void register() throws Exception {

    String email = "testMai@mail.com";
    String password = "password";
    String name = "name";
    String captcha = "captcha";
    String captchaSecret = "secret";

    RegisterRequest request = new RegisterRequest(email, password, name, captcha, captchaSecret);

    when(userService.getRegisterResponse(request))
        .thenReturn(ResultErrorsResponse.builder().result(true).build());

    mockMvc.perform(post("/api/auth/register")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result").value(true));

  }

  @Test
  void captcha() throws Exception {

    when(captchaService.getResponse())
        .thenReturn(CaptchaResponse.builder()
            .image("image")
            .secret("secret")
            .build());

    mockMvc.perform(get("/api/auth/captcha"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.image").value("image"))
        .andExpect(jsonPath("$.secret").value("secret"));
  }

  @Test
  void restorePassword() throws Exception {
    when(userService.restorePassword(anyString()))
        .thenReturn(new ResponseEntity(
            ResultErrorsResponse.builder()
                .result(true)
                .build(),
            HttpStatus.OK));

    mockMvc.perform(post("/api/auth/restore")
        .content(objectMapper.writeValueAsString(
            new RestorePasswordRequest("email")))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result").value(true));

  }

  @Test
  void changePassword() throws Exception {

    String code = "secret code";
    String password = "password";
    String captcha = "captcha";
    String captchaSecret = "secret captcha code";

    ChangePasswordRequest request =
        new ChangePasswordRequest(code, password, captcha, captchaSecret);

    when(userService.changePassword(any()))
        .thenReturn(new ResponseEntity(
            ResultErrorsResponse.builder()
                .result(true)
                .build(),
            HttpStatus.OK));

    mockMvc.perform(post("/api/auth/password")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result").value(true));

  }
}