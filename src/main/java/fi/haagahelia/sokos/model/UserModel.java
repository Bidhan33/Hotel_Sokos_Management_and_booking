package fi.haagahelia.sokos.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserModel {

    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private String role;
    private List<BookingModel> bookings = new ArrayList<>();

}