package com.example.Crackers.Controllers;
import java.sql.Timestamp;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.Crackers.Services.UserProfilesService;

@Controller
public class RegistrationController {

    @Autowired
    private UserProfilesService userProfilesService;

    private String message;

    // 👉 Show registration page
    @GetMapping("/register")
    public String showRegistrationPage() {
        return "register"; // register.html
    }

    // 👉 Handle form submit
    @PostMapping("/register")
    public String newAccountRegistration(@RequestParam Map<String, String> newUserData,
                                         Model model) {
        try {

            String email = newUserData.get("emailId").toLowerCase();

            // ✅ Validation
            if (email.isEmpty() || email.isBlank()) {
                message = "Please enter your Email ID.";
                model.addAttribute("error", message);
                return "register";
            }

            // ✅ Check existing email
            if (userProfilesService.isEmailIdExists(email)) {
                message = "Email ID already exists.";
                model.addAttribute("error", message);
                return "register";
            }

            // ✅ Save user
            Map<String, Object> respMap = userProfilesService.addNewUser((Map)newUserData);

            message = respMap.get("message").toString();

            // ✅ Success message
            model.addAttribute("success", message);

            System.out.println(new Timestamp(System.currentTimeMillis()) +
                    "\tWEB\tNEW-ACCOUNT-REGISTRATION\t" + message);

            return "register";

        } catch (Exception e) {
            message = "Something went wrong!";
            model.addAttribute("error", message);

            System.err.println(e.getMessage());

            return "register";
        }
    }
}
