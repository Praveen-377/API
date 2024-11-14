package com.sep.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateRepositoryRequestPojo {
    private String name;
    private String description;
    private String homepage;
    
    @JsonProperty("private")
    private boolean isPrivate;

    // Constructors
    public CreateRepositoryRequestPojo(String name, String description, String homepage, boolean isPrivate) {
        this.name = name;
        this.description = description;
        this.homepage = homepage;
        this.isPrivate = isPrivate;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
}
