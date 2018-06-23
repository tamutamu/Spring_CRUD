package net.mlgmag.Spring_Crud.service.User;

import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import net.mlgmag.Spring_Crud.definition.UserService;
import net.mlgmag.Spring_Crud.enums.Authority;
import net.mlgmag.Spring_Crud.model.User;
import net.mlgmag.Spring_Crud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userDao, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userDao;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        if (user.getAuthorities().contains(Authority.ADMIN)) {
            user.setAuthorities(ImmutableSet.of(Authority.USER, Authority.ADMIN));
        }
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        log.info("IN UserServiceImpl save {}", user);
        userRepository.save(user);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public void update(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        if (user.getAuthorities().contains(Authority.ADMIN)) {
            user.setAuthorities(ImmutableSet.of(Authority.USER, Authority.ADMIN));
        }
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        log.info("IN UserServiceImpl update {}", user);
        userRepository.saveAndFlush(user);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void delete(User user) {
        log.info("IN UserServiceImpl delete {}", user);
        userRepository.delete(user);
    }

    @Override
    public Optional<User> findById(UUID uuid) {
        log.info("IN UserServiceImpl findById {}", uuid);
        return userRepository.findById(uuid);
    }

    @Override
    public List<User> findAll() {
        log.info("IN UserServiceImpl findAll {}");
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        log.info("IN UserServiceImpl findByUsername {}", username);
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        log.info("IN UserServiceImpl findByEmail {}", email);
        return userRepository.findByEmail(email);
    }

    @Override
    public Boolean validate(User user, Model model) {
        boolean Error = false;

        if (findByUsername(user.getUsername()).orElse(null) != null) {
            Error = true;
            model.addAttribute("DuplicateUsername", "Username already exist");
        }

        if (findByEmail(user.getEmail()).orElse(null) != null) {
            Error = true;
            model.addAttribute("DuplicateEmail", "Email already exist");
        }

        return Error;
    }
}