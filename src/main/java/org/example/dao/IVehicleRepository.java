package org.example.dao;

import org.example.model.Vehicle;

import java.util.Collection;

public interface IVehicleRepository {

    boolean rentVehicle(String plate, String login);

    boolean returnVehicle(String plate, String login);

    boolean addVehicle(Vehicle vehicle);

    boolean removeVehicle(String plate);

    Vehicle getVehicle(String plate);

    Collection<Vehicle> getVehicles();

}
