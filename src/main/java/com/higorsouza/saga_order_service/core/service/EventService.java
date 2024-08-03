package com.higorsouza.saga_order_service.core.service;

import com.higorsouza.saga_order_service.config.exception.ValidationException;
import com.higorsouza.saga_order_service.core.document.Event;
import com.higorsouza.saga_order_service.core.dto.EventFilters;
import com.higorsouza.saga_order_service.core.repository.EventRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

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

    public List<Event> findAll() {
        return eventRepository.findAllByOrderByCreatedAtDesc();
    }

    public Event findByFilters(EventFilters eventFilters){
        validateEmptyFilters(eventFilters);
        if (!isEmpty(eventFilters.getOrderId())){
            return findByOrderId(eventFilters.getOrderId());
        }else {
            return findByTransactionId(eventFilters.getTransactionId());
        }
    }

    private Event findByOrderId(String orderId) {
        return eventRepository
                .findTop1ByOrderIdOrderByCreatedAtDesc(orderId)
                .orElseThrow(() -> new ValidationException("Event no found by orderId"));
    }

    private Event findByTransactionId(String transactionId) {
        return eventRepository
                .findTop1ByTransactionIdOrderByCreatedAtDesc(transactionId)
                .orElseThrow(() -> new ValidationException("Event no found by transactionId"));
    }


    private void validateEmptyFilters(EventFilters eventFilters) {
        if (isEmpty(eventFilters.getOrderId()) && isEmpty(eventFilters.getTransactionId())){
            throw new ValidationException("OrderId or TransactionId must be informed.");
        }
    }

    public Event save(Event event){
        return eventRepository.save(event);
    }
}
