package com.dissoi.guiaapp2;

import com.dissoi.guiaapp2.models.Ciudad;
import com.dissoi.guiaapp2.models.Empresa;
import com.dissoi.guiaapp2.models.Especialidad;
import com.dissoi.guiaapp2.models.Rubro;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;


public interface GuiaService
{
    @GET("/api-rest/rubros/todos/?format=json")
    Call<List<Rubro>> getAllRubros();

    @GET("/api-rest/empresas/by_rubro/{rubro_id}/?format=json")
    Call<List<Empresa>> getEmpresasByRubro(@Path("rubro_id") int rubroId);

    @GET("/api-rest/especialidades/todos/?format=json")
    Call<List<Especialidad>> getAllEspecialidades();

    @GET("/api-rest/empresas/by_especialidad/{especialidad_id}/?format=json")
    Call<List<Empresa>> getEmpresasByEspecialidad(@Path("especialidad_id") int especialidadId);

    @GET("/api-rest/empresas/by_especialidad_and_ciudad/{especialidad_id}/{ciudad_id}?format=json")
    Call<List<Empresa>> getEmpresasByEspecialidadAndCiudad(@Path("especialidad_id") int especialidadId, @Path("ciudad_id") int ciudadId);

    @GET("/api-rest/empresas/by_especialidad_recomendadas/{especialidad_id}/?format=json")
    Call<List<Empresa>> getEmpresasRecomendadasByEspecialidad(@Path("especialidad_id") int especialidadId);

    @GET("/api-rest/empresas/recomendadas/?format=json")
    Call<List<Empresa>> getEmpresasRecomendadas();

    @GET("/api-rest/empresas/by_id/{empresa_id}/?format=json")
    Call<List<Empresa>> getEmpresaById(@Path("empresa_id") int empresaId);

    @GET("/api-rest/empresas/todos/?format=json")
    Call<List<Empresa>> getEmpresaByIds(@Query("ids") String empresaIds);

    @GET("/api-rest/ciudades/todos/?format=json")
    Call<List<Ciudad>> getAllCiudades();
}
