package fi.haagahelia.sokos.service.impl;

import fi.haagahelia.sokos.entity.Room;
import fi.haagahelia.sokos.exception.Exception;
import fi.haagahelia.sokos.model.ResponseModel;
import fi.haagahelia.sokos.model.RoomModel;
import fi.haagahelia.sokos.repo.BookingRepository;
import fi.haagahelia.sokos.repo.RoomRepository;
import fi.haagahelia.sokos.service.interfac.IRoomService;
import fi.haagahelia.sokos.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class RoomService implements IRoomService {

    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public ResponseModel addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description) {
        ResponseModel response = new ResponseModel();

        try {
            // Assuming the URL is passed directly, no need for file storage service
            String imageUrl = null;
            if (photo != null && !photo.isEmpty()) {
                imageUrl = saveImageUrl(photo); // You can implement this method to directly store image URL
            }

            Room room = new Room();
            room.setRoomPhotoUrl(imageUrl);
            room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            room.setRoomDescription(description);

            Room savedRoom = roomRepository.save(room);
            RoomModel roomModel = Utils.mapRoomEntityToModel(savedRoom);

            response.setStatusCode(200);
            response.setMessage("Room added successfully");
            response.setRoom(roomModel);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error adding room: " + e.getMessage());
        }

        return response;
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public ResponseModel getAllRooms() {
        ResponseModel response = new ResponseModel();

        try {
            List<Room> roomList = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<RoomModel> roomModelList = Utils.mapRoomEntityListToModelList(roomList);

            response.setStatusCode(200);
            response.setMessage("Rooms retrieved successfully");
            response.setRoomList(roomModelList);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving rooms: " + e.getMessage());
        }

        return response;
    }

    @Override
    public ResponseModel deleteRoom(String roomId) {
        ResponseModel response = new ResponseModel();

        try {
            Long id = Long.valueOf(roomId);
            roomRepository.findById(id).orElseThrow(() -> new Exception("Room not found"));
            roomRepository.deleteById(id);
            response.setStatusCode(200);
            response.setMessage("Room deleted successfully");

        } catch (Exception e) {
            response.setStatusCode(404);
            response.setMessage("Error: " + e.getMessage());
        }

        return response;
    }

    @Override
    public ResponseModel updateRoom(String roomId, String description, String roomType, BigDecimal roomPrice, MultipartFile photo) {
        ResponseModel response = new ResponseModel();

        try {
            Long id = Long.valueOf(roomId);
            String imageUrl = null;

            if (photo != null && !photo.isEmpty()) {
                imageUrl = saveImageUrl(photo); // Storing image URL instead of uploading to a service
            }

            Room room = roomRepository.findById(id).orElseThrow(() -> new Exception("Room not found"));

            if (roomType != null) room.setRoomType(roomType);
            if (roomPrice != null) room.setRoomPrice(roomPrice);
            if (description != null) room.setRoomDescription(description);
            if (imageUrl != null) room.setRoomPhotoUrl(imageUrl);

            Room updatedRoom = roomRepository.save(room);
            RoomModel roomModel = Utils.mapRoomEntityToModel(updatedRoom);

            response.setStatusCode(200);
            response.setMessage("Room updated successfully");
            response.setRoom(roomModel);

        } catch (Exception e) {
            response.setStatusCode(404);
            response.setMessage("Error: " + e.getMessage());
        }

        return response;
    }

    @Override
    public ResponseModel getRoomById(String roomId) {
        ResponseModel response = new ResponseModel();

        try {
            Long id = Long.valueOf(roomId);
            Room room = roomRepository.findById(id).orElseThrow(() -> new Exception("Room not found"));
            RoomModel roomModel = Utils.mapRoomEntityToModelWithBookings(room);
            response.setStatusCode(200);
            response.setMessage("Room found");
            response.setRoom(roomModel);

        } catch (Exception e) {
            response.setStatusCode(404);
            response.setMessage("Error: " + e.getMessage());
        }

        return response;
    }

    @Override
    public ResponseModel getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        ResponseModel response = new ResponseModel();

        try {
            List<Room> availableRooms = roomRepository.findAvailableRoomsByDatesAndTypes(checkInDate, checkOutDate, roomType);
            List<RoomModel> roomModelList = Utils.mapRoomEntityListToModelList(availableRooms);
            response.setStatusCode(200);
            response.setMessage("Available rooms retrieved successfully");
            response.setRoomList(roomModelList);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error finding available rooms: " + e.getMessage());
        }

        return response;
    }

    @Override
    public ResponseModel getAllAvailableRooms() {
        ResponseModel response = new ResponseModel();

        try {
            List<Room> roomList = roomRepository.getAllAvailableRooms();
            List<RoomModel> roomModelList = Utils.mapRoomEntityListToModelList(roomList);
            response.setStatusCode(200);
            response.setMessage("Available rooms retrieved successfully");
            response.setRoomList(roomModelList);

        } catch (Exception e) {
            response.setStatusCode(404);
            response.setMessage("Error: " + e.getMessage());
        }

        return response;
    }

    // Utility method for saving image URL (can be customized based on how you're storing images)
    private String saveImageUrl(MultipartFile photo) {
        // You could implement logic to save the image URL or validate the image here.
        // If you're storing the URL directly, this could be just a return of the URL after saving the photo.
        return "https://example.com/path/to/uploaded-image.jpg"; // You can replace this with actual logic for saving and returning the URL
    }
}
