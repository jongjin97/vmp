package com.vanmanagement.vmp.users;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    void save() {
        UserEntity userEntity = userEntity();
        UserEntity savedUserEntity = userRepository.save(userEntity);

        assertNotNull(savedUserEntity);
        assertEquals(1, savedUserEntity.getSeq());
    }

    @Test
    @Order(2)
    void findById() {
        UserEntity userEntity = userEntity();

        Optional<UserEntity> findedUserEntiry = userRepository.findById(userEntity.getSeq());

        assertNotNull(findedUserEntiry);
        assertEquals(findedUserEntiry.get().getSeq(), userEntity.getSeq());
    }

    @Test
    @Order(3)
    void findByEmail() {
        UserEntity userEntity = userEntity();
        System.out.println(userEntity.getEmail());
        UserEntity findedUserEntiry = userRepository.findByEmail(userEntity.getEmail()).get();

        assertNotNull(findedUserEntiry);
        assertEquals(findedUserEntiry.getEmail(), userEntity.getEmail());
    }

    public UserEntity userEntity(){
        UserEntity userEntity = new UserEntity();
        userEntity.setSeq(1L);
        userEntity.setEmail("test@test.com");
        userEntity.setName("윤종진");
        userEntity.setPassword("123qwe");
        userEntity.setCreateAt(LocalDateTime.now());
        return userEntity;
    }
}