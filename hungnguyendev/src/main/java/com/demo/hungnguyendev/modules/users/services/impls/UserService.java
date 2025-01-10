package com.demo.hungnguyendev.modules.users.services.impls;

import org.springframework.stereotype.Service;

import com.demo.hungnguyendev.BaseController;
import com.demo.hungnguyendev.modules.users.requests.LoginRequest;
import com.demo.hungnguyendev.modules.users.resources.LoginResource;
import com.demo.hungnguyendev.modules.users.resources.UserResource;
import com.demo.hungnguyendev.modules.users.services.interfaces.UserServiceInterface;

@Service
public class UserService extends BaseController implements UserServiceInterface {
    
    @Override
    public LoginResource login(LoginRequest request){
        try{
            // String email = request.getEmail();
            // String password = request.getPassword();

            String token = "hello_world";
            UserResource user = new UserResource(1L, "hungthinh291@gmail.com");

            return new LoginResource(token, user);
        }
        catch(Exception e){
            throw new RuntimeException("Có vấn đề");
        }
    }

}
