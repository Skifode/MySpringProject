package main.controllers;

import main.repositories.Init;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class DefaultController {

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String index()
  {
    return "index";
  }
}
