package com.example.Crackers.Controllers;

import java.util.Map;
import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Crackers.Services.LoginService;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    private String message;

    // 👉 Show login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // login.html
    }

    // 👉 Handle normal login
    @PostMapping("/login")
    public String userLogin(@RequestParam Map<String, String> userLogin,
                            Model model) {
        try {
            Map<String, Object> loginResp = loginService.validateLogin(userLogin);

            // Login failed
            if (loginResp.get("httpStatus").toString().equals("BAD_REQUEST")) {
                message = loginResp.get("message").toString();
                model.addAttribute("error", message);

                System.out.println(new Timestamp(System.currentTimeMillis()) +
                        "\tWEB\tUSER-LOGIN\t" + message);

                return "login";
            }
            // Login success
            message = "Login successful!";
            model.addAttribute("success", message);

            model.addAttribute("user", loginResp);

            System.out.println(new Timestamp(System.currentTimeMillis()) +
                    "\tWEB\tUSER-LOGIN SUCCESS [USER ID: " + loginResp.get("userId") + "]");

            return "index"; // redirect page after login

        } catch (Exception e) {
            message = "Something went wrong!";
            model.addAttribute("error", message);

            System.err.println(e.getMessage());

            return "login";
        }
    }
}