package com.example.rentingservice.client.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RentSeekingRequest {
    @JsonProperty("request_id")
    private Integer requestId;
    @JsonProperty("rent_id")
    private Integer rentId;
}
