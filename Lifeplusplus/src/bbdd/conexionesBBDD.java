package bbdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.Medico;

public class conexionesBBDD {

	//  Database credentials
    static final String USER = "pri_lifepp";
    static final String PASS = "Life++";
	
	public static void crearPacientes(String dni, String password, String nombre, String correo, String fechanacim, int sexo, int tipo){
		Connection conn = null;
        Statement stmt = null;
        String sql;

        try {
            //STEP 1: Register JDBC driver
        	Class.forName("org.mariadb.jdbc.Driver");

            //STEP 2: Open a connection
            System.out.println("Connecting to a selected database...");

            conn = DriverManager.getConnection(
                    "jdbc:mariadb://195.235.211.197/prilifepp", USER, PASS);
            System.out.println("Connectado a la Base de Datos...");
            
            
            
            String miIDmedico = null;                                             
            sql = "SELECT MEDICOS.DNI FROM MEDICOS JOIN PACIENTES ON MEDICOS.DNI = PACIENTES.IDmedico GROUP BY MEDICOS.DNI ORDER BY COUNT(*) LIMIT 1";    
            System.out.println("sql command: "+ sql);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery( sql );
            if ( rs.next() ) {
            	miIDmedico = rs.getString("DNI");
                System.out.print( "DNI = " + miIDmedico );
            }
            
            
            
            String miDni = dni;
            String miPassword = password;
            String miNombre = nombre;
            String miCorreo = correo;
            String miFechaNacim = fechanacim;
            int miTipo = tipo;
            
            sql = "Insert into LOGIN(DNI, PASS, TIPO) values('"+miDni+"','"+miPassword+"','"+miTipo+"');"; //el first es del valor de la tabla first name"
            System.out.println("sql Insert: "+sql);
            stmt = conn.createStatement(); 
            stmt.executeUpdate(sql);  
            stmt.close();
            
            sql = "Insert into PACIENTES(Dni, Pass, Nombre, Correo, FechaNacim, Sexo, Tipo, IDmedico) values('"+miDni+"','"+miPassword+"','"+miNombre+"','"+miCorreo+"','"+miFechaNacim+"','"+sexo+"','"+miTipo+"','"+miIDmedico+"');"; //el first es del valor de la tabla first name"
            System.out.println("sql Insert: "+sql);
            stmt = conn.createStatement(); 
            stmt.executeUpdate(sql);  
            stmt.close();
		} catch (SQLException se) {
	        //Handle errors for JDBC
	        se.printStackTrace();
	    } catch (Exception e) {
	        //Handle errors for Class.forName
	        e.printStackTrace();
	    } finally {
	        //finally block used to close resources
	        try {
	            if (stmt != null) {
	                conn.close();
	            }
	        } catch (SQLException se) {
	        }// do nothing
	        try {
	            if (conn != null) {
	                conn.close();
	            }
	        } catch (SQLException se) {
	            se.printStackTrace();
	        }//end finally try
	    }//end try
	}
	
	public static void crearMedicos(String dni, String password, String nombre, String correo, String fechaNacim, String rol, String IDinstitucion, String fechaRegistro){
		Connection conn = null;
        Statement stmt = null;
        String sql;
        try {
        	Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mariadb://195.235.211.197/prilifepp", USER, PASS);
            System.out.println("Connectado a la Base de Datos...");
            int tipo = 2;
            
            sql = "Insert into LOGIN(DNI, PASS, TIPO) values('"+dni+"','"+password+"','"+tipo+"');"; //el first es del valor de la tabla first name"
            System.out.println("sql Insert: "+sql);
            stmt = conn.createStatement(); 
            stmt.executeUpdate(sql);  
            stmt.close();
            
            int verificacion = 0;
            sql = "Insert into MEDICOS(Dni, pass, Nombre, Correo, FechaNacim, Rol, IDinstitucion, Tipo, FechaRegistro, Verificacion) values('"+dni+"','"+password+"','"+nombre+"','"+correo+"','"+fechaNacim+"','"+rol+"','"+IDinstitucion+"','"+tipo+"','"+fechaRegistro+"','"+verificacion+"');";
            System.out.println("sql Insert: "+sql);
            stmt = conn.createStatement(); 
            stmt.executeUpdate(sql);  
            stmt.close();
		} catch (SQLException se) {
	        //Handle errors for JDBC
	        se.printStackTrace();
	    } catch (Exception e) {
	        //Handle errors for Class.forName
	        e.printStackTrace();
	    } finally {
	        //finally block used to close resources
	        try {
	            if (stmt != null) {
	                conn.close();
	            }
	        } catch (SQLException se) {
	        }// do nothing
	        try {
	            if (conn != null) {
	                conn.close();
	            }
	        } catch (SQLException se) {
	            se.printStackTrace();
	        }//end finally try
	    }//end try
	}
	
	public static Integer check_login(String dni, String contrasenia) {
         Connection conn = null;
         Statement stmt = null;
         String sql;
            try {
                //STEP 1: Register JDBC driver
                Class.forName("org.mariadb.jdbc.Driver");
                //STEP 2: Open a connection
                try {
                    conn = DriverManager.getConnection(
                            "jdbc:mariadb://195.235.211.197/prilifepp", USER, PASS);
                    System.out.println("Connectado a la Base de Datos...");
                     
                  //STEP 3: Realizando una consulta
                    sql = "SELECT * FROM LOGIN WHERE LOGIN.DNI = \""+ dni + "\" AND LOGIN.PASS = \"" +contrasenia+"\"";
          
                    System.out.println("sql command: "+ sql);
                    stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery( sql );
                   // System.out.println(rs);
                    String dni_persona;
                    int Tipo = 0;
  
                    if ( rs.next() ) {
                    	dni_persona = rs.getString("DNI");
                    	Tipo = rs.getInt("TIPO");  
                    	System.out.println(Tipo);
                        System.out.print( "DNI = " + dni_persona );
                        System.out.print( ", tipo = " + Tipo );                
                        System.out.println();

                    }       
                    rs.close();
                    stmt.close();
                    System.out.println(Tipo);
                    return Tipo;
                }catch(Exception e){
                	e.printStackTrace();
                    System.out.println("Se ha producido un error ");
                }    
                //STEP 6: Cerrando conexion.
                conn.close();
            } catch (SQLException se) { /*Handle errors for JDBC*/   se.printStackTrace();
            } catch (Exception e) {  /*Handle errors for Class.forName*/  e.printStackTrace();
            } finally {  /*finally block used to close resources*/
                try {
                    if (stmt != null) {conn.close();}
                } catch (SQLException se) { }// do nothing
                try {
                    if (conn != null) {conn.close();}
                } catch (SQLException se) { se.printStackTrace();
                }//end finally try
            }//end try            
            System.out.println("Fin Codigo!");
            return null;
            
    }
	
	
	/***
	 * 
	 */	
	
	public static Connection conectar()  {
        Connection conexion = null;
        try {

            conexion = DriverManager.getConnection(
                    "jdbc:mariadb://195.235.211.197/prilifepp", USER, PASS);

        }catch (SQLException e) {
            System.out.println("Ocurrio un error al conectar con la base de datos"+ e.getMessage());
        }
        return conexion;
    }

}
