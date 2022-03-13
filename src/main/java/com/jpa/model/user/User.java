package com.jpa.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.jpa.core.database.PersistentEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A Generic User implementation of a Role-Privilege based Authorization.
 * 
 * @author Tomás Sánchez
 */
@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class User extends PersistentEntity {

    @Column(name = "uname", nullable = false, unique = true)
    private String uname;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "users_role",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Role role;

    /**
     * Allows the creation of users with no roles.
     * 
     * @param uname the user name
     * @param password the assigned credetials (should be already encoded)
     */
    public User(String uname, String password) {
        this.uname = uname;
        this.password = password;
    }

}
