package org.example.dao.jcsv;

import org.example.dao.IVehicleRepository;
import org.example.model.Car;
import org.example.model.Motorcycle;
import org.example.model.Vehicle;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class VehicleRepository implements IVehicleRepository {
    private static VehicleRepository instance;
    public List<Vehicle> vehicles;
    String file;

    public static VehicleRepository getInstance(String file) {
        if (VehicleRepository.instance == null) {
            instance = new VehicleRepository(file);

        }
        return instance;
    }

    private VehicleRepository(String file) {
        vehicles = new ArrayList<>();
        this.file = file;
        Load();
    }

    public void Load() {
        List<String> record;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 6) {
                    continue; // Skip lines with too few fields
                }
                record = Arrays.stream(values).toList();
                String brand = record.get(1);
                String model = record.get(2);
                int year = Integer.parseInt(record.get(3));
                double price = Double.parseDouble(record.get(4));
                String plate = record.get(5);
                if (record.get(0).equals("Car")) {
                    vehicles.add(new Car(brand, model, year, price, plate));
                } else {
                    String category = record.get(7);
                    vehicles.add(new Motorcycle(brand, model, year, price, plate, category));
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to open the file.");
            // Consider what to do here. Maybe initialize users as an empty list?
        }
    }

    public void Save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
            writer.write(vehicles.stream()
                    .map(Vehicle::toCSV)
                    .collect(Collectors.joining()));
            writer.close();
        } catch (IOException e) {
            System.out.println("Failed to write to the file.");
            e.printStackTrace();
        }
    }

    @Override
    public boolean rentVehicle(String plate, String login) {
        UserRepository.getInstance("src/main/resources/users.csv").getUser(login).setRentedPlate(plate);
        UserRepository.getInstance("src/main/resources/users.csv").Save();
        getVehicle(plate).setRent(true);
        Save();
        return false;
    }

    @Override
    public boolean returnVehicle(String plate, String login) {
        UserRepository.getInstance("src/main/resources/users.csv").getUser(login).setRentedPlate(null);
        UserRepository.getInstance("src/main/resources/users.csv").Save();
        getVehicle(plate).setRent(false);
        Save();
        return false;
    }

    @Override
    public boolean addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        Save();
        return true;
    }

    @Override
    public boolean removeVehicle(String plate) {
        vehicles.remove(getVehicle(plate));
        Save();
        return false;
    }

    @Override
    public Vehicle getVehicle(String plate) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getPlate().equals(plate)) {
                return vehicle;
            }
        }
        return null;
    }

    @Override
    public Collection<Vehicle> getVehicles() {
        return vehicles;
    }
}
