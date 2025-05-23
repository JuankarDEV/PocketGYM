package com.example.pocketgym.Models;

import java.time.LocalDate;
import java.sql.Date;

public class Usuario {
    private int id;
    private String nombre;
    private String codigoNfc;
    private int nivel;
    private boolean suscripcionActiva;
    private Date fechaRegistro;
    private Date fechaVencimiento;

    public Usuario(int id, String nombre, String codigoNfc, int nivel, boolean suscripcionActiva, Date fechaRegistro) {
        this.id = id;
        this.nombre = nombre;
        this.codigoNfc = codigoNfc;
        this.nivel = nivel;
        this.suscripcionActiva = suscripcionActiva;
        this.fechaRegistro = fechaRegistro;
        this.fechaVencimiento = Date.valueOf(String.valueOf(LocalDate.now().plusDays(30)));
    }

    public Usuario(String nombre, String codigoNfc, int nivel, boolean suscripcionActiva, Date fechaVencimiento) {
        this.nombre = nombre;
        this.codigoNfc = codigoNfc;
        this.nivel = nivel;
        this.suscripcionActiva = suscripcionActiva;
        this.fechaVencimiento = fechaVencimiento;
    }

    public Usuario(String nombre, String dni, int nivel, boolean b) {
        this.nombre = nombre;
        this.codigoNfc = dni;
        this.nivel = nivel;
        this.suscripcionActiva = b;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCodigoNfc() { return codigoNfc; }
    public void setCodigoNfc(String codigoNfc) { this.codigoNfc = codigoNfc; }

    public int getNivel() { return nivel; }
    public void setNivel(int nivel) { this.nivel = nivel; }

    public boolean isSuscripcionActiva() { return suscripcionActiva; }
    public void setSuscripcionActiva(boolean suscripcionActiva) { this.suscripcionActiva = suscripcionActiva; }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}

