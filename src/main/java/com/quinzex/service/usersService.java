package com.quinzex.service;

import com.quinzex.dto.UserDTO;
import com.quinzex.entity.LmsLogin;
import com.quinzex.entity.Roles;
import com.quinzex.repository.LmsLoginRepo;
import com.quinzex.repository.RolesRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
public class usersService implements IUsersService {

    private final LmsLoginRepo lmsLoginRepo;
    private final RolesRepo rolesRepo;
    public usersService(LmsLoginRepo lmsLoginRepo, RolesRepo rolesRepo) {
        this.lmsLoginRepo = lmsLoginRepo;
        this.rolesRepo = rolesRepo;
    }
    @Override

    public String inactiveUser(String targetUserEmail, Authentication authentication) {
     LmsLogin user = lmsLoginRepo.findByEmail(targetUserEmail).orElseThrow(()->new RuntimeException("user not found"));
     LmsLogin loggedInUser=lmsLoginRepo.findByEmail(authentication.getName()).orElseThrow(()->new RuntimeException("Logged in user not found"));
     String targetUserRole=user.getRole().getRoleName();
     String loggedInUserRole=loggedInUser.getRole().getRoleName();
     if("ADMIN".equals(loggedInUserRole)){
         if("SUPER_ADMIN".equals(targetUserRole)){
             throw new AccessDeniedException("Admin cannot inactivate Super Admin");
         }
         if("ADMIN".equals(targetUserRole)){
             throw new AccessDeniedException("Admin cannot inactivate Admin");
         }
     }
     if("SUPER_ADMIN".equals(loggedInUserRole)){
         if("SUPER_ADMIN".equals(targetUserRole)){
             throw new AccessDeniedException("Super Admin cannot inactivate Super Admin");
         }
     }
        if (loggedInUser.getEmail().equalsIgnoreCase(targetUserEmail)) {
            throw new AccessDeniedException("You cannot inactivate yourself");
        }
      user.setIsAuthorized(false);
      lmsLoginRepo.save(user);
      return targetUserEmail + " has been inactivated";
    }

    @Override
    public String activeUser(String userEmail,Authentication authentication) {
        LmsLogin user = lmsLoginRepo.findByEmail(userEmail).orElseThrow(()->new RuntimeException("user not found"));
        LmsLogin loggedInUser = lmsLoginRepo.findByEmail(authentication.getName()).orElseThrow(()->new RuntimeException("Logged in user not found"));
        String targetUserRole=user.getRole().getRoleName();
        String loggedInUserRole=loggedInUser.getRole().getRoleName();
        if("ADMIN".equals(loggedInUserRole)){
            if("SUPER_ADMIN".equals(targetUserRole)){
                throw new AccessDeniedException("Admin cannot activate Super Admin");
            }
            if("ADMIN".equals(targetUserRole)){
                throw new AccessDeniedException("Admin cannot activate Admin");
            }
        }
        user.setIsAuthorized(true);
        lmsLoginRepo.save(user);
        return userEmail + " has been activated";
    }
    @Override
    public Page<UserDTO> getAllActiveUsers(Pageable pageable) {
        return lmsLoginRepo.findByIsAuthorized(true,pageable).map(user->new UserDTO(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getIsAuthorized(),
                user.getRole().getRoleName()
        ));


    }
    @Override
    public Page<UserDTO> getAllInactiveUsers(Pageable pageable) {
        return lmsLoginRepo.findByIsAuthorized(false,pageable).map(user->new UserDTO(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getIsAuthorized(),
                user.getRole().getRoleName()
        ));
    }
    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return lmsLoginRepo.findAll(pageable).map(user->new UserDTO(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getIsAuthorized(),
                user.getRole().getRoleName()
        ));
    }

    @Override
    public Page<UserDTO> searchUsers(String keyword, Pageable pageable) {
        return lmsLoginRepo.searchUsers(keyword, pageable).map(user->new UserDTO(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getIsAuthorized(),
                user.getRole().getRoleName()
        ));
    }
    @Override
    public UserDTO getLoggedInUserDetails(Authentication authentication) {
        LmsLogin loggedUser = lmsLoginRepo.findByEmail(authentication.getName()).orElseThrow(()->new RuntimeException("user not found"));
        if(Boolean.FALSE.equals(loggedUser.getIsAuthorized())){
            throw new AccessDeniedException("Your Account is in inactive state");
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(loggedUser.getEmail());
        userDTO.setFirstName(loggedUser.getFirstName());
        userDTO.setLastName(loggedUser.getLastName());
        userDTO.setRole(loggedUser.getRole().getRoleName());
        userDTO.setStatus(loggedUser.getIsAuthorized());
        return userDTO;
    }
    @Override
    public String editLoggedInUser(String firstName, String lastName,Authentication authentication) {
        LmsLogin loggedUser = lmsLoginRepo.findByEmail(authentication.getName()).orElseThrow(()->new RuntimeException("user not found"));
        if(Boolean.FALSE.equals(loggedUser.getIsAuthorized())){
            throw new AccessDeniedException("Your Account is in inactive state");
        }

        loggedUser.setFirstName(firstName);
        loggedUser.setLastName(lastName);

        lmsLoginRepo.save(loggedUser);
        return loggedUser.getEmail()+" has been updated";
    }


    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String editRole(String email,String roleName){
       LmsLogin user =  lmsLoginRepo.findByEmail(email).orElseThrow(()->new RuntimeException("user not found"));
          String presentUserRole=user.getRole().getRoleName();
       Roles role= rolesRepo.findByRoleName(roleName.toUpperCase()).orElseThrow(()->new RuntimeException("role not found"));
          if(presentUserRole.equals(roleName.toUpperCase())){
              throw new AccessDeniedException("Already the role is assigned for the user");
          }
          user.setRole(role);

          lmsLoginRepo.save(user);
          return email+" with "+roleName+" has been updated";


    }

}
