package com.demo.hungnguyendev.modules.users.services.interfaces;

import com.demo.hungnguyendev.modules.users.requests.LoginRequest;
import com.demo.hungnguyendev.modules.users.resources.LoginResource;

public interface UserServiceInterface {

    LoginResource login(LoginRequest request);
    
}