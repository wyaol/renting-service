package com.example.rentingservice.service;

import com.example.rentingservice.client.RentalDelegationClient;
import com.example.rentingservice.client.response.ClientResponse;
import com.example.rentingservice.client.response.RentSeekingResponse;
import com.example.rentingservice.repository.OrderRepository;
import com.example.rentingservice.repository.entity.OrderEntity;
import com.example.rentingservice.service.dto.OrderCreate;
import com.example.rentingservice.service.dto.OrderCreated;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.net.http.HttpTimeoutException;
import java.nio.charset.Charset;
import java.util.Map;

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
                OrderEntity.builder().id(234).build()
        );
        when(rentalDelegationClient.confirmRentSeeking(any(), any())).thenReturn(
                new ClientResponse<>(0, "", null)
        );

        final OrderCreated orderCreated = orderService.createOrder(
                new OrderCreate(123, BigDecimal.valueOf(3000), 456, 789));

        assertEquals(234, orderCreated.getId());
    }

    @Test
    void shouldCreateOrderSuccessWhenGetRentSeekingResponseSuccessAndConfirmRentSeekingTimeOut() {
        when(orderRepository.save(any())).thenReturn(
                OrderEntity.builder().id(234).build()
        );
        when(rentalDelegationClient.confirmRentSeeking(any(), any())).thenThrow(
                new FeignException.GatewayTimeout(
                        "",
                        Request.create(
                                Request.HttpMethod.POST, "", Map.of(), new byte[]{}, Charset.defaultCharset()), new byte[]{}, Map.of()
                )
        );
        when(rentalDelegationClient.getRentSeeking(any())).thenReturn(
                new ClientResponse<>(0, "", RentSeekingResponse.builder().rentId(234).build()));

        final OrderCreated orderCreated = orderService.createOrder(
                new OrderCreate(123, BigDecimal.valueOf(3000), 456, 789));

        assertEquals(234, orderCreated.getId());
    }
}
