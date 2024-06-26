package com.teleconsulting.demo.controller;

import com.teleconsulting.demo.model.AuthenticationRequest;
import com.teleconsulting.demo.model.AuthenticationResponse;
import com.teleconsulting.demo.model.Doctor;
import com.teleconsulting.demo.model.Patient;
import com.teleconsulting.demo.security.JwtService;
import com.teleconsulting.demo.service.PatientService;
import com.teleconsulting.demo.service.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LoginController {
    private final PatientService patientService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    public LoginController(PatientService patientService, AuthenticationManager authenticationManager, JwtService jwtService, UserDetailsServiceImpl userDetailsService) {
        this.patientService = patientService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword());
        try{
            System.out.println(authenticationToken.isAuthenticated());
            System.out.println("Authentication Token is "+authenticationToken);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            if(authentication.isAuthenticated()) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
                if("[ROLE_USER]".equals(userDetails.getAuthorities().toString()))
                {
                    System.out.println("UserDetails is "+userDetails);
                    String token = this.jwtService.generateToken(userDetails);
                    // set accessToken to cookie header
                    ResponseCookie cookie = ResponseCookie.from("accessToken", token)
                            .httpOnly(true)
                            .secure(false)
                            .path("/")
                            .maxAge(1800)
                            .build();
                    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
                    AuthenticationResponse authResponse = AuthenticationResponse.builder().token(token).message(userDetails.getUsername()).build();
                    return new ResponseEntity<>(authResponse,HttpStatus.OK);
                }
                else
                {
                    AuthenticationResponse authResponse = AuthenticationResponse.builder().token(null).message("You are not authorised !!").build();
                    return new ResponseEntity<>(authResponse,HttpStatus.BAD_REQUEST);
                }
            }
            else {
                throw new UsernameNotFoundException("invalid user request..!!");
            }
        }catch (BadCredentialsException e){
            throw new RuntimeException("Invalid Username or Password");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Clear the HttpOnly cookie
        ResponseCookie cookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().build();
    }


    //    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword());
//        try{
//            System.out.println(authenticationToken.isAuthenticated());
//            System.out.println("Authentication Token is "+authenticationToken);
//            authenticationManager.authenticate(authenticationToken);
//        }catch (BadCredentialsException e){
//            throw new RuntimeException("Invalid Username or Password");
//        }
//        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
//        if("[ROLE_USER]".equals(userDetails.getAuthorities().toString()))
//        {
//            System.out.println("UserDetails is "+userDetails);
//            String token = this.jwtService.generateToken(userDetails);
//            AuthenticationResponse response = AuthenticationResponse.builder().token(token).message(userDetails.getUsername()).build();
//            return new ResponseEntity<>(response,HttpStatus.OK);
//        }
//        else
//        {
//            AuthenticationResponse response = AuthenticationResponse.builder().token(null).message("You are not authorised !!").build();
//            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
//        }
//    }
    @PostMapping("/doc/login")
    public ResponseEntity<?> docLogin(@RequestBody AuthenticationRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword());
        try{
            authenticationManager.authenticate(authenticationToken);
        }catch (BadCredentialsException e){
            throw new RuntimeException("Invalid Username or Password");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        if("[ROLE_DOCTOR]".equals(userDetails.getAuthorities().toString()))
        {
            String token = this.jwtService.generateToken(userDetails);
            AuthenticationResponse response = AuthenticationResponse.builder().token(token).message(userDetails.getUsername()).build();
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        else
        {
            AuthenticationResponse response = AuthenticationResponse.builder().token(null).message("You are not authorised !!").build();
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/admin/login")
    public ResponseEntity<?> adminLogin(@RequestBody AuthenticationRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword());
        try{
            authenticationManager.authenticate(authenticationToken);
        }catch (BadCredentialsException e){
            throw new RuntimeException("Invalid Username or Password");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        if("[ROLE_ADMIN]".equals(userDetails.getAuthorities().toString()))
        {
            String token = this.jwtService.generateToken(userDetails);
            AuthenticationResponse response = AuthenticationResponse.builder().token(token).message(userDetails.getUsername()).build();
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        else
        {
            AuthenticationResponse response = AuthenticationResponse.builder().token(null).message("You are not authorised !!").build();
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }

    }
    @PostMapping("/srdoc/login")
    public ResponseEntity<?> srDocLogin(@RequestBody AuthenticationRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword());
        try{
            System.out.println(authenticationToken.isAuthenticated());
            System.out.println("Authentication Token is "+authenticationToken);
            authenticationManager.authenticate(authenticationToken);
        }catch (BadCredentialsException e){
            throw new RuntimeException("Invalid Username or Password");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        System.out.println("UserDetails is "+userDetails);
        System.out.println("Role is : "+userDetails.getAuthorities().toString());
        if("[ROLE_SRDOC]".equals(userDetails.getAuthorities().toString()))
        {
            String token = this.jwtService.generateToken(userDetails);
            AuthenticationResponse response = AuthenticationResponse.builder().token(token).message(userDetails.getUsername()).build();
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        else {
            AuthenticationResponse response = AuthenticationResponse.builder().token(null).message("You are not authorised !!").build();
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/register/patient")
    public ResponseEntity<?> RegisterPatient(@RequestBody Patient patient) {
        return ResponseEntity.ok(patientService.saveNewPatient(patient));
    }

//    @GetMapping("/send-otp")
//    public ResponseEntity<String> sendEmailNotificationsForToday(@RequestParam String email, String otp) {
//        try {
//            emailService.sendLoginOtp(email, otp);
//            return ResponseEntity.ok("OTP Sent Successfully!");
//        } catch (MessagingException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending OTP!p");
//        }
//    }
}
