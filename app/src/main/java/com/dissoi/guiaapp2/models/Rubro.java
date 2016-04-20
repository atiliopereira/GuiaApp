package com.dissoi.guiaapp2.models;


import com.google.gson.annotations.SerializedName;


public class Rubro
{
    public int id;
    public String nombre;
    public String descripcion;

    @SerializedName("n_empresas")
    public int nEmpresas;

    @Override
    public String toString()
    {
        return "nombre: " + nombre + " empresas: " + String.valueOf(nEmpresas);
    }
}
