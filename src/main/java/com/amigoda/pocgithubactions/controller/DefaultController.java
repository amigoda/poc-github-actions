package com.amigoda.pocgithubactions.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"", "/"})
public class DefaultController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultController.class);

    @GetMapping({"", "/"})
    public ModelAndView index() {
        LOGGER.info(">> DefaultController.index()");
        return new ModelAndView("index");
    }
}
