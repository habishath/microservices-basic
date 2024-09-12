package com.shath.DriverService.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CustomResponse {

    private String message;
    private Integer status;
}
