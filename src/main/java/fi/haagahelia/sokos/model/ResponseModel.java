package fi.haagahelia.sokos.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseModel {

    private int statusCode;
    private String message;

    private String token;
    private String role;
    private String expirationTime;
    private String bookingConfirmationCode;

    private UserModel user;
    private RoomModel room;
    private BookingModel booking;
    private List<UserModel> userList;
    private List<RoomModel> roomList;
    private List<BookingModel> bookingList;


}