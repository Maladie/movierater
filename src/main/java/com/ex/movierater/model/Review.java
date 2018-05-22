package com.ex.movierater.model;

import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;

public class Review extends ResourceSupport implements Serializable {

    private String author;

    private String content;

    private boolean accepted;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

}
