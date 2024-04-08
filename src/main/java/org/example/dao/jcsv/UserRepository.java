package org.example.dao.jcsv;

import org.example.dao.IUserRepository;
import org.example.model.User;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserRepository implements IUserRepository {
    public List<User> users;
    private static UserRepository instance;
    String file;

    public void Save(){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
            writer.write(users.stream()
                    .map(User::toCSV)
                    .collect(Collectors.joining()));
            writer.close();
        } catch (IOException e) {
            System.out.println("Failed to write to the file.");
            e.printStackTrace();
        }
    }
    public void Load(){
        List<String> record;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 5) {
                    continue; // Skip lines with too few fields
                }
                record = Arrays.stream(values).toList();
                String login = record.get(1);
                String password = record.get(2);
                String rentedPlate = record.get(4);
                if(record.get(3).equals("ADMIN")){
                    users.add(new User(login, password, User.Role.ADMIN, rentedPlate));
                }else {
                    users.add(new User(login, password, User.Role.USER, rentedPlate));
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to open the file.");
            // Consider what to do here. Maybe initialize users as an empty list?
        }
    }
    @Override
    public User getUser(String login) {
        return users.stream().filter(user -> user.getLogin().equals(login)).findFirst().orElse(null);
    }

    @Override
    public void addUser(User user) {
        users.add(user);
        Save();
    }

    @Override
    public void removeUser(String login) {
        users.removeIf(user -> user.getLogin().equals(login));
        Save();
    }

    @Override
    public Collection<User> getUsers() {
        return users;
    }

    public static UserRepository getInstance(String file){
        if(UserRepository.instance==null){
            UserRepository.instance = new UserRepository(file);
        }
        return instance;
    }
    private UserRepository(String file) {
        this.file = file;
        users = new java.util.ArrayList<>();
        Load();
    }
    public void setFile(String file) {
        this.file = file;
    }
}
