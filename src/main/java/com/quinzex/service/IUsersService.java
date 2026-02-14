package com.quinzex.service;

import com.quinzex.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface IUsersService {
    public String inactiveUser(String targetUserEmail, Authentication authentication);
    public String activeUser(String userEmail,Authentication authentication);
    public Page<UserDTO> getAllActiveUsers(Pageable pageable);
    public Page<UserDTO> getAllInactiveUsers(Pageable pageable);
    public Page<UserDTO> searchUsers(String keyword, Pageable pageable);
    public Page<UserDTO> getAllUsers(Pageable pageable);
    public UserDTO getLoggedInUserDetails(Authentication authentication);
    public String editLoggedInUser(String firstName, String lastName,Authentication authentication);
    public String editRole(String email,String roleName);
}
