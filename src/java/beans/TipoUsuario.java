/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.TipoUsuarioDAO;
import java.util.HashMap;
import java.util.List;

/**
 * POJO TIPOUSUARIO
 * @author María Galbis
 */
public class TipoUsuario {

    private int id;
    private String descripcion;
    private List<Usuario> usuarioList;

    public TipoUsuario() {}

    /**
     * Crea un tipo de usuario y lo rellena automáticamente a partir de su id, 
     * tomando también una lista de sus respectivos usuarios.
     * @param id 
     */
    public TipoUsuario(int id) {
        this.id = id;

        TipoUsuarioDAO tu = new TipoUsuarioDAO();
        HashMap<String, Object> map = tu.findTipoUsuarioMap(id);

        if (!map.isEmpty() || map != null) {
            descripcion = (String) map.get("DESCRIPCION");

            if ((List<Usuario>) tu.listarUsuarios(id) != null
                    && !((List<Usuario>) tu.listarUsuarios(id)).isEmpty()) {
                usuarioList = (List<Usuario>) tu.listarUsuarios(id);
            }

        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene una lista con los usuarios correspondientes a este tipo de
     * usuario
     *
     * @return List con los usuarios
     */
    public List<Usuario> getusuarioList() {
        return usuarioList;
    }

    /**
     * Establece una lista con los usuarios correspondientes a este tipo de
     * usuario
     *
     * @param usuarioList List con los usuarios
     */
    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    /**
     * Crea un String con los datos listo para hacer un insert
     *
     * @return String con los datos
     */
    public String toInsertData() {
        return "null, '" + descripcion + "'";
    }

    /**
     * Crea un HashMap con los datos listo para hacer un update
     *
     * @return HashMap con los datos
     */
    public HashMap toUpdateMap() {
        HashMap map = new HashMap();
        map.put("descripcion", "'" + descripcion + "'");
        return map;
    }
}
