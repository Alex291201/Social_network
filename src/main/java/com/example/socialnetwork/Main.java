package com.example.socialnetwork;

import com.example.socialnetwork.domain.validators.UserValidator;
import com.example.socialnetwork.repo.FriendRequestDatabaseRepository;
import com.example.socialnetwork.repo.FriendshipDatabaseRepository;
import com.example.socialnetwork.repo.MessageDatabaseRepository;
import com.example.socialnetwork.repo.UserDatabaseRepository;
import com.example.socialnetwork.service.UsersService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String [] args){
        final String url = "jdbc:postgresql://localhost:5432/socialnetwork";
        final String userName = "postgres";
        final String password = "alex11@AA";
        try {
            final Connection connection= DriverManager.getConnection(url,userName,password);
            UserDatabaseRepository repo = new UserDatabaseRepository(connection);
            FriendshipDatabaseRepository fRepo = new FriendshipDatabaseRepository(connection, repo);
            FriendRequestDatabaseRepository repoFr = new FriendRequestDatabaseRepository(connection,repo);
            MessageDatabaseRepository repomsg = new MessageDatabaseRepository(connection,repo);
            UsersService userService = new UsersService(repo, fRepo, repoFr, repomsg,new UserValidator());
            UserInterface ui= new UserInterface(userService);
            ui.run();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}