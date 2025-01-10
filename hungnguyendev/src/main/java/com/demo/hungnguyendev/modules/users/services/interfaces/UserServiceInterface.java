package com.demo.hungnguyendev.modules.users.services.interfaces;

import com.demo.hungnguyendev.modules.users.requests.LoginRequest;

public interface UserServiceInterface {

    Object authenticate(LoginRequest request);
    
}