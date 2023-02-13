/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import vista.interfaz;
import modelo.conexion;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author jose
 */
public class Clase_002_programacion {

    /**
     * @param args the command line arguments
     */
    
    // metodo inicial del programa.
    public static void main(String[] args) throws ClassNotFoundException 
    {
        // TODO code application logic here       
      
        
        conexion c = new conexion();
        c.conexion_db();
        
       
        // creamos una instancia de la jframe interfaz
       interfaz interfaz = new interfaz();
       // defimos el tamaño a visualizar en pantalla
       interfaz.setSize(800, 500);
       // lo hacemos visible
       interfaz.setVisible(true);
      
       
       
       
       
                 
        
    }
    
}
