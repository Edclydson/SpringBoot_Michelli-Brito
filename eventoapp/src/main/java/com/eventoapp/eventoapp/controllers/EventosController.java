package com.eventoapp.eventoapp.controllers;

import javax.validation.Valid;

import com.eventoapp.eventoapp.ConvidadoRepository;
import com.eventoapp.eventoapp.EventoRepository;
import com.eventoapp.eventoapp.models.Convidado;
import com.eventoapp.eventoapp.models.Evento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String form(@Valid Evento evento,BindingResult result,RedirectAttributes attributes){
        if(result.hasErrors()){
            attributes.addFlashAttribute("flashMessage", "Verifique os campos!");
            attributes.addFlashAttribute("flashType", "danger");
            return "redirect:/cadastrarevento";
        }
        er.save(evento);
        attributes.addFlashAttribute("flashMessage", "Evento cadastrado com sucesso!");
        attributes.addFlashAttribute("flashType", "success");
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
    public String detalheEvento(@PathVariable("codigo") long codigo,@Valid  Convidado convidado,BindingResult result,RedirectAttributes attributes){
        if(result.hasErrors()){
            attributes.addFlashAttribute("flashMessage", "Verifique os campos!");
            attributes.addFlashAttribute("flashType", "danger");
            return "redirect:/{codigo}";
        }
        Evento evento = er.findByCodigo(codigo);
        convidado.setEvento(evento);
        cr.save(convidado);
        attributes.addFlashAttribute("flashMessage", "Convidado adicionado com sucesso!");
        attributes.addFlashAttribute("flashType", "success");
        return "redirect:/{codigo}";
    }

    @RequestMapping(value="/deletarEvento",method=RequestMethod.GET)
    public String deletarEvento(long codigo){
        Evento evento = er.findByCodigo(codigo);
        if(cr.findByEvento(evento) != null){
            Iterable<Convidado> convidados = cr.findByEvento(evento);
            cr.deleteAll(convidados);
            er.delete(evento);
            return "redirect:/eventos";
        }else{
            er.delete(evento);
            return "redirect:/eventos";
        }

    }

    @RequestMapping(value="/deletarConvidado",method=RequestMethod.GET)
    public String deletarConvidado(String rg){
            Convidado convidado = cr.findByRg(rg);
            cr.delete(convidado);
            
            Evento evento = convidado.getEvento();
            long codigoLong = evento.getCodigo();
            String codigo = ""+ codigoLong;
            return "redirect:/"+ codigo;
    }

}
