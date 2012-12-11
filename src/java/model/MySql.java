/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.*;
import java.util.*;

/**
 *
 * @author María Galbis
 */
public class MySql {
    
    protected static Connection con;
    protected static boolean conectado;
    protected static Statement stmt;
    
    
    /**
     * Abre una conexión a la base de datos indicada
     * 
     * @param login
     * @param pass
     * @param urlOdbc nombre de la base de datos
     * @return true si se realiza la conexión, false en caso contrario
     * @throws Exception 
     */
    public static boolean abrirConexion(String login, String pass, 
            String urlOdbc) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = (java.sql.DriverManager.getConnection(urlOdbc, login, pass));
            conectado = true;
        } catch (ClassNotFoundException | SQLException e) {
            conectado = false;
            throw new Exception("No se ha podido abrir la conexion: "+ e.getMessage());
        } finally {
            return conectado;
        }
    }
 
    /**
     * Cierra la conexío de la base de datos
     * 
     * @throws Exception 
     */
    public static void cerrarConexion() throws Exception {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException ex) {
            throw new Exception("No se ha podido cerrar la conexion: "+ ex.getMessage());
        }
    }

    /**
     * Inicia una transacción
     * 
     * @throws Exception 
     */
    public static void initTrans() throws Exception {
        try {
            if (con != null) {
                con.setAutoCommit(false);
                stmt = con.createStatement();
            } else {
                throw new Exception("No está conectado a la base de datos.");
            }
        } catch (SQLException ex) {
            throw new Exception("No se ha podido iniciar la transacción: "
                    + ex.getMessage());
        }
    }
    
    /**
     * Realiza commit de una transacción
     * 
     * @throws Exception 
     */
    public static void commitTrans() throws Exception {
        try{
            if (con != null && conectado == true) {
                con.commit();
            } else {
                throw new Exception("No está conectado a la base de datos.");
            }
        } catch (SQLException ex) {
            throw new Exception("No se ha podido realizar commit de la "
                    + "transacción: "+ ex.getMessage());
        }
    }
    
    /**
     * Realiza rollback de una transacción
     * 
     * @throws Exception 
     */
    public static void rollbackTrans() throws Exception {
        try{
            if (con != null && conectado == true) {
                con.rollback();
            } else {
                throw new Exception("No está conectado a la base de datos.");
            }
        } catch (SQLException ex) {
            throw new Exception("No se ha podido realizar rollback de la "
                    + "transacción: "+ ex.getMessage());
        }
    }
    
    /**
     * Elimina una fila de una tabla
     * 
     * @param id El id de la fila
     * @param tabla nombre de la tabla
     * @return true si se realiza la transacción, false en caso contrario
     * @throws Exception 
     */
    public static boolean delete(int id, String tabla) {
        boolean resultado = false;
        try {
            if(con != null && conectado == true){
                stmt.executeUpdate("DELETE FROM "+tabla+" WHERE ID = "+id);
                resultado =  true;
            } else {
                throw new Exception("No está conectado a la base de datos.");
            }
        } catch (SQLException ex) {
            resultado =  false;
            throw new Exception("No se ha podido eliminar el registro: "+ ex.getMessage());
        } finally {
            return resultado;
        }
    }
    
    /**
     * Actualiza un campo de una tabla
     * 
     * @param id El id de la fila
     * @param tabla El nombre de la tabla
     * @param valores Map con los nombres de columna y su valor
     * @return True si se realiza la transacción, false en caso contrario
     */
    public static boolean update(int id, String tabla, HashMap valores) {
        String param = valores.toString().replace("{", "").replace("}", "");
        boolean resultado = false;
        try {
            if(con != null && conectado == true) {
                stmt.executeUpdate("UPDATE "+tabla+" SET "+param+" WHERE ID = "+id);
                resultado = true;
            } else {
                throw new Exception("No está conectado a la base de datos.");
            }
        } catch (SQLException ex) {
            resultado = false;
            throw new Exception("No se ha podido actualizar el registro: "+ ex.getMessage());
        } finally {
            return resultado;
        }
    }
    
    /**
     * Actualiza un camop de una tabla
     * 
     * @param id El id de la fila
     * @param tabla El nombre de la tabla
     * @param valores string con los nombres de columna y su valor
     * @return True si se realiza la transacción, false en caso contrario
     */
    public static boolean update(int id, String tabla, String valores) {
        boolean resultado = false;
        try {
            if(con != null && conectado == true) {
                stmt.executeUpdate("UPDATE "+tabla+" SET "+valores+" WHERE ID = "+id);
                resultado = true;
            } else {
                throw new Exception("No está conectado a la base de datos.");
            }
        } catch (SQLException ex) {
            resultado = false;
            throw new Exception("No se ha podido actualizar el registro: "+ ex.getMessage());
        } finally {
            return resultado;
        }
    }
    
    /**
     * Realiza un insert en la tabla indicada
     * 
     * @param tabla El nombre de la tabla
     * @param parametros List con los parámetros del insert
     * @return el id de la fila introducida
     */
    public static int insert(String tabla, String param) {
        int id = 0;
        try{
            if(con != null) {
                stmt.executeUpdate("INSERT INTO "+tabla+" VALUES ("+param+")");
                
                id = getLast(tabla, null);
            } else {
                throw new Exception("No está conectado a la base de datos.");
            }
        } catch (SQLException ex){
            id = 0;
            throw new Exception("No se ha podido insertar el registro: "+ ex.getMessage());
        } finally {
            return id;
        }
        
    }
    
    /**
     * Obtiene el id de la primera fila de la tabla
     * 
     * @param tabla Nombre de la tabla
     * @param condiciones Parámetros de la consulta
     * @return int con el id
     */
    public static int getFirst (String tabla, List condiciones) {
        int resultado = 0;
        try {
            if(con != null && conectado == true){
                String sql = "SELECT * FROM "+tabla+" WHERE 1=1";
            
                if(condiciones != null && !condiciones.isEmpty()){
                    for (int i=0; i<condiciones.size(); i++){
                        sql += " AND "+condiciones.get(i);
                    }
                }
          
                ResultSet resultSet = stmt.executeQuery(sql);
                resultSet.first();
            
                resultado = resultSet.getInt("id");
            } else {
                throw new Exception("No está conectado a la base de datos.");
            }
        } catch (SQLException ex) {
            resultado = 0;
            throw new Exception("No se ha podido realizar la consulta: "+ ex.getMessage());
        } finally {
            return resultado;
        }
    }
    
    /**
     * Obtiene el id de la última fila de la tabla
     * 
     * @param tabla Nombre de la tabla
     * @param condiciones Parámetros de la consulta
     * @return int con el id
     */
    public static int getLast (String tabla, List condiciones) {
        int resultado = 0;
        try {
            if(con != null && conectado == true){
                String sql = "SELECT * FROM "+tabla+" WHERE 1=1";
            
                if(condiciones != null && !condiciones.isEmpty()){
                    for (int i=0; i<condiciones.size(); i++){
                        sql += " AND "+condiciones.get(i);
                    }
                }
          
                ResultSet resultSet = stmt.executeQuery(sql);
                resultSet.last();
            
                resultado = resultSet.getInt("id");
            } else {
                throw new Exception("No está conectado a la base de datos.");
            }
        } catch (SQLException ex) {
            resultado = 0;
            throw new Exception("No se ha podido realizar la consulta: "+ ex.getMessage());
        } finally {
            return resultado;
        }
    }
    
    /**
     * Cuenta los registros de una consulta
     * 
     * @param tabla Nombre de la tabla
     * @param condiciones Parámetros de la consulta
     * @return int con el número de filas
     */
    public static int count(String tabla, String condiciones) {
        int resultado = 0;
        try {
            if(con != null && conectado == true){
                Statement st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            
                String sql = "SELECT * FROM "+tabla+" WHERE 1=1";
            
                if(!condiciones.equalsIgnoreCase("")){
                        sql += " AND "+condiciones;
                }
    
                ResultSet resultSet = st.executeQuery(sql);
                resultSet.first();
            
                do { resultado++;
                } while(resultSet.next());

            } else {
                throw new Exception("No está conectado a la base de datos.");
            }
        } catch (SQLException ex) {
            resultado = 0;
            throw new Exception("No se ha podido realizar la consulta: "+ ex.getMessage());
        } finally {
            return resultado;
        }
    }
    
    /**
     * Obtiene los ids de una consulta
     * 
     * @param tabla nombre de la tabla
     * @param condiciones lista de parametros
     * @return ArrayList con los ids
     */
    public static ArrayList<Integer> getIds(String tabla, String condiciones, String orderBy) {
        ArrayList<Integer> arr = new ArrayList();
        
        try {
            if(con != null && conectado == true){
                Statement st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
                
                String sql = "SELECT * FROM "+tabla+" WHERE 1=1 ";
            
                if(condiciones != null && !condiciones.equalsIgnoreCase("")){
                        sql += " AND "+condiciones;
                }
                
                if(orderBy != null && !orderBy.equalsIgnoreCase("")){
                        sql += " ORDER BY "+orderBy+" DESC ";
                }
      
                ResultSet resultSet = st.executeQuery(sql);
                resultSet.first();

                do{ arr.add(resultSet.getInt("ID"));
                } while(resultSet.next());
                
            } else {
                throw new Exception("No está conectado a la base de datos.");
            }
        } catch (SQLException ex) {
            throw new Exception("No se ha podido realizar la consulta: "+ ex.getMessage());
        } finally {
            return arr;
        }
    }
    
    /**
     * Obtiene el número de páginas de la contulta
     * 
     * @param tabla Nombre de la tabla
     * @param campos Nombre de las columnas 
     * @param condiciones Parámetros de la consulta
     * @param limit Número de registros por página
     * @return int con el número de páginas que sale en total
     */
    public static int getPages(String tabla, String condiciones, int limit) {
        int resultado = 0;
        try {
            if(con != null && conectado == true) {
                Statement st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            
                String sql = "SELECT * FROM "+tabla+" WHERE 1=1";
            
                if(!condiciones.equalsIgnoreCase("")){
                        sql += " AND "+condiciones;
                }
            
                ResultSet resultSet = st.executeQuery(sql);
                resultSet.first();
            
                double reg = 0.0;
            
                do { reg++;
                } while(resultSet.next());

                resultado = ((Number) Math.ceil(reg/((Number) limit).doubleValue())).intValue();
            } else {
                throw new Exception("No está conectado a la base de datos.");
            }
        } catch (SQLException ex) {
            resultado = 0;
            throw new Exception("No se ha podido realizar la consulta: "+ ex.getMessage());
        } finally {
            return resultado;
        }
        
    }
    
    /**
     * Obtiene los registros de la página indicada
     * 
     * @param tabla El nombre de la tabla
     * @param campos Nombre de las columnas
     * @param condiciones Parámetros de la consulta
     * @param pagina Número de página 
     * @param limit Número de registros por página
     * @return Array con los id
     * @throws Exception 
     */
    public static ArrayList getPage(String tabla, String condiciones, 
            int pagina, int limit, String orderBy) {
        ArrayList arr = new ArrayList();
        
        try {
            if(con != null && conectado == true){
                Statement st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            
                String sql = "SELECT * FROM "+tabla+" WHERE 1=1 ";
            
                if(!condiciones.equalsIgnoreCase("")){
                        sql += " AND "+condiciones;
                }
                
                if(!orderBy.equalsIgnoreCase("")){
                        sql += " ORDER BY "+orderBy+" DESC ";
                }
            
                sql += " LIMIT "+pagina+", "+limit;
            
                ResultSet resultSet = st.executeQuery(sql);
                resultSet.first();
                
                do{ 
                    arr.add(resultSet.getInt("id"));
                } while(resultSet.next());
            } else {
                throw new Exception("No está conectado a la base de datos.");
            }
        } catch (SQLException ex) {
            throw new Exception("No se ha podido realizar la consulta: "+ ex.getMessage());
        } finally {
            return arr;
        }
    }
    
    /**
     * Obiene los datos de una fila
     * 
     * @param tabla Nombre de la tabla
     * @param id id de la fina
     * @return HashMap con los datos
     * @throws Exception 
     */
    public static HashMap<String, Object> getRow(String tabla, int id) {
        HashMap<String, Object> fila = new HashMap();

        try {
            if(con != null && conectado == true) {
                Statement st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            
                String sql = "SELECT * FROM "+tabla+" WHERE ID = "+id;
            
                ResultSet resultSet = st.executeQuery(sql);
                resultSet.first();
         
                ArrayList<String> colNames = (ArrayList) getColNames(tabla);
            
                for (int i=0; i<colNames.size(); i++) {
                    fila.put(colNames.get(i), resultSet.getObject(colNames.get(i)));
                }
            } else {
                throw new Exception("No está conectado a la base de datos.");
            }
        } catch (SQLException ex) {
            throw new Exception("No se ha podido realizar la consulta: "+ ex.getMessage());
        } finally {
            return fila;  
        }   
    } 
    
    /**
     * Obtiene un resultSet de una consulta
     * 
     * @param tabla Nombre de la tabla 
     * @param condiciones Parámetros de consulta
     * @return El resultSet
     * @throws Exception 
     */
    public static ResultSet getResultSet(String tabla, String condiciones) throws Exception {
        ResultSet resultSet = null;
        try {
            if(con != null && conectado == true) {
                Statement st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            
                String sql = "SELECT * FROM "+tabla+" WHERE 1=1 ";
            
                if(condiciones != null){
                        sql += " AND "+condiciones;
                }
            
                resultSet = st.executeQuery(sql);
        
            } else {
                throw new Exception("No está conectado a la base de datos.");
            }
        } catch (SQLException ex) {
            throw new Exception("No se ha podido realizar la consulta: "+ ex.getMessage());
        } finally {
            return resultSet;  
        }  
    }

    /**
     * Obtiene los nombres de las columnas de la tabla indicada
     * 
     * @param tabla Nombre de la tabla
     * @return ArrayList con los nombres de las columnas
     * @throws Exception 
     */
    public static ArrayList getColNames(String tabla) {    
        ArrayList<String> colNames = new ArrayList();
        
        try{
            if(con != null && conectado == true){
                DatabaseMetaData metaData = con.getMetaData();
                ResultSet rs = metaData.getColumns(null, null, tabla, null);
            
                while (rs.next()) {
                    colNames.add(rs.getString(4).toUpperCase());
                }
            } else {
                throw new Exception("No está conectado a la base de datos.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException("No se ha podido realizar la consulta: "+ ex.getMessage());
        } finally {
            return colNames;
        }
    }
    
}
