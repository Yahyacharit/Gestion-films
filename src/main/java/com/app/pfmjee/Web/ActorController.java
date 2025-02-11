package com.app.pfmjee.Web;

import com.app.pfmjee.DAO.ActorDAO;
import com.app.pfmjee.Entities.Actor;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/actors")
public class ActorController {

    @Autowired
    private ActorDAO actorDAO;

    @GetMapping
    public String listActors(Model model, HttpSession session) {
        List<Actor> actors = actorDAO.findAll();
        String role = (String) session.getAttribute("role");
        model.addAttribute("actors", actors);
        model.addAttribute("role", role);
        return "actors/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("actor", new Actor());
        return "actors/add";
    }

    @PostMapping("/add")
    public String addActor(@ModelAttribute Actor actor) {
        actorDAO.save(actor);
        return "redirect:/actors";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Short id, Model model) {
        Actor actor = actorDAO.findById(id);
        if (actor == null) {
            return "redirect:/actors";
        }
        model.addAttribute("actor", actor);
        return "actors/edit";
    }

    @PostMapping("/update")
    public String updateActor(@ModelAttribute Actor actor) {
        actorDAO.update(actor);
        return "redirect:/actors";
    }

    @GetMapping("/delete/{id}")
    public String deleteActor(@PathVariable Short id) {
        actorDAO.deleteById(id);
        return "redirect:/actors";
    }
}
