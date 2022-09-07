package controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;
import java.util.Date;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import application.Developer;
import application.Medico;
import application.Paciente;
import bbdd.conexionesBBDD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/*
 * CONTROLADOR PANTALLA EDITAR USUARIO
 */

public class EditarUsuarioControlador implements Initializable {
	@FXML
	private JFXTextField txtNombre, txtDni, txtCorreo, txtInstitucion, txtIdentificacion, txtRol;
	@FXML
	private JFXButton btnFecha, btnInstitucion, btnIdentificacion, btnRol, btnRegresar, btnDarBaja, btnAceptarSolicitud, btnRechazar;
	@FXML
	private Label tituloFecha, tituloDni, tituloInstitucion, tituloIdentificacion, tituloRol, feedback;
	@FXML
	private DatePicker cuadroFecha;
	@FXML
	private JFXPasswordField fieldContrasena;

	String dni_;
	String dniRegreso_;
	public EditarUsuarioControlador(String dni_) {
		this.dni_ = dni_;
	}
	
	public EditarUsuarioControlador(String dni_, String dniRegreso_) {
		this.dni_ = dni_;
		this.dniRegreso_ = dniRegreso_;
	}
	
	/*
	 * Variables para instanciar objetos especificos
	 */
	private Medico medicoRegreso = null;
	private Paciente pacienteRegreso = null;
	private Medico medicoElegido = null;
	private Paciente pacienteElegido = null;
	private Developer devElegido = null;

	int posElegido = 0;
	String txtCheck;
	
	//COPIAR ESTO PARA QUE FUNCIONE LA SQL
	String sql;
	Connection conexion = null;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;

	/*
	 * Metodo para obtener una fecha en formato local
	 * 
	 * @param oldDate la fecha en formato Date
	 * 
	 * @return LocalDate la fecha en formato LocalDate
	 */
	public LocalDate DateToLocal(Date oldDate) {
		SimpleDateFormat formatear1 = new SimpleDateFormat("MM-dd-yyy");
		String fecha = formatear1.format(oldDate);
		String[] nums = fecha.split("-");
		LocalDate nuevaLocal = LocalDate.of(Integer.parseInt(nums[2]), Integer.parseInt(nums[0]),
				Integer.parseInt(nums[1]));
		return nuevaLocal;
	}

