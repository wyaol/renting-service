package com.example.rentingservice.controller;

import com.example.rentingservice.controller.request.OrderCreateRequest;
import com.example.rentingservice.exceptions.RentSeekingAlreadyConfirmedException;
import com.example.rentingservice.exceptions.ServiceConnectRefusedException;
import com.example.rentingservice.exceptions.ServiceErrorException;
import com.example.rentingservice.service.OrderService;
import com.example.rentingservice.service.dto.OrderCreated;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerTest {
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void shouldCreateOrderSuccess() throws Exception {
        when(orderService.createOrder(any())).thenReturn(new OrderCreated(7865));

        mockMvc.perform(post("/renting-orders")
                .header("clientId", 789)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        objectMapper.writeValueAsString(
                                OrderCreateRequest.builder()
                                        .agentId(123)
                                        .mouthPrice(BigDecimal.valueOf(3000))
                                        .rentalDelegationId(456)
                                        .build()
                        )
                ))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").value(""))
                .andExpect(jsonPath("$.data.id").value(7865));
    }

    @Test
    void shouldCreateOrderFailedWhenRentSeekingAlreadyConfirmed() throws Exception {
        when(orderService.createOrder(any())).thenThrow(new RentSeekingAlreadyConfirmedException("already confirmed"));

        mockMvc.perform(post("/renting-orders")
                .header("clientId", 789)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        objectMapper.writeValueAsString(
                                OrderCreateRequest.builder()
                                        .agentId(123)
                                        .mouthPrice(BigDecimal.valueOf(3000))
                                        .rentalDelegationId(456)
                                        .build()
                        )
                ))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(4001))
                .andExpect(jsonPath("$.msg").value("already confirmed"));
    }

    @Test
    void shouldCreateOrderFailedWhenClientNotInvalid() throws Exception {
        when(orderService.createOrder(any())).thenThrow(new ServiceConnectRefusedException());

        mockMvc.perform(post("/renting-orders")
                .header("clientId", 789)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        objectMapper.writeValueAsString(
                                OrderCreateRequest.builder()
                                        .agentId(123)
                                        .mouthPrice(BigDecimal.valueOf(3000))
                                        .rentalDelegationId(456)
                                        .build()
                        )
                ))
                .andExpect(status().isInternalServerError());
    }
}
