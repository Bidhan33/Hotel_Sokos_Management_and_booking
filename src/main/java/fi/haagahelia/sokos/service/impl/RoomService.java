package fi.haagahelia.sokos.service.impl;

import fi.haagahelia.sokos.entity.Room;
import fi.haagahelia.sokos.exception.Exception;
import fi.haagahelia.sokos.model.ResponseModel;
import fi.haagahelia.sokos.model.RoomModel;
import fi.haagahelia.sokos.repo.RoomRepository;
import fi.haagahelia.sokos.service.interfac.IRoomService;
import fi.haagahelia.sokos.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class RoomService implements IRoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public ResponseModel addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description) throws Exception {
        ResponseModel response = new ResponseModel();

        try {
            // Validate inputs
            if (photo == null || photo.isEmpty()) {
                throw new Exception("Room photo is required");
            }
            if (roomType == null || roomType.trim().isEmpty()) {
                throw new Exception("Room type is required");
            }
            if (roomPrice == null || roomPrice.compareTo(BigDecimal.ZERO) <= 0) {
                throw new Exception("Room price must be positive");
            }

            // Process photo (in a real app, you'd upload to storage)
            String imageUrl = "https://storage.example.com/rooms/" + System.currentTimeMillis() + "-" + photo.getOriginalFilename();

            // Create and save room
            Room room = new Room();
            room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            room.setRoomPhotoUrl(imageUrl);
            room.setRoomDescription(description);

            Room savedRoom = roomRepository.save(room);
            RoomModel roomModel = Utils.mapRoomEntityToModel(savedRoom);

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Room added successfully");
            response.setRoom(roomModel);

        } catch (Exception e) {
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
            throw e; // Re-throw for controller to handle
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
            List<Room> rooms = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Rooms retrieved successfully");
            response.setRoomList(Utils.mapRoomEntityListToModelList(rooms));
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error retrieving rooms: " + e.getMessage());
        }

        return response;
    }

    @Override
    public ResponseModel deleteRoom(String roomId) throws Exception {
        ResponseModel response = new ResponseModel();

        try {
            Long id = Long.parseLong(roomId);
            Room room = roomRepository.findById(id)
                    .orElseThrow(() -> new Exception("Room not found with ID: " + roomId));

            roomRepository.delete(room);
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Room deleted successfully");
        } catch (NumberFormatException e) {
            throw new Exception("Invalid room ID format");
        }

        return response;
    }

    @Override
    public ResponseModel updateRoom(String roomId, String description, String roomType, BigDecimal roomPrice, MultipartFile photo) throws Exception {
        ResponseModel response = new ResponseModel();

        try {
            Long id = Long.parseLong(roomId);
            Room room = roomRepository.findById(id)
                    .orElseThrow(() -> new Exception("Room not found with ID: " + roomId));

            // Update fields
            if (roomType != null) room.setRoomType(roomType);
            if (roomPrice != null) room.setRoomPrice(roomPrice);
            if (description != null) room.setRoomDescription(description);
            if (photo != null && !photo.isEmpty()) {
                room.setRoomPhotoUrl("https://storage.example.com/rooms/" + System.currentTimeMillis() + "-" + photo.getOriginalFilename());
            }

            Room updatedRoom = roomRepository.save(room);
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Room updated successfully");
            response.setRoom(Utils.mapRoomEntityToModel(updatedRoom));

        } catch (NumberFormatException e) {
            throw new Exception("Invalid room ID format");
        }

        return response;
    }

    @Override
    public ResponseModel getRoomById(String roomId) throws Exception {
        ResponseModel response = new ResponseModel();

        try {
            Long id = Long.parseLong(roomId);
            Room room = roomRepository.findById(id)
                    .orElseThrow(() -> new Exception("Room not found with ID: " + roomId));

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Room found");
            response.setRoom(Utils.mapRoomEntityToModelWithBookings(room));

        } catch (NumberFormatException e) {
            throw new Exception("Invalid room ID format");
        }

        return response;
    }

    @Override
    public ResponseModel getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        ResponseModel response = new ResponseModel();

        try {
            List<Room> availableRooms = roomRepository.findAvailableRoomsByDatesAndTypes(checkInDate, checkOutDate, roomType);
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Available rooms retrieved successfully");
            response.setRoomList(Utils.mapRoomEntityListToModelList(availableRooms));
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error finding available rooms: " + e.getMessage());
        }

        return response;
    }

    @Override
    public ResponseModel getAllAvailableRooms() {
        ResponseModel response = new ResponseModel();

        try {
            List<Room> availableRooms = roomRepository.getAllAvailableRooms();
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Available rooms retrieved successfully");
            response.setRoomList(Utils.mapRoomEntityListToModelList(availableRooms));
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error retrieving available rooms: " + e.getMessage());
        }

        return response;
    }
}