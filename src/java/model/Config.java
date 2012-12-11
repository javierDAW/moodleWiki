/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.*;
import java.util.logging.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * Control de configuración de paginación
 * @author María Galbis
 */
public class Config {

    private static Document document;

    /**
     * Obtener el valor del nodo indicado, en este caso puede ser el nodo
     * "documents" o el nodo "comments"
     * 
     * @param id El id del usuario
     * @param path La ruta donde se encuentra el archivo xml
     * @param node El nodo del que queremos tomar su valor
     * @return valor del nodo buscado
     */
    public static int getInitPages(int id, String path, String node) {
        int pages = 0;

        Element rootElement = getDocument(path).getDocumentElement();
        NodeList elements = rootElement.getElementsByTagName("user");

        id = userExists(id) ? id : 0;
        
        for (int i = 0; i < elements.getLength(); i++) {
            Element element = (Element) elements.item(i);

            if (Integer.parseInt(element.getAttribute("id")) == id) {

                for (int j = 0; j < element.getChildNodes().getLength(); j++) {
                    Node element2 = (Node) element.getChildNodes().item(j);

                    if (element2.getNodeType() == 1 && element2.getNodeName().equals("pagination")) {

                        for (int k = 0; k < element2.getChildNodes().getLength(); k++) {
                            Node element3 = (Node) element2.getChildNodes().item(k);

                            if (element3.getNodeName().equals(node)) {
                                pages = Integer.parseInt(element3.getTextContent());
                                break;
                            }
                        }
                    }
                }
            }
        }

        return pages;
    }

    /**
     * Establecer el valor del nodo indicado, "documents" o "comments"
     * 
     * @param id El id del usuario
     * @param pages El número de páginas que queremos establecer
     * @param path La ruta donde se encuentra el archivo xml
     * @param node El nodo que queremos modificar
     */
    public static void setInitPages(int id, int pages, String path, String node) {

        Document doc = getDocument(path);     //recuperamos el documento xml 
        Element rootElement = doc.getDocumentElement();  //recuperamos el elemento raiz
        
        //listamos los nodos "user"
        NodeList elements = rootElement.getElementsByTagName("user"); 

        if (userExists(id)) { //en el caso de que exista el usuario
            for (int i = 0; i < elements.getLength(); i++) {  //los recorremos
                Element element = (Element) elements.item(i);

                //si coinciden los ids
                if (Integer.parseInt(element.getAttribute("id")) != 0
                        && Integer.parseInt(element.getAttribute("id")) == id) {  

                    for (int j = 0; j < element.getChildNodes().getLength(); j++) {
                        Node element2 = (Node) element.getChildNodes().item(j);

                        //cogemos y recorremos el nodo "pagination"
                        if (element2.getNodeType() == 1 
                                && element2.getNodeName().equals("pagination")) {
                            for (int k = 0; k < element2.getChildNodes().getLength(); k++) {
                                Node element3 = (Node) element2.getChildNodes().item(k);

                                //si encuentra el nodo buscado
                                if (element3.getNodeName().equals(node)) {   
                                    //le asignamos el nuevo contenido
                                    element3.setTextContent(String.valueOf(pages));  
                                    break;
                                } else { //si no lo encuentra
                                    //lo creamos
                                    Element newElement = doc.createElement(node);
                                    element2.appendChild(newElement);
                                    //y le asignamos el nuevo contenido
                                    newElement.setTextContent(String.valueOf(pages));  
                                }
                            }
                        }
                    }
                }
            }
        } else {  // si no existe ese usuario
            Element user = doc.createElement("user");  //creamos un elemento "user"
            rootElement.appendChild(user);  //lo colocamos como hijo del elemento raíz
            user.setAttribute("id", String.valueOf(id));  //le asignamos el atributo id

            //creamos un elemento "pagination"
            Element pagination = doc.createElement("pagination");  
            user.appendChild(pagination);  //lo colocamos como hijo del elemento "user"

            //creamos un elemento "documents/comments"
            Element newElement = doc.createElement(node);  
            //lo colocamos como hijo del elemento "pagination"
            pagination.appendChild(newElement);   
            //y le asignamos el nuevo contenido
            newElement.setTextContent(String.valueOf(pages));  
        }



        try {
            //para reescribir el archivo config.xml
            //setting up a transformer
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();

            //generating string from xml tree
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);
            String xmlString = sw.toString();

            //Saving the XML content to File
            OutputStream f0;
            byte buf[] = xmlString.getBytes();

            f0 = new FileOutputStream(path);

            for (int i = 0; i < buf.length; i++) {
                try {
                    f0.write(buf[i]);
                } catch (IOException ex) {
                    Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            f0.close();
            buf = null;
        } catch (IOException | TransformerException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Comprueba si el nodo "user" con el id indicado existe dentro del archivo
     * @param id El id del usuario
     * @return true o false dependiendo si lo encuentra o no
     */
    private static boolean userExists(int id) {
        boolean exists = false;

        Element rootElement = document.getDocumentElement();  //recuperamos el elemento raiz

        //listamos los nodos "user"
        NodeList elements = rootElement.getElementsByTagName("user"); 

        for (int i = 0; i < elements.getLength(); i++) {  //los recorremos
            Element element = (Element) elements.item(i);

            if (Integer.parseInt(element.getAttribute("id")) == id) {
                exists = true;
                break;
            }
        }
        
        return exists;
    }

    /**
     * Prepara el documento para leerlo o escribirlo
     * 
     * @param path La ruta del archivo xml
     * @return el documento
     */
    private static Document getDocument(String path) {

        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(fileInputStream);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println("No se encuentra el archivo " + path);
        }
        return document;
    }
}
