package com.vanmanagement.vmp.users;

import com.vanmanagement.vmp.errors.AccountAlreadyExistsException;
import com.vanmanagement.vmp.errors.NotFoundException;
import com.vanmanagement.vmp.errors.PhoneAlreadyExistsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Transactional
    public UserEntity login(String email, String password) {
        UserEntity userEntity = findByEmail(email).orElseThrow(() ->
                new NotFoundException("Could not found user for " + email));
        if(!passwordEncoder.matches(password, userEntity.getPassword()))
            throw new IllegalArgumentException("Bad Password");

        return userEntity;
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    @Transactional
    public Optional<UserEntity> saveUser(RegisterRequest registerRequest){
        Optional<UserEntity> users = findByEmail(registerRequest.getEmail());
        if(users.isPresent())
            throw new AccountAlreadyExistsException(registerRequest.getEmail() + " Account already exists");
        Optional<UserEntity> usersPhone = findByPhone(registerRequest.getPhone());
        if(usersPhone.isPresent())
            throw new PhoneAlreadyExistsException(registerRequest.getPhone() + " phone already exists");
        UserEntity userEntity = registerRequest.toEntity();

        userEntity.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        Optional<UserEntity> savedUserEntity = Optional.ofNullable(userRepository.save(userEntity));

        return savedUserEntity;
    }
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<UserEntity> userEntity = userRepository.findByEmail(username);
//        List<GrantedAuthority> authorities = new ArrayList<>();
//
//        authorities.add(new SimpleGrantedAuthority(Role.USER.value()));
//
//        return new org.springframework.security.core.userdetails.User(userEntity.get().getEmail(),
//                userEntity.get().getPassword(), authorities);
//    }
}