	/*
	 * Metodo para obtener una fecha en formato Date
	 * 
	 * @param oldLocal la fecha en formato LocalDate
	 * 
	 * @return Date la fecha en formato Date
	 */
	public Date LocalToDate(LocalDate oldLocal) {
		Date nuevaDate = Date.from(oldLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return nuevaDate;
	}

	/*
	 * Metodo para borrar una instancia especifica y persistirlo en el fichero
	 * 
	 * @exception IOException
	 */
	public void DarBaja() throws IOException {
		int tipopersona = 0;
		conexion = conexionesBBDD.conectar();
		try {
            preparedStatement = conexion.prepareStatement("SELECT LOGIN.Tipo FROM LOGIN WHERE DNI = ?");
            
            preparedStatement.setString(1, this.dni_);
            
            resultSet = preparedStatement.executeQuery();
                        
            if (resultSet.next()) {
            	tipopersona = resultSet.getInt("Tipo");
            }else {
            	System.out.println("ERROR");
            }
            
            conexion.close();

        } catch (Exception e2) {
        	System.out.println(e2);
        }
		
		if (tipopersona==1) { //PACIENTE
			conexion = conexionesBBDD.conectar();
			try {
	            preparedStatement = conexion.prepareStatement("DELETE FROM PACIENTES WHERE DNI = \'"+dni_+"\'");
	            
	            preparedStatement.setString(1, this.dni_);
	            
	            resultSet = preparedStatement.executeQuery();
	                        
	            if (resultSet.next()) {
	            	System.out.println("ERROR!");
	            }else {
	            	System.out.println("Eliminado Correctamente!");
	            }
	            
	            conexion.close();

	        } catch (Exception e2) {
	        	System.out.println(e2);
	        }
			
			conexion = conexionesBBDD.conectar();
			try {
	            preparedStatement = conexion.prepareStatement("DELETE FROM LOGIN WHERE DNI = \'"+dni_+"\'");
	            
	            preparedStatement.setString(1, this.dni_);
	            
	            resultSet = preparedStatement.executeQuery();
	                        
	            if (resultSet.next()) {
	            	System.out.println("Error!");
	            }else {
	            	System.out.println("Eliminado Correctamente!");
	            }
	            
	            conexion.close();

	        } catch (Exception e2) {
	        	System.out.println(e2);
	        }
			
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Login.fxml"));
				LoginControlador controlLogin = new LoginControlador();
				loader.setController(controlLogin);
				Parent rootLogin = loader.load();
				Stage stage = new Stage();
				stage.getIcons().add(new Image("/Img/lifeplusplus.png"));
				stage.setTitle("Life++");
				stage.setScene(new Scene(rootLogin));
				stage.setMinHeight(600);
				stage.setMinWidth(600);
				Stage s_login = (Stage) btnRegresar.getScene().getWindow();
				stage.setHeight(s_login.getHeight());
				stage.setWidth(s_login.getWidth());
				stage.setX(s_login.getX());
				stage.setY(s_login.getY());
				stage.show();
				s_login.hide();

			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} else { //DEV
		
	}
	}

	/*
	 * Metodo para aceptar una solicitud y persistirla en el fichero
	 * 
	 * @exception IOException
	 */
	public void AceptarSolicitud(ActionEvent event) throws IOException {
		/*sm.getSolicitudes().remove(posElegido);
		sm.saveSolicitudes();

		Medico nuevoMedico = medicoElegido;
		nuevoMedico.setNombre(txtNombre.getText());
		nuevoMedico.setDni(txtDni.getText());
		nuevoMedico.setCorreo(txtCorreo.getText());
		nuevoMedico.setInstitucion(txtInstitucion.getText());
		nuevoMedico.setIdentificacionInstitucion(Integer.parseInt(txtIdentificacion.getText()));
		nuevoMedico.setRol(txtRol.getText());
		nuevoMedico.setPassword(fieldContrasena.getText());
		nuevoMedico.setNacimiento(LocalToDate(cuadroFecha.getValue()));

		sm.getMedicos().add(nuevoMedico);
		sm.saveMedicos();*/

		Regresar(new ActionEvent());
	}

	/*
	 * Metodo para rechazar una solicitud y borrarlo del fichero
	 * 
	 * @exception IOException
	 */
	public void RechazarSolicitud(ActionEvent event) throws IOException {
		/*txtCheck = btnRechazar.getText();

		if (txtCheck.equals("Rechazar")) {
			btnRechazar.setText("Confirmar rechazar");
		} else {
			sm.getSolicitudes().remove(posElegido);
			sm.saveSolicitudes();

			Regresar(new ActionEvent());
		}*/
	}

	/*
	 * Metodo para regresar a la pantalla anterior
	 * 
	 * @exception IOException
	 */
	public void Regresar(ActionEvent event) throws IOException {
		int tipopersona = 0;
		conexion = conexionesBBDD.conectar();
		try {
            preparedStatement = conexion.prepareStatement("SELECT LOGIN.Tipo FROM LOGIN WHERE DNI = ?");
            
            preparedStatement.setString(1, this.dni_);
            
            resultSet = preparedStatement.executeQuery();
                        
            if (resultSet.next()) {
            	tipopersona = resultSet.getInt("Tipo");
            }else {
            	System.out.println("ERROR");
            }
            
            conexion.close();

        } catch (Exception e2) {
        	System.out.println(e2);
        }
		
		if(dniRegreso_ == null) {	
			if (tipopersona==1) { //PACIENTE
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/PpalPaciente.fxml"));
					PpalPacienteControlador ppalPacienteControlador = new PpalPacienteControlador(dni_);
					//ppalPacienteControlador.setYo(sp.getPaciente(pacienteRegreso.getDni(), pacienteRegreso.getPassword()));
					loader.setController(ppalPacienteControlador);
					Parent rootLogin = loader.load();
					Stage stage = new Stage();
					stage.getIcons().add(new Image("/Img/lifeplusplus.png"));
					stage.setTitle("Life++");
					stage.setScene(new Scene(rootLogin));
					stage.setMinHeight(600);
					stage.setMinWidth(600);
					Stage s_login = (Stage) btnRol.getScene().getWindow();
					//stage.setHeight(s_login.getHeight());
					//stage.setWidth(s_login.getWidth());
					stage.setX(s_login.getX());
					stage.setY(s_login.getY());
					stage.show();
					s_login.hide();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else { //MEDICO
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/PpalMedico.fxml"));
					PpalMedicoControlador ppalMedicoControlador = new PpalMedicoControlador(dni_);
					loader.setController(ppalMedicoControlador);
					Parent rootLogin = loader.load();
					Stage stage = new Stage();
					stage.getIcons().add(new Image("/Img/lifeplusplus.png"));
					stage.setTitle("Life++");
					stage.setScene(new Scene(rootLogin));
					stage.setMinHeight(600);
					stage.setMinWidth(600);
					Stage s_login = (Stage) btnRol.getScene().getWindow();
					stage.setHeight(s_login.getHeight());
					stage.setWidth(s_login.getWidth());
					stage.setX(s_login.getX());
					stage.setY(s_login.getY());
					stage.show();
					s_login.hide();
	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else { //DEV
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/PpalDeveloper.fxml"));
				PpalDeveloperControlador ppalDeveloperControlador = new PpalDeveloperControlador(dniRegreso_);
				loader.setController(ppalDeveloperControlador);
				Parent rootLogin = loader.load();
				Stage stage = new Stage();
				stage.getIcons().add(new Image("/Img/lifeplusplus.png"));
				stage.setTitle("Life++");
				stage.setScene(new Scene(rootLogin));
				stage.setMinHeight(600);
				stage.setMinWidth(600);
				Stage s_login = (Stage) btnRol.getScene().getWindow();
				stage.setHeight(s_login.getHeight());
				stage.setWidth(s_login.getWidth());
				stage.setX(s_login.getX());
				stage.setY(s_login.getY());
				stage.show();
				s_login.hide();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * Metodo para seleccionar el medico que se edita
	 * 
	 * @param elegido Medico, el medico que se va a editar
	 */
	public void setMedicoEditable(String dni_) {
		tituloInstitucion.setVisible(false);
		conexion = conexionesBBDD.conectar();
		try {
            preparedStatement = conexion.prepareStatement("SELECT MEDICOS.Nombre, MEDICOS.Correo, MEDICOS.DNI, MEDICOS.FechaNacim, MEDICOS.pass, MEDICOS.Rol, MEDICOS.IDinstitucion FROM MEDICOS WHERE DNI = ?");
            
            preparedStatement.setString(1, this.dni_);
            
            resultSet = preparedStatement.executeQuery();
                        
            if (resultSet.next()) {
            	txtNombre.setText(resultSet.getString("Nombre"));
            	txtCorreo.setText(resultSet.getString("Correo"));
            	txtDni.setText(resultSet.getString("DNI"));
            	fieldContrasena.setText(resultSet.getString("pass"));
            	txtIdentificacion.setText(resultSet.getString("IDinstitucion"));
        		txtRol.setText(resultSet.getString("Rol"));
        		cuadroFecha.setPromptText(resultSet.getString("FechaNacim"));
            }else {
            	System.out.println("ERROR");
            }
            
            conexion.close();

        } catch (Exception e2) {
        	System.out.println(e2);
        }
	}

	/*
	 * Metodo para seleccionar el paciente que se edita
	 * 
	 * @param elegido Paciente, el paciente que se va a editar
	 */
	public void setPacienteEditable(String dni_) {	
		conexion = conexionesBBDD.conectar();
		try {
            preparedStatement = conexion.prepareStatement("SELECT PACIENTES.Nombre, PACIENTES.Correo, PACIENTES.DNI, PACIENTES.FechaNacim, PACIENTES.pass FROM PACIENTES WHERE DNI = ?");
            
            preparedStatement.setString(1, this.dni_);
            
            resultSet = preparedStatement.executeQuery();
                        
            if (resultSet.next()) {
            	txtNombre.setText(resultSet.getString("Nombre"));
            	txtCorreo.setText(resultSet.getString("Correo"));
            	txtDni.setText(resultSet.getString("DNI"));
            	fieldContrasena.setText(resultSet.getString("pass"));
            	cuadroFecha.setPromptText(resultSet.getString("FechaNacim"));
            } else {
            	System.out.println("ERROR");
            }
            
            conexion.close();

        } catch (Exception e2) {
        	System.out.println(e2);
        }
		
		txtInstitucion.setVisible(false);
		txtIdentificacion.setVisible(false);
		txtRol.setVisible(false);
		btnInstitucion.setVisible(false);
		btnIdentificacion.setVisible(false);
		btnRol.setVisible(false);
		tituloInstitucion.setVisible(false);
		tituloIdentificacion.setVisible(false);
		tituloRol.setVisible(false);	
	}

	/*
	 * Metodo para seleccionar el developer que se va a editar
	 * 
	 * @param personal Developer
	 */
	public void setDatosPersonales(Developer personal) {
		devElegido = personal;

		txtNombre.setText(devElegido.getNombre());
		txtDni.setText(devElegido.getDni());
		txtCorreo.setText(devElegido.getCorreo());
		fieldContrasena.setText(devElegido.getPassword());
		tituloFecha.setVisible(false);
		cuadroFecha.setVisible(false);
		btnFecha.setVisible(false);
		txtInstitucion.setVisible(false);
		txtIdentificacion.setVisible(false);
		txtRol.setVisible(false);
		btnInstitucion.setVisible(false);
		btnIdentificacion.setVisible(false);
		btnRol.setVisible(false);
		tituloInstitucion.setVisible(false);
		tituloIdentificacion.setVisible(false);
		tituloRol.setVisible(false);
		btnDarBaja.setVisible(false);
	}

	/*
	 * Metodo para actualizar datos de la persona que se edite
	 */
	public void Actualizar(ActionEvent event) {
		int tipopersona = 0;
		conexion = conexionesBBDD.conectar();
		try {
            preparedStatement = conexion.prepareStatement("SELECT LOGIN.Tipo FROM LOGIN WHERE DNI = ?");
            
            preparedStatement.setString(1, this.dni_);
            
            resultSet = preparedStatement.executeQuery();
                        
            if (resultSet.next()) {
            	tipopersona = resultSet.getInt("Tipo");
            }else {
            	System.out.println("ERROR");
            }
            
            conexion.close();

        } catch (Exception e2) {
        	System.out.println(e2);
        }
		
		if(tipopersona == 1) { //SOY PACIENTE

			String nuevoPacienteNombre  = txtNombre.getText();
			String nuevoPacientedDni = txtDni.getText();
			String nuevoPacienteCorreo  = txtCorreo.getText();
			String nuevoPacientePass  = fieldContrasena.getText();
			LocalDate nuevoPacienteFech = cuadroFecha.getValue();
			
			conexion = conexionesBBDD.conectar();
			try {
				if(cuadroFecha.getValue() != null)
				{
					preparedStatement = conexion.prepareStatement("UPDATE PACIENTES SET Dni = \'"+nuevoPacientedDni+"\', Nombre =\'"+nuevoPacienteNombre+"\', Correo = \'"+nuevoPacienteCorreo+"\', pass=\'"+nuevoPacientePass+"\', FechaNacim = \'"+nuevoPacienteFech+"\' WHERE Dni = \'"+dni_+"\'");			
				}
				else {
					preparedStatement = conexion.prepareStatement("UPDATE PACIENTES SET Dni = \'"+nuevoPacientedDni+"\', Nombre =\'"+nuevoPacienteNombre+"\', Correo = \'"+nuevoPacienteCorreo+"\', pass=\'"+nuevoPacientePass+"\' WHERE Dni = \'"+dni_+"\'");
				}
	            //FALTA ESTO FechaNacim ='1/1/2022'
	            preparedStatement.setString(1, this.dni_);
	            
	            resultSet = preparedStatement.executeQuery();
	                        
	            if (resultSet.next()) {
	            	//tipopersona = resultSet.getInt("Tipo");
	            }else {
	            	//System.out.println("COMPLETADO CORRECTAMENTE");
	            }
	            
	            preparedStatement = conexion.prepareStatement("UPDATE LOGIN SET DNI = \'"+nuevoPacientedDni+"\', PASS = \'"+nuevoPacientePass+"\', TIPO = \'1\'  WHERE Dni = \'"+dni_+"\'");
	            //FALTA ESTO FechaNacim ='1/1/2022'
	            preparedStatement.setString(1, this.dni_);
	            
	            resultSet = preparedStatement.executeQuery();
	                        
	            if (resultSet.next()) {
	            	//tipopersona = resultSet.getInt("Tipo");
	            }else {
	            	System.out.println("COMPLETADO CORRECTAMENTE");
	            }
	            conexion.close();

	        } catch (Exception e2) {
	        	System.out.println(e2);
	        }
			
		} else if (tipopersona == 2) { //SOY MEDICO
			
			String nuevoMedicoNombre  = txtNombre.getText();
			String nuevoMedicoDni = txtDni.getText();
			String nuevoMedicoCorreo  = txtCorreo.getText();
			String nuevoMedicoPass  = fieldContrasena.getText();
			String nuevoMedicoIdentificacionInstitucion  = txtIdentificacion.getText();
			String nuevoMedicoRol  = txtRol.getText();
			
			//String nuevoMedicoFech = LocalToDate(cuadroFecha.getValue());

			conexion = conexionesBBDD.conectar();
			try {
	            preparedStatement = conexion.prepareStatement("UPDATE MEDICOS SET Dni = \'"+nuevoMedicoDni+"\', Nombre =\'"+nuevoMedicoNombre+"\', Correo = \'"+nuevoMedicoCorreo+"\', pass=\'"+nuevoMedicoPass+"\', IDinstitucion=\'"+nuevoMedicoIdentificacionInstitucion+"\', Rol=\'"+nuevoMedicoRol+"\' WHERE Dni = \'"+dni_+"\'");
	            //FALTA ESTO FechaNacim ='1/1/2022'
	            preparedStatement.setString(1, this.dni_);
	            
	            resultSet = preparedStatement.executeQuery();
	                        
	            if (resultSet.next()) {
	            	//tipopersona = resultSet.getInt("Tipo");
	            }else {
	            	//System.out.println("COMPLETADO CORRECTAMENTE");
	            }
	            
	            preparedStatement = conexion.prepareStatement("UPDATE LOGIN SET DNI = \'"+nuevoMedicoDni+"\', PASS = \'"+nuevoMedicoPass+"\', TIPO = \'2\' WHERE Dni = \'"+dni_+"\'");
	            //FALTA ESTO FechaNacim ='1/1/2022'
	            preparedStatement.setString(1, this.dni_);
	            
	            resultSet = preparedStatement.executeQuery();
	                        
	            if (resultSet.next()) {
	            	//tipopersona = resultSet.getInt("Tipo");
	            }else {
	            	System.out.println("COMPLETADO CORRECTAMENTE");
	            }
	            conexion.close();

	        } catch (Exception e2) {
	        	System.out.println(e2);
	        }
			
		} else { //SOY DEVELOPER
			String nuevoDevNombre  = txtNombre.getText();
			String nuevoDevDni = txtDni.getText();
			String nuevoDevCorreo  = txtCorreo.getText();
			String Dev = fieldContrasena.getText();
			
			//String nuevoMedicoFech = LocalToDate(cuadroFecha.getValue());

			conexion = conexionesBBDD.conectar();
			try {
	            preparedStatement = conexion.prepareStatement("UPDATE MEDICOS SET Dni = \'"+nuevoDevNombre+"\', Nombre =\'"+nuevoDevNombre+"\', Correo = \'"+nuevoDevCorreo+"\', pass=\'"+Dev+"\' WHERE Dni = \'"+dni_+"\'");
	            //FALTA ESTO FechaNacim ='1/1/2022'
	            preparedStatement.setString(1, this.dni_);
	            
	            resultSet = preparedStatement.executeQuery();
	                        
	            if (resultSet.next()) {
	            	//tipopersona = resultSet.getInt("Tipo");
	            }else {
	            	//System.out.println("COMPLETADO CORRECTAMENTE");
	            }
	            
	            preparedStatement = conexion.prepareStatement("UPDATE LOGIN SET DNI = \'"+nuevoDevNombre+"\', PASS = \'"+Dev+"\', TIPO = \'3\' WHERE Dni = \'"+dni_+"\'");
	            //FALTA ESTO FechaNacim ='1/1/2022'
	            preparedStatement.setString(1, this.dni_);
	            
	            resultSet = preparedStatement.executeQuery();
	                        
	            if (resultSet.next()) {
	            	//tipopersona = resultSet.getInt("Tipo");
	            }else {
	            	System.out.println("COMPLETADO CORRECTAMENTE");
	            }
	            conexion.close();

	        } catch (Exception e2) {
	        	System.out.println(e2);
	        }
		}
		
		
		feedback.setText("Datos actualizados correctamente");
		feedback.setVisible(true);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		conexion = conexionesBBDD.conectar();
		feedback.setVisible(false);
		txtCheck = btnDarBaja.getText();
		btnAceptarSolicitud.setVisible(false);
		btnRechazar.setVisible(false);
		
		
	}

	/*
	 * Getters
	 */
	public JFXButton getBtnDarBaja() {
		return btnDarBaja;
	}

	public JFXButton getBtnAceptarSolicitud() {
		return btnAceptarSolicitud;
	}

	public JFXButton getBtnRechazarSolicitud() {
		return btnRechazar;
	}

	/*
	 * Setters
	 */

	public void setMedicoRegreso(Medico nuevoMedicoRegreso) {
		medicoRegreso = nuevoMedicoRegreso;
	}

	public void setPacienteRegreso(Paciente nuevoPacienteRegreso) {
		pacienteRegreso = nuevoPacienteRegreso;
	}

	public void setElegido(int nuevaPos) {
		posElegido = nuevaPos;
	}

	public void OcultarBaja() {
		btnDarBaja.setVisible(false);
	}

}