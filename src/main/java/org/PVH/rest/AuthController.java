package org.PVH.rest;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.PVH.model.ERole;
import org.PVH.model.Role;
import org.PVH.model.User;
import org.PVH.payload.request.LoginRequest;
import org.PVH.payload.response.JwtResponse;
import org.PVH.payload.response.MessageResponse;
import org.PVH.repository.RoleRepository;
import org.PVH.repository.UserRepository;
import org.PVH.security.jwt.JwtUtils;
import org.PVH.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@ConditionalOnProperty(name = "pvh.security.enable", havingValue = "true")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));


        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles));
    }

    @PostMapping("/signup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User signUpRequest) {
        try{
            if (userRepository.existsByUsername(signUpRequest.getUsername()) || userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username or Email is already taken!"));
            }

            // Create new user's account
            User user = new User(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()), signUpRequest.getEmail());

            Set<Role> strRoles = signUpRequest.getRoles();
            Set<Role> roles = new HashSet<>();

            if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER.name())
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
            } else {
            strRoles.forEach(role -> {
                switch (role.getName()) {
                case ROLE_ADMIN:
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN.name())
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);

                    break;
                default:
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER.name())
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
                }
            });
            }

            user.setRoles(roles);
            userRepository.save(user);

            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Something with your Request is wrong"));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public Collection<User> getAllUsersPage() {
        Collection<User> users = userRepository.findAll();

        if (users.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return users;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "application/json")
    @Transactional
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return new ResponseEntity<String>("User was not found.", HttpStatus.NOT_FOUND);
        }
        userRepository.delete(user.get());
        return new ResponseEntity<String>("User Deleted", HttpStatus.NO_CONTENT);
    }

}
