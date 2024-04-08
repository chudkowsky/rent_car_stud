package org.example.dao.jdbc;

import org.example.dao.IUserRepository;
import org.example.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class JdbcUserRepository implements IUserRepository {
    private static JdbcUserRepository instance;
    private final DatabaseManager databaseManager;

    private static final String GET_ALL_USERS_SQL = "SELECT login, password, role, rentedPlate FROM tuser";
    private static final String GET_USER_SQL = "SELECT * FROM tuser WHERE login LIKE ?";
    private static final String ADD_USER_SQL = "INSERT INTO tuser (login, password, role, rentedPlate) VALUES (?,?,?,?)";
    private static final String REMOVE_USER_SQL = "DELETE FROM tuser WHERE login LIKE ?";

    private JdbcUserRepository() {
        this.databaseManager = DatabaseManager.getInstance();
    }


    @Override
    public User getUser(String login) {
        User user = null;
        Connection connection = null;
        ResultSet rs = null;
        try {
            connection = databaseManager.getConnection();
            connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_SQL);
            preparedStatement.setString(1, login);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("login"),
                        rs.getString("password"),
                        User.Role.valueOf(rs.getString("role")), rs.getString("rentedPlate"));
                preparedStatement.close();
            } else {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (Exception e) {
            }
            try {
                if (rs != null) rs.close();
            } catch (Exception e) {
            }
        }
        return user;
    }

    @Override
    public void addUser(User user) {
        try (Connection connection = databaseManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(ADD_USER_SQL)) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole().name());
            preparedStatement.setString(4, user.getRentedPlate());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeUser(String login) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_USER_SQL)) {
            preparedStatement.setString(1, login);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<User> getUsers() {
        Collection<User> users = new ArrayList<>();
        try (Connection connection = databaseManager.getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(GET_ALL_USERS_SQL)) {
            while (resultSet.next()) {
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                User.Role role = User.Role.valueOf(resultSet.getString("role"));
                String plate = resultSet.getString("rentedPlate");
                User user = new User(login, password, role, plate);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }


    public static JdbcUserRepository getInstance() {
        if (JdbcUserRepository.instance == null) {
            JdbcUserRepository.instance = new JdbcUserRepository();
        }
        return instance;
    }

}
