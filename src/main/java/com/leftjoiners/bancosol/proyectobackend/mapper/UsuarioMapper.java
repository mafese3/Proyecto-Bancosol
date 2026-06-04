package com.leftjoiners.bancosol.proyectobackend.mapper;

import com.leftjoiners.bancosol.proyectobackend.dto.*;
import com.leftjoiners.bancosol.proyectobackend.entity.UsuarioEntity;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper extends MapperDTO<Usuario, UsuarioEntity> {
    @Override
    public Usuario toDTO(UsuarioEntity entity) {
        if (entity == null) return null;

        Usuario dto = new Usuario();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setCp(entity.getCp());
        //dto.setContrasenya(entity.getContrasenya());
        dto.setUsuario(entity.getUsuario());            //SIN ESTO EN REACT NO IBA
        dto.setEmail(entity.getEmail());
        dto.setTelefono(entity.getTelefono());
        dto.setRol(entity.getRol().getNombre());
        dto.setEntidad(entity.getEntidad().getNombre());
        dto.setZonaAsignada(entity.getZonaAsignada().getNombre());
        dto.setDistrito(entity.getDistrito().getNombre());
        dto.setLocalidad(entity.getLocalidad().getNombre());

        return dto;
    }
}
