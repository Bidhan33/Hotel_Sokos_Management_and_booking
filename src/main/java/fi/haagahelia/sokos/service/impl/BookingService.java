package fi.haagahelia.sokos.service.impl;

import fi.haagahelia.sokos.entity.Booking;
import fi.haagahelia.sokos.entity.Room;
import fi.haagahelia.sokos.entity.User;
import fi.haagahelia.sokos.exception.Exception;
import fi.haagahelia.sokos.model.ResponseModel;
import fi.haagahelia.sokos.model.BookingModel;
import fi.haagahelia.sokos.repo.BookingRepository;
import fi.haagahelia.sokos.repo.RoomRepository;
import fi.haagahelia.sokos.repo.UserRepository;
import fi.haagahelia.sokos.service.interfac.IBookingService;
import fi.haagahelia.sokos.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService implements IBookingService {

    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseModel saveBooking(Long roomId, Long userId, Booking bookingRequest) {
        ResponseModel response = new ResponseModel();

        try {
            // Validate booking dates
            LocalDate today = LocalDate.now();
            if (bookingRequest.getCheckInDate().isBefore(today)) {
                throw new Exception("Check-in date cannot be in the past");
            }
            
            if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate()) || 
                bookingRequest.getCheckOutDate().isEqual(bookingRequest.getCheckInDate())) {
                throw new Exception("Check-out date must be after check-in date");
            }

            // Validate guest numbers
            if (bookingRequest.getNumOfAdults() < 1) {
                throw new Exception("At least one adult is required for booking");
            }
            
            if (bookingRequest.getNumOfChildren() < 0) {
                throw new Exception("Number of children cannot be negative");
            }

            // Get room and user
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new Exception("Room not found"));
                    
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new Exception("User not found"));

            // Check if room is available for requested dates
            List<Room> availableRooms = roomRepository.findAvailableRoomsByDatesAndTypes(
                    bookingRequest.getCheckInDate(),
                    bookingRequest.getCheckOutDate(),
                    room.getRoomType()
            );
            
            if (availableRooms.stream().noneMatch(r -> r.getId().equals(room.getId()))) {
                throw new Exception("Room is not available for the selected dates");
            }

            // Generate booking confirmation code
            String bookingCode = Utils.generateRandomConfirmationCode(8);
            
            // Set booking details
            bookingRequest.setBookingConfirmationCode(bookingCode);
            bookingRequest.setRoom(room);
            bookingRequest.setUser(user);
            // Note: totalNumOfGuest will be calculated automatically by @PrePersist/@PreUpdate
            
            // Save booking
            Booking savedBooking = bookingRepository.save(bookingRequest);
            
            // Map to response model
            BookingModel bookingModel = Utils.mapBookingEntityToModelWithRelations(savedBooking, true);
            
            response.setStatusCode(200);
            response.setMessage("Booking successful");
            response.setBooking(bookingModel);
            response.setBookingConfirmationCode(bookingCode);

        } catch (Exception e) {
            response.setStatusCode(400);
            response.setMessage("Booking error: " + e.getMessage());
        } catch (java.lang.Exception e) {
            response.setStatusCode(500);
            response.setMessage("System error during booking: " + e.getMessage());
        }
        
        return response;
    }

    @Override
    public ResponseModel findBookingByConfirmationCode(String confirmationCode) {
        ResponseModel response = new ResponseModel();

        try {
            Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode)
                    .orElseThrow(() -> new Exception("Booking not found with confirmation code: " + confirmationCode));
                    
            BookingModel bookingModel = Utils.mapBookingEntityToModelWithRelations(booking, true);
            
            response.setStatusCode(200);
            response.setMessage("Booking found");
            response.setBooking(bookingModel);

        } catch (Exception e) {
            response.setStatusCode(404);
            response.setMessage("Error: " + e.getMessage());
        } catch (java.lang.Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving booking: " + e.getMessage());
        }
        
        return response;
    }

    @Override
    public ResponseModel getAllBookings() {
        ResponseModel response = new ResponseModel();

        try {
            List<Booking> bookings = bookingRepository.findAll();
            List<BookingModel> bookingModels = bookings.stream()
                    .map(booking -> Utils.mapBookingEntityToModelWithRelations(booking, true))
                    .toList();
                    
            response.setStatusCode(200);
            response.setMessage("Bookings retrieved successfully");
            response.setBookingList(bookingModels);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving bookings: " + e.getMessage());
        }
        
        return response;
    }

    @Override
    public ResponseModel cancelBooking(String bookingId) {
        ResponseModel response = new ResponseModel();

        try {
            Long id = Long.valueOf(bookingId);
            Booking booking = bookingRepository.findById(id)
                    .orElseThrow(() -> new Exception("Booking not found with id: " + bookingId));
            
            // Check if booking is in the future
            if (booking.getCheckInDate().isBefore(LocalDate.now())) {
                throw new Exception("Cannot cancel a booking that has already started or completed");
            }
            
            bookingRepository.deleteById(id);
            
            response.setStatusCode(200);
            response.setMessage("Booking cancelled successfully");

        } catch (Exception e) {
            response.setStatusCode(404);
            response.setMessage("Error: " + e.getMessage());
        } catch (java.lang.Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error cancelling booking: " + e.getMessage());
        }
        
        return response;
    }
}