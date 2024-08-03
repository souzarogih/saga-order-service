package com.higorsouza.saga_order_service.core.service;

import com.higorsouza.saga_order_service.core.document.Event;
import com.higorsouza.saga_order_service.core.document.Order;
import com.higorsouza.saga_order_service.core.dto.OrderRequest;
import com.higorsouza.saga_order_service.core.producer.SagaProducer;
import com.higorsouza.saga_order_service.core.repository.OrderRepository;
import com.higorsouza.saga_order_service.core.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Log4j2
@Service
@AllArgsConstructor
public class OrderService {

    private static final String TRANSACTION_ID_PATTERN = "%s_%s";

    @Autowired
    EventService eventService;

    @Autowired
    SagaProducer sagaProducer;

    private final JsonUtil jsonUtil;

    @Autowired
    OrderRepository orderRepository;

    public Order createOrder(OrderRequest orderRequest){
        var order = Order
                .builder()
                .products(orderRequest.getProducts())
                .createdAt(LocalDateTime.now())
                .transactionId(
                        String.format(TRANSACTION_ID_PATTERN, Instant.now().toEpochMilli(), UUID.randomUUID())
                )
                .build();
        log.info("Salvando order");
        orderRepository.save(order);
        log.info("Enviando evento para o kafka");
        sagaProducer.sendEvent(jsonUtil.toJson(createPayload(order)));
        return order;
    }

    public Event createPayload(Order order) {
        var event = Event
                .builder()
                .orderId(order.getId())
                .transactionId(order.getTransactionId())
                .payload(order)
                .createdAt(LocalDateTime.now())
                .build();
        eventService.save(event);
        return event;
    }
}
