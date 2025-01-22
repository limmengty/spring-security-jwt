package com.ms.spring_security_jwt.modules.user.service.implementation;

import com.ms.spring_security_jwt.infrastructure.exception.ConflictException;
import com.ms.spring_security_jwt.infrastructure.exception.NotFoundException;
import com.ms.spring_security_jwt.infrastructure.model.request.BaseRequest;
import com.ms.spring_security_jwt.infrastructure.repository.BaseRepository;
import com.ms.spring_security_jwt.infrastructure.service.implementation.BaseCrudServiceImpl;
import com.ms.spring_security_jwt.modules.user.model.entity.UserEntity;
import com.ms.spring_security_jwt.modules.user.model.request.BackendCreateUserRequest;
import com.ms.spring_security_jwt.modules.user.model.request.BackendUpdateUserRequest;
import com.ms.spring_security_jwt.modules.user.repository.UserRepository;
import com.ms.spring_security_jwt.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl extends BaseCrudServiceImpl<UserEntity, Long> implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public BaseRepository<UserEntity, Long> getRepository() {
        return this.userRepository;
    }

    @Override
    public UserEntity create(BaseRequest<UserEntity> req) {
        // 1. casting the request
        BackendCreateUserRequest userRequest = (BackendCreateUserRequest) req;

        // 2. check username exist or not
        if (isUsernameExist(userRequest.getUsername()))
            throw new ConflictException("Username already taken!");

        // 3. check email exist or not
        if (isEmailExist(userRequest.getEmail()))
            throw new ConflictException("Email already taken!");

        // 4. call super class to execute the existing functionalities
        return super.create(userRequest);
    }

    @Override
    public UserEntity update(Long id, BaseRequest<UserEntity> req) {
        // 1. casting the request
        BackendUpdateUserRequest userRequest = (BackendUpdateUserRequest) req;

        // 2. check the id that has provided exist in our db or not
        UserEntity foundUser = this.userRepository.findOneAvailable(id).orElseThrow(() -> new NotFoundException("Uer not found!"));

        // 3. check username exist or not
        if (!Objects.equals(foundUser.getUsername(), userRequest.getUsername()))
            if (isUsernameExist(userRequest.getUsername()))
                throw new ConflictException("Username already taken!");

        // 4. check email exist or not
        if (!Objects.equals(foundUser.getEmail(), userRequest.getEmail()))
            if (isEmailExist(userRequest.getEmail()))
                throw new ConflictException("Email already taken!");

        // 5. setting existing data to the request object
        userRequest.setPassword(foundUser.getPassword());
        userRequest.setExistingEntity(foundUser);

        // 6. call super class to execute the existing functionalities
        return super.update(id, userRequest);
    }

    private Boolean isEmailExist(String email) {
        return this.userRepository.existByFieldAvailable("email", email);
    }

    private Boolean isUsernameExist(String username) {
        return this.userRepository.existByFieldAvailable("username", username);
    }
}
