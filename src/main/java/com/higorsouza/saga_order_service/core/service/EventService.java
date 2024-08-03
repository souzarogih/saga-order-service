package com.higorsouza.saga_order_service.core.service;

import com.higorsouza.saga_order_service.core.document.Event;
import com.higorsouza.saga_order_service.core.repository.EventRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Log4j2
@Service
@AllArgsConstructor
public class EventService {

    @Autowired
    EventRepository eventRepository;

    public void notifyEnding(Event event){
        event.setOrderId(event.getOrderId());
        event.setCreatedAt(LocalDateTime.now());
        save(event);
        log.info("Order {} with saga notified! TransactionId: {}", event.getOrderId(), event.getTransactionId());
    }

    public Event save(Event event){
        return eventRepository.save(event);
    }
}
