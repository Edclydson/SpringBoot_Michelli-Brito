package com.eventoapp.eventoapp.controllers;

import com.eventoapp.eventoapp.EventoRepository;
import com.eventoapp.eventoapp.models.Evento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class EventosController {
    
    @Autowired
    private EventoRepository er;

    @RequestMapping(value="/cadastrarevento",method=RequestMethod.GET)
    public String form(){
        return "eventos/formEvento";
    }

    @RequestMapping(value="/cadastrarevento",method=RequestMethod.POST)
    public String form(Evento evento){
        er.save(evento);
        return "redirect:/cadastrarevento";
    }

}
