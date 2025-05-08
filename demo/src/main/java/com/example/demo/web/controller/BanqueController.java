package com.example.demo.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.web.models.Compte;

@Controller
public class BanqueController {
    private List<Compte> comptes = new ArrayList<>();
    private int compteur = 1;

    @GetMapping("/")
    public String listeComptes(Model model) {
        model.addAttribute("comptes", comptes);
        return "listeComptes";
    }

    @GetMapping("/ajouter")
    public String formulaireAjout(Model model) {
        model.addAttribute("compte", new Compte());
        return "ajouterCompte";
    }

    @PostMapping("/ajouter")
    public String ajouterCompte(@ModelAttribute Compte compte) {
        compte.setId(compteur++);
        comptes.add(compte);
        return "redirect:/";
    }

    @GetMapping("/details/{id}")
    public String detailsCompte(@PathVariable int id, Model model) {
        Compte compte = comptes.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
        model.addAttribute("compte", compte);
        return "detailsCompte";
    }

    @PostMapping("/depot/{id}")
    public String depot(@PathVariable int id, @RequestParam double montant) {
        comptes.stream()
               .filter(c -> c.getId() == id)
               .findFirst()
               .ifPresent(c -> c.setSolde(c.getSolde() + montant));
        return "redirect:/details/" + id;
    }

    @PostMapping("/retrait/{id}")
    public String retrait(@PathVariable int id, @RequestParam double montant) {
        comptes.stream()
               .filter(c -> c.getId() == id)
               .findFirst()
               .ifPresent(c -> {
                   if (c.getSolde() >= montant) {
                       c.setSolde(c.getSolde() - montant);
                   }
               });
        return "redirect:/details/" + id;
    }
}
