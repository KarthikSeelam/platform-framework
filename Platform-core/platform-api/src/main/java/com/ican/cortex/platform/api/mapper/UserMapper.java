package com.ican.cortex.platform.api.mapper;


import com.ican.cortex.platform.api.dto.UserDTO;
import com.ican.cortex.platform.api.primary.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    /**
     * Convert User entity to UserDTO
     * @param user User entity
     * @return UserDTO
     */
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setCreatedDate(user.getCreatedDate());
        userDTO.setLastModifiedDate(user.getLastModifiedDate());
        return userDTO;
    }

    /**
     * Convert UserDTO to User entity
     * @param userDTO UserDTO
     * @return User entity
     */
  /*  public User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setCreatedDate(userDTO.getCreatedDate());
        user.setLastModifiedDate(userDTO.getLastModifiedDate());
        return user;
    }*/
}
