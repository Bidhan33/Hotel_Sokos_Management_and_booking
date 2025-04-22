package fi.haagahelia.sokos.service.interfac;

import fi.haagahelia.sokos.entity.Booking;
import fi.haagahelia.sokos.model.ResponseModel;

public interface IBookingService {
    ResponseModel saveBooking(String roomId, String userId, Booking bookingRequest);
    ResponseModel findBookingByConfirmationCode(String confirmationCode);
    ResponseModel getAllBookings();
    ResponseModel cancelBooking(String bookingId);
}