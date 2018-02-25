package net.mlgmag.Spring_Crud.service.serviceImpl.User;

import net.mlgmag.Spring_Crud.model.User;
import net.mlgmag.Spring_Crud.repository.UserRepository;
import net.mlgmag.Spring_Crud.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;
import java.util.UUID;

@Service
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
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void update(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public User getById(UUID uuid) {
        System.out.println(userRepository.getOne(uuid));
        return userRepository.getOne(uuid);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Boolean validate(User user, Model model) {
        boolean Error = false;
        if (findByUsername(user.getUsername()) != null) {
            Error = true;
            model.addAttribute("DuplicateUsername", "Username already exist");
        }
        if (findByEmail(user.getEmail()) != null) {
            Error = true;
            model.addAttribute("DuplicateEmail", "Email already exist");
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            Error = true;
            model.addAttribute("PasswordMatch", "Passwords don't match");
        }
        return Error;
    }
}
