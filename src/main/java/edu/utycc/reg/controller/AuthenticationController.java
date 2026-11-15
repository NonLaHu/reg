package edu.utycc.reg.controller;

import edu.utycc.reg.service.AuthenticationService;
import edu.utycc.reg.security.JwtService;
import edu.utycc.reg.dto.LoginUserDto;
import edu.utycc.reg.dto.LoginResponse;
import edu.utycc.reg.entity.User;
import edu.utycc.reg.security.CustomUserDetails;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto, HttpServletRequest request) {
        String clientIp = request.getRemoteAddr();
        logger.info("Login attempt for user '{}' from IP: {};", loginUserDto.getEmail(), clientIp);

        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        CustomUserDetails userDetails = new CustomUserDetails(authenticatedUser);

        String jwtToken = jwtService.generateToken(userDetails);

        boolean isAdmin = authenticatedUser.getUserType() == User.UserType.ADMIN;

        LoginResponse loginResponse = new LoginResponse()
                .setToken(jwtToken)
                .setExpiresIn(jwtService.getExpirationTime())
                .setAdmin(isAdmin);

        logger.info("Login Successful for user '{}' from IP: {};", loginUserDto.getEmail(), clientIp);

        return ResponseEntity.ok(loginResponse);
    }

}