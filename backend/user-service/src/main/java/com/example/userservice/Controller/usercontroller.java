package com.example.userservice.Controller;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import javax.validation.Valid;

import com.example.userservice.Exception.AppException;
import com.example.userservice.Exception.ResourceNotFoundException;
import com.example.userservice.Model.Role;
import com.example.userservice.Model.RoleName;
import com.example.userservice.Model.User;
import com.example.userservice.Payload.*;
import com.example.userservice.Repository.RoleRepository;
import com.example.userservice.Repository.UserRepository;
import com.example.userservice.security.CurrentUser;
import com.example.userservice.security.JwtTokenProvider;

import com.example.userservice.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@RequestMapping("/user-service")
public class usercontroller {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    Environment env;


    @GetMapping("/welcome")
    public String welcome(){
        return "welcome User-Service"
                + env.getProperty("local.server.port")
                + env.getProperty("app.jwtSecret")
                + env.getProperty("app.jwtExpirationInMs" )
                ;
    }


/*
    {
        "name":"5555",
         "username" : "5555",
         "email": "5555@5555.com",
         "password":"555555"
    }

    {
    "success": false,
    "message": "Username is already taken!"
    }

    {
    "success": true,
    "message": "User registered successfully"
}
*/
    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }
        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));
        user.setRoles(Collections.singleton(userRole));
        int cash = 0;
        user.setCash((long) cash);
        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user-service/user/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        // 헤더에 http://localhost:8081/user-service/user/{username} 정보가 들어가게 된다.
        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }


/*
    {
        "usernameOrEmail": "2222",
            "password":"222222"
    }
    {
    "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIiwiaWF0IjoxNjI0MjI3MDkwLCJleHAiOjE2MjQ4MzE4OTB9.mmRN9MxVJ7xpwSjoRONpoA-0d6yFExciehKyqtSMP58Au3lN_tD2R6x0RCBqQw3Cp7YWWdBPdyo4OZ863Ugz_A",
    "tokenType": "Bearer"
    }

 */
    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("jwt", jwt);
        User user = userRepository.findByUsernameOrEmail(loginRequest.getUsernameOrEmail(), loginRequest.getUsernameOrEmail()).orElse(new User());
        responseHeaders.add("userId", user.getId().toString());
        return ResponseEntity.ok().headers(responseHeaders).body(new JwtAuthenticationResponse(jwt));
    }




/*
    http://localhost:8081/user-service/checkUsernameAvailability?username=admin
    {
    "available": true
    }
 */
    //회원가입 시, 유저네임이 올바른지 확인 & 중복확인
    @GetMapping("/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }



    /*
    http://localhost:8081/user-service/checkEmailAvailability?email=admin@admin.com
    {
    "available": true
    }
 */
    // 회원가입 시, 이메일이 올바른지 확인 & 중복확인
    @GetMapping("/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }



/*
http://localhost:8081/user-service/user/2222
{
    "id": 2,
    "username": "2222",
    "name": "2222",
    "cash": 80
}
 */
    // 마이 페이지 (Profile)에서  User(성명) / Username(ID) / Cash (현재 보유 캐시) 조회
    @GetMapping("/user/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username, @CurrentSecurityContext(expression="authentication.name") String username1) {
        if(username1.equals(username)) {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
            UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCash());
            return userProfile;
        }
        else return null;
    }



    /*
    {
        "id": 7,
            "name": "5555",
            "username": "5555",
            "authorities": [
        {
            "authority": "ROLE_USER"
        }
    ],
        "enabled": true,
            "accountNonLocked": true,
            "accountNonExpired": true,
            "credentialsNonExpired": true
    }

 */
    // 현재 유저정보 조회
    @GetMapping("/user/me")
    public UserPrincipal getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserPrincipal userPrincipal = new UserPrincipal(currentUser.getId(), currentUser.getName(), currentUser.getUsername(),  currentUser.getAuthorities());
        return userPrincipal;
    }



 /*
  850
  */
    // 유저의 현재 보유 캐시 조회
    @GetMapping("/CheckCash/me")
    public String CheckCash( @CurrentSecurityContext(expression="authentication.name") String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        Long coin = user.getCash();
        return coin.toString();
    }

    /*
    [
    {
        "createdAt": "2021-06-07T03:47:45",
            "updatedAt": "2021-06-07T22:46:19",
            "id": 1,
            "name": "1111",
            "username": "1111",
            "email": "1111@1111.com",
            "password": "$2a$10$zHsh4BOXoS61n6e2hu7Vz./J2JMTxP0NPhjAX2BsxE6JRleVmYOA.",
            "cash": 950,
            "roles": [
        {
            "id": 2,
                "name": "ROLE_ADMIN"
        }
        ]
    },
    {
        "createdAt": "2021-06-07T09:33:18",
            "updatedAt": "2021-06-20T18:28:46",
            "id": 2,
            "name": "2222",
            "username": "2222",
            "email": "2222@2222.com",
            "password": "$2a$10$tX9aQPZ22.q8hK.UHwoDHuD9sP13R5jX4mb1gPE9xjSpfTloWPjWm",
            "cash": 80,
            "roles": [
        {
            "id": 1,
                "name": "ROLE_AUTHOR"
        }
        ]
    }]
    */

    // 모든 유저 조회
    @GetMapping("/SelectUser/all")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Collection<User> SelectUserAll() {
        return userRepository.findAll();
    }

/*
http://localhost:8000/user-service/userinfoEdit/5555
{
    "name":"5544",
    "username" : "5533",
    "email": "5533@5555.com",
    "password":"555555"
}
{
    "success": true,
    "message": "User updated successfully"
}
 */
    // 유저정보 수정
    @PutMapping("/userinfoEdit/{username}")
    public ResponseEntity<?> userinfoEdit(@PathVariable(value = "username") String username, @Valid @RequestBody SignUpRequest signUpRequest){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            if(!signUpRequest.getUsername().equals(user.getUsername())) {
                return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                        HttpStatus.BAD_REQUEST);
            }
        }
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            if(!signUpRequest.getEmail().equals(user.getEmail())) {
                return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                        HttpStatus.BAD_REQUEST);
            }
        }
        // Updating user's account
        user.setName(signUpRequest.getName());
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRoles(user.getRoles());
        user.setCash(user.getCash());
        User result = userRepository.save(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user-service/user/{username}")
                .buildAndExpand(result.getUsername()).toUri();
        return ResponseEntity.created(location).body(new ApiResponse(true, "User updated successfully"));
    }

/*
http://localhost:8000/user-service/deleteUser/5555
 */
    // 유저정보 삭제
    @DeleteMapping("/deleteUser/{username}")
    public void deleteUser(@PathVariable String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        userRepository.delete(user);
    }





}
