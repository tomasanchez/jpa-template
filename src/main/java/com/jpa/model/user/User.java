package com.jpa.model.user;

import java.util.Collection;
import java.util.Collections;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
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
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"uname"})})
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class User extends PersistentEntity {

    @Column(name = "uname", nullable = false)
    private String uname;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles = Collections.emptyList();

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
