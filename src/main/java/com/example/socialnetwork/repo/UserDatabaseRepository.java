package com.example.socialnetwork.repo;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.exceptions.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDatabaseRepository extends AbstractDatabaseRepository<User, Integer> {

    public UserDatabaseRepository(Connection con) {
        super(con);
    }

    public User find(String username) throws RepositoryException {
        try {
            return getEntity(username);
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    protected void insertEntity(User entity) throws SQLException {

        if(getEntity(entity.getUsername())==null) {
            String sql = "INSERT INTO Users(username,first_name,last_name,password,email) VALUES (?,?,?,?,?)";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getFirst_name());
            statement.setString(3, entity.getLast_name());
            statement.setString(4, entity.getPassword());
            statement.setString(5, entity.getEmail());

            statement.executeUpdate();
        }
    }

    @Override
    public List get_all(){
        String sql = "Select * From Users";
        PreparedStatement statement= null;
        try {
            statement = con.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            List<User> list = new ArrayList<User>();
            while(result.next()) {
                list.add(new User(result.getInt("id"), result.getString("username"), result.getString("first_name"),result.getString("last_name"),result.getString("password"), result.getString("email")));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void deleteEntity(User entity) throws SQLException {
        String sql="DELETE FROM Users WHERE id=?";
        PreparedStatement statement=con.prepareStatement(sql);
        statement.setInt(1,entity.getId());
        statement.executeUpdate();
    }

    @Override
    protected User getEntity(Integer integer) throws SQLException {
        String sql = "Select * From Users Where id=?";
        PreparedStatement statement=con.prepareStatement(sql);
        statement.setInt(1,integer);
        ResultSet result = statement.executeQuery();
        if(result.next())
            return new User(result.getInt("id"), result.getString("username"), result.getString("first_name"),result.getString("last_name"),result.getString("email"), result.getString("password"));
        return null;
    }

    protected User getEntity(String username) throws SQLException {
        String sql = "Select * From Users Where username=?";
        PreparedStatement statement=con.prepareStatement(sql);
        statement.setString(1,username);
        ResultSet result = statement.executeQuery();
        if(result.next())
            return new User(result.getInt("id"), result.getString("username"), result.getString("first_name"),result.getString("last_name"),result.getString("email"), result.getString("password"));
        return null;
    }
}
