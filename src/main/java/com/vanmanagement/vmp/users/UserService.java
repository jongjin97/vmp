package com.vanmanagement.vmp.users;

import com.vanmanagement.vmp.errors.AccountAlreadyExistsException;
import com.vanmanagement.vmp.errors.NotFoundException;
import com.vanmanagement.vmp.errors.PhoneAlreadyExistsException;
import com.vanmanagement.vmp.jwt.*;
import com.vanmanagement.vmp.payment.Payment;
import com.vanmanagement.vmp.payment.PaymentEntity;
import com.vanmanagement.vmp.payment.PaymentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PaymentRepository paymentRepository;
    private final Jwt jwt;
    private final TokenService tokenService;

    public UserService(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, PaymentRepository paymentRepository, Jwt jwt, TokenService tokenService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.jwt = jwt;
        this.tokenService = tokenService;
    }

    @Transactional
    public UserTokenDto login(String email, String password) {
        UserEntity userEntity = findByEmail(email).orElseThrow(() ->
                new NotFoundException("Could not found user for " + email));
        if(!passwordEncoder.matches(password, userEntity.getPassword()))
            throw new IllegalArgumentException("Bad Password");
        LocalDateTime now = LocalDateTime.now();

        String accessToken = jwt.create(Jwt.Claims.of(userEntity.getSeq()
                , email
                , new String[]{userEntity.getRole()}));

        RefreshEntity refresh = new RefreshEntity(jwt.createReftesh(),
                now, now.plusSeconds(jwt.getRefreshExpirySeconds()), userEntity);
        tokenService.save(refresh, userEntity.getSeq());

        UserTokenDto userTokenDto = new UserTokenDto(userEntity, accessToken);

        return userTokenDto;
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
    public Optional<UserEntity> updateUserPoint(Long id, Payment payment) throws AccountNotFoundException {
        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Could not found user for " + id));
        long currPoint = userEntity.getPoint();
        userEntity.setPoint(currPoint+ payment.getPaid_amount());
        PaymentEntity paymentEntity = modelMapper.map(payment, PaymentEntity.class);
        paymentEntity.setUser(userEntity);
        paymentRepository.save(paymentEntity);

        return Optional.of(userEntity);
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