package com.example.Crackers.Controllers;
import java.sql.Timestamp;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.Crackers.Services.UserLoginCredentialsService;
import com.example.Crackers.Services.UserSessionsService;

@Controller
@RequestMapping("/login")
public class UserLoginCredentialsController {

    @Autowired
    private UserLoginCredentialsService userLoginCredentialsService;

    @Autowired
    private UserSessionsService userSessionsService;

    private String message;

    // 👉 Show Confirm Page
    @GetMapping("/confirm")
    public String showConfirmPage(@RequestParam int id,
                                 @RequestParam String token,
                                 Model model) {
        model.addAttribute("userId", id);
        model.addAttribute("token", token);
        return "confirm";
    }

    // 👉 Handle Confirm Submit
    @PostMapping("/confirm")
    public String confirmAccount(@RequestParam int userId,
                                 @RequestParam String token,
                                 Model model) {
        try {

            Map<String, Object> resp = userLoginCredentialsService.confirmUserToken(userId, token);
            message = resp.get("message").toString();

            model.addAttribute("success", message);

            return "confirm";

        } catch (Exception e) {
            message = "Something went wrong!";
            model.addAttribute("error", message);
            return "confirm";
        }
    }

    // 👉 Show Forgot Password Page
    @GetMapping("/forgot")
    public String showForgotPage() {
        return "forgot";
    }

    // 👉 Handle Forgot Password
    @PostMapping("/forgot")
    public String forgotPassword(@RequestParam String emailId,
                                Model model) {
        try {

            Map<String, Object> resp = userLoginCredentialsService.userForgotLoginCreds(emailId);
            message = resp.get("message").toString();

            model.addAttribute("success", message);

            return "forgot";

        } catch (Exception e) {
            message = "Something went wrong!";
            model.addAttribute("error", message);
            return "forgot";
        }
    }

    // 👉 Show Change Password Page
    @GetMapping("/change")
    public String showChangePage(@RequestParam int id,
                                @RequestParam String token,
                                Model model) {
        model.addAttribute("userId", id);
        model.addAttribute("token", token);
        return "change-password";
    }

    // 👉 Handle Change Password
    @PostMapping("/change")
    public String changePassword(@RequestParam int userId,
                                @RequestParam String password,
                                Model model) {
        try {

            Map<String, Object> data = Map.of("password", password);

            Map<String, Object> resp =
                    userLoginCredentialsService.userChangeForgotPassword(userId, data);

            message = resp.get("message").toString();

            model.addAttribute("success", message);

            return "change-password";

        } catch (Exception e) {
            message = "Something went wrong!";
            model.addAttribute("error", message);
            return "change-password";
        }
    }

    // 👉 First login update (optional page)
    @GetMapping("/firstlogin")
    public String updateFirstLogin(@RequestParam String token, Model model) {
        try {

            if (!userSessionsService.isSessionTokenExists(token)) {
                model.addAttribute("error", "Invalid Session Token");
                return "error";
            }

            int userId = userSessionsService.getBySessionToken(token).getUserId();

            Map<String, Object> resp =
                    userLoginCredentialsService.updateFirstLoginStatus(userId);

            model.addAttribute("success", resp.get("message"));

            return "success";

        } catch (Exception e) {
            model.addAttribute("error", "Something went wrong!");
            return "error";
        }
    }
}