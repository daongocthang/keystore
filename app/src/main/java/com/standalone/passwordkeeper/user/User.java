package com.standalone.passwordkeeper.user;

import com.standalone.core.dao.Column;

public class User {
    @Column(primary = true)
    long id;
    @Column
    String title;
    @Column
    String username;
    @Column
    String password;

    public User() {
    }

    User(Builder builder) {
        title = builder.title;
        username = builder.username;
        password = builder.password;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static class Builder {
        String title;
        String username;
        String password;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
