package com.shath.DriverService.service;

import com.shath.DriverService.model.Driver;
import com.shath.DriverService.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DriverService {

    @Autowired
    private final DriverRepository driverRepository;

    public Boolean addDriver(Driver driver){
        List<Driver> existingDrivers = driverRepository.findAll();

        boolean driverExists = existingDrivers.stream()
                .anyMatch(existingDriver -> existingDriver.getName().equalsIgnoreCase(driver.getName())
                        && existingDriver.getAge().equals(driver.getAge()) && existingDriver.getLocation().equals((driver.getLocation())));

        if(driverExists){
            throw new RuntimeException("Driver with same details already exists");
        }

        return driverRepository.insertDriver(driver);
    }

    public Driver getDriver(int id) {
        Optional<Driver> driver = driverRepository.getDriverById(id);
        return driver.orElseThrow(() -> new RuntimeException("Driver Not Found for id: " + id));
    }

    public List<Driver> getAllDrivers(){
        return driverRepository.findAll();
    }

    public void updateDriver(Driver driver) {
        driverRepository.updateDriver(driver);
    }

    public void deleteDriver(int id) {
        Driver driver = driverRepository.getDriverById(id).orElseThrow(() -> new RuntimeException("Employee Not Found for id: " + id));
        driverRepository.deleteDriver(id);
    }

    public List<Driver> findDriversByLocation(String location) {
        List<Driver> driver = driverRepository.getDriverByLocation(location);
        return driver;
    }
}
