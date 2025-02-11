package com.app.pfmjee.Web;

import com.app.pfmjee.Entities.Role;
import com.app.pfmjee.Entities.User;
import com.app.pfmjee.Security.JwtUtil;
import com.app.pfmjee.DAO.UserDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private JwtUtil jwtUtil = new JwtUtil();

    private final UserDAO userDAO;

    @Autowired
    public AuthController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "Auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {
        User user = userDAO.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            Role role = user.getRole();
            System.out.println("Utilisateur connecté : " + username + " avec rôle : " + role);

            String token = jwtUtil.generateToken(username, role);

            session.setAttribute("token", token);

            return "redirect:/films";
        } else {
            model.addAttribute("errorMessage", "Invalid username or password!");
            return "Auth/login";
        }
    }




    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "Auth/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {
        if (userDAO.findByUsername(username) != null) {
            model.addAttribute("errorMessage", "Le nom d'utilisateur est déjà pris !");
            return "Auth/register";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(Role.USER);

        userDAO.save(user);

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        session.setAttribute("token", token);

        return "redirect:/films";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return "redirect:/auth/login";
    }

}
