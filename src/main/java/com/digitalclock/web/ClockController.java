package com.digitalclock.web;

import com.digitalclock.city.CityCatalog;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClockController {

    private static final DateTimeFormatter SERVER_TIME_FORMAT =
            DateTimeFormatter.ofPattern("h:mm:ss a", Locale.US);

    @GetMapping("/clock")
    public String clock(Model model) {
        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime now = ZonedDateTime.now(zone);

        model.addAttribute("timezone", zone.getId());
        model.addAttribute("timezones", buildTimezoneList(zone.getId()));
        model.addAttribute("serverTime", now.format(SERVER_TIME_FORMAT));
        return "clock";
    }

    private static List<String> buildTimezoneList(String defaultZone) {
        List<String> zones = new ArrayList<>(CityCatalog.TIMEZONE_IDS);
        if (!zones.contains(defaultZone)) {
            zones.add(0, defaultZone);
        }
        return zones;
    }
}
