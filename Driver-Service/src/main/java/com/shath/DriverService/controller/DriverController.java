package com.shath.DriverService.controller;

import com.shath.DriverService.model.Driver;
import com.shath.DriverService.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/drivers")
@RequiredArgsConstructor
public class DriverController {

    @Autowired
    private final DriverService driverService;

    @GetMapping
    public ResponseEntity<List<Driver>> getDriversByLocation(@RequestParam String location) {
        List<Driver> drivers = driverService.findDriversByLocation(location);
        return new ResponseEntity<>(drivers, HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createDriver(@Valid @RequestBody Driver driver){
        driverService.addDriver(driver);
        return "Driver has been added";
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<Object> getDriver(@PathVariable int id) {
        try {
            Driver driver = driverService.getDriver(id);
            return new ResponseEntity<>(driver, HttpStatus.OK);
        } catch (RuntimeException e) {
            CustomResponse response = new CustomResponse("Driver not found", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/view/all")
    public List<Driver> getAllDrivers(){
        List<Driver> drivers = driverService.getAllDrivers();
        return  drivers;
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateDriver(@PathVariable int id, @RequestBody Driver driver) {
        try{
//            driver.setDriverId(id);
//            driverService.updateDriver(driver);
//            CustomResponse response = new CustomResponse(String.format("Driver with id %d has updated", id), HttpStatus.OK.value());
//            return new ResponseEntity<>(response, HttpStatus.OK);

            // Fetch the existing driver to check if it exists
            Driver existingDriver = driverService.getDriver(id);

            if (existingDriver == null) {
                // Driver with the specified id does not exist
                CustomResponse response = new CustomResponse("Driver with id " + id + " not found", HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            // Set the ID for the driver to be updated
            driver.setDriverId(id);

            // Perform the update operation
            driverService.updateDriver(driver);

            // Return success response
            CustomResponse response = new CustomResponse("Driver with id " + id + " has been updated", HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch (RuntimeException e) {
            // Handle any unexpected exceptions
            CustomResponse response = new CustomResponse("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object>  deleteDriver(@PathVariable int id){
        try
        {
            Driver existingDriver = driverService.getDriver(id);

            if (existingDriver == null) {
                // Driver with the specified id does not exist
                CustomResponse response = new CustomResponse("Driver with id " + id + " not found", HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            driverService.deleteDriver(id);
            CustomResponse response = new CustomResponse("Driver with id " + id + " has been deleted", HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (RuntimeException e) {
            CustomResponse response = new CustomResponse("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
