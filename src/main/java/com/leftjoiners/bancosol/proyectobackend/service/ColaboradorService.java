/*
Javier Urbaneja Benítez: 100%
*/

package com.leftjoiners.bancosol.proyectobackend.service;

import com.leftjoiners.bancosol.proyectobackend.dao.ColaboradoresRespository;
import com.leftjoiners.bancosol.proyectobackend.dto.Colaborador;
import com.leftjoiners.bancosol.proyectobackend.entity.ColaboradorEntity;
import com.leftjoiners.bancosol.proyectobackend.mapper.ColaboradorMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ColaboradorService {
    private final ColaboradoresRespository colaboradoresRespository;
    private final ColaboradorMapper colaboradorMapper;

    public List<Colaborador> listarColaboradores () {
        List<ColaboradorEntity> colaboradores = this.colaboradoresRespository.findAll();
        return this.colaboradorMapper.toDTOList(colaboradores);
    }

    public Colaborador buscarColaborador (Integer id) {
        ColaboradorEntity colaborador = this.colaboradoresRespository.findById(id).orElse(null);
        return this.colaboradorMapper.toDTO(colaborador);
    }

    public void guardarColaborador(Integer id, String nombre, String codigo, String contactoPrincipal, String domicilio, String cp, String observaciones){
        ColaboradorEntity colaborador;

        if(id != null) {
            colaborador = this.colaboradoresRespository.findById(id).orElse(new ColaboradorEntity());
        } else {
            colaborador = new ColaboradorEntity();
        }

        colaborador.setNombre(nombre);
        colaborador.setCodigo(codigo);
        colaborador.setContactoPrincipal(contactoPrincipal);
        colaborador.setDomicilio(domicilio);
        colaborador.setCp(cp);
        colaborador.setObservaciones(observaciones);

        this.colaboradoresRespository.save(colaborador);
    }

    public void eliminarColaborador(Integer id) {
        if (id != null){
            this.colaboradoresRespository.deleteById(id);
        }
    }
}
