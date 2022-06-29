package com.vudrag.knjiznica.dataObjects;

public class Genre {
    Long id;
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Genre(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
