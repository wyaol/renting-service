package com.example.rentingservice.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Response<T> {
    private static final Integer SUCCESS_CODE = 0;

    private Integer code = SUCCESS_CODE;
    private String msg = "";
    private T data;

    public static <E> Response<E> success(E data) {

        return new Response<>(SUCCESS_CODE, "", data);
    }
}
