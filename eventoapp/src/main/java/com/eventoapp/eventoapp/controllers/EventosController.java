package com.eventoapp.eventoapp.controllers;

import com.eventoapp.eventoapp.ConvidadoRepository;
import com.eventoapp.eventoapp.EventoRepository;
import com.eventoapp.eventoapp.models.Convidado;
import com.eventoapp.eventoapp.models.Evento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EventosController {
    
    @Autowired
    private EventoRepository er;

    @Autowired
    private ConvidadoRepository cr;

    @RequestMapping(value="/cadastrarevento",method=RequestMethod.GET)
    public String form(){
        return "eventos/formEvento";
    }

    @RequestMapping(value="/cadastrarevento",method=RequestMethod.POST)
    public String form(Evento evento){
        er.save(evento);
        return "redirect:/cadastrarevento";
    }

    @RequestMapping("/eventos")
    public ModelAndView listaeventos(){
        ModelAndView mv = new ModelAndView("index");
        Iterable<Evento> eventos = er.findAll();
        mv.addObject("Eventos", eventos);
        return mv;
    }

    @RequestMapping(value="/{codigo}",method=RequestMethod.GET)
    public ModelAndView detalheEvento(@PathVariable("codigo") long codigo){
        Evento evento = er.findByCodigo(codigo);
        ModelAndView mv = new ModelAndView("eventos/detalhesEvento");
        mv.addObject("Evento", evento);
        Iterable<Convidado> convidados = cr.findByEvento(evento);
        mv.addObject("Convidado",convidados);
        return mv;
    }

    @RequestMapping(value="/{codigo}",method=RequestMethod.POST)
    public String detalheEvento(@PathVariable("codigo") long codigo, Convidado convidado){
        Evento evento = er.findByCodigo(codigo);
        convidado.setEvento(evento);
        cr.save(convidado);
        return "redirect:/{codigo}";
    }

}
