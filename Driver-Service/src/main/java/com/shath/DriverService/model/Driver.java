package com.shath.DriverService.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Driver {

    private Integer driverId;

    @NotNull(message = "Driver name cannot be null")
    @Size(min = 2, message = "Driver name must be at least 2 characters long")
    private String name;

    @NotNull(message = "Age cannot be null")
    @Min(value = 18, message = "Driver must be at least 18 years old")
    @Max(value = 100, message = "Driver age must be less than or equal to 100")
    private Integer age;

    @NotNull(message = "Location cannot be null")
    private String location;
}
