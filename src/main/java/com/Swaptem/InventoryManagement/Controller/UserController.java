package com.Swaptem.InventoryManagement.Controller;

import com.Swaptem.InventoryManagement.DTO.UserDTO;
import com.Swaptem.InventoryManagement.Entity.User;
import com.Swaptem.InventoryManagement.Service.JwtService;
import com.Swaptem.InventoryManagement.Service.UserMapper;
import com.Swaptem.InventoryManagement.Service.UserService;
import com.Swaptem.InventoryManagement.Validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/users")
public class UserController {

    final UserService userService;
    final UserValidation userValidation;
    final UserMapper userMapper;
    final JwtService jwtService;

    @Autowired
    public UserController(UserService userServiceInput, UserValidation userValidationInput, UserMapper userMapper, JwtService jwtService){
        this.userService = userServiceInput;
        this.userValidation = userValidationInput;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }

    @PostMapping()
    public ResponseEntity<String> RegisterUser(@RequestBody User userInput){
        if(userValidation.UsernameIsValid(userInput.getUsername()) && userValidation.UserPasswordIsValid(userInput.getPassword()) && userValidation.UserCurrencyIsValid(userInput.getCurrency())){
            if(userService.registerUser(userInput)){
                return new ResponseEntity<>("User added", HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>("Could not add user", HttpStatus.NOT_ACCEPTABLE);

    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> GetUserById(@PathVariable int userId){
        User user = userService.getUserById(userId);
        if(user != null){
            UserDTO userDTO = userMapper.ToUserDTO(user);
            return ResponseEntity.ok(userDTO);
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
    }

    @PutMapping()
    public ResponseEntity<String> UpdateUser(@RequestBody UserDTO userDTOInput, @RequestHeader String authentication){
        int userId = jwtService.getUserIdFromJwtToken(authentication);

        boolean succes = false;
        if(userValidation.UsernameIsValid(userDTOInput.getUsername())){
            User user = userMapper.ToUser(userDTOInput);
            user.setUserId(userId);
            succes = userService.updateUser(user);
            if(succes){
                return new ResponseEntity<>("User updated", HttpStatus.ACCEPTED);
            }
        }
        return new ResponseEntity<>("User not updated", HttpStatus.NOT_ACCEPTABLE);
    }


    @DeleteMapping("/{userIdInput}")
    public ResponseEntity<String> DeleteUser(@PathVariable int userIdInput, @RequestHeader String authentication){
        int userId = jwtService.getUserIdFromJwtToken(authentication);
        boolean isAdmin = userService.isUserAdmin(userId);


        User user = userService.getUserById(userIdInput);
        if(isAdmin && user != null){
            boolean succes = userService.deleteUserById(userIdInput);
            if (succes){
                return new ResponseEntity<>("User deleted", HttpStatus.ACCEPTED);
            }
            return new ResponseEntity<>("User not deleted", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>("Requesting user is not an admin", HttpStatus.NOT_ACCEPTABLE);
    }
    
}
