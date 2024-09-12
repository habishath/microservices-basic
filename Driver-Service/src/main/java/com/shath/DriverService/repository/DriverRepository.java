package com.shath.DriverService.repository;

import com.shath.DriverService.model.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class DriverRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Driver> getDriverByLocation(String location) {
       // String sql = "SELECT * FROM driver where location = ?";
        String sql = "SELECT * FROM get_driverByLoc(?)";
        return jdbcTemplate.query(sql, new DriverRowMapper(), location);
    }

    public static class DriverRowMapper implements RowMapper<Driver> {
        @Override
        public Driver mapRow(ResultSet rs, int rowNum) throws SQLException {
            Driver driver = new Driver();
            driver.setDriverId(rs.getInt("driverId"));
            driver.setName(rs.getString("name"));
            driver.setAge(rs.getInt("age"));
            driver.setLocation(rs.getString("location"));
            return driver;
        }
    }

    public void insertDriver(Driver driver){
        String sql = "CALL insert_driver(?, ?, ?)";
        jdbcTemplate.update(sql, driver.getName(), driver.getAge(), driver.getLocation());
    }

    public Optional<Driver> getDriverById(int id){
        String sql = "SELECT * FROM get_driver(?)";
        try {
            Driver driver = jdbcTemplate.queryForObject(sql, new DriverRowMapper(), id);
            return Optional.ofNullable(driver);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Driver> findAll(){
        String sql = "SELECT * FROM get_drivers()";
        return jdbcTemplate.query(sql, new DriverRowMapper());
    }

    public void updateDriver(Driver driver) {
        String sql = "CALL update_driver(?, ?, ?, ?)";
        jdbcTemplate.update(sql, driver.getDriverId(), driver.getName(), driver.getAge(), driver.getLocation());
    }

    public void deleteDriver(Driver driver) {
        String sql = "CALL delete_driver(?)";
        jdbcTemplate.update(sql, driver.getDriverId());
    }

}
