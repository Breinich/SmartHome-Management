package SmartHome.com.smarthome.Auth;

import SmartHome.com.smarthome.Auth.User.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class HelloController {

    // User data
    @GetMapping("/me")
    public ResponseEntity<Principal> userData(Principal principal) {
        return new ResponseEntity<Principal>(principal, HttpStatus.OK);
    }

    // Can be called by anyone
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    // Can be called by authenticated user
    @GetMapping("/auth_hello")
    public String authHello(Principal principal) {
        return "You are authenticated as " + principal.getName();
    }

    // Can be called by admins only
    @GetMapping("/admin_hello")
    @Secured(User.ROLE_ADMIN)
    public String adminHello() {
        return "Wow, you are an admin";
    }
}
