package com.jpa.app.seeder;

import com.jpa.core.security.crypto.BCryptPasswordEncoder;
import com.jpa.model.user.Privilege;
import com.jpa.model.user.Role;
import com.jpa.model.user.User;
import com.jpa.repositories.PrivilegeRepository;
import com.jpa.repositories.RoleRepository;
import com.jpa.repositories.UserRepository;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AuthSeeder implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

    private final UserRepository userRepository = new UserRepository();

    private final RoleRepository roleRepository = new RoleRepository();

    private final PrivilegeRepository privilegeRepository = new PrivilegeRepository();


    /**
     * The auth seeds follows a hierarchy for roles and privileges.
     */
    public void seed() {

        generatePrivileges();

        generateRoles();

        generateUsers();

    }


    public void generatePrivileges() {

        withTransaction(() -> {
            createPrivilegeIfNotExists("READ");
            createPrivilegeIfNotExists("CREATE");
            createPrivilegeIfNotExists("DELETE");
        });
    }

    public void generateRoles() {

        withTransaction(() -> {

            // User Role => Granted with ONLY the [READ] privilege
            Collection<Privilege> userPrivileges = new ArrayList<>();
            privilegeRepository.findByName("READ").ifPresent(userPrivileges::add);
            Role roleUser = createRoleIfNotExists("USER", userPrivileges);

            // Staff Role => Granted with all privileges of a User and with the [CREATE] privilege
            Collection<Privilege> staffPrivileges = new HashSet<>();
            privilegeRepository.findByName("CREATE").ifPresent(staffPrivileges::add);
            staffPrivileges.addAll(roleUser.getPrivileges());

            Role staffRole = createRoleIfNotExists("STAFF", staffPrivileges);

            // Admin Role => Granted with all privileges of a Staff, and includes the [DELETE]
            Collection<Privilege> adminPrivileges = new HashSet<>();
            privilegeRepository.findByName("DELETE").ifPresent(adminPrivileges::add);
            adminPrivileges.addAll(staffRole.getPrivileges());
            createRoleIfNotExists("ADMIN", adminPrivileges);

        });
    }

    public void generateUsers() {

        withTransaction(() -> {
            roleRepository.findByName("ADMIN").ifPresent(role -> createUserIfNotExists("admin", "admin",
                    "https://images.pexels.com/photos/2726111/pexels-photo-2726111.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
                    , role));

            roleRepository.findByName("STAFF").ifPresent(role -> createUserIfNotExists("staff", "staff",
                    "https://images.pexels.com/photos/2726111/pexels-photo-2726111.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
                    , role));

            roleRepository.findByName("USER").ifPresent(role -> createUserIfNotExists("user", "user",
                    "https://images.pexels.com/photos/2726111/pexels-photo-2726111.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
                    , role));
        });

    }


    /**
     * Creates a new privilege if it does not exist.
     *
     * @param name The name of the privilege.
     * @return a new privilege.
     */
    private Privilege createPrivilegeIfNotExists(String name) {

        return privilegeRepository.findByName(name).orElseGet(() -> {
            Privilege privilege = new Privilege();
            privilege.setName(name);
            return privilegeRepository.createEntity(privilege);
        });

    }


    /**
     * Persists a new Role when does not exist.
     *
     * @param name       the role name
     * @param privileges the associated privileges
     * @return a new Role or the existing one.
     */
    private Role createRoleIfNotExists(String name, Collection<Privilege> privileges) {

        return roleRepository.findByName(name).orElseGet(() -> {

            Role role = new Role();

            role.setName(name);

            // Creates a new privilege for convenience, a ROLE_<roleName>
            Privilege rolePrivilege = createPrivilegeIfNotExists(String.format("ROLE_%s", name));
            Set<Privilege> newPrivileges = new HashSet<>(privileges);
            newPrivileges.add(rolePrivilege);
            role.setPrivileges(newPrivileges);

            return roleRepository.createEntity(role);
        });

    }


    /**
     * Persists a User if does not exist.
     *
     * @param username The username to be used.
     * @param password a raw password.
     * @param profile  an image profile
     * @param role     a new Role
     */
    public void createUserIfNotExists(String username, String password, String profile, Role role) {
        userRepository.findByUsername(username).orElseGet(() -> {

            User user = new User(username, new BCryptPasswordEncoder().encode(password));
            user.setRole(role);
            user.setProfileUrl(profile);

            return userRepository.createEntity(user);
        });
    }

}
