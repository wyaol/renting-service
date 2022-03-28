package com.example.rentingservice.service;


import com.example.rentingservice.client.RentalDelegationClient;
import com.example.rentingservice.client.request.RentSeekingRequest;
import com.example.rentingservice.repository.OrderRepository;
import com.example.rentingservice.repository.entity.OrderEntity;
import com.example.rentingservice.service.dto.OrderCreate;
import com.example.rentingservice.service.dto.OrderCreated;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private RentalDelegationClient rentalDelegationClient;

    private OrderRepository orderRepository;

    public OrderCreated createOrder(OrderCreate orderCreate) {
        final Integer rentalDelegationId = orderCreate.getRentalDelegationId();
        final OrderEntity orderEntity = orderRepository.save(OrderEntity.builder()
                .agentId(orderCreate.getAgentId())
                .clientId(orderCreate.getClientId())
                .mouthPrice(orderCreate.getMouthPrice())
                .rentalDelegationId(rentalDelegationId)
                .build());
        final Integer id = orderEntity.getId();
        rentalDelegationClient.confirmRentSeeking(rentalDelegationId, RentSeekingRequest.builder()
                .rentId(id)
                .requestId(UUID.randomUUID().toString())
                .build());
        return OrderCreated.builder().id(id).build();
    }
}
