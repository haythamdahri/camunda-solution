package com.microservices.fr.tech.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/data")
public class HomeController {
	
	@GetMapping(path = "/apiv")

}
