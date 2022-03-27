package com.example.rentingservice.repository;

import com.example.rentingservice.repository.entity.OrderEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void  shouldCreateOrderSuccess() {
        OrderEntity orderEntity1 = OrderEntity.builder()
                .clientId(123)
                .mouthPrice(BigDecimal.valueOf(2000))
                .agentId(456)
                .rentalDelegationId(789)
                .build();
        OrderEntity orderEntity = orderRepository.save(
                orderEntity1
        );
        assertEquals(orderEntity, orderEntity1);
    }
}
