package lu.pata.security.springsecurityjwt;

import lu.pata.security.springsecurityjwt.models.AuthenticationRequest;
import lu.pata.security.springsecurityjwt.models.AuthenticationResponse;
import lu.pata.security.springsecurityjwt.services.MyUserDetailsService;
import lu.pata.security.springsecurityjwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloResource {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @RequestMapping({ "/hello" })
    public String hello(){
        return "Hello world";
    }

    @RequestMapping(value = "/auth",method = RequestMethod.POST)
    public ResponseEntity<?> createAuthToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        }catch (BadCredentialsException ex){
            throw new Exception("Incorrect password",ex);
        }

        UserDetails userDetails=userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        String jwt=jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

}
