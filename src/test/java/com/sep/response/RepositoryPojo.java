package com.sep.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RepositoryPojo {
    private String name;
    private boolean privateRepo; // Maps to "private" in JSON

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPrivateRepo() {
        return privateRepo;
    }

    public void setPrivateRepo(boolean privateRepo) {
        this.privateRepo = privateRepo;
    }

    @Override
    public String toString() {
        return "RepositoryPojo{" +
                "name='" + name + '\'' +
                ", privateRepo=" + privateRepo +
                '}';
    }
}
