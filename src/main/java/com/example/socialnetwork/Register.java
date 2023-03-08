package com.example.socialnetwork;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.domain.validators.UserValidator;
import com.example.socialnetwork.domain.validators.Validator;
import com.example.socialnetwork.exceptions.RepositoryException;
import com.example.socialnetwork.exceptions.ValidationException;
import com.example.socialnetwork.repo.FriendRequestDatabaseRepository;
import com.example.socialnetwork.repo.FriendshipDatabaseRepository;
import com.example.socialnetwork.repo.MessageDatabaseRepository;
import com.example.socialnetwork.repo.UserDatabaseRepository;
import com.example.socialnetwork.service.UsersService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class Register {
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
    private TextField username;
    @FXML
    private TextField first_name;
    @FXML
    private TextField last_name;
    @FXML
    private PasswordField pass;
    @FXML
    private PasswordField pass2;
    @FXML
    private TextField email;
    public Register() throws SQLException {
    }

    public void back(ActionEvent actionEvent) throws IOException {
        HelloApplication m = new HelloApplication();
        m.changeScene("hello-view.fxml");
    }

    public void submit(ActionEvent actionEvent) throws ValidationException, RepositoryException, IOException {
        Data data = Data.getInstance();
        int ok=1;
        if (!Objects.equals(pass.getText(), pass2.getText())){
            System.out.println("err");
            ok=0;
            username.setText(null);
            first_name.setText(null);
            last_name.setText(null);
            pass.setText(null);
            pass2.setText(null);
            email.setText(null);}
        for (User user : service.getAll()){
            if(username.getText().equals(user.getUsername())){
                //exista deja
                ok = 0;
                username.setText(null);
                first_name.setText(null);
                last_name.setText(null);
                pass.setText(null);
                pass2.setText(null);
                email.setText(null);

            }
        }
        if(ok==1){
            Integer id = service.add(username.getText(),first_name.getText(),last_name.getText(),pass.getText(),email.getText());
            data.setUser(id);
            HelloApplication m = new HelloApplication();
            m.changeScene("profile.fxml");
        }
    }
}
