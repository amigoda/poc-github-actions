package com.amigoda.pocgithubactions.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.zone.ZoneRulesException;

@RestController
@RequestMapping("/api")
public class ApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);

    @GetMapping("/time")
    public String time(
            @RequestParam(value = "timeZone", required = false) String timeZone
    ) {
        final LocalDateTime now;
        try {
            now = now(timeZone);
        }
        catch (ZoneRulesException exc) {
            return "ZoneRulesException: " + exc.getMessage();
        }
        catch (DateTimeException exc) {
            return "DateTimeException: " + exc.getMessage();
        }

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return now.format(formatter);
    }

    private static LocalDateTime now(String timeZone) {
        if (timeZone == null || timeZone.isEmpty()) {
            return LocalDateTime.now(Clock.systemDefaultZone());
        }
        return LocalDateTime.now(ZoneId.of(timeZone));
    }
}
