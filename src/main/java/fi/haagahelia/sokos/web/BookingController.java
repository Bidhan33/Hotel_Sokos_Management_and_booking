package fi.haagahelia.sokos.web;

import fi.haagahelia.sokos.entity.Booking;
import fi.haagahelia.sokos.model.ResponseModel;
import fi.haagahelia.sokos.service.interfac.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private IBookingService bookingService;

    // Endpoint to book a room for a user
    @PostMapping("/book-room/{roomId}/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<ResponseModel> saveBooking(@PathVariable String roomId,
                                                     @PathVariable String userId,
                                                     @RequestBody Booking bookingRequest) {

        // Call the service layer to save the booking
        ResponseModel response = bookingService.saveBooking(roomId, userId, bookingRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Endpoint to get all bookings (Admin access)
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseModel> getAllBookings() {
        ResponseModel response = bookingService.getAllBookings();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Endpoint to get booking by confirmation code
    @GetMapping("/get-by-confirmation-code/{confirmationCode}")
    public ResponseEntity<ResponseModel> getBookingByConfirmationCode(@PathVariable String confirmationCode) {
        ResponseModel response = bookingService.findBookingByConfirmationCode(confirmationCode);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Endpoint to cancel a booking
    @DeleteMapping("/cancel/{bookingId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<ResponseModel> cancelBooking(@PathVariable String bookingId) {
        ResponseModel response = bookingService.cancelBooking(bookingId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
