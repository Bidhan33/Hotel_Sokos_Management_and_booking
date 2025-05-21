package fi.haagahelia.sokos.utils;

import fi.haagahelia.sokos.entity.Booking;
import fi.haagahelia.sokos.entity.Room;
import fi.haagahelia.sokos.entity.User;
import fi.haagahelia.sokos.model.BookingModel;
import fi.haagahelia.sokos.model.RoomModel;
import fi.haagahelia.sokos.model.UserModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateRandomConfirmationCode(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

    public static UserModel mapUserEntityToModel(User user) {
        UserModel model = new UserModel();
        model.setId(user.getId());
        model.setName(user.getName());
        model.setEmail(user.getEmail());
        model.setPhoneNumber(user.getPhoneNumber());
        model.setRole(user.getRole());
        return model;
    }

    public static RoomModel mapRoomEntityToModel(Room room) {
        RoomModel model = new RoomModel();
        model.setId(room.getId());
        model.setRoomType(room.getRoomType());
        model.setRoomPrice(room.getRoomPrice());
        model.setRoomPhotoUrl(room.getRoomPhotoUrl());
        model.setRoomDescription(room.getRoomDescription());
        return model;
    }

    public static BookingModel mapBookingEntityToModel(Booking booking) {
        BookingModel model = new BookingModel();
        model.setId(booking.getId());
        model.setCheckInDate(booking.getCheckInDate());
        model.setCheckOutDate(booking.getCheckOutDate());
        model.setNumOfAdults(booking.getNumOfAdults());
        model.setNumOfChildren(booking.getNumOfChildren());
        model.setTotalNumOfGuest(booking.getTotalNumOfGuest());
        model.setBookingConfirmationCode(booking.getBookingConfirmationCode());
        return model;
    }

    public static RoomModel mapRoomEntityToModelWithBookings(Room room) {
        RoomModel model = mapRoomEntityToModel(room);
        if (room.getBookings() != null) {
            model.setBookings(room.getBookings().stream()
                .map(Utils::mapBookingEntityToModel)
                .collect(Collectors.toList()));
        }
        return model;
    }

    public static BookingModel mapBookingEntityToModelWithRelations(Booking booking, boolean includeUser) {
        BookingModel model = mapBookingEntityToModel(booking);
        if (includeUser && booking.getUser() != null) {
            model.setUser(mapUserEntityToModel(booking.getUser()));
        }
        if (booking.getRoom() != null) {
            model.setRoom(mapRoomEntityToModel(booking.getRoom()));
        }
        return model;
    }

    public static UserModel mapUserEntityToModelWithBookings(User user) {
        UserModel model = mapUserEntityToModel(user);
        if (user.getBookings() != null) {
            model.setBookings(user.getBookings().stream()
                .map(booking -> mapBookingEntityToModelWithRelations(booking, false))
                .collect(Collectors.toList()));
        }
        return model;
    }

    public static List<UserModel> mapUserEntityListToModelList(List<User> userList) {
        return userList.stream()
            .map(Utils::mapUserEntityToModel)
            .collect(Collectors.toList());
    }

    public static List<RoomModel> mapRoomEntityListToModelList(List<Room> roomList) {
        return roomList.stream()
            .map(Utils::mapRoomEntityToModel)
            .collect(Collectors.toList());
    }

    public static List<BookingModel> mapBookingEntityListToModelList(List<Booking> bookingList) {
        return bookingList.stream()
            .map(Utils::mapBookingEntityToModel)
            .collect(Collectors.toList());
    }
}
