package com.leftjoiners.bancosol.proyectobackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    private Integer id;
    private String rol;
    private String usuario;
    private String contrasenya;
    private String nombre;
    private String telefono;
    private String email;
    private String entidad;
    private String zonaAsignada;
    private String localidad;
    private Integer cp;
    private String distrito;

    private List<Integer> colaboradoresCoordinados;
    private List<Integer> tiendasCoordinadas;
    private List<Integer> tiendasCapitaneadas;
}