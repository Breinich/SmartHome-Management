package com.itcatcetc.smarthome.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcatcetc.smarthome.login.email.EmailDetails;
import com.itcatcetc.smarthome.login.email.EmailService;
import com.itcatcetc.smarthome.login.user.User;
import com.itcatcetc.smarthome.login.user.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
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
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/smarthome/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;


    // User data
    @GetMapping("/me")
    @PreAuthorize("(hasRole('GUEST') or hasRole('HOMIE')) and #email == authentication.principal.username")
    public ResponseEntity<String> userData(@Valid @Email @RequestParam String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String res;
        try {
            res = objectMapper.writeValueAsString(user.get());
        } catch (Exception e) {
            res = user.get().toString();
        }
        return ResponseEntity.ok(res);
    }

    // Can be called by anyone
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    // Can be called by authenticated user
    @GetMapping
    @PreAuthorize("hasRole('GUEST') or hasRole('HOMIE')")
    public String authHello(Principal principal) {
        return "You are authenticated as " + principal.getName();
    }

    // Can be called by admins only
    @GetMapping("/homie")
    @PreAuthorize("hasRole('HOMIE')")
    public String homieHello() {
        return "Wow, you are a homie";
    }

    @GetMapping("/guest")
    @PreAuthorize("hasRole('GUEST')")
    public String guestHello2() {
        return "Wow, you are a guest";
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@Valid @RequestBody UserHeader user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getEmail(), user.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("User signed-in successfully!", HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserHeader userH) {

        User user = new User();
        user.setEmail(userH.getEmail());
        user.setPassword(userH.getPassword());
        user.setFirstName(userH.getFirstName());
        user.setLastName(userH.getLastName());

        // add check for email exists in DB
        if (userRepository.existsByEmail(user.getEmail())) {
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        String role = Role.GUEST;
        user.addRole(role);

        userRepository.save(user);

        //send email to user
        sendEmail(user);

        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }

    private void sendEmail(User user) {
        EmailDetails details = new EmailDetails();
        details.setRecipient(user.getEmail());
        details.setSubject("Welcome to SmartHome!");
        details.setMsgBody("Hello " + user.getFirstName() + ",\n\nWelcome to SmartHome! We are excited to have you on board.\n\nBest,\nSmartHome Team");
        emailService.sendSimpleMail(details);
    }
}
