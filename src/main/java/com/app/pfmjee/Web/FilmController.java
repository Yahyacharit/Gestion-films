package com.app.pfmjee.Web;

import com.app.pfmjee.DAO.ActorDAO;
import com.app.pfmjee.DAO.CategoryDAO;
import com.app.pfmjee.DAO.FilmDAO;
import com.app.pfmjee.DAO.LanguageDAO;
import com.app.pfmjee.Entities.Actor;
import com.app.pfmjee.Entities.Category;
import com.app.pfmjee.Entities.Film;
import com.app.pfmjee.Entities.Language;
import com.app.pfmjee.Security.JwtUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/films")
public class FilmController {

    @Autowired
    private FilmDAO filmDAO;
    @Autowired
    private ActorDAO actorDAO;
    @Autowired
    private LanguageDAO languageDAO;
    @Autowired
    private CategoryDAO categoryDAO;
    private JwtUtil jwtUtil = new JwtUtil();


    @GetMapping
    public String listFilms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            Model model,
            HttpSession session) {

        String token = (String) session.getAttribute("token");

        String role = null;
        if (token != null && jwtUtil.validateToken(token, jwtUtil.extractUsername(token))) {
            role = jwtUtil.extractClaim(token, claims -> claims.get("role", String.class));
            session.setAttribute("role", role);
            model.addAttribute("role", role);
            System.out.println("🔹 Rôle envoyé au template : " + role);
        } else {
            session.setAttribute("role", role);
        }

        int totalFilms = filmDAO.countFilms();
        List<Film> films = filmDAO.findFilmsPaginated(page, size);

        int totalPages = (int) Math.ceil((double) totalFilms / size);
        model.addAttribute("films", films);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", size);

        return "films/list";
    }


    @GetMapping("/{id}")
    public String get(Model model, @PathVariable int id) {
        Film byId = filmDAO.findById(id);
        if (byId == null) {
            return "redirect:/films";
        }
        model.addAttribute("film", byId);
        return "films/detail";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        List<Category> categories = categoryDAO.findAll();
        List<Actor> actors = actorDAO.findAll();
        List<Language> languages = languageDAO.findAll();

        model.addAttribute("categories", categories);
        model.addAttribute("actors", actors);
        model.addAttribute("languages", languages);
        model.addAttribute("film", new Film());
        return "films/add";
    }

    @PostMapping("/add")
    public String addFilm(
            @RequestParam(value = "title") String title,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "releaseYear") String releaseYear,
            @RequestParam(value = "language") String language,
            @RequestParam(value = "actors", required = false) List<String> actorIds,
            @RequestParam(value = "categories", required = false) List<String> categoryIds,
            Model model) {

        Set<Actor> actors = new HashSet<>();
        if (actorIds != null) {
            for (String actorId : actorIds) {
                Actor actor = actorDAO.findById(Integer.parseInt(actorId));
                if (actor != null) {
                    actors.add(actor);
                }
            }
        }

        Set<Category> categories = new HashSet<>();
        if (categoryIds != null) {
            for (String categoryId : categoryIds) {
                Category category = categoryDAO.findById(Byte.parseByte(categoryId));
                if (category != null) {
                    categories.add(category);
                }
            }
        }

        Language languagById = languageDAO.findById((byte) Integer.parseInt(language));

        System.out.println("Acteurs sélectionnés: " + actors);
        System.out.println("Catégories sélectionnées: " + categories);

        Film film = new Film();
        film.setTitle(title);
        film.setDescription(description);
        film.setReleaseYear(Short.valueOf(releaseYear));
        film.setLanguage(languagById);
        film.setActors(actors);
        film.setRentalRate(BigDecimal.valueOf(3));
        film.setCategories(categories);

        // Sauvegarde en base
        filmDAO.save(film);

        return "redirect:/films";
    }

    @GetMapping("/edit/{id}")
    public String showEditFilmForm(@PathVariable("id") int id, Model model) {
        Film film = filmDAO.findById(id); // Récupération du film à modifier

        if (film == null) {
            return "redirect:/films";
        }

        List<Language> languages = languageDAO.findAll();
        List<Category> categories = categoryDAO.findAll();
        List<Actor> actors = actorDAO.findAll();

        model.addAttribute("film", film);
        model.addAttribute("languages", languages);
        model.addAttribute("categories", categories);
        model.addAttribute("actors", actors);

        return "films/edit";
    }


    @PostMapping("/edit/{id}")
    public String updateFilm(
            @PathVariable Short id,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "releaseYear") String releaseYear,
            @RequestParam(value = "language") String language,
            @RequestParam(value = "actors", required = false) List<String> actorIds,
            @RequestParam(value = "categories", required = false) List<String> categoryIds) {

        Film film = filmDAO.findById(id);
        if (film == null) {
            return "redirect:/films";
        }


        film.setTitle(title);
        film.setDescription(description);
        film.setReleaseYear(Short.valueOf(releaseYear));

        Language languageById = languageDAO.findById((byte) Integer.parseInt(language));
        film.setLanguage(languageById);

        Set<Actor> actors = new HashSet<>();
        if (actorIds != null) {
            for (String actorId : actorIds) {
                Actor actor = actorDAO.findById(Integer.parseInt(actorId));
                if (actor != null) {
                    actors.add(actor);
                }
            }
        }
        film.setActors(actors);

        Set<Category> categories = new HashSet<>();
        if (categoryIds != null) {
            for (String categoryId : categoryIds) {
                Category category = categoryDAO.findById(Byte.parseByte(categoryId));
                if (category != null) {
                    categories.add(category);
                }
            }
        }
        film.setCategories(categories);

        System.out.println("Film mis à jour: " + film);
        System.out.println("Acteurs sélectionnés: " + actors);
        System.out.println("Catégories sélectionnées: " + categories);

        filmDAO.update(film);

        return "redirect:/films";
    }


    @GetMapping("/delete/{id}")
    public String deleteFilm(@PathVariable int id) {
        System.out.println(id);
        filmDAO.deleteById(id);
        return "redirect:/films";
    }
}
