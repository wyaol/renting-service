package com.example.rentingservice.controller;


import com.example.rentingservice.controller.request.OrderCreateRequest;
import com.example.rentingservice.controller.response.OrderCreateResponse;
import com.example.rentingservice.controller.response.Response;
import com.example.rentingservice.exceptions.BusinessException;
import com.example.rentingservice.exceptions.ServiceConnectRefusedException;
import com.example.rentingservice.exceptions.ServiceErrorException;
import com.example.rentingservice.service.OrderService;
import com.example.rentingservice.service.dto.OrderCreate;
import com.example.rentingservice.service.dto.OrderCreated;
import com.example.rentingservice.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/renting-orders")
@RequiredArgsConstructor
public class OrderController {

    private OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response<OrderCreateResponse> createOrder(
            @RequestHeader Integer clientId,
            @RequestBody OrderCreateRequest orderCreateRequest
    ) {
        final OrderCreate orderCreate = ObjectMapperUtil.convert(orderCreateRequest, OrderCreate.class);
        orderCreate.setClientId(clientId);
        final OrderCreated orderCreated = orderService.createOrder(orderCreate);
        return Response.success(ObjectMapperUtil.convert(orderCreated, OrderCreateResponse.class));
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<Object> handlerBusinessException(BusinessException businessException) {
        return new Response<>(businessException.getCode(), businessException.getMsg(), null);
    }

    @ExceptionHandler({ServiceConnectRefusedException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<Object> handlerInternalServerException(Exception e) {
        return new Response<>(5000, "unknown error", null);
    }
}
