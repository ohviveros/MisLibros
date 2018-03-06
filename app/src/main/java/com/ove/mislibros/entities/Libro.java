package com.ove.mislibros.entities;

import com.orm.SugarRecord;
import java.util.List;

/**
 * Created by ove on 1/03/18.
 */

public class Libro extends SugarRecord {

    private String titulo;
    private String autor;
    private String editorial;
    private String edicion;
    private String codigoIsbn;
    private String imagen;
    private String estado;
    private String prestatario;

    public Libro() {
    }

    public Libro(String titulo, String autor, String editorial, String edicion,
                 String codigoIsbn, String imagen, String estado, String prestatario) {
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.edicion = edicion;
        this.codigoIsbn = codigoIsbn;
        this.imagen = imagen;
        this.estado = estado;
        this.prestatario = prestatario;
    }

    public List<Libro> buscar(){
        return Libro.listAll(Libro.class);
    }

    public Libro buscarPorId(Long id){
        return Libro.findById(Libro.class, id);
    }

    public void guardar(Libro libro){
        libro.save();
    }
}
