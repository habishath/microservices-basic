package com.shath.DriverService.repository;

import com.shath.DriverService.dao.SettingUpDaoConstant;
import com.shath.DriverService.model.Driver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



@Repository
public class DriverRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final Logger LOGGER = LogManager.getLogger(DriverRepository.class);


    public List<Driver> getDriverByLocation(String location) {
//       // String sql = "SELECT * FROM driver where location = ?";
//        String sql = "SELECT * FROM get_driverByLoc(?)";
//        return jdbcTemplate.query(sql, new DriverRowMapper(), location);

        Connection connection = null;
        CallableStatement callablest = null;
        ResultSet resultSet = null;
        List<Driver> driverList = null;

        try {
            connection = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
            callablest = connection.prepareCall(SettingUpDaoConstant.ISettingUp.GET_DRIVER_BY_LOCATION);
            callablest.setObject(1, location, Types.VARCHAR);
            callablest.execute();

            resultSet = callablest.getResultSet();

            if(resultSet != null){
                driverList = new ArrayList<>();
                while (resultSet.next()){
                    Driver driver= new Driver();
                    driver.setDriverId(resultSet.getInt("driverid"));
                    driver.setName(resultSet.getString("name"));
                    driver.setAge(resultSet.getInt("age"));
                    driver.setLocation(resultSet.getString("location"));
                    driverList.add(driver);
                }
            }
            LOGGER.info("Driver fetched successfully for Location: {}", location);
        } catch (SQLException e) {
            LOGGER.error("Error while fetching driver with Location: {}", location, e);
        }finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (callablest != null) {
                    callablest.close();
                }
            } catch (SQLException e) {
                LOGGER.error("Error while closing resources for getDriverById", e);
            }
            DataSourceUtils.releaseConnection(connection, jdbcTemplate.getDataSource());

        }
        return driverList;


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

    public Boolean insertDriver(Driver driver){
//        String sql = "CALL insert_driver(?, ?, ?)";
//        jdbcTemplate.update(sql, driver.getName(), driver.getAge(), driver.getLocation());
        Connection connection = null;
        CallableStatement callablest = null;
        boolean isInserted = false;

        try {
            connection = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
            callablest = connection.prepareCall(SettingUpDaoConstant.ISettingUp.INSERT_DRIVER);
            callablest.setObject(1, driver.getName(), Types.VARCHAR);
            callablest.setObject(2, driver.getAge(), Types.INTEGER);
            callablest.setObject(3, driver.getLocation(), Types.VARCHAR);

            ResultSet rs = callablest.executeQuery();
            if (rs.next()) {
                isInserted = rs.getBoolean(1);  // Assuming the function returns a boolean value
            }

            LOGGER.info("Driver Inserted Successfully");

        } catch (SQLException e) {
            LOGGER.error("Error while inserting driver with ID {}", driver.getDriverId() ,e);
        }finally {
            try {

                if (callablest != null) {
                    callablest.close();
                }
            } catch (SQLException e) {
                LOGGER.error("Error while closing resources for insertDriver", e);
            }
            DataSourceUtils.releaseConnection(connection, jdbcTemplate.getDataSource());

        }
        return isInserted;
    }

    public Optional<Driver> getDriverById(int id){
//        String sql = "SELECT * FROM get_driver(?)";
//        try {
//            Driver driver = jdbcTemplate.queryForObject(sql, new DriverRowMapper(), id);
//            return Optional.ofNullable(driver);
//        } catch (EmptyResultDataAccessException e) {
//            return Optional.empty();
//        }

        Connection connection = null;
        CallableStatement callablest = null;
        ResultSet resultSet = null;
        Driver driver = null;

        try {
            connection = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
            callablest = connection.prepareCall(SettingUpDaoConstant.ISettingUp.SETUP_FIND_BY_ID);
            callablest.setObject(1, id, Types.INTEGER);
            callablest.execute();

            resultSet = callablest.getResultSet();

            if(resultSet != null && resultSet.next()){
                driver = new Driver();
                driver.setDriverId(resultSet.getInt("driverid"));
                driver.setName(resultSet.getString("name"));
                driver.setAge(resultSet.getInt("age"));
                driver.setLocation(resultSet.getString("location"));
            }

            LOGGER.info("Driver fetched successfully for ID: {}", id);
        } catch (SQLException e) {
            LOGGER.error("Error while fetching driver with ID: {}", id, e);
        }finally {
            try{
                if (resultSet != null) {
                    resultSet.close();
                }
                if (callablest != null) {
                    callablest.close();
                }
            } catch (SQLException e) {
                LOGGER.error("Error while closing resources for getDriverById", e);
            }
            DataSourceUtils.releaseConnection(connection, jdbcTemplate.getDataSource());

        }
        return Optional.ofNullable(driver);
    }

    public List<Driver> findAll() {
//        String sql = "SELECT * FROM get_drivers()";
//        return jdbcTemplate.query(sql, new DriverRowMapper());

        Connection connection = null;
        CallableStatement callableSt = null;
        Long startTime = System.currentTimeMillis();
        ResultSet resultSet = null;
        List<Driver> list = null;
        try {
            connection = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
            callableSt = connection.prepareCall(SettingUpDaoConstant.ISettingUp.SETUP_FIND_ALL);
            callableSt.execute();
            resultSet = callableSt.getResultSet();
            if(resultSet != null ) {
                list = new ArrayList<>();
                while (resultSet.next()) {
                    Driver driver = new Driver();
                    driver.setDriverId(resultSet.getInt("rDriverId"));
                    driver.setName(resultSet.getString("rName"));
                    driver.setAge(resultSet.getInt("rAge"));
                    driver.setLocation(resultSet.getString("rLocation"));
                    list.add(driver);
                }
            }

            LOGGER.info("Drivers fetched successfully");


        } catch (SQLException e) {
            LOGGER.error("Error while fetching drivers", e);

        }finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (callableSt != null) {
                    callableSt.close();
                }
            } catch (SQLException e) {
                LOGGER.error("Error while closing resources", e);
            }
            DataSourceUtils.releaseConnection(connection, jdbcTemplate.getDataSource());

            long endTime = System.currentTimeMillis();
            LOGGER.info("findAll() executed in {} ms", (endTime - startTime));
        }
        return list;
    }


    public void updateDriver(Driver driver) {
//        String sql = "CALL update_driver(?, ?, ?, ?)";
//        jdbcTemplate.update(sql, driver.getDriverId(), driver.getName(), driver.getAge(), driver.getLocation());
        Connection connection = null;
        CallableStatement callablest = null;

        try {
            connection = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
            callablest = connection.prepareCall(SettingUpDaoConstant.ISettingUp.UPDATE_Driver);
            callablest.setObject(1, driver.getDriverId(), Types.INTEGER);
            callablest.setObject(2, driver.getName(), Types.VARCHAR);
            callablest.setObject(3, driver.getDriverId(), Types.INTEGER);
            callablest.setObject(4, driver.getLocation(), Types.VARCHAR);
            callablest.execute();

            LOGGER.info("Driver Updated Successfully");

        } catch (SQLException e) {
            LOGGER.error("Error while updating driver with ID {}", driver.getDriverId() ,e);
        }finally {
            try {

                if (callablest != null) {
                    callablest.close();
                }
            } catch (SQLException e) {
                LOGGER.error("Error while closing resources for updateDriver", e);
            }
            DataSourceUtils.releaseConnection(connection, jdbcTemplate.getDataSource());

        }

    }

    public void deleteDriver(int id) {
//        String sql = "CALL delete_driver(?)";
//        jdbcTemplate.update(sql, driver.getDriverId());

        Connection connection = null;
        CallableStatement callablest = null;
        boolean isInserted = false;

        try {
            connection = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
            callablest = connection.prepareCall(SettingUpDaoConstant.ISettingUp.DELETE_DRIVER);
            callablest.setObject(1, id, Types.VARCHAR);

            ResultSet rs = callablest.executeQuery();
            if (rs.next()) {
                isInserted = rs.getBoolean(1);  // Assuming the function returns a boolean value
            }

            LOGGER.info("Driver Deleted Successfully");

        } catch (SQLException e) {
            LOGGER.error("Error while deleting driver with ID {}", id ,e);
        }finally {
            try {

                if (callablest != null) {
                    callablest.close();
                }
            } catch (SQLException e) {
                LOGGER.error("Error while closing resources for insertDriver", e);
            }
            DataSourceUtils.releaseConnection(connection, jdbcTemplate.getDataSource());

        }
    }

}
