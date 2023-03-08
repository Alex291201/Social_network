package com.example.socialnetwork.repo;

import com.example.socialnetwork.domain.Friendship;
import com.example.socialnetwork.domain.Message;
import com.example.socialnetwork.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MessageDatabaseRepository extends AbstractDatabaseRepository<Message, Integer> {
    private final UserDatabaseRepository userRepo;
    public MessageDatabaseRepository(Connection con, UserDatabaseRepository userRepo) {
        super(con);
        this.userRepo = userRepo;
    }

    @Override
    protected void insertEntity(Message entity) throws SQLException {

        String sql="INSERT INTO message (sender,receiver,msg) VALUES (?,?,?)";
        PreparedStatement statement = con.prepareStatement(sql);
        statement.setInt(1, entity.getFrom().getId());
        statement.setInt(2, entity.getTo().getId());
        statement.setString(3, entity.getMessage());
        statement.executeUpdate();
    }

    @Override
    public List get_all(){
        String sql = "Select * From message";
        PreparedStatement statement= null;
        try {
            statement = con.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            List<Message> list = new ArrayList<Message>();
            while(result.next()) {
                int id = result.getInt("id");
                User sender = userRepo.find(result.getInt("sender"));
                User receiver = userRepo.find(result.getInt("receiver"));
                String mesaj = result.getString("msg");
                Message mesajj = new Message(id,receiver,sender,mesaj);
                list.add(mesajj);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void deleteEntity(Message entity) throws SQLException {
        String sql="DELETE FROM message WHERE id=?";
        PreparedStatement statement=con.prepareStatement(sql);
        statement.setInt(1,entity.getId());
        statement.executeUpdate();
    }




    @Override
    protected Message getEntity(Integer integer) throws SQLException {
        String sql = "Select * From message Where id=?";
        PreparedStatement statement=con.prepareStatement(sql);
        statement.setInt(1,integer);
        ResultSet result = statement.executeQuery();
        if(result.next()) {
            int id = result.getInt("id");
            User sender = userRepo.find(result.getInt("sender"));
            User receiver = userRepo.find(result.getInt("receiver"));
            String mesaj = result.getString("msg");
            Message mesajj = new Message(id,receiver,sender,mesaj);
            return mesajj;
        }
        return null;
    }

    public Message find(int user1, int user2){
        try {
            return getEntity(user1, user2);
        } catch (SQLException e) {
            return null;
        }
    }
    public List<Message> getMessages (int sender, int receiver) throws SQLException{
        String sql = "Select * From message Where (sender=? and receiver=?) or (sender=? and receiver=?)";
        PreparedStatement statement=con.prepareStatement(sql);
        statement.setInt(1,sender);
        statement.setInt(2,receiver);
        statement.setInt(3,receiver);
        statement.setInt(4,sender);
        ResultSet result = statement.executeQuery();
        List<Message> list = new ArrayList<Message>();
        while(result.next())
        {
            int id = result.getInt("id");
            User u1 = userRepo.find(result.getInt("sender"));
            User u2 = userRepo.find(result.getInt("receiver"));
            String mesaj = result.getString("msg");
            Message mesajj = new Message(id,u2,u1,mesaj);
            list.add(mesajj);
        }

        return list;
    }


    private Message getEntity(int sender, int receiver) throws SQLException{
        String sql = "Select * From message Where (sender=? and receiver=?) or (sender=? and receiver=?)";
        PreparedStatement statement=con.prepareStatement(sql);
        statement.setInt(1,sender);
        statement.setInt(2,receiver);
        statement.setInt(3,receiver);
        statement.setInt(4,sender);
        ResultSet result = statement.executeQuery();

        if(result.next())
        {
            int id = result.getInt("id");
            User u1 = userRepo.find(result.getInt("sender"));
            User u2 = userRepo.find(result.getInt("receiver"));
            String mesaj = result.getString("msg");
            Message mesajj = new Message(id,u2,u1,mesaj);
            return mesajj;
        }

        return null;
    }
}
