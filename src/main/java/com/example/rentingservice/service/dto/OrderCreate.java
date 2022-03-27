package com.example.rentingservice.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrderCreate {
    private Integer rentalDelegationId;
    private BigDecimal mouthPrice;
    private Integer clientId;
    private Integer agentId;
}
