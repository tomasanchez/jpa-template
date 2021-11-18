package com.jpa.model.user;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import com.jpa.core.database.PersistentEntity;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"uname"})})
public class User extends PersistentEntity {


    private String uname;
    private String password;

    public User() {}

    public User(String uname, String password) {
        this.uname = uname;
        this.password = password;
    }

    public String getUname() {
        return uname;
    }

    public User setUname(String uname) {
        this.uname = uname;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

}
