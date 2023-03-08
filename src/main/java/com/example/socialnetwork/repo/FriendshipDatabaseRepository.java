package com.example.socialnetwork.repo;

import com.example.socialnetwork.domain.Friendship;
import com.example.socialnetwork.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FriendshipDatabaseRepository extends AbstractDatabaseRepository<Friendship, Integer> {
    private final UserDatabaseRepository userRepo;
    public FriendshipDatabaseRepository(Connection con, UserDatabaseRepository userRepo) {
        super(con);
        this.userRepo = userRepo;
    }

    @Override
    protected void insertEntity(Friendship entity) throws SQLException {

        String sql="INSERT INTO Friendships (user1,user2,friendsFrom) VALUES (?,?,?)";
        PreparedStatement statement = con.prepareStatement(sql);
        statement.setInt(1, entity.getUser1().getId());
        statement.setInt(2, entity.getUser2().getId());
        statement.setDate(3, java.sql.Date.valueOf(entity.getFriendsfrom().toString()));
        statement.executeUpdate();
    }

    @Override
    public List get_all(){
        String sql = "Select * From friendships";
        PreparedStatement statement= null;
        try {
            statement = con.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            List<Friendship> list = new ArrayList<Friendship>();
            while(result.next()) {
                int id = result.getInt("id");
                User user1 = userRepo.find(result.getInt("user1"));
                User user2 = userRepo.find(result.getInt("user2"));
                LocalDate date = result.getDate("friendsFrom").toLocalDate();
                Friendship friendship = new Friendship(id, user1, user2);
                friendship.setFriendsfrom(date);
                list.add(friendship);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void deleteEntity(Friendship entity) throws SQLException {
        String sql="DELETE FROM Friendships WHERE id=?";
        PreparedStatement statement=con.prepareStatement(sql);
        statement.setInt(1,entity.getId());
        statement.executeUpdate();
    }

    @Override
    protected Friendship getEntity(Integer integer) throws SQLException {
        String sql = "Select * From Friendships Where id=?";
        PreparedStatement statement=con.prepareStatement(sql);
        statement.setInt(1,integer);
        ResultSet result = statement.executeQuery();
        if(result.next()) {
            int id = result.getInt("id");
            User user1 = userRepo.find(result.getInt("user1"));
            User user2 = userRepo.find(result.getInt("user2"));
            LocalDate date = result.getDate("friendsFrom").toLocalDate();
            Friendship friendship = new Friendship(id, user1, user2);
            friendship.setFriendsfrom(date);
            return friendship;
        }
        return null;
    }

    public Friendship find(int user1, int user2){
        try {
            return getEntity(user1, user2);
        } catch (SQLException e) {
            return null;
        }
    }
    public List<Friendship> getFriendships(int user) throws SQLException
    {
        String sql = "Select * From friendships Where (user1=?) or (user2=?)";
        PreparedStatement statement=con.prepareStatement(sql);
        statement.setInt(1,user);
        statement.setInt(2,user);
        ResultSet result = statement.executeQuery();
        List<Friendship> list = new ArrayList<Friendship>();
        while(result.next()) {
            int id = result.getInt("id");
            User user1 = userRepo.find(result.getInt("user1"));
            User user2 = userRepo.find(result.getInt("user2"));
            LocalDate date = result.getDate("friendsFrom").toLocalDate();
            Friendship friendship = new Friendship(id, user1, user2);
            friendship.setFriendsfrom(date);
            list.add(friendship);
        }
        return list;
    }
    private Friendship getEntity(int user1, int user2) throws SQLException{
        String sql = "Select * From friendships Where (user1=? and user2=?) or (user1=? and user2=?)";
        PreparedStatement statement=con.prepareStatement(sql);
        statement.setInt(1,user1);
        statement.setInt(2,user2);
        statement.setInt(4,user1);
        statement.setInt(3,user2);
        ResultSet result = statement.executeQuery();
        if(result.next())
        {
            int id = result.getInt("id");
            User u1 = userRepo.find(result.getInt("user1"));
            User u2 = userRepo.find(result.getInt("user2"));
            LocalDate date = result.getDate("friendsFrom").toLocalDate();
            Friendship friendship = new Friendship(id, u1, u2);
            friendship.setFriendsfrom(date);
            return friendship;
        }

        return null;
    }
}
