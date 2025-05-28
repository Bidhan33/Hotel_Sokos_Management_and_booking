/* package fi.haagahelia.sokos.web;

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

    // Delete a user (Admin only) - Working endpoint
    @DeleteMapping("/delete/{userId}")
   // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseModel> deleteUser(@PathVariable("userId") String userId) {
        ResponseModel response = userService.deleteUser(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Get the logged-in user's profile information - Fixed
    @GetMapping("/get-logged-in-profile-info")
    public ResponseEntity<ResponseModel> getLoggedInUserProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            // Debug logging
            System.out.println("Authentication object: " + authentication);
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body(new ResponseModel(401, "Not authenticated"));
            }
            
            String email = authentication.getName();
            System.out.println("Authenticated email: " + email);
            
            ResponseModel response = userService.getMyInfo(email);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            System.err.println("Error in getLoggedInUserProfile: " + e.getMessage());
            return ResponseEntity.status(500).body(new ResponseModel(500, "Server error"));
        }
    }

    // Get booking history of a user by user ID - Fixed
    @GetMapping("/get-user-bookings/{userId}")
    public ResponseEntity<ResponseModel> getUserBookingHistory(@PathVariable("userId") String userId) {
        try {
            // First verify authentication
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body(new ResponseModel(401, "Not authenticated"));
            }
            
            ResponseModel response = userService.getUserBookingHistory(userId);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseModel(500, "Error retrieving bookings"));
        }
    }
}
    */