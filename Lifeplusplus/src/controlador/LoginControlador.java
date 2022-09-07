package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import bbdd.conexionesBBDD;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/*
 * CONTROLADOR PANTALLA LOGIN
 */

public class LoginControlador {

	@FXML
	private JFXButton BtnInicio, BtnRegistro;
	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;
	@FXML
	private JFXTextField txtDniLogin;
	@FXML
	private JFXPasswordField txtPasswordLogin;
	@FXML
	private Label labelOcultoLogin, LabelExplicacion;

	Runnable comprobarFecha = new Runnable() {
		public void run() {
			try {
				Platform.runLater(() -> {
					BtnInicio.setVisible(true);
					labelOcultoLogin.setVisible(false);
					labelOcultoLogin.setText("Datos introducidos incorrectos");
					contadorLogIn = 0;
				});
			} catch (Exception e) {
				System.out.println("Error en los hilos del bloqueo del login");
				// e.printStackTrace();
			}
		}
	};

	ScheduledExecutorService executor;

	private int contadorLogIn = 0;

	@FXML
	void abrir_registro(MouseEvent event) {

	}

	/*
	 * Metodo para iniciar sesion
	 * 
	 * @exception IOException
	 */
	@FXML
	public void iniciarSesion(ActionEvent event) throws IOException {
		contadorLogIn++;

		if (contadorLogIn >= 5) {
			BtnInicio.setVisible(false);
			labelOcultoLogin.setText("Demasiados intentos. Espere unos segundos");
		}
		String dni = txtDniLogin.getText();
		String password = txtPasswordLogin.getText();
		int tipopersona = 0;
		
		tipopersona = conexionesBBDD.check_login(dni, password);
		if (tipopersona == 1) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/PpalPaciente.fxml"));
				PpalPacienteControlador controlPpalPaciente = new PpalPacienteControlador(dni);
				loader.setController(controlPpalPaciente);
				Parent rootPaciente = loader.load();
				Stage stage = new Stage();
				stage.getIcons().add(new Image("/Img/lifeplusplus.png"));
				stage.setTitle("Life++");
				stage.setScene(new Scene(rootPaciente));
				stage.setMinHeight(600);
				stage.setMinWidth(600);
				Stage s_login = (Stage) BtnInicio.getScene().getWindow();
				//stage.setHeight(s_login.getHeight());
				//stage.setWidth(s_login.getWidth());
				stage.setX(s_login.getX());
				stage.setY(s_login.getY());
				stage.show();
				s_login.hide();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (tipopersona == 2) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/PpalMedico.fxml"));
				PpalMedicoControlador controlPpalMedico = new PpalMedicoControlador(dni);
				loader.setController(controlPpalMedico);
				Parent rootMedico = loader.load();
				Stage stage = new Stage();
				stage.getIcons().add(new Image("/Img/lifeplusplus.png"));
				stage.setTitle("Life++");
				stage.setScene(new Scene(rootMedico));
				stage.setMinHeight(600);
				stage.setMinWidth(600);
				Stage s_login = (Stage) BtnInicio.getScene().getWindow();
				stage.setHeight(s_login.getHeight());
				stage.setWidth(s_login.getWidth());
				stage.setX(s_login.getX());
				stage.setY(s_login.getY());
				stage.show();
				s_login.hide();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (tipopersona == 3) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/PpalDeveloper.fxml"));
				PpalDeveloperControlador controlDeveloper = new PpalDeveloperControlador(dni);
				loader.setController(controlDeveloper);
				Parent rootDeveloper = loader.load();
				Stage stage = new Stage();
				stage.getIcons().add(new Image("/Img/lifeplusplus.png"));
				stage.setTitle("Life++");
				stage.setScene(new Scene(rootDeveloper));
				stage.setMinHeight(600);
				stage.setMinWidth(600);
				Stage s_login = (Stage) BtnInicio.getScene().getWindow();
				stage.setHeight(s_login.getHeight());
				stage.setWidth(s_login.getWidth());
				stage.setX(s_login.getX());
				stage.setY(s_login.getY());
				stage.show();
				s_login.hide();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			labelOcultoLogin.setVisible(true);
		}
	}

	/*
	 * Metodo para navegar a la pantalla de registro
	 * 
	 * @exception IOException
	 */
	@FXML
	public void registro(ActionEvent event) throws IOException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/RegistroPacientes.fxml"));
			RegistroPacientesControlador registroPaciente = new RegistroPacientesControlador();
			loader.setController(registroPaciente);
			Parent rootRegistroPaciente = loader.load();
			Stage stage = new Stage();
			stage.getIcons().add(new Image("/Img/lifeplusplus.png"));
			stage.setTitle("Life++");
			stage.setScene(new Scene(rootRegistroPaciente));
			stage.setMinHeight(600);
			stage.setMinWidth(600);
			Stage s_login = (Stage) BtnInicio.getScene().getWindow();
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

	/*
	 * Metodo para entrar pulsando Enter
	 */
	public void LogInConEnter(KeyEvent event) throws IOException {
		if (event.getCode() == KeyCode.ENTER) {
			iniciarSesion(null);
		}
	}

	/*
	 * Metodo para mostrar que el dni o password es incorrecto
	 */
	public void ExplicarSituacion(String textoMostrar) {
		LabelExplicacion.setText(textoMostrar);
		LabelExplicacion.setTextFill(Color.web("#EB7193"));
		LabelExplicacion.setVisible(true);
		System.out.println("Deberia haber cambiado el Label");
		ScaleTransition transicionTamano = new ScaleTransition();
		transicionTamano.setDuration(Duration.millis(600));
		transicionTamano.setNode(LabelExplicacion);
		transicionTamano.setToX(1.08f);
		transicionTamano.setToY(1.08f);
		transicionTamano.setCycleCount(4);
		transicionTamano.setAutoReverse(true);
		transicionTamano.play();
	}

	@FXML
	void initialize() {
		LabelExplicacion.setVisible(false);
		executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(comprobarFecha, 10, 60, TimeUnit.SECONDS);
	}
}