package com.higorsouza.saga_order_service.core.repository;

import com.higorsouza.saga_order_service.core.document.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {
}
