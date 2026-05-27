package com.leftjoiners.bancosol.proyectobackend.controller;

import com.leftjoiners.bancosol.proyectobackend.dao.*;
import com.leftjoiners.bancosol.proyectobackend.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/tiendas")
@Controller
public class TiendasController {

    @Autowired
    protected TiendaRepository tiendaRepository;

    @Autowired
    protected CadenaRepository cadenaRepository;

    @Autowired
    protected ZonaRepository zonaRepository;

    @Autowired
    protected LocalidadRepository localidadRepository;

    @Autowired
    protected MunicipioRepository municipioRepository;

    @GetMapping("")
    public String doTiendas(Model model,
                            @RequestParam(value = "tiendas", required = false) List<TiendaEntity> tiendasFiltradas) {

        List<TiendaEntity> tiendas = tiendaRepository.findAll();

        model.addAttribute("tiendas", tiendas);
        model.addAttribute("currentSection", "tiendas");

        //Filtrado:
        List<CadenaEntity> cadenas = cadenaRepository.findAll();
        model.addAttribute("cadenas", cadenas);
        List<ZonaEntity> zonas = zonaRepository.findAll();
        model.addAttribute("zonas", zonas);
        List<LocalidadEntity> localidades = localidadRepository.findAll();
        model.addAttribute("localidades", localidades);

        return "/tiendas/tiendas";
    }

    @PostMapping("/filtrarTiendas")
    public String doFiltrarTiendas(Model model,
                                   @RequestParam(value = "cadena-tienda", required = false) Integer cadenaId,
                                   @RequestParam(value = "localidad-tienda", required = false) Integer localidadId,
                                   @RequestParam(value = "zona-tienda", required = false) Integer zonaId){


        List<TiendaEntity> tiendasFiltradas = this.tiendaRepository.filtrarTiendasMulticriterio(cadenaId, localidadId, zonaId);
        model.addAttribute("tiendas", tiendasFiltradas);
        model.addAttribute("currentSection", "tiendas");

        model.addAttribute("cadenas", this.cadenaRepository.findAll());
        model.addAttribute("zonas", this.zonaRepository.findAll());
        model.addAttribute("localidades", this.localidadRepository.findAll());

        model.addAttribute("cadenaMarcada", cadenaId);
        model.addAttribute("zonaMarcada", zonaId);
        model.addAttribute("localidadMarcada", localidadId);

        return "/tiendas/tiendas";
    }


    @GetMapping("/crearTienda")
    public String doCrearTienda(Model model, @RequestParam(value = "id", required = false) Integer idTienda) {

        // Si viene un ID en la URL, significa que queremos EDITAR. Si no, CREAR.
        boolean isEditando = (idTienda != null);
        model.addAttribute("editando", isEditando);
        model.addAttribute("currentSection", "tiendas");


        if (isEditando) {
            TiendaEntity tienda = tiendaRepository.findById(idTienda).orElse(null);
            model.addAttribute("tiendaActual", tienda);
        }

        model.addAttribute("cadenas", cadenaRepository.findAll());
        model.addAttribute("zonas", zonaRepository.findAll());
        model.addAttribute("municipios", municipioRepository.findAll());
        model.addAttribute("localidades", localidadRepository.findAll());

        return "tiendas/crear_tienda";
    }

    @PostMapping("/guardarTienda")
    public String guardarTienda(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam("nombre") String nombre,
            @RequestParam(value = "lineales", required = false) Integer lineales,
            @RequestParam("domicilio") String domicilio,
            @RequestParam("cadenaId") Integer cadenaId,
            @RequestParam("localidadId") Integer localidadId) {

        TiendaEntity tienda;

        if (id != null) {
            tienda = tiendaRepository.findById(id).orElse(new TiendaEntity());
        } else {
            tienda = new TiendaEntity();
        }

        tienda.setNombre(nombre);
        tienda.setDomicilio(domicilio);

        tienda.setLineales(lineales != null ? lineales : 0);

        CadenaEntity cadena = cadenaRepository.findById(cadenaId).orElse(null);
        tienda.setCadena(cadena);

        LocalidadEntity localidad = localidadRepository.findById(localidadId).orElse(null);
        tienda.setLocalidad(localidad);

        tiendaRepository.save(tienda);

        return "redirect:/tiendas";
    }
}
