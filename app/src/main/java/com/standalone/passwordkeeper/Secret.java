package com.standalone.passwordkeeper;

import com.standalone.core.dao.Column;

public class Secret {
    @Column(primary = true)
    long id;
    @Column
    String title;
    @Column
    String username;
    @Column
    String password;
    @Column
    boolean master = false;

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

    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }
}
