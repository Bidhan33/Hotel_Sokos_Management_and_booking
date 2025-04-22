package fi.haagahelia.sokos.service.interfac;

import fi.haagahelia.sokos.model.ResponseModel;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IRoomService {
    ResponseModel addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description);
    List<String> getAllRoomTypes();
    ResponseModel getAllRooms();
    ResponseModel deleteRoom(String roomId);
    ResponseModel updateRoom(String roomId, String description, String roomType, BigDecimal roomPrice, MultipartFile photo);
    ResponseModel getRoomById(String roomId);
    ResponseModel getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);
    ResponseModel getAllAvailableRooms();
}