package fi.haagahelia.sokos.web;

import fi.haagahelia.sokos.model.LoginModel;
import fi.haagahelia.sokos.model.ResponseModel;
import fi.haagahelia.sokos.entity.User;
import fi.haagahelia.sokos.service.interfac.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseModel> register(@RequestBody User user) {
        ResponseModel response = userService.register(user);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseModel> login(@RequestBody LoginModel loginModel) {
        ResponseModel response = userService.login(loginModel);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
