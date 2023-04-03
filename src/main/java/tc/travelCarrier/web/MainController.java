package tc.travelCarrier.web;

import lombok.RequiredArgsConstructor;
import tc.travelCarrier.domain.Weekly;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/")
    public String mainPage(Model model){
        Weekly weekly = new Weekly();
        model.addAttribute("weekly",weekly);
        return "page/weekly-form";
    }
}
