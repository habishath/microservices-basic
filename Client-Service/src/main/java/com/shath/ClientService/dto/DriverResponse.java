package com.shath.ClientService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class DriverResponse {

    private List<Driver> data;
    private String message;


}
