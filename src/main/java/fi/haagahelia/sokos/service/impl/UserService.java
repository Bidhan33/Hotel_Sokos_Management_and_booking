/* package fi.haagahelia.sokos.service.impl;

import fi.haagahelia.sokos.entity.User;
import fi.haagahelia.sokos.exception.Exception;
import fi.haagahelia.sokos.model.LoginModel;
import fi.haagahelia.sokos.model.ResponseModel;
import fi.haagahelia.sokos.model.UserModel;
import fi.haagahelia.sokos.repo.UserRepository;
import fi.haagahelia.sokos.service.interfac.IUserService;
import fi.haagahelia.sokos.utils.JWT;
import fi.haagahelia.sokos.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWT jwtUtil;  // Changed from JWTUtil to JWT

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public ResponseModel register(User user) {
        ResponseModel response = new ResponseModel();

        try {
            if (user.getRole() == null || user.getRole().isBlank()) {
                user.setRole("USER");
            }

            if (userRepository.existsByEmail(user.getEmail())) {
                throw new Exception("Email already exists: " + user.getEmail());
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);

            UserModel userModel = Utils.mapUserEntityToModel(savedUser);  // Changed to use Utils class
            response.setStatusCode(200);
            response.setUser(userModel);
            response.setMessage("Registration successful");

        } catch (Exception e) {
            response.setStatusCode(400);
            response.setMessage("Registration error: " + e.getMessage());
        }

        return response;
    }

    @Override
    public ResponseModel login(LoginModel loginRequest) {
        ResponseModel response = new ResponseModel();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword())
            );

            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new Exception("User not found"));

            String token = jwtUtil.generateToken(user);
            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(user.getRole());
            response.setExpirationTime("7 days");
            response.setMessage("Login successful");

        } catch (Exception e) {
            response.setStatusCode(401);
            response.setMessage("Login failed: " + e.getMessage());
        }

        return response;
    }

    @Override
    public ResponseModel getAllUsers() {
        ResponseModel response = new ResponseModel();

        try {
            List<User> users = userRepository.findAll();
            List<UserModel> userModels = Utils.mapUserEntityListToModelList(users);  // Changed to use Utils class
            response.setStatusCode(200);
            response.setUserList(userModels);
            response.setMessage("Users retrieved successfully");

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving users: " + e.getMessage());
        }

        return response;
    }

    @Override
    public ResponseModel getUserById(String userId) {
        ResponseModel response = new ResponseModel();

        try {
            User user = userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new Exception("User not found"));
            UserModel userModel = Utils.mapUserEntityToModel(user);  // Changed to use Utils class
            response.setStatusCode(200);
            response.setUser(userModel);
            response.setMessage("User found");

        } catch (Exception e) {
            response.setStatusCode(404);
            response.setMessage("Error: " + e.getMessage());
        }

        return response;
    }

    @Override
    public ResponseModel getMyInfo(String email) {
        ResponseModel response = new ResponseModel();

        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new Exception("User not found"));
            UserModel userModel = Utils.mapUserEntityToModel(user);  // Changed to use Utils class
            response.setStatusCode(200);
            response.setUser(userModel);
            response.setMessage("User info retrieved");

        } catch (Exception e) {
            response.setStatusCode(404);
            response.setMessage("Error: " + e.getMessage());
        }

        return response;
    }

    @Override
    public ResponseModel deleteUser(String userId) {
        ResponseModel response = new ResponseModel();

        try {
            Long id = Long.valueOf(userId);
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new Exception("User not found"));

            userRepository.deleteById(id);
            response.setStatusCode(200);
            response.setMessage("User deleted successfully");

        } catch (Exception e) {
            response.setStatusCode(404);
            response.setMessage("Deletion error: " + e.getMessage());
        }

        return response;
    }

    @Override
    public ResponseModel getUserBookingHistory(String userId) {
        ResponseModel response = new ResponseModel();

        try {
            User user = userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new Exception("User not found"));

            UserModel userModel = Utils.mapUserEntityToModelWithBookings(user);  // Changed to use Utils class
            response.setStatusCode(200);
            response.setUser(userModel);
            response.setMessage("Booking history retrieved");

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving booking history: " + e.getMessage());
        }

        return response;
    }
}
    */