package net.atos.spring_webapp;

import net.atos.spring_webapp.model.User;
import net.atos.spring_webapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class AppStarter implements CommandLineRunner {
    private UserRepository userRepository;
    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private PermissionRepository permissionRepository;
    private MessageRepository messageRepository;

    @Autowired
    public AppStarter(UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository, PermissionRepository permissionRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.permissionRepository = permissionRepository;
        this.messageRepository = messageRepository;
    }
    private void printUsers(List<User> users){
        users.stream().forEach(
                user -> System.out.printf("|%10d|%10s|%10s|%10b|%15s|\n",
                        user.getUserId(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getEnable(),
                        user.getRegisterDate())
        );
    }

    private void getAllUsers(){
        // wypisz wszystkich użytkowników
        List<User> users = userRepository.findAll();
        printUsers(users);
    }
    private void getAllUsersSortedByEmail(){
        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "registerDate"));
        printUsers(users);
    }
    private void countUser(){
        long usersLength = userRepository.count();
        System.out.println("No. users: " + usersLength);
    }
    private void aggregatePermissions(){
        userRepository.aggregatePermissions()
                .forEach(objects -> System.out.println(
                        "Permission: " + objects[0] + " count: " + objects[1]));
    }
    private void aggregatePermissionsByRoleName(){
        userRepository.aggregatePermissionsByRoleName()
                .forEach(objects -> System.out.println(
                        "Permission: " + objects[0] + " count: " + objects[1]));
    }
    // EDYCJA STATUSU V1
    private void changeUserStatus(long userId, boolean status){
        // SQL select * from user where user_id = ?
        Optional<User> isUser = userRepository.findById(userId);
        if(isUser.isPresent()){
            User user = isUser.get();
            // SQL update user set enable = 1
            user.setEnable(status);
            userRepository.save(user);
        }
    }
    private void changeUserStatusNativeSQL(boolean status, int userId){
        userRepository.updateUserStatus(status, userId);
    }
    private void deleteDisabledUsers(int userId){
        userRepository.deleteUserRoles(userId);
    }
    private List<User> getUsersByStatus(boolean status){
        return userRepository.findAllByEnable(status);
    }
    private List<User> getUsersByStatusJPQL(boolean status){
        return userRepository.getAllUsersWhereStatus(status);
    }

    @Transactional(
            rollbackFor = Exception.class,
            noRollbackFor = {},
            readOnly = false
    )
    public void addManyUsers(List<User> users) throws Exception {
        for (int i = 0; i < users.size() ; i++) {
            if(i == 5){
                System.out.println("ROLLBACK");
                throw new Exception();
            }
            System.out.println("Próba zapisu...");
            userRepository.save(users.get(i));
        }
    }

    @Override
    public void run(String... args)  {
//        List<User> users = new ArrayList<User>(
//                Arrays.asList(
//                        new User("x1@x.pl","Xxxx111_"),
//                        new User("x2@x.pl","Xxxx111_"),
//                        new User("x3@x.pl","Xxxx111_"),
//                        new User("x4@x.pl","Xxxx111_"),
//                        new User("x5@x.pl","Xxxx111_"),
//                        new User("x6@x.pl","Xxxx111_")
//        ));
//        try {
//            addManyUsers(users);
//        }catch (Exception e){
//            System.out.println("Nic nie zapisano!");
//        }
    }
}
