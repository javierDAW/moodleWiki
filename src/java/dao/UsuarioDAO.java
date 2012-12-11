/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import beans.Usuario;
import beans.Comentario;
import beans.Documento;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.*;

/**
 * DAO USUARIO
 * @author María Galbis
 */
public class UsuarioDAO {

    private String tabla = "USUARIO";

    public UsuarioDAO(){}
    
    /**
     * Realiza un insert en la tabla usuario
     * @param usuario Pojo usuario
     * @return El usuario insertado
     */
    public Usuario insert(Usuario usuario) {
        int id = 0;

        try {
            Utiles.conect();
            MySql.initTrans();
            id = MySql.insert(this.tabla, usuario.toInsertData());
            MySql.commitTrans();
            MySql.cerrarConexion();
        } catch (Exception ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return findUsuario(id);
    }

    /**
     * Realiza un update en la tabla usuario
     * @param usuario Pojo usuario
     */
    public void update(Usuario usuario) {
        try {
            Utiles.conect();
            MySql.initTrans();
            MySql.update(usuario.getId(), this.tabla, usuario.toUpdateMap());
            MySql.commitTrans();
            MySql.cerrarConexion();
        } catch (Exception ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Realiza un delete en la tabla usuario
     * @param id id del usuario
     */
    public void delete(int id) {
        try {
            Utiles.conect();
            MySql.initTrans();
            MySql.delete(id, this.tabla);
            MySql.commitTrans();
            MySql.cerrarConexion();
        } catch (Exception ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Obtiene un HashMap con los valores de los campos del usuario indicado
     * @param id id del usuario
     * @return HashMap con los datos
     */
    public HashMap findUsuarioMap(int id) {
        HashMap<String, Object> map = new HashMap();

        try {
            Utiles.conect();
            map = MySql.getRow(this.tabla, id);
            MySql.cerrarConexion();
        } catch (Exception ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return map;
    }
    
    /**
     * Obtiene un pojo usuario con los datos
     * @param id id del usuario
     * @return Pojo usuario
     */
     public Usuario findUsuario(Integer id) {
         if(id == 0){return null;}
        
        UsuarioDAO us = new UsuarioDAO();
        HashMap row = us.findUsuarioMap(id);
        
        TipoUsuarioDAO tuDAO = new TipoUsuarioDAO();
        Usuario usuario = new Usuario();
        if (!row.isEmpty() && row != null) {
            usuario.setId(id);
            usuario.setNombre((String) row.get("NOMBRE"));
            usuario.setApe1((String) row.get("APE1"));
            usuario.setApe2((String) row.get("APE2"));
            usuario.setTelefono((String) row.get("TELEFONO"));
            usuario.setEmail((String) row.get("EMAIL"));
            usuario.setLogin((String) row.get("LOGIN"));
            usuario.setPassword((String) row.get("PASSWORD"));
            usuario.setTipoUsuario(tuDAO.findTipoUsuario((int) row.get("ID_TIPO_USUARIO")));
        }
        
        return usuario;
    }
    
     /**
      * Obtiene un list con todos los usuarios de la base de datos, con sus documentos
      * y sus comentarios paginados
      * @param documentoPages limit de documentos
      * @param comentarioPages limit de comentarios
      * @return List con los usuarios
      */
    public List<Usuario> findAllUsuario(Integer documentoPages, Integer comentarioPages) {
        List aux = new ArrayList();

        try {
            Utiles.conect();
            List<Integer> ids = (ArrayList) MySql.getIds(tabla, null, null);
            MySql.cerrarConexion();
       
            for (int i = 0; i < ids.size(); i++) {
                aux.add(new Usuario(ids.get(i)));
            }
        } catch (Exception ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return aux;
    }
    
    /**
     * Obtiene un list con todos los usuarios de la base de datos
     * @return List con los usuarios
     */
    public List<Usuario> findAllUsuario() {
        List aux = new ArrayList();

        try {
            Utiles.conect();
            List<Integer> ids = (ArrayList) MySql.getIds(tabla, null, null);
            MySql.cerrarConexion();

            for (int i = 0; i < ids.size(); i++) {
                aux.add(findUsuario(ids.get(i)));
            }
        } catch (Exception ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return aux;
    }

    /**
     * Cuenta los usuarios
     * @param param Parametros de consulta
     * @return int con el numero de usuarios
     */
    public int getUsuarioCount(String param) {
        int count = 0;
        
        try {
            Utiles.conect();
            count = MySql.count(this.tabla, param);
            MySql.cerrarConexion();
        } catch (Exception ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return count;
    }

    /**
     * Lista los documentos del usuario indicado
     * @param id
     * @return Lista con los pojos documento
     */
    public List<Documento> listarDocumentos(int id) {
        List<Documento> documentos = new ArrayList();

        try {
            String param = "ID_USUARIO = " + id;

            Utiles.conect();
            ArrayList<Integer> ids = MySql.getIds("DOCUMENTO", param, "FECHA");
            MySql.cerrarConexion();

            if (ids != null && !ids.isEmpty()) {
                for (int i = 0; i < ids.size(); i++) {
                    documentos.add(new Documento(ids.get(i)));
                }
            }
            
        } catch (Exception ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return documentos;
    }

    /**
     * Obtiene una lista de listas de los documentos del usuario indicado
     * @param id
     * @param limit limite de documentos por página
     * @param limitComentarios limite de comentarios por página
     * @return lista con las listas de los pojos documento
     */
    public List<Object> listarDocumentos(int id, int limit, int limitComentarios) {
        List<Object> pag = new ArrayList();
        
        try {
            String param = "ID_USUARIO = " + id;

            Utiles.conect();
            int paginas = MySql.getPages("DOCUMENTO", param, limit);
            ArrayList<Integer> ids = MySql.getIds("DOCUMENTO", param, "FECHA");

            MySql.cerrarConexion();

            int n = 0;
            
            if (ids != null && !ids.isEmpty()) {
                
                for (int i = 0; i < paginas; i++) {
                    List<Documento> documentos = new ArrayList();

                    for (int j = n; j < limit; n++) {
                        Documento doc = new Documento((int) ids.get(n));
                        DocumentoDAO docDAO = new DocumentoDAO();
                        doc.setComentarioListPages(docDAO.listarComentarios(doc.getId(), limitComentarios));
                        documentos.add(doc);
                        if(n == ids.size()-1) {
                            pag.add(documentos);
                            break;
                        } 
                    }

                    pag.add(documentos);
                    
                }
            }
            
        } catch (Exception ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return pag;
    }

    /**
     * Lista los comentarios del usuario indicado
     * @param id
     * @return lista con los pojos comentario
     */
    public List<Comentario> listarComentarios(int id) {
        List<Comentario> comentarios = new ArrayList();
        
        try {
            String param = "ID_USUARIO = " + id;

            Utiles.conect();
            ArrayList<Integer> ids = MySql.getIds("COMENTARIO", param, "FECHA");
            MySql.cerrarConexion();

            if (ids != null && !ids.isEmpty()) {
                for (int i = 0; i < ids.size(); i++) {
                    comentarios.add(new Comentario((int) ids.get(i)));
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return comentarios;
    }

    /**
     * Lista de listas con los comentarios del usuario indicado
     * @param id
     * @param limit limite de comentarios por página
     * @return lista con las listas de pojos comentario
     */
    public List<Object> listarComentarios(int id, int limit) {
        List<Object> pag = new ArrayList();
        
        try {
            String param = "ID_USUARIO = " + id;

            Utiles.conect();
            int paginas = MySql.getPages("COMENTARIO", param, limit);
            ArrayList<Integer> ids = MySql.getIds("COMENTARIO", param, "FECHA");

            MySql.cerrarConexion();

            int n = 0;

            if (ids != null && !ids.isEmpty()) {
                for (int i = 0; i < paginas; i++) {
                    List<Comentario> comentarios = new ArrayList();

                    for (int j = n; j < limit; n++) {
                        comentarios.add(new Comentario((int) ids.get(n)));
                        if(n == ids.size()-1) {
                            pag.add(comentarios);
                            break;
                        } 
                    }

                    pag.add(comentarios);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return pag;
    }
}
