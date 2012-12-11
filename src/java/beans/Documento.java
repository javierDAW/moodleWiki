/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.*;
import java.util.*;
import model.*;

/**
 * POJO DOCUMENTO
 * @author María Galbis
 */
public class Documento {

    private int id;
    private String titulo;
    private String contenido;
    private Date fecha;
    private Integer nota;
    private String etiquetas;
    private boolean privado;
    private Usuario usuario;
    private List<Comentario> comentarioList;
    private List<Object> comentarioListPages;

    /**
     * Documento vacío
     */
    public Documento() {
    }

    /**
     * Crea un Documento y lo rellena directamente a partir de su id
     * 
     * @param id
     * @param pagesComentario Número de comentarios por página
     */
    public Documento(int id) {
        DocumentoDAO doc = new DocumentoDAO();
        UsuarioDAO uDAO = new UsuarioDAO();
        HashMap row = doc.findDocumentoMap(id);

        if (!row.isEmpty() && row != null) {
            this.id = id;
            titulo = (String) row.get("TITULO");
            contenido = (String) row.get("CONTENIDO");
            fecha = (Date) row.get("FECHA");
            nota = (Integer) row.get("NOTA");
            etiquetas = (String) row.get("ETIQUETAS");
            privado = (boolean) row.get("PRIVADO");
            usuario = uDAO.findUsuario((Integer) row.get("ID_USUARIO"));
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public String getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(String etiquetas) {
        this.etiquetas = etiquetas;
    }

    public boolean getPrivado() {
        return privado;
    }

    public void setPrivado(boolean privado) {
        this.privado = privado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene una lista con los comentarios correspondientes a este documento
     *
     * @return List con los comentarios
     */
    public List<Comentario> getComentarioList() {
        return comentarioList;
    }

    /**
     * Obtiene una lista paginada de los comentarios correspondientes a este
     * documento
     *
     * @param page Número de página
     * @return List con los comentarios de la página indicada
     */
    public List<Comentario> getComentarioList(int page) {
        return (List<Comentario>) comentarioListPages.get(page - 1);
    }

    /**
     * Establece una lista con los comentarios correspondientes de este
     * documento
     *
     * @param comentarioList List con los comentarios
     */
    public void setComentarioList(List<Comentario> comentarioList) {
        this.comentarioList = comentarioList;
    }

    public void setComentarioListPages(List<Object> comentarioListPages) {
        this.comentarioListPages = comentarioListPages;
    }

    /**
     * Crea un String con los datos listo para hacer un insert
     *
     * @return String con los datos
     */
    public String toInsertData() {
        return "null, '" + titulo + "', '" + contenido + "', '" + Utiles.dateToMySQLDate(fecha, true)
                + "', " + nota + ", " + usuario.getId() + ", '" + etiquetas + "', "
                + privado;
    }

    /**
     * Crea un HashMap con los datos listo para hacer un update
     *
     * @return HashMap con los datos
     */
    public HashMap toUpdateMap() {
        HashMap map = new HashMap();
        map.put("titulo", "'" + titulo + "'");
        map.put("contenido", "'" + contenido + "'");
        map.put("fecha", "'" + Utiles.dateToMySQLDate(fecha, true) + "'");
        map.put("nota", nota);
        map.put("id_usuario", usuario.getId());
        map.put("etiquetas", "'" + etiquetas + "'");
        map.put("privado", privado);
        return map;
    }
}
