package com.higorsouza.saga_order_service.core.controller;

import com.higorsouza.saga_order_service.core.document.Event;
import com.higorsouza.saga_order_service.core.dto.EventFilters;
import com.higorsouza.saga_order_service.core.service.EventService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    EventService eventService;

    @GetMapping
    public Event findByFilters(EventFilters eventFilters) {
        return eventService.findByFilters(eventFilters);
    }

    @GetMapping("/all")
    public List<Event> findAll() {
        return eventService.findAll();
    }
}
