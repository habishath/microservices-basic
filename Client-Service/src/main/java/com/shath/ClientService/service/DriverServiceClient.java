package com.shath.ClientService.service;

import com.shath.ClientService.dto.Driver;
import com.shath.ClientService.dto.DriverResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverServiceClient {

    private final WebClient webClient;

    @Autowired
    public DriverServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    public DriverResponse getDriversByLocation(String location) {
        try {
            List<Driver> drivers = webClient.get()
                    .uri("/api/v1/drivers?location={location}", location)
                    .retrieve()
                    .bodyToFlux(Driver.class)
                    .collectList()
                    .block();

            if(location == null || location.trim().isEmpty()){
                return new DriverResponse(new ArrayList<>(), "please input the location");
            }

            else if (drivers.isEmpty()){
                return new DriverResponse(drivers, "no drivers available in the requested location");
            }

            return new DriverResponse(drivers, "success");

        } catch (RuntimeException e) {
            return new DriverResponse(new ArrayList<>() , "Sorry, service unavailable, Please try again after sometime");
        }
    }
}
