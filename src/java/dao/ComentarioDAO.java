/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import beans.Comentario;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.*;

/**
 * DAO COMENTARIO
 * @author Maria Galbis
 */
public class ComentarioDAO {

    private String tabla = "COMENTARIO";

    public ComentarioDAO(){}
    
    /**
     * 
     * @param comentario
     * @return 
     */
    public Comentario insert(Comentario comentario) {
        int id = 0;

        try {
            Utiles.conect();
            MySql.initTrans();
            id = MySql.insert(this.tabla, comentario.toInsertData());
            MySql.commitTrans();
            MySql.rollbackTrans();
            MySql.cerrarConexion();
        } catch (Exception ex) {
            Logger.getLogger(ComentarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new Comentario(id);
    }

    /**
     * 
     * @param comentario 
     */
    public void update(Comentario comentario) {
        try {
            Utiles.conect();
            MySql.initTrans();
            MySql.update(comentario.getId(), this.tabla, comentario.toUpdateMap());
            MySql.commitTrans();
            MySql.rollbackTrans();
            MySql.cerrarConexion();
        } catch (Exception ex) {
            Logger.getLogger(ComentarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
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
            Logger.getLogger(ComentarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Obtiene un HashMap con los datos del comentario indicado
     * @param id
     * @return HashMap con los datos
     */
    public HashMap findComentarioMap(int id) {
        HashMap<String, Object> map = new HashMap();

        try {
            Utiles.conect();
            MySql.initTrans();
            map = MySql.getRow(this.tabla, id);
            MySql.cerrarConexion();
        } catch (Exception ex) {
            Logger.getLogger(ComentarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return map;
    }
    
    /**
     * Obtiene el Comentario indicado
     * @param id
     * @return Comentario
     */
    public Comentario findComentario(int id){
        HashMap map = findComentarioMap(id);
        
        UsuarioDAO uDAO = new UsuarioDAO();
        DocumentoDAO dDAO = new DocumentoDAO();
        
        Comentario comentario = new Comentario();
        if(map != null && !map.isEmpty()){
            comentario.setId(id);
            comentario.setContenido((String) map.get("CONTENIDO"));
            comentario.setFecha((Date) map.get("FECHA"));
            comentario.setDocumento(dDAO.findDocumento((int) map.get("ID_DOCUMENTO")));
            comentario.setUsuario(uDAO.findUsuario((int) map.get("ID_USUARIO")));
        }
        
        return comentario;
    }

    /**
     * Obtiene una lista con todos los comentarios de la base de datos
     * @return Lista con los Comentarios
     */
    public List<Comentario> findAllComentario() {
        List aux = new ArrayList();

        try {
            Utiles.conect();
            List<Integer> ids = (ArrayList) MySql.getIds(tabla, null, "FECHA");
            MySql.cerrarConexion();

            for (int i = 0; i < ids.size(); i++) {
                aux.add(new Comentario(ids.get(i)));
            }
        } catch (Exception ex) {
            Logger.getLogger(ComentarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return aux;
    }

    /**
     * Cuenta los comentarios que corresponden a los parámetros indicados
     * @param param
     * @return Número de registros
     */
    public int getComentarioCount(String param) {
        int count = 0;

        try {
            Utiles.conect();
            MySql.initTrans();
            count = MySql.count(this.tabla, param);
            MySql.cerrarConexion();
        } catch (Exception ex) {
            Logger.getLogger(ComentarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }
}
