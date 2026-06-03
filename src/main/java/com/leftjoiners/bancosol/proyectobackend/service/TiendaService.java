package com.leftjoiners.bancosol.proyectobackend.service;

import com.leftjoiners.bancosol.proyectobackend.dao.*;
import com.leftjoiners.bancosol.proyectobackend.dto.Tienda;
import com.leftjoiners.bancosol.proyectobackend.entity.*;
import com.leftjoiners.bancosol.proyectobackend.mapper.TiendaMapper;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TiendaService {

    private final TiendaRepository tiendaRepository;
    private final CadenaRepository cadenaRepository;
    private final LocalidadRepository localidadRepository;
    private final DistritoRepository distritoRepository;
    private final TiendaCampanyaRepository tiendaCampanyaRepository;
    private final CampanyaRepository campanyaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TiendaMapper tiendaMapper;

    public List<Tienda> listarTiendas() {
        List<TiendaEntity> tiendas = this.tiendaRepository.findAll();
        return this.tiendaMapper.toDTOList(tiendas);
    }

    public List<Tienda> filtrarTiendas(Integer cadenaId, Integer localidadId, Integer zonaId) {
        List<TiendaEntity> tiendasEntities = this.tiendaRepository.filtrarTiendasMulticriterio(cadenaId, localidadId, zonaId);
        return this.tiendaMapper.toDTOList(tiendasEntities);
    }

    public Tienda buscarTienda(Integer id) {
        TiendaEntity tienda = this.tiendaRepository.findById(id).orElse(null);
        return this.tiendaMapper.toDTO(tienda);
    }

    public void guardarTienda(Integer id, String nombre, Integer lineales, String domicilio,
                              String cp, Integer distritoId, Integer cadenaId, Integer localidadId,
                              Integer coordPrimaveraId, Integer coordGRId) {
        TiendaEntity tiendaEntity;

        if (id != null) {
            tiendaEntity = this.tiendaRepository.findById(id).orElse(new TiendaEntity());
        } else {
            tiendaEntity = new TiendaEntity();
        }

        tiendaEntity.setNombre(nombre);
        tiendaEntity.setDomicilio(domicilio);

        if (lineales != null) {
            tiendaEntity.setLineales(lineales);
        } else {
            tiendaEntity.setLineales(0);
        }

        tiendaEntity.setCp(cp);

        if (distritoId != null) {
            tiendaEntity.setDistrito(this.distritoRepository.findById(distritoId).orElse(null));
        } else {
            tiendaEntity.setDistrito(null);
        }

        if (cadenaId != null) {
            tiendaEntity.setCadena(this.cadenaRepository.findById(cadenaId).orElse(null));
        } else {
            tiendaEntity.setCadena(null);
        }

        if (localidadId != null) {
            tiendaEntity.setLocalidad(this.localidadRepository.findById(localidadId).orElse(null));
        } else {
            tiendaEntity.setLocalidad(null);
        }

        tiendaEntity = this.tiendaRepository.save(tiendaEntity);

        this.gestionarCoordinador(tiendaEntity, 2, coordPrimaveraId);
        this.gestionarCoordinador(tiendaEntity, 1, coordGRId);
    }

    @Transactional
    public void eliminarTienda(Integer idTienda) {
        if (idTienda != null) {
            TiendaEntity tienda = this.tiendaRepository.findById(idTienda).orElse(null);

            if (tienda != null) {
                if (tienda.getTiendasCampanya() != null && !tienda.getTiendasCampanya().isEmpty()) {
                    //elimino relaciones
                    this.tiendaCampanyaRepository.deleteAll(tienda.getTiendasCampanya());
                }

                this.tiendaRepository.delete(tienda);
            }
        }
    }

    private void gestionarCoordinador(TiendaEntity tienda, Integer tipoCampanyaId, Integer coordinadorId) {
        UsuarioEntity coordinador = null;

        if (coordinadorId != null) {
            coordinador = this.usuarioRepository.findById(coordinadorId).orElse(null);
        }

        boolean relacionEncontrada = false;

        if (tienda.getTiendasCampanya() != null) {
            for (TiendaCampanyaEntity tc : tienda.getTiendasCampanya()) {
                if (tc.getCampanya().getTipoCampanya().getId().equals(tipoCampanyaId)) {
                    tc.setCoordinador(coordinador);
                    this.tiendaCampanyaRepository.save(tc);
                    relacionEncontrada = true;
                    break;
                }
            }
        }

        if (!relacionEncontrada && coordinador != null) {
            CampanyaEntity campanyaActiva = this.campanyaRepository.buscarUltimaCampanyaPorTipo(tipoCampanyaId);

            if (campanyaActiva != null) {
                TiendaCampanyaEntity nuevaRelacion = new TiendaCampanyaEntity();
                nuevaRelacion.setTienda(tienda);
                nuevaRelacion.setCampanya(campanyaActiva);
                nuevaRelacion.setCoordinador(coordinador);
                this.tiendaCampanyaRepository.save(nuevaRelacion);
            }
        }
    }
}