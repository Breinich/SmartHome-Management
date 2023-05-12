package com.itcatcetc.smarthome.login;

import com.itcatcetc.smarthome.login.user.User;
import com.itcatcetc.smarthome.login.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(path="api/v1/smarthome/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // User data
    @GetMapping("/me")
    @PreAuthorize("(hasRole('GUEST') or hasRole('HOMIE')) and #email == authentication.principal.email")
    public ResponseEntity<User> userData(String email) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Can be called by anyone
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    // Can be called by authenticated user
    @GetMapping
    public String authHello(Principal principal) {
        return "You are authenticated as " + principal.getName();
    }

    // Can be called by admins only
    @GetMapping("/homie")
    @PreAuthorize("hasRole('ROLE_HOMIE')")
    public String homieHello() {
        return "Wow, you are a homie";
    }

    @GetMapping("/guest1")
    @PreAuthorize("hasRole('ROLE_GUEST')")
    public String guestHello1() {
        return "Wow, you are a guest";
    }

    @GetMapping("/guest2")
    @PreAuthorize("hasRole('GUEST')")
    public String guestHello2() {
        return "Wow, you are a guest";
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@Valid @RequestBody User user){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getEmail(), user.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("User signed-in successfully!.", HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody User user){

        // add check for email exists in DB
        if(userRepository.existsByEmail(user.getEmail())){
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        String role = Role.GUEST;
        user.addRole(role);

        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }
}
