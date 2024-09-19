package com.shath.DriverService.dao;

public class SettingUpDaoConstant {

    public interface ISettingUp{

        String SETUP_FIND_ALL = "{call public.get_drivers()}";
        String SETUP_FIND_BY_ID = "{call public.get_driver(?)}";
        String GET_DRIVER_BY_LOCATION = "{call get_driverByLoc(?)}";
        String UPDATE_Driver = "{call update_driver(?, ?, ?, ?)}";
        String INSERT_DRIVER = "{call insert_driver(?, ?, ?)}";
        String DELETE_DRIVER = "{call  delete_driver(?)}";


    }
}
