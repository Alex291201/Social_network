package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.FriendRequest;
import com.example.socialnetwork.domain.Friendship;
import com.example.socialnetwork.domain.Message;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.domain.validators.Validator;
import com.example.socialnetwork.exceptions.RepositoryException;
import com.example.socialnetwork.exceptions.ServiceException;

import com.example.socialnetwork.exceptions.ValidationException;
import com.example.socialnetwork.repo.FriendRequestDatabaseRepository;
import com.example.socialnetwork.repo.FriendshipDatabaseRepository;
import com.example.socialnetwork.repo.MessageDatabaseRepository;
import com.example.socialnetwork.repo.UserDatabaseRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsersService {
    private final UserDatabaseRepository repo;

    private final FriendshipDatabaseRepository repoFriendships;

    private final FriendRequestDatabaseRepository repoFr;

    private final MessageDatabaseRepository repomsg;
    private final Validator<User> validator;


    public UsersService(UserDatabaseRepository repo, FriendshipDatabaseRepository repoFriendships, FriendRequestDatabaseRepository repoFr,MessageDatabaseRepository repomsg, Validator<User> validator) {
        this.repo = repo;
        this.repoFriendships = repoFriendships;
        this.repoFr = repoFr;
        this.repomsg=repomsg;
        this.validator = validator;
    }

    public void addFriendship(String user1, String user2) throws RepositoryException, ServiceException {
        User u1 = repo.find(user1);
        User u2 = repo.find(user2);
        if (u1 != null && u2 != null) {
            if (repoFriendships.find(u1.getId(), u2.getId()) != null)
                throw new ServiceException("Friendship already exists");
            Friendship friends = new Friendship(u1, u2);
            repoFriendships.add(friends);
        } else
            throw new ServiceException("Invalid users.\n");

    }
    public void addMessage(int sender,int receiver,String msg) throws ServiceException, RepositoryException {
        User sen = repo.find(sender);
        User rec = repo.find(receiver);
        if (sen != null && rec != null) {
            Message msj = new Message(rec,sen,msg);
            repomsg.add(msj);
        } else
            throw new ServiceException("Invalid users.\n");
    }
    public void addFriendReq(String user1, String user2) throws RepositoryException, ServiceException {
        User u1 = repo.find(user1);
        User u2 = repo.find(user2);
        if (u1 != null && u2 != null) {
            if (repoFriendships.find(u1.getId(), u2.getId()) != null || repoFr.find(u1.getId(), u2.getId()) != null)
                throw new ServiceException("Friendship already exists");
            FriendRequest friends = new FriendRequest(u1, u2);
            repoFr.add(friends);
        } else
            throw new ServiceException("Invalid users.\n");

    }
    public void removeFr(String receiver, String sender) throws RepositoryException, ServiceException {
        User to = repo.find(receiver);
        User from = repo.find(sender);
        if (to != null && from != null) {
            FriendRequest friends = repoFr.find(to.getId(),from.getId());
            if (friends != null) {

                repoFr.remove(friends);
            } else
                throw new ServiceException("Invalid friendship.\n");

        } else throw new ServiceException("Invalid users.\n");
    }

    public void removeFriendship(String user1, String user2) throws RepositoryException, ServiceException {
        User u1 = repo.find(user1);
        User u2 = repo.find(user2);
        if (u1 != null && u2 != null) {
            Friendship friends = repoFriendships.find(u1.getId(), u2.getId());
            if (friends != null) {

                repoFriendships.remove(friends);
            } else
                throw new ServiceException("Invalid friendship.\n");

        } else throw new ServiceException("Invalid users.\n");
    }

    public Integer add(String username,String first_name,String last_name, String email, String password) throws RepositoryException, ValidationException {
        User u = new User(username, first_name,last_name,password, email);
        validator.validate(u);
        repo.add(u);
        return u.getId();

    }

    public void remove(String username) throws RepositoryException, SQLException {
        User user = repo.find(username);
        List<Friendship> list= new ArrayList<Friendship>();
        list = repoFriendships.getFriendships(user.getId());
        for (Friendship friendship:list
             ) {
            repoFriendships.remove(friendship);

        }
            repo.remove(user);

    }
    public List<Friendship> getAllFriendships(){
        return repoFriendships.get_all();
    }

    public List<User> getAll() {
        return repo.get_all();
    }

    public List<Message> getAll_msg(int receiver,int sender) throws SQLException {
        return repomsg.getMessages(sender,receiver);
    }
    public List<FriendRequest> getAll_fr() throws SQLException, RepositoryException {
        return repoFr.get_all();
    }
}