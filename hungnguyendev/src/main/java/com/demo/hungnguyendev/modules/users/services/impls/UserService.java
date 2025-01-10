package com.demo.hungnguyendev.modules.users.services.impls;

import org.springframework.stereotype.Service;

import com.demo.hungnguyendev.BaseController;
import com.demo.hungnguyendev.modules.users.dtos.LoginRequest;
import com.demo.hungnguyendev.modules.users.dtos.LoginResponse;
import com.demo.hungnguyendev.modules.users.dtos.UserDTO;
import com.demo.hungnguyendev.modules.users.services.interfaces.UserServiceInterface;

@Service
public class UserService extends BaseController implements UserServiceInterface {
    
    @Override
    public LoginResponse login(LoginRequest request){
        try{
            // String email = request.getEmail();
            // String password = request.getPassword();

            String token = "hello_world";
            UserDTO user = new UserDTO(1L, "hungthinh291@gmail.com");

            return new LoginResponse(token, user);
        }
        catch(Exception e){
            throw new RuntimeException("Có vấn đề");
        }
    }

}
