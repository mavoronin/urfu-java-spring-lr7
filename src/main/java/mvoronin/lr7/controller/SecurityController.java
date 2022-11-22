package mvoronin.lr7.controller;

import mvoronin.lr7.dto.UserDto;
import mvoronin.lr7.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class SecurityController {

    private UserService userService;

    public SecurityController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/index")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        var userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model) {
        var existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null, "На этот адрес электронной почты уже зарегистрирована учетная запись.");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "register";
        }

        userService.saveUser(userDto);
        return "redirect:/register?success";
    }

    @GetMapping("/users")
    public String users(Model model) {
        var users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }
}
