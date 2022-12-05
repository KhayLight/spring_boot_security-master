package com.web.spring_boot_securitymaster.service;

import com.web.spring_boot_securitymaster.dao.RoleDao;
import com.web.spring_boot_securitymaster.dao.UserDao;
import com.web.spring_boot_securitymaster.model.Role;
import com.web.spring_boot_securitymaster.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserDao userDao;


    private final RoleDao roleDao;


    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    public List<Role> findAllRoles() {
        return roleDao.findAll();
    }

    @Override
    public void saveUser(User user, String role) {

        user.setPassword(user.getPassword());
        Set<Role> roles = new HashSet<>();

        roles.add(roleDao.findRoleByRole(role));
        user.setRoles(roles);
        userDao.save(user);

    }

    @Override
    public void reg(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        saveUser(user, "ROLE_USER");

    }

    @Override
    public User getUserById(long id) {
        return userDao.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(String.format("User with ID = %d not found", id), 1));
    }

    @Override
    public void deleteUserById(long id) {
        Optional<User> user = userDao.findById(id);
        if (user.isPresent()) {
            try {
                userDao.delete(user.get());
            } catch (PersistenceException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public User checkUserByUsernameAndPassword(String username, String password) {
        return userDao.findByUsernameAndPassword(username, password);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if (null == user) {
            throw new UsernameNotFoundException(String.format("User username %s not found", username));
        }
        return user;
    }
}
