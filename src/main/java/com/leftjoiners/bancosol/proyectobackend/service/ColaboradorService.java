/*
Javier Urbaneja Benítez: 100%
*/

package com.leftjoiners.bancosol.proyectobackend.service;

import com.leftjoiners.bancosol.proyectobackend.dao.ColaboradoresRespository;
import com.leftjoiners.bancosol.proyectobackend.dao.LocalidadRepository;
import com.leftjoiners.bancosol.proyectobackend.dao.UsuarioRepository;
import com.leftjoiners.bancosol.proyectobackend.dto.Colaborador;
import com.leftjoiners.bancosol.proyectobackend.dto.Localidad;
import com.leftjoiners.bancosol.proyectobackend.entity.ColaboradorEntity;
import com.leftjoiners.bancosol.proyectobackend.entity.LocalidadEntity;
import com.leftjoiners.bancosol.proyectobackend.entity.UsuarioEntity;
import com.leftjoiners.bancosol.proyectobackend.mapper.ColaboradorMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ColaboradorService {
    private final ColaboradoresRespository colaboradoresRespository;
    private final ColaboradorMapper colaboradorMapper;
    private final LocalidadRepository localidadRepository;
    private final UsuarioRepository usuarioRepository;

    public List<Colaborador> listarColaboradores () {
        List<ColaboradorEntity> colaboradores = this.colaboradoresRespository.findAll();
        return this.colaboradorMapper.toDTOList(colaboradores);
    }

    public Colaborador buscarColaborador (Integer id) {
        ColaboradorEntity colaborador = this.colaboradoresRespository.findById(id).orElse(null);
        return this.colaboradorMapper.toDTO(colaborador);
    }

    public Integer guardarColaborador(Integer id, String nombre, String codigo, String contactoPrincipal, String domicilio, String cp, String observaciones, Integer idLocalidadSede, Integer idLocalidadColabora, Boolean temporal, Integer idCoordinador){
        ColaboradorEntity colaborador;

        if(id != null) {
            colaborador = this.colaboradoresRespository.findById(id).orElse(new ColaboradorEntity());
        } else {
            colaborador = new ColaboradorEntity();
        }

        LocalidadEntity localidadSede = this.localidadRepository.findById(idLocalidadSede).orElse(null);
        LocalidadEntity colaboraEn = this.localidadRepository.findById(idLocalidadColabora).orElse(null);
        UsuarioEntity coordinador = this.usuarioRepository.findById(idCoordinador).orElse(null);

        colaborador.setNombre(nombre);
        colaborador.setCodigo(codigo);
        colaborador.setContactoPrincipal(contactoPrincipal);
        colaborador.setDomicilio(domicilio);
        colaborador.setCp(cp);
        colaborador.setLocalidadSede(localidadSede);
        colaborador.setColaboraEn(colaboraEn);
        colaborador.setTemporal(temporal);
        colaborador.setCoordinador(coordinador);
        colaborador.setObservaciones(observaciones);

        colaborador = this.colaboradoresRespository.save(colaborador);
        return colaborador.getId();
    }

    public void eliminarColaborador(Integer id) {
        if (id != null){
            this.colaboradoresRespository.deleteById(id);
        }
    }
}
