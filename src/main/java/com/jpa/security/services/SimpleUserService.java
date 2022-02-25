package com.jpa.security.services;

import java.util.Collections;
import javax.persistence.EntityNotFoundException;
import com.jpa.core.security.userdetails.UserDetails;
import com.jpa.core.security.userdetails.UserDetailsService;
import com.jpa.model.user.User;
import com.jpa.repositories.UserRepository;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleUserService implements UserDetailsService {

    private UserRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws EntityNotFoundException {

        User user =
                usersRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);

        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        return new com.jpa.core.security.userdetails.User(user.getUname(), user.getPassword(),
                enabled, accountNonExpired, credentialsNonExpired, accountNonLocked,
                Collections.emptyList());
    }

}
