package mil0812.springsecurity.controllers;

import mil0812.springsecurity.models.User;
import mil0812.springsecurity.services.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

  private final UserService userService;

  public MainController(UserService userService) {
    this.userService = userService;
  }


  public UserService getUserService() {
    return userService;
  }

  @GetMapping
  public String homePage(){
    return "home";
  }

  @GetMapping("/user")
  public String userPage(Authentication authentication, Model model) {
    String username = authentication.getName();
    User user = userService.getUserProfile(username);
    model.addAttribute("username", user.getUsername());
    model.addAttribute("avatar", user.getAvatar());
    return "user-page";
  }


  @Secured("ADMIN")
  @GetMapping("/admin")
  public String adminPage(Authentication authentication, Model model) {
    String username = authentication.getName();
    User user = userService.getUserProfile(username);
    model.addAttribute("username", user.getUsername());
    model.addAttribute("avatar", user.getAvatar());
    return "admin-page";
  }
}