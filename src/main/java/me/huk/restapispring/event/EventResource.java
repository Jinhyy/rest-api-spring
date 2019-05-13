package me.huk.restapispring.event;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;

public class EventResource extends Resource<Event> {

    public EventResource(Event content, Link... links) {
        super(content, links);
    }
}
