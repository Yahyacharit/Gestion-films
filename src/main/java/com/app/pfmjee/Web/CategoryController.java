package com.app.pfmjee.Web;

import com.app.pfmjee.DAO.CategoryDAO;
import com.app.pfmjee.Entities.Category;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryDAO categoryDAO;

    @GetMapping
    public String list(Model model, HttpSession session) {
        List<Category> categories = categoryDAO.findAll();
        String role = (String) session.getAttribute("role");
        model.addAttribute("categories", categories);
        model.addAttribute("role", role);
        return "categories/list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("category", new Category());
        return "categories/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Category category) {
        categoryDAO.save(category);
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Byte id, Model model) {
        Category category = categoryDAO.findById(id);
        if (category == null) {
            return "redirect:/categories";
        }
        model.addAttribute("category", category);
        return "categories/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Category category) {
        categoryDAO.update(category);
        return "redirect:/categories";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Byte id) {
        categoryDAO.deleteById(id);
        return "redirect:/categories";
    }
}

