package wako.Belajar_Spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import wako.Belajar_Spring.entity.User;
import wako.Belajar_Spring.model.RegisterUserRequest;
import wako.Belajar_Spring.model.UserResponse;
import wako.Belajar_Spring.repository.UserRepository;
import wako.Belajar_Spring.security.BCrypt;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public void register(RegisterUserRequest request) {
        validationService.validate(request);

        if(userRepository.existsById(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setName(request.getName());

        userRepository.save(user);

    }

    public UserResponse get(User user) {
        return UserResponse.builder()
            .username(user.getUsername())
            .name(user.getName())
            .build();
    }
}
