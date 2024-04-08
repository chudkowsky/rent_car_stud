package org.example.dao.jcsv;

import org.example.dao.IVehicleRepository;
import org.example.model.Vehicle;

import java.util.Collection;

public class VehicleRepository implements IVehicleRepository {
    @Override
    public boolean rentVehicle(String plate, String login) {
        return false;
    }

    @Override
    public boolean returnVehicle(String plate, String login) {
        return false;
    }

    @Override
    public boolean addVehicle(Vehicle vehicle) {
        return false;
    }

    @Override
    public boolean removeVehicle(String plate) {
        return false;
    }

    @Override
    public Vehicle getVehicle(String plate) {
        return null;
    }

    @Override
    public Collection<Vehicle> getVehicles() {
        return null;
    }
}
