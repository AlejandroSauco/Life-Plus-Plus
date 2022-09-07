package application;

/*
 * Conector JDBC para MariaDB
 * https://mariadb.com/kb/en/about-mariadb-connector-j/
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Mariadb {

    //  Database credentials
    static final String USER = "pri_lifepp";
    static final String PASS = "Life++";

    public static void main(String[] args) {
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

            //STEP 3: Execute a query
            System.out.println("Creando la tabla si no existe...");
            sql = "CREATE TABLE IF NOT EXISTS REGISTRATION "
                    + "(id INTEGER not NULL AUTO_INCREMENT, "
                    + "first VARCHAR(255), "
                    + "last VARCHAR(255), "
                    + "age INTEGER, "
                    + "PRIMARY KEY ( id ))";
            System.out.println("sql Create Table: "+sql);
            stmt = conn.createStatement(); //crear setntencia
            stmt.executeUpdate(sql);//ejecutarla
            stmt.close();//cerrarla
            
            //STEP 4: Insertando un valor.
            String miNombre = "Alvarret";
            String miApellido = "Prueba";
            int miedad = 20;
            sql = "Insert into REGISTRATION(first, last, age) values('"+miNombre+"','"+miApellido+"','"+miedad+"');"; //el first es del valor de la tabla first name"
            System.out.println("sql Insert: "+sql);
            stmt = conn.createStatement(); 
            stmt.executeUpdate(sql);  
            stmt.close(); 
            
            //STEP 5: Realizando una consulta
            sql = "SELECT * FROM REGISTRATION";
            System.out.println("sql Select: "+sql);
            stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery( sql );
			while ( rs.next() ) {
				int id = rs.getInt("id");
				String  nombre = rs.getString("first");
				String  apellido = rs.getString("last");
				int edad = rs.getInt("age");
				System.out.print( "ID = " + id );
				System.out.print( ", nombre = " + nombre );
				System.out.print( ", apellido = " + apellido );
				System.out.println( ", edad = " + edad );
			}
			rs.close();
			stmt.close();
			
			//STEP 6: Cerrando conexion.
			conn.close();

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
        
        System.out.println("Fin Codigo!");
	}
}//end JDBCExample