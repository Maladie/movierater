package com.ex.movierater.model;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

public class LinkTO extends ResourceSupport {

    public LinkTO(Link link) {
        if (link != null) add(link);
    }
}
