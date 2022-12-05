package com.web.spring_boot_securitymaster.controller;

import com.web.spring_boot_securitymaster.dao.RoleDao;
import com.web.spring_boot_securitymaster.model.Role;
import com.web.spring_boot_securitymaster.model.User;
import com.web.spring_boot_securitymaster.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleDao roleDao;


    @GetMapping("")
    public String showUserList(Model model) {
        List<User> list = userService.getAllUsers();
        model.addAttribute("listUsers", list);
        return "index";
    }

    @GetMapping(value = "/showNewUserForm")
    public String showNewUserForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);

        List<Role> list = roleDao.findAll();
        model.addAttribute("listRoles", list);
        return "newUser";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") User user, BindingResult bindingResult, Model model, @RequestParam(name = "roles", defaultValue = "") String role) {

        System.out
                .println(role);
        if (role.equals("")) return "redirect:/";

        userService.saveUser(user, role);
        return "redirect:/admin";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") long id, Model model) {

        User user = userService.getUserById(id);

        System.out.println(user.getPassword());

        model.addAttribute("user", user);

        List<Role> list = roleDao.findAll();
        model.addAttribute("listRoles", list);
        return "updateUser";
    }

    @PostMapping({"/showFormForUpdate/{id}"})
    public String showFormForUpdate1(@ModelAttribute("user") User user, BindingResult bindingResult, Model model, @PathVariable(value = "id") Long id, @RequestParam(name = "roles", defaultValue = "") String role) {
        Set<Role> roles = new HashSet<>();

        roles.add(roleDao.findRoleByRole(role));
        User user1 = userService.getUserById(id);
        user1.setPassword(user.getPassword());
        user1.setUsername(user.getUsername());
        user1.setRoles(roles);
        userService.saveUser(user1, role);

        return "redirect:/admin";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable(value = "id") long id) {


        this.userService.deleteUserById(id);
        return "redirect:/admin";
    }
}