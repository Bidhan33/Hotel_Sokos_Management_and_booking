package fi.haagahelia.sokos.web;

import fi.haagahelia.sokos.model.ResponseModel;
import fi.haagahelia.sokos.service.interfac.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;

    // Get all users (Admin only)
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseModel> getAllUsers() {
        ResponseModel response = userService.getAllUsers();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Get a user by ID
    @GetMapping("/get-by-id/{userId}")
    public ResponseEntity<ResponseModel> getUserById(@PathVariable("userId") String userId) {
        ResponseModel response = userService.getUserById(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Delete a user (Admin only)
    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseModel> deleteUser(@PathVariable("userId") String userId) {
        ResponseModel response = userService.deleteUser(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Get the logged-in user's profile information
    @GetMapping("/get-logged-in-profile-info")
    public ResponseEntity<ResponseModel> getLoggedInUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ResponseModel response = userService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Get booking history of a user by user ID
    @GetMapping("/get-user-bookings/{userId}")
    public ResponseEntity<ResponseModel> getUserBookingHistory(@PathVariable("userId") String userId) {
        ResponseModel response = userService.getUserBookingHistory(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
