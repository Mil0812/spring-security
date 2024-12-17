package mil0812.springsecurity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import mil0812.springsecurity.controllers.MainController;
import mil0812.springsecurity.models.User;
import mil0812.springsecurity.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class SpringSecurityApplicationTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @Test
  @WithMockUser(username = "user", roles = {"USER"})
  public void userPage_AccessWithUserRole_ShouldReturnOk() throws Exception {
    // Mock user data
    User mockUser = new User();
    mockUser.setUsername("user");
    mockUser.setAvatar("https://icon2.cleanpng.com/lnd/20241215/pp/549436fd278c38255ad45556e98c88.webp");

    when(userService.getUserProfile("user")).thenReturn(mockUser);

    mockMvc.perform(get("/user"))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  public void adminPage_AccessWithAdminRole_ShouldReturnOk() throws Exception {
    User mockUser = new User();
    mockUser.setUsername("admin");
    mockUser.setAvatar("https://icon2.cleanpng.com/lnd/20241122/ir/6a4e768799115fe3383508fe0b3e81.webp");

    when(userService.getUserProfile("admin")).thenReturn(mockUser);

    mockMvc.perform(get("/admin"))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", roles = {"USER"})
  public void adminPage_AccessWithUserRole_ShouldReturnForbidden() throws Exception {
    mockMvc.perform(get("/admin"))
        .andExpect(status().isForbidden());
  }

  @Test
  public void adminPage_AccessWithoutAuthentication_ShouldRedirectToLogin() throws Exception {
    mockMvc.perform(get("/admin"))
        .andExpect(status().isFound()) // 302 Found
        .andExpect(redirectedUrlPattern("**/login")); // Redirects to the login page
  }

  @Test
  public void userPage_AccessWithoutAuthentication_ShouldReturnRedirect() throws Exception {
    mockMvc.perform(get("/user"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrlPattern("**/login"));
  }

  @Test
  public void homePage_AccessWithoutAuthentication_ShouldReturnOk() throws Exception {
    mockMvc.perform(get("/"))
        .andExpect(status().isOk());
  }
}
