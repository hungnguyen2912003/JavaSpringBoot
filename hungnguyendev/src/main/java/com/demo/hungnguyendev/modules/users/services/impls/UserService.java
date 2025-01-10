package com.demo.hungnguyendev.modules.users.services.impls;

import java.util.HashMap;
import java.util.Map; // Added import for Map

import org.slf4j.Logger; // Changed to use SLF4J Logger
import org.slf4j.LoggerFactory; // Changed to use SLF4J LoggerFactory
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.demo.hungnguyendev.BaseController;
import com.demo.hungnguyendev.modules.users.entities.User;
import com.demo.hungnguyendev.modules.users.repositories.UserRepository;
import com.demo.hungnguyendev.modules.users.requests.LoginRequest;
import com.demo.hungnguyendev.modules.users.resources.LoginResource;
import com.demo.hungnguyendev.modules.users.resources.UserResource;
import com.demo.hungnguyendev.modules.users.services.interfaces.UserServiceInterface;
import com.demo.hungnguyendev.resources.ErrorResource;
import com.demo.hungnguyendev.services.JwtService;

@Service
public class UserService extends BaseController implements UserServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Object authenticate(LoginRequest request) {
        try {
            User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Email hoặc mật khẩu không chính xác"));

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new BadCredentialsException("Email hoặc mật khẩu không chính xác");
            }

            String token = jwtService.generateToken(user.getId(), user.getEmail());
            UserResource userResource = new UserResource(user.getId(), user.getEmail(), user.getName());

            return new LoginResource(token, userResource);
        } catch (BadCredentialsException e) {
            logger.error("Lỗi xác thực: {}", e.getMessage());

            Map<String, String> errors = new HashMap<>();
            errors.put("message", e.getMessage());
            ErrorResource errorResource = new ErrorResource("Có vấn đề xảy ra trong quá trình xác thực", errors); // Updated constructor call
            return errorResource;
        }
    }
}