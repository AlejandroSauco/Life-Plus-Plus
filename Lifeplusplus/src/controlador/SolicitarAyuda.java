package controlador;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import bbdd.conexionesBBDD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/*
 * CONTROLADOR PARA PANTALLA DE SOLICITAR AYUDA
 */
public class SolicitarAyuda {

	@FXML
	TextArea textAreaSolucion;
	@FXML
	private Label tituloPrincipal, numDni, txterror;
	@FXML
	private JFXButton btnRegresar;
	@FXML
	private JFXTextField textFieldDni;
	@FXML
	private JFXTextArea textAreaDescripcion;

	/*
	 * Variables para instanciar objetos especificos
	 */
	private String dni_;
	private String dniDev;
	private int tipo; // De qué tipo de persona es la pantalla de la que venimos (1 -> paciente; 2 ->  medico; 3 -> developer)
	
	String sql;
	Connection conexion = null;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;

	
	public void BloquearPregunta()
	{
		textAreaDescripcion.setEditable(false);
	}
	
	public SolicitarAyuda(String nuevoDni, int nuevoTipo)
	{
		dni_ = nuevoDni;
		tipo = nuevoTipo;
	}
	
	public SolicitarAyuda(String nuevoDni, String nuevoDniDev)
	{
		dni_ = nuevoDni;
		dniDev = nuevoDniDev;
		tipo = 3;
	}
	
	/*
	 * Metodo para regresar a la pantalla anterior
	 * 
	 * @exception IOException
	 */
	
	@FXML
	void initialize() {
		mostrarDatos();
		tituloPrincipal.setText("Solicitar ayuda");
	}
	
	@FXML
	public void mostrarDatos() {
		numDni.setText(dni_);
		if (tipo == 3) {
			conexion = conexionesBBDD.conectar();
			try {
	            preparedStatement = conexion.prepareStatement("SELECT * FROM Ayudas WHERE DniUsuario = ?");        
	            preparedStatement.setString(1, this.dni_);
	            
	            resultSet = preparedStatement.executeQuery();
	            if (resultSet.next()) {
	            textAreaDescripcion.setText(resultSet.getString("Descripcion"));
	            }
	            else
	            {
	            	System.out.println("ERROR");
	            }
	            conexion.close();
	        } catch (Exception e) {
	        	System.out.println(e);
	        }
		}
	}
	
	@FXML
	public void Regresar(ActionEvent event) throws IOException {
		if (tipo == 3) { // VOLVER A DEVELOPER
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/PpalDeveloper.fxml"));
				PpalDeveloperControlador ppalDeveloperControlador = new PpalDeveloperControlador(dniDev);
				loader.setController(ppalDeveloperControlador);
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
		}
		else if(tipo == 1) { // VOLVER A PACIENTE
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/PpalPaciente.fxml"));
				PpalPacienteControlador ppalPacienteControlador = new PpalPacienteControlador(dni_);
				loader.setController(ppalPacienteControlador);
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
		} else { // VOLVER A MEDICO
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
		}
	}
	
	/*
	 * Metodo para confirmar el envio de la solicitud
	 * 
	 * @exception ParseException
	 */
	@FXML
	public void ConfirmarEnvioAyuda(ActionEvent event) throws ParseException {
		String Descripcion = textAreaDescripcion.getText();
		String Solucion = textAreaSolucion.getText();
		if(Descripcion.isEmpty()) {
			txterror.setText("El campo de nombre está vacío");
		} else {
			conexion = conexionesBBDD.conectar();
			try {
				System.out.println("Entro en el try");
				preparedStatement = conexion.prepareStatement("DELETE FROM Ayudas WHERE DniUsuario = ?");
				preparedStatement.setString(1, this.dni_);
				resultSet = preparedStatement.executeQuery();
                
	            if (resultSet.next()) {
	            	//tipopersona = resultSet.getInt("Tipo");
	            }else {
	            	//System.out.println("COMPLETADO CORRECTAMENTE");
	            }
	            System.out.println("He borrado el dato");
				preparedStatement = conexion.prepareStatement("INSERT INTO Ayudas (Descripcion, Solucion, DniUsuario) values('"+Descripcion+"','"+Solucion+"','"+dni_+"')");
				System.out.println("He procesado la segunda accion");
				preparedStatement.setString(1, this.dni_);     
	            resultSet = preparedStatement.executeQuery();
	            System.out.println("He añadido el dato");
	                        
	            if (resultSet.next()) {
	            	//tipopersona = resultSet.getInt("Tipo");
	            }else {
	            	//System.out.println("COMPLETADO CORRECTAMENTE");
	            }	            
	            conexion.close();
	            System.out.println("He cerrado la conexion");

	        } catch (Exception e2) {
	        	System.out.println(e2);
	        }
			if (tipo == 1) { // SOY PACIENTE				
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/PpalPaciente.fxml"));
					PpalPacienteControlador ppalPacienteControlador = new PpalPacienteControlador(dni_);
					loader.setController(ppalPacienteControlador);
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
			} else if (tipo == 3) // SOY EL DEVELOPER
			{
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/PpalDeveloper.fxml"));
					PpalDeveloperControlador ppalDeveloperControlador = new PpalDeveloperControlador(dni_);
					loader.setController(ppalDeveloperControlador);
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
			} else { // SI ES MEDICO!
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
			}
		}
	}
	
	/*
	 * Getters
	 */
	
	public TextArea getSolucionArea() {
		return textAreaSolucion;
	}
}