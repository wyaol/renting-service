package com.example.rentingservice.client.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ClientResponse<T> {
    private Integer code;
    private String msg;
    private T data;
}
