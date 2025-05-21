package fi.haagahelia.sokos.web;

import fi.haagahelia.sokos.exception.Exception;
import fi.haagahelia.sokos.model.ResponseModel;
import fi.haagahelia.sokos.service.interfac.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private IRoomService roomService;

    @PostMapping("/add")
   // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseModel> addNewRoom(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("roomType") String roomType,
            @RequestParam("roomPrice") BigDecimal roomPrice,
            @RequestParam(value = "roomDescription", required = false) String roomDescription) {
        
        try {
            ResponseModel response = roomService.addNewRoom(photo, roomType, roomPrice, roomDescription);
            return ResponseEntity
                    .status(response.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (Exception e) {
            ResponseModel errorResponse = new ResponseModel();
            errorResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseModel> getAllRooms() {
        ResponseModel response = roomService.getAllRooms();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/types")
    public ResponseEntity<List<String>> getRoomTypes() {
        return ResponseEntity.ok(roomService.getAllRoomTypes());
    }

    @GetMapping("/room-by-id/{roomId}")
    public ResponseEntity<ResponseModel> getRoomById(@PathVariable String roomId) {
        try {
            ResponseModel response = roomService.getRoomById(roomId);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            ResponseModel errorResponse = new ResponseModel();
            errorResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/all-available-rooms")
    public ResponseEntity<ResponseModel> getAvailableRooms() {
        ResponseModel response = roomService.getAllAvailableRooms();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/available-rooms-by-date-and-type")
    public ResponseEntity<ResponseModel> getAvailableRoomsByDateAndType(
            @RequestParam LocalDate checkInDate,
            @RequestParam LocalDate checkOutDate,
            @RequestParam String roomType) {
        
        try {
            ResponseModel response = roomService.getAvailableRoomsByDateAndType(checkInDate, checkOutDate, roomType);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            ResponseModel errorResponse = new ResponseModel();
            errorResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/update/{roomId}")
   // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseModel> updateRoom(
            @PathVariable String roomId,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "roomType", required = false) String roomType,
            @RequestParam(value = "roomPrice", required = false) BigDecimal roomPrice,
            @RequestParam(value = "roomDescription", required = false) String roomDescription) {
        
        try {
            ResponseModel response = roomService.updateRoom(roomId, roomDescription, roomType, roomPrice, photo);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            ResponseModel errorResponse = new ResponseModel();
            errorResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/delete/{roomId}")
   // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseModel> deleteRoom(@PathVariable String roomId) {
        try {
            ResponseModel response = roomService.deleteRoom(roomId);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            ResponseModel errorResponse = new ResponseModel();
            errorResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}