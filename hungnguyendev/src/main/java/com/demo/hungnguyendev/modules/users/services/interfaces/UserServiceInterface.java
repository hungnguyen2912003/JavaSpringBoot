package com.demo.hungnguyendev.modules.users.services.interfaces;

import com.demo.hungnguyendev.modules.users.dtos.LoginRequest;
import com.demo.hungnguyendev.modules.users.dtos.LoginResponse;

public interface UserServiceInterface {

    LoginResponse login(LoginRequest request);
    
}