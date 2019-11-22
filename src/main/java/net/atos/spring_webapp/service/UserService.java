package net.atos.spring_webapp.service;

import net.atos.spring_webapp.model.Permission;
import net.atos.spring_webapp.model.User;
import net.atos.spring_webapp.repository.PermissionRepository;
import net.atos.spring_webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private PermissionRepository permissionRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PermissionRepository permissionRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User assignPermissionToUser(User user, int roleId){
        Optional<Permission> optionalPermission = permissionRepository.findById((byte)roleId);
        if (optionalPermission.isPresent()){
            user.addPermission(optionalPermission.get());
        }
        return user;
    }

    public void registerUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(assignPermissionToUser(user,  1));
    }
}
