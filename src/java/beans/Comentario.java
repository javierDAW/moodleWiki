/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.*;
import java.util.*;
import model.Utiles;

/**
 * POJO COMENTARIO
 * @author maría Galbis
 */
public class Comentario {

    private int id;
    private String contenido;
    private Date fecha;
    private Usuario usuario;
    private Documento documento;

    /**
     * Crea un Comentario vacío
     */
    public Comentario() {
    }

    /**
     * Crea un Comentario y lo rellena automáticamente a partir del id indicado
     * @param id El id del comentario
     */
    public Comentario(int id) {
        this.id = id;

        ComentarioDAO com = new ComentarioDAO();
        UsuarioDAO uDAO = new UsuarioDAO();
        DocumentoDAO dDAO = new DocumentoDAO();
        HashMap row = com.findComentarioMap(id);

        if (!row.isEmpty() || row != null) {
            contenido = (String) row.get("CONTENIDO");
            fecha = (Date) row.get("FECHA");
            usuario = uDAO.findUsuario((int) row.get("ID_USUARIO"));
            documento = dDAO.findDocumento((int) row.get("ID_DOCUMENTO"));
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    /**
     * Crea un string con los datos listo para hacer un insert
     *
     * @return String de los datos
     */
    public String toInsertData() {
        return "null, '" + contenido + "', '" + Utiles.dateToMySQLDate(fecha, true) + "', "
                + documento.getId() + ", " + usuario.getId();
    }

    /**
     * Crea un HashMap con los datos listo para hacer un update
     *
     * @return HashMap con los datos
     */
    public HashMap toUpdateMap() {
        HashMap map = new HashMap();
        map.put("contenido", "'" + contenido + "'");
        map.put("fecha", "'" + Utiles.dateToMySQLDate(fecha, true) + "'");
        map.put("id_documento", documento.getId());
        map.put("id_usuario", usuario.getId());
        return map;
    }
}
