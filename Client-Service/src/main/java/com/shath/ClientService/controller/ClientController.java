package com.shath.ClientService.controller;

import com.shath.ClientService.dto.Driver;
import com.shath.ClientService.dto.DriverResponse;
import com.shath.ClientService.service.DriverServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ClientController {

    private final DriverServiceClient driverServiceClient;

    @GetMapping("/drivers")
    public DriverResponse getDriversByLocation(@RequestParam String location) {
        return driverServiceClient.getDriversByLocation(location);
    }
}
