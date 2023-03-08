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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class MySentRequest {  String url = "jdbc:postgresql://localhost:5432/socialnetwork";
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
    private ListView<User> Request_list;

    public MySentRequest() throws SQLException {
    }
    @FXML
    private void initialize() throws SQLException, RepositoryException {
        Data data = Data.getInstance();
        Integer id = data.getUser();
        Request_list.getItems().clear();

        for(FriendRequest f : service.getAll_fr())
        {
            if (Objects.equals(f.getFrom().getId(), id)) {

                Request_list.getItems().add(f.getTo());
            }

        }
        Request_list.setCellFactory(list -> new ListCell<User>(){
            @Override
            protected void updateItem(User item, boolean empty){
                super.updateItem(item,empty);
                if(item==null||empty){
                    setText(null);
                }else{setText(item.getFirst_name()+" "+item.getLast_name());}
            }
        });



    }
    public void back(ActionEvent actionEvent) throws IOException {
        HelloApplication m = new HelloApplication();
        m.changeScene("profile.fxml");
    }


    public void decline(ActionEvent actionEvent) throws ServiceException, RepositoryException, SQLException {
        User u=Request_list.getSelectionModel().getSelectedItem();
        Data data = Data.getInstance();
        Integer id = data.getUser();

        for (User ur: service.getAll()){
            if (Objects.equals(ur.getId(), id)){
                service.removeFr(u.getUsername(),ur.getUsername());
            }
        }
        initialize();
    }
}