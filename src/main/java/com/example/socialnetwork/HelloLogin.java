package com.example.socialnetwork;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.domain.validators.UserValidator;
import com.example.socialnetwork.domain.validators.Validator;
import com.example.socialnetwork.repo.FriendRequestDatabaseRepository;
import com.example.socialnetwork.repo.FriendshipDatabaseRepository;
import com.example.socialnetwork.repo.MessageDatabaseRepository;
import com.example.socialnetwork.repo.UserDatabaseRepository;
import com.example.socialnetwork.service.UsersService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HelloLogin {
    String url = "jdbc:postgresql://localhost:5432/socialnetwork";
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
    private Button signUpButton;
    @FXML
    private Button loginButton;
    @FXML

    private TextField username;
    @FXML
    private PasswordField pass;


    public HelloLogin() throws SQLException {
    }

    public void logIn(ActionEvent actionEvent) throws IOException {
        Data data = Data.getInstance();
        int ok = 0;

        for (User user: service.getAll()){
            if(username.getText().equals(user.getUsername())&&pass.getText().equals(user.getPassword())){
                ok = 1;
                data.setUser(user.getId());
                HelloApplication m = new HelloApplication();
                m.changeScene("profile.fxml");
            }
        }


    }

    public void signIn(ActionEvent actionEvent) throws IOException {
            HelloApplication m = new HelloApplication();
            m.changeScene("register.fxml");


    }
}
