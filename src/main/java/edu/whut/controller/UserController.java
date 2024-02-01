package edu.whut.controller;

import edu.whut.pojo.User;
import edu.whut.service.UserService;
import edu.whut.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;
    @PostMapping ("login")
    public Result getLogin(@RequestBody User user) {
        Result res = userService.getLogin(user);
        return res;
    }
}
