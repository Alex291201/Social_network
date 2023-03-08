package com.example.socialnetwork;

import com.example.socialnetwork.domain.FriendRequest;
import com.example.socialnetwork.domain.Friendship;
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
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class Profile {
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
    private ListView<User> Friend_list;
    @FXML
    private Text current_user;

    @FXML
    private TextField Friend_user;

    public Profile() throws SQLException {
    }

    @FXML
    private void initialize(){
        Data data = Data.getInstance();
        Integer id = data.getUser();
        Friend_list.getItems().clear();

        for (User u: service.getAll())
            if(Objects.equals(u.getId(), id)){

                current_user.setText(u.getFirst_name());}
        current_user.setTextAlignment(TextAlignment.CENTER);
        for(Friendship f : service.getAllFriendships())
        {
            if (Objects.equals(f.getUser1().getId(), id)){

                Friend_list.getItems().add(f.getUser2());
            }
            if (Objects.equals(f.getUser2().getId(), id)){
                Friend_list.getItems().add(f.getUser1());
            }

        }
        Friend_list.setCellFactory(list -> new ListCell<User>(){
            @Override
            protected void updateItem(User item, boolean empty){
                super.updateItem(item,empty);
                if(item==null||empty){
                    setText(null);
                }else{setText(item.getFirst_name()+" "+item.getLast_name());}
            }
        });



    }

    public void del(ActionEvent actionEvent) throws ServiceException, RepositoryException {
        User u=Friend_list.getSelectionModel().getSelectedItem();
        Data data = Data.getInstance();
        Integer id = data.getUser();

        for (User ur: service.getAll()){
            if (Objects.equals(ur.getId(), id)){
                service.removeFriendship(u.getUsername(),ur.getUsername());
            }
        }
        initialize();
    }

    public void sendre(ActionEvent actionEvent) throws RepositoryException, ServiceException {
        Data data = Data.getInstance();

        Integer id = data.getUser();

        String username2 = Friend_user.getText();

        service.addFriendReq(usersDatabaseRepository.find(id).getUsername(),username2);


    }

    public void next(ActionEvent actionEvent) throws IOException {
        HelloApplication m = new HelloApplication();
        m.changeScene("my_friend_request.fxml");
    }

    public void next2(ActionEvent actionEvent) throws IOException {
        HelloApplication m = new HelloApplication();
        m.changeScene("send_req.fxml");
    }

    public void chat(ActionEvent actionEvent) throws IOException {
        User u=Friend_list.getSelectionModel().getSelectedItem();
        Data data = Data.getInstance();
        data.setFriend(u.getId());
        HelloApplication m = new HelloApplication();
        m.changeScene("my_chat.fxml");
    }
}
