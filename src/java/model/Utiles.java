/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import beans.Documento;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TreeMap;
import net.ausiasmarch.common.Convert;

/**
 * Métodos estáticos que puedan servir de utilidad de forma general
 * 
 * @author María Galbis
 */
public class Utiles {
    
    /**
     * Crea una conexión a la base de datos indicada
     */
    public static void conect(){
        MySql.abrirConexion("root", "myadmin", "jdbc:mysql://localhost:3307/moodle");
    }
    
    /**
     * Transforma el formato de una fecha para hacer un insert/update en MySQL
     * @param fecha 
     * @return String con el nuevo formato
     */
    public static String dateToMySQLDate(Date fecha, boolean hora) {
        
        SimpleDateFormat sdf = hora? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                : new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(fecha);
    }
    
    /**
     * Transforma el formato de la fecha para mostrarlo como nosotros indiquemos
     * @param fecha
     * @param time
     * @return 
     */
    public static String dateView(Date fecha, boolean time) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(fecha);
        int dia = calendar.get(Calendar.DAY_OF_MONTH); //dia del mes 
        String mes; //mes, de 0 a 11 
        int anyo = calendar.get(Calendar.YEAR); //año 

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");
        
        switch(calendar.get(Calendar.MONTH)){
            case 0: mes = "Ene"; break;
            case 1: mes = "Feb"; break;
            case 2: mes = "Mar"; break;
            case 3: mes = "Abr"; break;
            case 4: mes = "May"; break;
            case 5: mes = "Jun"; break;
            case 6: mes = "Jul"; break;
            case 7: mes = "Ago"; break;
            case 8: mes = "Sep"; break;
            case 9: mes = "Oct"; break;
            case 10: mes = "Nov"; break;
            case 11: mes = "Dic"; break;
            default: mes = "";
        }
        
        if(time){
            return dia+" "+mes+" "+anyo+"&nbsp; "+sdf.format(fecha);
        } else {
            return dia+" "+mes+" "+anyo;
        }
    }
    
    public static ArrayList getYears(ArrayList<Documento> arr){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        
        ArrayList fechas = new ArrayList();
        for(Documento d : arr){
            fechas.add(sdf.format(d.getFecha()));
        }
        
        ArrayList aux = new ArrayList();
        for(int i=0;i<fechas.size();i++){
            if(!aux.contains(fechas.get(i))){
                aux.add(fechas.get(i));
            }
        }
        return aux;
    }
    
    public static TreeMap getMonths(ArrayList<Documento> arr){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        
        ArrayList fechas = new ArrayList();
        for(Documento d : arr){
            fechas.add(sdf.format(d.getFecha()));
        }
        
        HashMap aux = new HashMap();
        //ArrayList ano = new ArrayList();
        for(int i=0;i<fechas.size();i++){
            if(!aux.containsKey(fechas.get(i))){
                aux.put(fechas.get(i), ((String) fechas.get(i)).substring(5,7));
            }
        }
        return new TreeMap(aux);
    }
    
    public static String mes(String mes){
        if(Convert.isValidInt(mes)){
        switch(mes){
            case "01": mes = "Enero"; break;
            case "02": mes = "Febrero"; break;
            case "03": mes = "Marzo"; break;
            case "04": mes = "Abril"; break;
            case "05": mes = "Mayo"; break;
            case "06": mes = "Junio"; break;
            case "07": mes = "Julio"; break;
            case "08": mes = "Agosto"; break;
            case "09": mes = "Septiembre"; break;
            case "10": mes = "Octubre"; break;
            case "11": mes = "Noviembre"; break;
            case "12": mes = "Diciembre"; break;
            default: mes = "";
        }
        } else {
            switch(mes){
            case "Enero": mes = "01"; break;
            case "Febrero": mes = "02"; break;
            case "Marzo": mes = "03"; break;
            case "Abril": mes = "04"; break;
            case "Mayo": mes = "05"; break;
            case "Junio": mes = "06"; break;
            case "Julio": mes = "07"; break;
            case "Agosto": mes = "08"; break;
            case "Septiembre": mes = "09"; break;
            case "Octubre": mes = "10"; break;
            case "Noviembre": mes = "11"; break;
            case "Diciembre": mes = "12"; break;
            default: mes = "";
        }
        }
        return mes;
    }
    
    /**
     * Comprueba si un usuario existe dentro de la base de datos
     * 
     * @param user 
     * @return true o false
     * @throws Exception 
     */
    public static boolean userExists(String user) throws Exception {
        conect();
        ResultSet rs = MySql.getResultSet("usuario", null);
        rs.first();
        
        boolean exists = false;
        
        do {
            if(rs.getString("login").equalsIgnoreCase(user)){
                    exists = true;
                    break;
            }
        } while(rs.next());
            
        MySql.cerrarConexion();
 
        return exists;
    }
    
    /**
     * Comprueba si la contraseña corresponde al usuario pasado
     * @param user
     * @param pass
     * @return el id del usuario si lo encuentra, 0 si no
     * @throws Exception 
     */
    public static int passValidate(String user, String pass) throws Exception {
        conect();
        ResultSet rs = MySql.getResultSet("usuario", "login = '"+user+"'");
        rs.first();
        
        int id = 0;

        if(rs.getString("password").equals(pass)){
              id = rs.getInt("id");
        }
        
        MySql.cerrarConexion();
 
        return id;
    }
    
}
