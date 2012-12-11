/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import beans.Usuario;
import beans.TipoUsuario;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.MySql;
import model.Utiles;

/**
 * DAO TIPOUSUARIO
 * @author María Galbis
 */
public class TipoUsuarioDAO {

    private String tabla = "TIPO_USUARIO";

    public TipoUsuarioDAO() {
    }

    /**
     * Realiza un insert en la tabla tipo_usuario
     * @param tipoUsuario
     * @return el tipoUsuario insertado
     */
    public TipoUsuario insert(TipoUsuario tipoUsuario) {
        int id = 0;

        try {
            Utiles.conect();
            MySql.initTrans();
            id = MySql.insert(this.tabla, tipoUsuario.toInsertData());
            MySql.commitTrans();
            MySql.rollbackTrans();
            MySql.cerrarConexion();
        } catch (Exception ex) {
            Logger.getLogger(TipoUsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new TipoUsuario(id);
    }

    /**
     * Realiza un update en la tabla tipo_usuario
     * @param tipoUsuario 
     */
    public void update(TipoUsuario tipoUsuario) {
        try {
            Utiles.conect();
            MySql.initTrans();
            MySql.update(tipoUsuario.getId(), this.tabla, tipoUsuario.toUpdateMap());
            MySql.commitTrans();
            MySql.rollbackTrans();
            MySql.cerrarConexion();
        } catch (Exception ex) {
            Logger.getLogger(TipoUsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Realiza un delete en la tabla tipo_usuario
     * @param id 
     */
    public void delete(int id) {
        try {
            Utiles.conect();
            MySql.initTrans();
            MySql.delete(id, this.tabla);
            MySql.commitTrans();
            MySql.rollbackTrans();
            MySql.cerrarConexion();
        } catch (Exception ex) {
            Logger.getLogger(TipoUsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Obtiene un hashMap con los datos del tipo_usuario indicado
     * @param id
     * @return HashMap con los datos
     */
    public HashMap findTipoUsuarioMap(int id) {
        HashMap<String, Object> map = new HashMap();

        try {
            Utiles.conect();
            MySql.initTrans();
            map = MySql.getRow(this.tabla, id);
            MySql.cerrarConexion();
        } catch (Exception ex) {
            Logger.getLogger(TipoUsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return map;
    }

    /**
     * Obtiene el pojo tipoUsuario lleno con sus datos
     * @param id
     * @return el pojo tipoUsuario
     */
    public TipoUsuario findTipoUsuario(int id) {
        HashMap map = findTipoUsuarioMap(id);

        TipoUsuario tipoUsuario = new TipoUsuario();

        if (map != null && !map.isEmpty()) {
            tipoUsuario.setId((int) map.get("ID"));
            tipoUsuario.setDescripcion((String) map.get("DESCRIPCION"));
        }

        return tipoUsuario;
    }

    /**
     * Cuenta los registros en la tabla tipo_usuario 
     * @param param parámetros de consulta
     * @return numero de registros encontrados
     */
    public int getTipoUsuarioCount(String param) {
        int count = 0;

        try {
            Utiles.conect();
            MySql.initTrans();
            count = MySql.count(this.tabla, param);
            MySql.cerrarConexion();
        } catch (Exception ex) {
            Logger.getLogger(TipoUsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return count;
    }

    /**
     * lista los usuarios del tipoUsuario indicado
     * @param id
     * @return lista con los pojos usuario
     */
    public List<Usuario> listarUsuarios(int id) {
        List<Usuario> usuarios = new ArrayList();

        try {
            String param = "ID_TIPO_USUARIO = " + id;

            Utiles.conect();
            List<Integer> ids = MySql.getIds("USUARIO", param, null);
            MySql.cerrarConexion();

            UsuarioDAO uDAO = new UsuarioDAO();
            if (ids != null || !ids.isEmpty()) {
                for (int i = 0; i < ids.size(); i++) {
                    usuarios.add(uDAO.findUsuario((int) ids.get(i)));
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(TipoUsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return usuarios;
    }
}
