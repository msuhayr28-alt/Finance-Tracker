package com.Suhayr.Finance.Tracker.controller;

import com.Suhayr.Finance.Tracker.dto.LoginUserDto;
import com.Suhayr.Finance.Tracker.dto.RegisterUserDto;
import com.Suhayr.Finance.Tracker.dto.VerifyUserDto;
import com.Suhayr.Finance.Tracker.model.User;
import com.Suhayr.Finance.Tracker.responses.LoginResponse;
import com.Suhayr.Finance.Tracker.service.AuthenticationService;
import com.Suhayr.Finance.Tracker.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticateController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticateController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto){
        User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto){
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDto verifyUserDto){
        try {
            authenticationService.verifyUser(verifyUserDto);
            return ResponseEntity.ok("Account verified successfully");
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email){
        try {
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code send");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());


        }
    }



}
