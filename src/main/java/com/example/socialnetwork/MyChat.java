package com.example.socialnetwork;

import com.example.socialnetwork.domain.FriendRequest;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.domain.validators.UserValidator;
import com.example.socialnetwork.domain.validators.Validator;
import com.example.socialnetwork.exceptions.RepositoryException;
import com.example.socialnetwork.exceptions.ServiceException;
import com.example.socialnetwork.repo.FriendRequestDatabaseRepository;
import com.example.socialnetwork.repo.FriendshipDatabaseRepository;
import com.example.socialnetwork.repo.MessageDatabaseRepository;
import com.example.socialnetwork.repo.UserDatabaseRepository;
import com.example.socialnetwork.service.UsersService;
import com.example.socialnetwork.domain.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class MyChat{  String url = "jdbc:postgresql://localhost:5432/socialnetwork";
    String userName = "postgres";
    String password = "alex11@AA";

    final Connection connection = DriverManager.getConnection(url, userName, password);
    Validator<User> userValidator = new UserValidator();

    UserDatabaseRepository usersDatabaseRepository = new UserDatabaseRepository(connection);
    FriendshipDatabaseRepository friendshipDatabaseRepository = new FriendshipDatabaseRepository(connection,usersDatabaseRepository);

    FriendRequestDatabaseRepository friendrequestdatabserepository= new FriendRequestDatabaseRepository(connection,usersDatabaseRepository);
    MessageDatabaseRepository messagedatabaserepository = new MessageDatabaseRepository(connection,usersDatabaseRepository);
    UsersService service = new UsersService(usersDatabaseRepository,friendshipDatabaseRepository, friendrequestdatabserepository, messagedatabaserepository,userValidator);

    @FXML
    private TextField Message;
    @FXML
    private TextArea Chat_box;
    
    public MyChat() throws SQLException {
    }

    @FXML
    private void initialize() throws SQLException, RepositoryException {
        Data data = Data.getInstance();
        Integer receiver = data.getFriend();
        Integer sender = data.getUser();
        Chat_box.clear();
        for (Message msg: service.getAll_msg(receiver,sender)){
            Chat_box.appendText(msg.getFrom().getFirst_name() + ": " + msg.getMessage() + "\n");
        }

    }
    public void back(ActionEvent actionEvent) throws IOException {
        HelloApplication m = new HelloApplication();
        m.changeScene("profile.fxml");
    }


    public void send(ActionEvent actionEvent) throws ServiceException, RepositoryException, SQLException {
        Data data = Data.getInstance();
        Integer receiver = data.getFriend();
        Integer sender = data.getUser();
        String msg = Message.getText();
        service.addMessage(sender,receiver,msg);
        Message.clear();
        initialize();
    }

    public void refresh(ActionEvent actionEvent) throws SQLException, RepositoryException {
        initialize();
    }
}