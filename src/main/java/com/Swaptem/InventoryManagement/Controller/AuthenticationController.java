package com.Swaptem.InventoryManagement.Controller;


import com.Swaptem.InventoryManagement.DTO.JwtResponseDTO;
import com.Swaptem.InventoryManagement.DTO.UserDTO;
import com.Swaptem.InventoryManagement.DTO.UserLoginDTO;
import com.Swaptem.InventoryManagement.Entity.User;
import com.Swaptem.InventoryManagement.Service.JwtService;
import com.Swaptem.InventoryManagement.Service.UserMapper;
import com.Swaptem.InventoryManagement.Service.UserService;
import com.Swaptem.InventoryManagement.Validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;





@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    final UserService userService;
    final UserValidation userValidation;
    final UserMapper userMapper;

    final JwtService jwtService;


    @Autowired
    public AuthenticationController(UserService userServiceInput, UserValidation userValidationInput, UserMapper userMapperInput, JwtService jwtService){
        this.userService = userServiceInput;
        this.userValidation = userValidationInput;
        this.userMapper = userMapperInput;
        this.jwtService = jwtService;
    }




    @PostMapping("")
    public ResponseEntity<JwtResponseDTO> authenticateUser(@RequestBody UserLoginDTO loginRequest) {
        User validatedUser = userService.CheckUserCredentials(loginRequest);
        JwtResponseDTO jwtResponseDTO = new JwtResponseDTO();

        if(validatedUser != null){
            String jwt = jwtService.generateJwtToken(validatedUser);
            jwtResponseDTO.setJwtToken(jwt);
        } else{
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }

        return ResponseEntity.ok(jwtResponseDTO);
    }








}
