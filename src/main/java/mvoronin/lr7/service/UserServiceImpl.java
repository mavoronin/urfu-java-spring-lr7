package mvoronin.lr7.service;

import mvoronin.lr7.dto.UserDto;
import mvoronin.lr7.entity.Role;
import mvoronin.lr7.entity.User;
import mvoronin.lr7.repository.RoleRepository;
import mvoronin.lr7.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto) {
        var user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        var role = roleRepository.findByName("ROLE_ADMIN");
        if (role == null) {
            role = checkRoleExists();
        }

        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public UserDto findUserByEmail(String email) {
        var user = userRepository.findByEmail(email);
        return user == null ? null : mapToUserDto(user);
    }

    @Override
    public List<UserDto> findAllUsers() {
        var users = userRepository.findAll();
        return users.stream()
                .map(user -> mapToUserDto(user))
                .collect(Collectors.toList());
    }

    private UserDto mapToUserDto(User user) {
        var userDto = new UserDto();
        var splitName = user.getName().split(" ");
        userDto.setFirstName(splitName[0]);
        userDto.setLastName(splitName[1]);
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    private Role checkRoleExists() {
        var role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }
}
