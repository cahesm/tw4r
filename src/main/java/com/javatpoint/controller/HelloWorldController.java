package com.javatpoint.controller;
import com.javatpoint.model.UnitsSearch;
import com.javatpoint.service.DisponibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class HelloWorldController {
    
    @Autowired
  DisponibilityService disponibilityService;
    
	@RequestMapping("/hello")
	public String helloWorld(Model m) {
		//String message = "Hello World, Spring MVC @ Javatpoint";
                
                m.addAttribute("main", true );
                m.addAttribute("offers", new UnitsSearch() );
                
                
                m.addAttribute("dispList", disponibilityService.getNewestDisponibilities() );
                
		//m.addAttribute("message", message);
		return "hello"; 
	}
}
