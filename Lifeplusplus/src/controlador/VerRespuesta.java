package controlador;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;

import application.Solicitud;
import bbdd.conexionesBBDD;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
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
 * CONTROLADOR PANTALLA VER RESPUESTA DE LA SOLICITUD DE AYUDA
 */
public class VerRespuesta {

	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;
	@FXML
	private JFXTextArea textAreaDescripcion;
	@FXML
	private Label tituloPrincipal, numDni, txterror;
	@FXML
	private JFXButton enviarAyudaBtn, btnRegresar;
	@FXML
	private TextArea textAreaSolucion;

	//COPIAR ESTO PARA QUE FUNCIONE LA SQL
	String sql;
	Connection conexion = null;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;

	Solicitud elegido = null;

	/*
	 * Setters
	 */
	
	private String dni_;
	
	private int tipo;
	
	public void BloquearLabels()
	{
		textAreaDescripcion.setEditable(false);
		textAreaSolucion.setEditable(false);
	}
	
	public VerRespuesta(String nuevoDni, int nuevoTipo) {
		dni_ = nuevoDni;
		tipo = nuevoTipo;
	}

	public JFXButton getBotonEnviar() {
		return enviarAyudaBtn;
	}

	@FXML
	void ConfirmarEnvioAyuda(ActionEvent event) {
		// no hace nada ya que cancelamos su uso desde un principio
	}

	/*
	 * Metodo para regresar a la pantalla anterior
	 * 
	 * @exception IOException
	 */
	@FXML
	public void Regresar(ActionEvent event) throws IOException {
		if (tipo == 1) { // PACIENTE
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
		} else if(tipo == 2) { // MEDICO
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
		} else { // DEVELOPER
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
		}
	}

	@FXML
	void initialize() {
		numDni.setText(dni_);
		conexion = conexionesBBDD.conectar();
		try {
            preparedStatement = conexion.prepareStatement("SELECT * FROM Ayudas WHERE DniUsuario = ?");
            
            preparedStatement.setString(1, this.dni_);
            
            resultSet = preparedStatement.executeQuery();
                        
            if (resultSet.next()) {
            	textAreaDescripcion.setText(resultSet.getString("Descripcion"));
            	textAreaSolucion.setText(resultSet.getString("Solucion"));
            }else {
            	System.out.println("ERROR");
            }
            
            conexion.close();

        } catch (Exception e2) {
        	System.out.println(e2);
        }
		tituloPrincipal.setText("Ver respuesta de mi última consulta");
	}
}
