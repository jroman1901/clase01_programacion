package modelo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.management.remote.JMXConnectorFactory.connect;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Jose Peña
 */
public class conexion 
{
    /**
     * Variable de tipo conexion para la base de datos
     */
   public Connection  con;
    
    public conexion()
    {
        
    }
    public void conexion_db() throws ClassNotFoundException
    {
        // con la funcion property obtenemos la ubcación del archivo ejecutable.
        String url= System.getProperty("user.dir");   
        System.out.print(url);
    try
    {     
        // obtenemos acceso ala base de datos con el paquete de clases jdbc
        /**
         * JDBC significa Java Database Conection conjunto de librerias para conectase a base de datos
         * 
         */
        Class.forName("org.sqlite.JDBC");
        /*
        cargamos la clase org.sqlite.jdbc esta linea de codigo es vital para el proceso
        */
        
        /*
        Cargamos la conexion pasando la ruta de la base de datos
        */
        
        con = DriverManager.getConnection("jdbc:sqlite:"+url+"/"+"alumnos.db");       
        
        
        /*
        Validamos si la conexión es nula
        */
        if (con!=null) 
        {
            // solo por temas de control mostramos el estado de la conexión
            
            System.out.println("Conectado");
        }
    }
    // contrl de errores de tipo slq
    catch (SQLException ex) 
        {
            // error de conexión mostrando el mensaje de error
            System.err.println("No se ha podido conectar a la base de datos\n"+ex.getMessage());
             JOptionPane.showMessageDialog(null,"No se ha podido conectar a la base de datos"+ex.getMessage());
        }
}
    /*
    Metodo publico para cerrar la base de datos
    */
 public void close(){
     /*
     lo cargamos en un try por temas de seguridad del cierre de la conexión
     */   
     try {
            con.close();
        } catch (SQLException ex) 
        {
            // trazas de aplicaciones y con la clase de excepción y el nivel de excepción.
            
            Logger.getLogger(conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
 public void eliminarAlumno(int id)
 {
     try
     {
         if(con!=null)
         {
                         PreparedStatement st = con.prepareStatement("delete  from alumnos where id="+id+"");
                         st.execute();
                         
         }              
         else
         {
               System.out.println("Error no es posible realizar la operacion");
                 JOptionPane.showMessageDialog(null,"Error no es posible realizar la operacion");
             
         }
         
     }
     catch(Exception ex)
     {
         System.out.println("Error al realizar la actualización por"+ex.getMessage());
          JOptionPane.showMessageDialog(null,"Error al realizar la actualización por"+ex.getMessage());
     }
 }
 
 public void editarAlumno(Alumno alumno,int id)
 {
     try
     {
         if(con!=null)
         {
            PreparedStatement st = con.prepareStatement("update alumnos set carnet='"+alumno.getCarnet()+"',nombre='"+alumno.getNombre()+"',curso='"+alumno.getCurso()+"' where id="+id+"");
            st.execute();
            
             
         }
         else
         {
             System.out.println("Error no es posible realizar la operacion");
             
             
         }
     }
     catch(Exception ex)
     {
           JOptionPane.showMessageDialog(null,"Error al realizar la actualización por"+ex.getMessage());
     }
     
 }
 // metodo para guardar alumno
 public void guardarAlumno(Alumno alumno)
 {
        try
        {
          if (con!=null) 
            {
            PreparedStatement st = con.prepareStatement("insert into alumnos (carnet,nombre,curso) values (?,?,?)");
            st.setString(1,alumno.getCarnet());
            st.setString(2, alumno.getNombre());     
            st.setString(3, alumno.getCurso());    
            // apasamos parametros al a base de datos
            st.execute();
            // ejecutamos la instrucción
            }
            else
            {   // mostramos que no esta conectamos nosotro decidimos que hacer con este apartado
                System.out.println("no esta conectado");
            
            }
           
        } catch (SQLException ex) 
        {
            // en caso de error mostramos el mensaje de error.
              JOptionPane.showMessageDialog(null,"Error al realizar la actualización por"+ex.getMessage());
        }
 
    }
   
  public void listarAlumnos(DefaultTableModel tableModel) throws SQLException
  {
      // declaramos las variables para la carga de datos
        ResultSet resultado = null;
        //Declaramos un tablemodel para la carga de datos en una tabla en memoria
        tableModel.setRowCount(0);
        // valor count 0
        tableModel.setColumnCount(0);
        // valor count tanto a nivel de fila como de columna
        // preparamos la consulta a la base de datos y lo hacemos a traves de la variable
        // global con en el metodo prepareStatement ( y la consulta empieza con el comando select 
       // el dato * significa que son todos los datos y el siguiente comando el la tabla.
        
        PreparedStatement st = con.prepareStatement("select * from alumnos");
        // siempre controlamos los errores
        try {
            // cargamos el resultado a la base de daots
            resultado = st.executeQuery();
            // si el resultao es nullo
            if(resultado != null)
            {
                // definirmos el numero de columnas que tiene tiene la data que se carga en resultado
                int numeroColumna = resultado.getMetaData().getColumnCount();
                // hacemos un ciclo for para cargar el nombre de las columnas en el jTable
                // utilizamos un ciclo for para agregar el nombre que aparece en la tabla de datos 
                for(int j = 1;j <= numeroColumna;j++)
                {
                    // cargamos el resultado de la consulta
                    tableModel.addColumn(resultado.getMetaData().getColumnName(j));
                }
                // este while sirve para cargar los datos de las filas es decir los registros
                while(resultado.next())
                {
                    // cargamos los datos a un vector de tipo string
                    String []datos = new String[numeroColumna];
                    // rellenamos los datos a traves de un ciclo for
                    for(int i = 1;i <= numeroColumna;i++)
                    {
                           // asignamos la información al vector
                        datos[i-1] = (String) resultado.getString(i);
                    }
                    tableModel.addRow(datos);
                }
            }
        }catch(SQLException e)
        {
              JOptionPane.showMessageDialog(null,"Error al realizar la actualización por"+e.getMessage());
        }

    finally  // para operaciones de finalización si se ejecutan o no.
     {
         try
         {
             /*
             st.Close cierra la conexión a la consulta
             con.Close cerramos la conexión a la base de datos
             Cerramos la conexión a la base de datos
             */
             st.close();
             con.close();
             
             if(resultado != null)
             {
                resultado.close();
             }
         }
         catch (Exception e)
         {
               JOptionPane.showMessageDialog(null,"Error al realizar la actualización por"+e.getMessage());
         }
     }
 
 
  }
  
 
    
}
