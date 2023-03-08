package com.example.socialnetwork;

import com.example.socialnetwork.domain.Friendship;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.exceptions.RepositoryException;
import com.example.socialnetwork.exceptions.ServiceException;
import com.example.socialnetwork.exceptions.ValidationException;
import com.example.socialnetwork.service.UsersService;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class UserInterface {
    private final UsersService userService;

    public UserInterface(UsersService userService) {
        this.userService = userService;
    }

    private void menu(){
        System.out.println("Choose one option: ");
        System.out.println("0. Exit");
        System.out.println("1. Add User");
        System.out.println("2. Remove User");
        System.out.println("3. Login");
        System.out.println("4. Add Friend");
        System.out.println("5. Remove Friend");
        System.out.println("6. Show friendships.");
    }
    public void run(){
        Scanner in = new Scanner(System.in);

        String val;
        String current_user = null;

        label:
        while(true){
            menu();

            try{
                val = in.nextLine();
                switch (val) {
                    case "1":
                        System.out.println(val);
                        uiAddUser(in);
                        break;
                    case "2":
                        uiRemoveUser(in,current_user);
                        break;
                    case "3":
                        current_user=uiLogin(in);
                        break;
                    case "4":
                        uiAddFriend(in,current_user);
                        break;
                    case "5":
                        uiRemoveFriend(in,current_user);
                        break;
                    case "6":
                        uiShowFriendList();
                        break;
                    case "0":
                        break label;
                }
            }
            catch(Exception e){
                System.out.println("The given value should be an integer");
            }
        }
    }

    private void uiShowFriendList() {
        List<Friendship>friends = userService.getAllFriendships();
        Friendship [] friendships = friends.toArray(new Friendship[0]);
        for(Friendship fr: friendships){
            System.out.println(fr.getUser1().getUsername() + " and " + fr.getUser2().getUsername() + " are friends");
        }
    }


    private void uiRemoveFriend(Scanner in,String user1) {

        System.out.println("Give username: ");
        String user2;
        user2=in.nextLine();
        try{
            userService.removeFriendship(user1,user2);
        }
        catch(RepositoryException | ServiceException re){
            System.out.println(re.getMessage());
        }
    }

    private void uiAddFriend(Scanner in,String user1) {
        System.out.println("Give second username: ");
        String user2;
        user2=in.nextLine();
        try{
            userService.addFriendship(user1,user2);
        }
        catch(RepositoryException | ServiceException re){
            System.out.println(re.getMessage());
        }
    }

    private String uiLogin(Scanner in) {
        while(true){
        String user;
        System.out.println("Give username: ");
        user=in.nextLine();
        System.out.println("Give password: ");
        String pas;
        pas=in.nextLine();
        List<User> users = userService.getAll();
        for(User u:users){
            if(Objects.equals(u.getUsername(), user) && Objects.equals(u.getPassword(), pas)) {
                System.out.println("loginsucc");
                return user;

            }
            }
            System.out.println("Incorrect pass or username");
        }
    }

    private void uiRemoveUser(Scanner in , String username) {

        try{
            userService.remove(username);
            System.out.println("User removed.\n");
        }
        catch(RepositoryException | SQLException re){
            System.out.println(re.getMessage());
        }
    }

    private void uiAddUser(Scanner in) {
        System.out.println("Give username: ");
        String username = in.nextLine();
        System.out.println("Give first_name: ");
        String first_name = in.nextLine();
        System.out.println("Give last_name: ");
        String last_name = in.nextLine();
        System.out.println("Give email: ");
        String email=in.nextLine();
        System.out.println("Give password: ");
        String password=in.nextLine();


        try{
            userService.add(username,first_name,last_name,password,email);
            System.out.println("User added.\n");
        }
        catch(ValidationException | RepositoryException ex){
            System.out.println(ex.getMessage());
        }

    }

}
