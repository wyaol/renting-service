package com.example.rentingservice.service;


import com.example.rentingservice.client.RentalDelegationClient;
import com.example.rentingservice.client.request.RentSeekingRequest;
import com.example.rentingservice.client.response.ClientResponse;
import com.example.rentingservice.exceptions.ServiceConnectRefusedException;
import com.example.rentingservice.repository.OrderRepository;
import com.example.rentingservice.repository.entity.OrderEntity;
import com.example.rentingservice.service.dto.OrderCreate;
import com.example.rentingservice.service.dto.OrderCreated;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;

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
        try {
            retryMethod(
                    this::confirmRentSeeking,
                    Map.of("rentalDelegationId", rentalDelegationId, "id", id),
                    FeignException.GatewayTimeout.class,
                    6
            );
        } catch (FeignException.GatewayTimeout e) {
            rentalDelegationClient.getRentSeeking(rentalDelegationId);
        }

        return OrderCreated.builder().id(id).build();
    }

    private ClientResponse<Object> confirmRentSeeking(Map<String, Integer> map) {
        return rentalDelegationClient.confirmRentSeeking(map.get("rentalDelegationId"), RentSeekingRequest.builder()
                .rentId(map.get("id"))
                .build());
    }

    private <T, R, E> R retryMethod(Function<T, R> function, T t, Class<E> exception, int maxRetryTime) {
        return _retryMethod(function, t,  0, maxRetryTime, exception);
    }

    private <T, R, E> R _retryMethod(Function<T, R> function, T t, int retryTime, int maxRetryTime, Class<E> exception) {
        if (retryTime == maxRetryTime) throw new ServiceConnectRefusedException();
        try {
            return function.apply(t);
        } catch (Throwable e) {
            if (exception.isInstance(e)) {
                return _retryMethod(function, t, retryTime + 1, maxRetryTime, exception);
            } else {
                throw e;
            }
        }
    }
}
