package com.group.libraryapp.dto.response;

import com.group.libraryapp.domain.user.User;

public class UserResponse {
    private long Id;
    private String Name;
    private Integer age;

    public UserResponse(long id, String name, Integer age) {
        Id = id;
        Name = name;
        this.age = age;
    }
    public UserResponse(User user) {
        this.Id = user.getId();
        this.Name = user.getName();
        this.age = user.getAge();
    }

    public UserResponse(long id, User user) {
        this.Id = id;
        this.Name = user.getName();
        this.age = user.getAge();
    }

    public long getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public Integer getAge() {
        return age;
    }
}
