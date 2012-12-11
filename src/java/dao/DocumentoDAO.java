/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import beans.Comentario;
import beans.Documento;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.MySql;
import model.Utiles;

/**
 * DAO DOCUMENTO
 * @author Maria Galbis
 */
public class DocumentoDAO {

    private String tabla = "DOCUMENTO";

    public DocumentoDAO(){}
    
    /**
     * Realiza un insert del documento indicado, devolviendo este mismo
     * documento
     *
     * @param documento Documento a insertar
     * @return Documento insertado
     */
    public Documento insert(Documento documento) {
        int id = 0;

        try {
            Utiles.conect();
            MySql.initTrans();
            id = MySql.insert(this.tabla, documento.toInsertData());
            MySql.commitTrans();
            MySql.cerrarConexion();
        } catch (Exception ex) {
            Logger.getLogger(DocumentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return findDocumento(id);
    }

    /**
     * Realiza un update del documento indicado
     *
     * @param documento Documento a actualizar
     */
    public void update(Documento documento) {
        try {
            Utiles.conect();
            MySql.initTrans();
            MySql.update(documento.getId(), this.tabla, documento.toUpdateMap());
            MySql.commitTrans();
            MySql.cerrarConexion();
        } catch (Exception ex) {
            Logger.getLogger(DocumentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Realiza un delete del documento con el id indicado
     *
     * @param id Id del documento a eliminar
     */
    public void delete(int id) {
        try {
            Utiles.conect();
            MySql.initTrans();
            MySql.delete(id, this.tabla);
            MySql.commitTrans();
            MySql.cerrarConexion();
        } catch (Exception ex) {
            Logger.getLogger(DocumentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Busca un documento con el id indicado y devuelve un HashMap con los datos
     * 
     * @param id
     * @return HashMap con los datos
     */
    public HashMap findDocumentoMap(int id) {
        HashMap<String, Object> map = new HashMap();
        
        try {
            Utiles.conect();
            MySql.initTrans();
            map = MySql.getRow(this.tabla, id);
            MySql.cerrarConexion();
        } catch (Exception ex) {
            Logger.getLogger(DocumentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return map;
    }
    
    /**
     * Busca el documento con el id indicado y lo devuelve
     *
     * @param id Id del documento buscado
     * @return Documento con el id indicado
     * @throws Exception
     */
    public Documento findDocumento(int id) {
        HashMap row = findDocumentoMap(id);
        Documento documento = new Documento();
        
        UsuarioDAO uDAO = new UsuarioDAO();
        if (!row.isEmpty() && row != null) {
            documento.setId(id);
            documento.setTitulo((String) row.get("TITULO"));
            documento.setContenido((String) row.get("CONTENIDO"));
            documento.setFecha((Date) row.get("FECHA"));
            documento.setNota((Integer) row.get("NOTA"));
            documento.setEtiquetas((String) row.get("ETIQUETAS"));
            documento.setUsuario(uDAO.findUsuario((int) row.get("ID_USUARIO")));
            documento.setPrivado((boolean) row.get("PRIVADO"));
        }
        
        return documento;
    }

    /**
     * Obtiene una lista con todos los documentos de la base de datos
     * @return Lista de Documentos
     */
    public List<Documento> findAllDocumento(String param) {
        List aux = new ArrayList();

        try {
            Utiles.conect();
            List<Integer> ids = (ArrayList) MySql.getIds(tabla, param, "FECHA");
            MySql.cerrarConexion();

            for (int i = 0; i < ids.size(); i++) {
                aux.add(findDocumento(ids.get(i)));
            }
        } catch (Exception ex) {
            Logger.getLogger(DocumentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return aux;
    }

    /**
     * Obtiene una lista paginada con todos los documentos que respondan a unos 
     * parámetros
     * 
     * @param limit Documentos por página
     * @param cond
     * @return Lista con los Documentos paginados
     */
    public ArrayList findAllDocumento(int limit, String cond) {
        ArrayList pag = new ArrayList();

        try {
            Utiles.conect();
            int paginas = MySql.getPages(tabla, cond, limit);
            List<Integer> ids = MySql.getIds(tabla, cond, "FECHA");
            MySql.cerrarConexion();

            int n = 0;

            if (ids != null && !ids.isEmpty()) {
                for (int i = 0; i < paginas; i++) {
                    List<Documento> documentos = new ArrayList();

                    for (int j = 0; j < limit; n++) {

                        if (n < ids.size()) {
                            Documento doc = new Documento((int) ids.get(n));
                            doc.setComentarioList(listarComentarios((int) ids.get(n)));
                            documentos.add(doc);
                        }
                        j++;
                    }

                    pag.add(documentos);

                }
            }
        } catch (Exception ex) {
            Logger.getLogger(DocumentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return pag;
    }

    /**
     * Cuenta las páginas que se obtienen al establecer un límite, de todos los
     * documentos que respondan a unos parámetros
     * 
     * @param limit
     * @param cond
     * @return 
     */
    public int getAllDocumentPagesCount(int limit, String cond) {
        Utiles.conect();
        int pages = MySql.getPages(tabla, cond, limit);
        try {
            MySql.cerrarConexion();
        } catch (Exception ex) {
            Logger.getLogger(DocumentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return pages;
    }

    /**
     * Cuenta los documentos que siguen los parámetros indicados
     *
     * @param param String con los parámetros
     * @return Número de registros encontrados
     * @throws Exception
     */
    public int getDocumentoCount(String param) {
        int count = 0;
        
        try {
            Utiles.conect();
            MySql.initTrans();
            count = MySql.count(this.tabla, param);
            MySql.cerrarConexion();
        } catch (Exception ex) {
            Logger.getLogger(DocumentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return count;
    }

    /**
     * Crea una lista de comentarios que tengan como clave ajena el id del
     * documento indicado y la devuelve
     *
     * @param id Id del documento
     * @return Lista de comentarios
     * @throws Exception
     */
    public List<Comentario> listarComentarios(int id) {
        List<Comentario> comentarios = new ArrayList();
        
        try {
            String param = "ID_DOCUMENTO = " + id;

            Utiles.conect();
            MySql.initTrans();
            ArrayList ids = MySql.getIds("COMENTARIO", param, null);
            MySql.cerrarConexion();

            if (ids != null && !ids.isEmpty()) {
                for (int i = 0; i < ids.size(); i++) {
                    comentarios.add(new Comentario((int) ids.get(i)));
                }
            }
            
        } catch (Exception ex) {
            Logger.getLogger(DocumentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return comentarios;
    }

    /**
     * Crea una lista de listas de comentarios, dependiendo de los registros por
     * página que hayamos indicado, donde cada lista representa una página
     *
     * @param id Id del documento
     * @param limit Registros por página
     * @return Lista de listas
     * @throws Exception
     */
    public List<Object> listarComentarios(int id, int limit) {
        List<Object> pag = new ArrayList();
        
        try {
            String param = "ID_DOCUMENTO = " + id;

            Utiles.conect();
            int paginas = MySql.getPages("COMENTARIO", param, limit);
            ArrayList<Integer> ids = MySql.getIds("COMENTARIO", param, null);
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
            Logger.getLogger(DocumentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
         
        return pag;
    }
}
