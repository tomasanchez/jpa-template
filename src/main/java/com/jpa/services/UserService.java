package com.jpa.services;

import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import com.jpa.core.security.crypto.BCryptPasswordEncoder;
import com.jpa.exceptions.user.UserAlreadyExistsException;
import com.jpa.model.user.User;
import com.jpa.repositories.RoleRepository;
import com.jpa.repositories.UserRepository;
import lombok.Getter;

@Getter
public class UserService extends TransactionalService {

    private UserRepository userRepository = new UserRepository();
    private RoleRepository roleRepository = new RoleRepository();

    /**
     * Retrieves an User given an id.
     * 
     * @param id the entity unique id
     * @return the user with the given id
     * @throws EntityNotFoundException when no user with the given id
     */
    public User findById(String id) {
        return userRepository.getEntity(id).orElseThrow(EntityNotFoundException::new);
    }

    /**
     * Retrieves an user with the given id.
     * 
     * @param username the unique username
     * @return the user or null if no user with the given username
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Persist an user with the given username.
     * 
     * @param user the user DTO
     * @return the persisted user
     * @throws UserAlreadyExistsException when the username is taken.
     */
    public User save(User user) throws UserAlreadyExistsException {


        findByUsername(user.getUname()).ifPresent(u -> {

            throw new UserAlreadyExistsException(
                    String.format("There is already an user with the username %s", u.getUname()));

        });


        user.setRole(roleRepository.findByName("USER").orElseThrow(IllegalArgumentException::new));

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));


        User created;

        withTransaction(() -> {
            userRepository.createEntity(user);
        });

        created = userRepository.findByUsername(user.getUname())
                .orElseThrow(IllegalStateException::new);

        return created;
    }

}
