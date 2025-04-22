package fi.haagahelia.sokos.service.interfac;

import fi.haagahelia.sokos.model.LoginModel;
import fi.haagahelia.sokos.model.ResponseModel;
import fi.haagahelia.sokos.entity.User;

public interface IUserService {

    ResponseModel register(User user);

    ResponseModel login(LoginModel loginModel);

    ResponseModel getAllUsers();

    ResponseModel getUserBookingHistory(String userId);

    ResponseModel deleteUser(String userId);

    ResponseModel getUserById(String userId);

    ResponseModel getMyInfo(String email);
}
