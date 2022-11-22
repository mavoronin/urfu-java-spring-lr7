package mvoronin.lr7.service;

import mvoronin.lr7.dto.UserDto;

import java.util.List;

public interface UserService {

    void saveUser(UserDto userDto);

    UserDto findUserByEmail(String email);

    List<UserDto> findAllUsers();
}
