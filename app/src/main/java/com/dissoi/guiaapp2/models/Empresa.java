package com.dissoi.guiaapp2.models;

import java.util.ArrayList;
import java.util.List;


public class Empresa
{
    public int id;
    public Especialidad especialidad;
    public Horario horario;
    public List<Telefono> telefonos = new ArrayList<>();
    public List<Celular> celulares = new ArrayList<>();
    public Ciudad ciudad;
    public List<Linea> lineas = new ArrayList<>();
    public String nombre;
    public String descripcionCorta;
    public String descripcionLarga;
    public String horarioAtencion;
    public String tiempoDeRespuesta;
    public String email;
    public String web;
    public String sms;
    public String facebook;
    public String instagram;
    public String whatsapp;
    public String direccion;
    public String ubicacion;
    public Boolean recomendado;
    public String foto;
}
