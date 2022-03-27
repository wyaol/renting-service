package com.example.rentingservice.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrderCreateRequest {
    private Integer rentalDelegationId;
    private BigDecimal mouthPrice;
    private Integer agentId;
}
