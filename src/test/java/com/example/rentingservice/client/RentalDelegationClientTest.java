package com.example.rentingservice.client;

import com.example.rentingservice.client.request.RentSeekingRequest;
import com.example.rentingservice.client.response.ClientResponse;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.http.trafficlistener.ConsoleNotifyingWiremockNetworkTrafficListener;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableFeignClients(basePackages = "com.example.rentingservice.client.*")
class RentalDelegationClientTest {

    private static final WireMockConfiguration wireMockConfiguration = WireMockConfiguration
            .wireMockConfig().networkTrafficListener(
                    new ConsoleNotifyingWiremockNetworkTrafficListener()).port(8001);
    private static final WireMockServer wireMockServer = new WireMockServer(wireMockConfiguration);


    @Autowired
    private RentalDelegationClient rentalDelegationClient;

    @BeforeAll
    static void startWireMock() {
        wireMockServer.start();
    }

    @AfterAll
    static void stopWireMock() {
        wireMockServer.stop();
    }

    @Test
    void shouldConfirmRentSeekingSuccess() {
        ClientResponse<Object> response = rentalDelegationClient
                .confirmRentSeeking(
                        789,
                        RentSeekingRequest.builder().requestId("123").rentId(456) .build());
        assertEquals(0, response.getCode());
    }
}
