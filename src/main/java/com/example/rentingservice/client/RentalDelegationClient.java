package com.example.rentingservice.client;

import com.example.rentingservice.client.request.RentSeekingRequest;
import com.example.rentingservice.client.response.ClientResponse;
import com.example.rentingservice.client.response.RentSeekingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="rental-delegation-service", url = "${services.rental-delegation.url}")
public interface RentalDelegationClient {
    @PostMapping("/letting-orders/{rentalDelegationId}/rent-seeking/confirmation")
    ClientResponse<Object> confirmRentSeeking(
            @PathVariable("rentalDelegationId") Integer rentalDelegationId,
            @RequestBody RentSeekingRequest rentSeeking
    );

    @GetMapping("letting-orders/{rentalDelegationId}/rent-seeking")
    ClientResponse<RentSeekingResponse> getRentSeeking(@PathVariable("rentalDelegationId") Integer rentalDelegationId);
}
