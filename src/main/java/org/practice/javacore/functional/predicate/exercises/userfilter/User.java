package org.practice.javacore.functional.predicate.exercises.userfilter;

public class User {
    private final String username;
    private final boolean active;

    public User(String username, boolean isActive) {
        this.username = username;
        this.active = isActive;
    }

    public String getUsername() {
        return username;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", active=" + active +
                '}';
    }
}
