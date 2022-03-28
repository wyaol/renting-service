package com.example.rentingservice.service;

import com.example.rentingservice.client.RentalDelegationClient;
import com.example.rentingservice.client.response.ClientResponse;
import com.example.rentingservice.repository.OrderRepository;
import com.example.rentingservice.repository.entity.OrderEntity;
import com.example.rentingservice.service.dto.OrderCreate;
import com.example.rentingservice.service.dto.OrderCreated;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RentalDelegationClient rentalDelegationClient;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateOrderSuccess() {

        when(orderRepository.save(any())).thenReturn(
                OrderEntity.builder().id(234) .build()
        );
        when(rentalDelegationClient.confirmRentSeeking(any(), any())).thenReturn(
                new ClientResponse<>(0, "", null)
        );

        final OrderCreated orderCreated = orderService.createOrder(
                new OrderCreate(123, BigDecimal.valueOf(3000), 456, 789));

        assertEquals(234, orderCreated.getId());
    }
}
