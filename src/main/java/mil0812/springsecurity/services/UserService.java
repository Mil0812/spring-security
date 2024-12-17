package mil0812.springsecurity.services;

import mil0812.springsecurity.models.User;
import mil0812.springsecurity.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

  @Autowired
  UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);

    if(user == null){
      throw new UsernameNotFoundException("Користувача з даним логіном не знайдено: "+username);
    }

    UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
        .username(user.getUsername())
        .password(user.getPassword())
        .roles(user.getRole())
        .build();
    return userDetails;
  }

  public User getUserProfile(String username) {
    return userRepository.findByUsername(username);
  }
}
