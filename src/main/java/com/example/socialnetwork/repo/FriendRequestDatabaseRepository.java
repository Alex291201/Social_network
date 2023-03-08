package com.example.socialnetwork.repo;

import com.example.socialnetwork.domain.FriendRequest;
import com.example.socialnetwork.domain.Friendship;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.exceptions.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FriendRequestDatabaseRepository extends AbstractDatabaseRepository<FriendRequest,Integer> {

    private final UserDatabaseRepository userRepo;

    public FriendRequestDatabaseRepository(Connection con, UserDatabaseRepository userRepo) {
        super(con);
        this.userRepo=userRepo;
    }

    @Override
    protected void insertEntity(FriendRequest entity) throws SQLException {
        String sql="INSERT INTO requests (sentto,sentfrom,sentat) VALUES (?,?,?)";
        PreparedStatement statement = con.prepareStatement(sql);
        statement.setInt(1, entity.getTo().getId());
        statement.setInt(2, entity.getFrom().getId());
        statement.setDate(3, java.sql.Date.valueOf(entity.getSentat().toString()));
        statement.executeUpdate();
    }

    @Override
    protected void deleteEntity(FriendRequest entity) throws SQLException {
        String sql="DELETE FROM requests WHERE id = ?";

        PreparedStatement statement=con.prepareStatement(sql);
        statement.setInt(1,entity.getId());
        statement.executeUpdate();
    }

    @Override
    protected FriendRequest getEntity(Integer integer) throws SQLException, RepositoryException {
        String sql = "Select * From requests Where id=?";
        PreparedStatement statement=con.prepareStatement(sql);
        statement.setInt(1,integer);
        ResultSet result = statement.executeQuery();
        if(result.next()) {
            int id = result.getInt("id");
            User user1 = userRepo.find(result.getInt("sentto"));
            User user2 = userRepo.find(result.getInt("sentfrom"));
            LocalDate date = result.getDate("sentat").toLocalDate();
            FriendRequest friendship = new FriendRequest(id, user1, user2);
            friendship.setSentat(date);
            return friendship;
        }
        return null;
    }
    public FriendRequest find(int receiver, int sender){
        try {
            return getEntity(receiver,sender);
        } catch (SQLException e) {
            return null;
        }
    }
    @Override
    public List<FriendRequest> get_all() throws SQLException, RepositoryException {
        String sql = "Select * From requests";
        PreparedStatement statement= null;
        try {
            statement = con.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            List<FriendRequest> list = new ArrayList<FriendRequest>();
            while(result.next()) {
                int id = result.getInt("id");
                User to = userRepo.find(result.getInt("sentto"));
                User from = userRepo.find(result.getInt("sentfrom"));
                LocalDate date = result.getDate("sentat").toLocalDate();
                FriendRequest friendship = new FriendRequest(id, from, to);
                friendship.setSentat(date);
                list.add(friendship);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private FriendRequest getEntity(int receiver, int sender) throws SQLException{
        String sql = "Select * From requests Where (sentto=? and sentfrom=?) ";
        PreparedStatement statement=con.prepareStatement(sql);
        statement.setInt(1,receiver);
        statement.setInt(2,sender);
        ResultSet result = statement.executeQuery();
        if(result.next())
        {
            int id = result.getInt("id");
            User u1 = userRepo.find(result.getInt("sentto"));
            User u2 = userRepo.find(result.getInt("sentfrom"));
            LocalDate date = result.getDate("sentat").toLocalDate();
            FriendRequest friendship = new FriendRequest(id, u1, u2);
            friendship.setSentat(date);
            return friendship;
        }

        return null;
    }
}
