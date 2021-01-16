package main;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class ApiGeneralTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @WithUserDetails("vadik@mail.com")
  public void correctPersonalStatistic() throws Exception {
    mockMvc.perform(get("/api/statistics/my"))
        .andExpect(authenticated())
        .andExpect(status().isOk());
  }

  @Test
  public void badPersonalStatistic() throws Exception {
    mockMvc.perform(get("/api/statistics/my"))
        .andExpect(unauthenticated())
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void getSettings() throws Exception {
    mockMvc.perform(get("/api/settings"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.MULTIUSER_MODE").isNotEmpty())
        .andExpect(jsonPath("$.POST_PREMODERATION").isNotEmpty())
        .andExpect(jsonPath("$.STATISTICS_IS_PUBLIC").isNotEmpty());
  }

}
