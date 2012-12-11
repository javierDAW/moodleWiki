/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.*;
import java.util.*;

/**
 * POJO USUARIO
 * @author María Galbis
 */
public class Usuario {

    private int id;
    private String nombre;
    private String ape1;
    private String ape2;
    private String telefono;
    private String email;
    private String login;
    private String password;
    private TipoUsuario tipoUsuario;
    private List<Documento> documentoList;
    private List<Object> documentoListPages;
    private List<Comentario> comentarioList;
    private List<Object> comentarioListPages;

    public Usuario() {}

    /**
     * Crea un Usuario y lo rellena automáticamente a partir de su id
     * 
     * @param id
     * @param documentoPages Documentos por página
     * @param comentarioPages Comentarios por página
     */
    public Usuario(int id) {
        this.id = id;

        UsuarioDAO us = new UsuarioDAO();
        TipoUsuarioDAO tuDAO = new TipoUsuarioDAO();
        HashMap row = us.findUsuarioMap(id);
        if (!row.isEmpty() && row != null) {
            nombre = (String) row.get("NOMBRE");
            ape1 = (String) row.get("APE1");
            ape2 = (String) row.get("APE2");
            telefono = (String) row.get("TELEFONO");
            email = (String) row.get("EMAIL");
            login = (String) row.get("LOGIN");
            password = (String) row.get("PASSWORD");
            tipoUsuario = tuDAO.findTipoUsuario((int) row.get("ID_TIPO_USUARIO"));

        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApe1() {
        return ape1;
    }

    public void setApe1(String ape1) {
        this.ape1 = ape1;
    }

    public String getApe2() {
        return ape2;
    }

    public void setApe2(String ape2) {
        this.ape2 = ape2;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    /**
     * Obtiene una lista con los documentos correspondientes a este usuario
     *
     * @return List con los documentos
     */
    public List<Documento> getDocumentoList() {
        return documentoList;
    }

    /**
     * Obtiene una lista paginada de los documentos correspondientes a este
     * documento
     *
     * @param page Número de página
     * @return List con los documentos
     */
    public List<Documento> getDocumentoList(int page) {
        return (List<Documento>) documentoListPages.get(page - 1);
    }

    /**
     * Establece una lista con los documentos correspondientes de este usuario
     *
     * @param documentoList List con los documentos
     */
    public void setDocumentoList(List<Documento> documentoList) {
        this.documentoList = documentoList;
    }

    public void setDocumentoListPages(List<Object> documentoListPages) {
        this.documentoListPages = documentoListPages;
    }

    /**
     * Obtiene una lista con los comentarios correspondientes a este usuario
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
     * @param limit Comentarios por página
     * @return List con los comentarios
     */
    public List<Comentario> getComentarioList(int page) {
        return (List<Comentario>) comentarioListPages.get(page - 1);
    }

    /**
     * Establece una lista con los comentarios correspondientes de este usuario
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
        return "null, '" + nombre + "', '" + ape1 + "', '" + ape2 + "', '" + telefono
                + "', '" + email + "', '" + login + "', '" + password + "', " + tipoUsuario;
    }

    /**
     * Crea un String con los datos listo para hacer un update
     *
     * @return String con los datos
     */
    public String toUpdateData() {
        return "nombre = '" + nombre + "', ape1 = '" + ape1 + "', ape2 = '" + ape2 + "', "
                + "telefono = '" + telefono + "', email = '" + email + "', login = '" + login + "', "
                + "password = '" + password + "', tipo_usuario = " + tipoUsuario;
    }

    /**
     * Crea un HashMap con los datos listo para hacer un update
     *
     * @return HashMap con los datos
     */
    public HashMap toUpdateMap() {
        HashMap map = new HashMap();
        map.put("nombre", "'" + nombre + "'");
        map.put("ape1", "'" + ape1 + "'");
        map.put("ape2", "'" + ape2 + "'");
        map.put("telefono", "'" + telefono + "'");
        map.put("email", "'" + email + "'");
        map.put("login", "'" + login + "'");
        map.put("password", "'" + password + "'");
        map.put("tipo_usuario", tipoUsuario);
        return map;
    }
}
