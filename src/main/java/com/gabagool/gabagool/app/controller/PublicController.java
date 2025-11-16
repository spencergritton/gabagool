package com.gabagool.gabagool.app.controller;

import com.gabagool.gabagool.app.dto.request.RegisterRequest;
import com.gabagool.gabagool.app.service.PersonService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class PublicController {
    private static final Logger log = LoggerFactory.getLogger(PublicController.class);
    private final PersonService personService;

    public PublicController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public String index(Model model) {
        List<Map<String, Object>> voices = List.of(
                Map.of("value", "af_heart", "label", "Heart (American Female)", "selected", true),
                Map.of("value", "am_fenrir", "label", "Fenrir (American Male)"),
                Map.of("value", "bf_emma", "label", "Emma (British Female)")
        );
        model.addAttribute("voiceOptions", voices);
        return "home/index";
    }

    @GetMapping("/register")
    public String register() {
        return "home/register";
    }

    @PostMapping("/register")
    @Transactional
    public String register(@ModelAttribute @Valid RegisterRequest request, Model model) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            model.addAttribute("message", "Passwords do not match");
            return "fragments/error";
        }

        try {
            personService.createPerson(request);
            model.addAttribute("message",
                    "Successful account registration. Redirecting you to login...");
            model.addAttribute("redirectUrl", "/login");
            return "fragments/success";
        } catch (Exception e) {
            log.error("Issue creating user for request {}", request, e);
            model.addAttribute("message", "Sever error. Please retry.");
            return "fragments/error";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "home/login";
    }
}
