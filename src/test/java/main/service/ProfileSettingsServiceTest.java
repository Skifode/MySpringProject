package main.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.api.request.ProfileSettingsRequest;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
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
class ProfileSettingsServiceTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @Test
  @WithUserDetails("vadik@mail.com")
  void editProfile() throws Exception {

    String urlTemplate = "/api/profile/my";

    ProfileSettingsRequest badPass = new ProfileSettingsRequest();
    badPass.setPassword("123");

    ProfileSettingsRequest goodPass = new ProfileSettingsRequest();
    goodPass.setPassword("password");

    ProfileSettingsRequest veryBadName = new ProfileSettingsRequest();
    veryBadName.setName("admin");

    ProfileSettingsRequest goodName = new ProfileSettingsRequest();
    goodName.setName(RandomString.make(6));

    mockMvc.perform(post(urlTemplate)
        .content(objectMapper.writeValueAsString(badPass))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.result")
            .value(false))
        .andExpect(jsonPath("$.errors.password")
            .value("Пароль короче 6-ти символов"));

    mockMvc.perform(post(urlTemplate)
        .content(objectMapper.writeValueAsString(goodPass))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result").value(true));

    mockMvc.perform(post(urlTemplate)
        .content(objectMapper.writeValueAsString(veryBadName))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.result")
            .value(false))
        .andExpect(jsonPath("$.errors.name")
            .value("Имя указано неверно"));

    mockMvc.perform(post(urlTemplate)
        .content(objectMapper.writeValueAsString(goodName))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result").value(true));

  }
}