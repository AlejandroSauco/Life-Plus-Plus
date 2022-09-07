package controlador;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import bbdd.conexionesBBDD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/*
 * CONTROLADOR PANTALLA REGISTRO PACIENTE
 */
public class RegistroPacientesControlador {

	@FXML
	private JFXButton BtnAtras, BtnRegistroMedico, btnAceptarRegistroPaciente;
	@FXML
	private JFXTextField txtNombrePaciente, txtDniPaciente, txtCorreoPaciente;
	@FXML
	private DatePicker fechaNacimiento;
	@FXML
	private JFXPasswordField txtPassword, txtConfirmPassword;
	@FXML
	private CheckBox selectMasculino, selectFemenino;
	@FXML
	private Label txtError;
	

	/*
	 * Metodo para confirmar el registro como paciente Validacion de campos
	 * correctamente completados
	 */
	@FXML
	public void ConfirmarRegistroPaciente(ActionEvent event) {
				
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nombre = txtNombrePaciente.getText();
		String dni = txtDniPaciente.getText();
		String correo = txtCorreoPaciente.getText();
		int sexo = (selectMasculino.isSelected())?1:0;
		String fecha = null;
		Date fechaAux = null;
		try {
			fecha = fechaNacimiento.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			fechaAux = sdf.parse(fecha);
		} catch (Exception e) {

		}
		String password = txtPassword.getText();
		String confirmPassword = txtConfirmPassword.getText();

		if (nombre.isEmpty()) {
			txtError.setText("El campo de nombre está vacío");
		} else if (!validarNombre(nombre)) {
			txtError.setText("El nombre tiene que tener Nombre y Apellidos");
		} else if (dni.isEmpty()) {
			txtError.setText("El campo de DNI está vacío");
		} /*else if (sp.checkIfExist(dni)) {
			txtError.setText("El paciente ya está registrado");
		} */else if (correo.isEmpty()) {
			txtError.setText("El campo de correo está vacío");
		} else if (!validarCorreo(correo)) {
			txtError.setText("El formato del correo es incorrecto");
		} else if (fecha == null || fecha.equals("")) {
			txtError.setText("El campo de fecha está vacío");
		} else if (fechaAux == null || new Date().getTime() <= fechaAux.getTime()) {
			txtError.setText("La fecha de nacimiento no es correcta");
		} else if (password.isEmpty()) {
			txtError.setText("El campo de contraseña está vacío");
		} else if (confirmPassword.isEmpty()) {
			txtError.setText("El campo de confirmar contraseña está vacío");
		} else if (!confirmPassword.equals(password)) {
			txtError.setText("Las contraseñas no coinciden");
		} else {
			try {
				conexionesBBDD.crearPacientes(dni, password, nombre, correo, fecha, sexo, 1);
				
			}catch (Exception e) {

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
				controlLogin.ExplicarSituacion("Usuario registrado correctamente. Inicie sesión");
				stage.setMinHeight(600);
				stage.setMinWidth(600);
				Stage s_login = (Stage) btnAceptarRegistroPaciente.getScene().getWindow();
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
	 * Metodo para acceder a registro como medico desde la pantalla de registro de
	 * paciente
	 * 
	 * @exception IOException
	 */
	@FXML
	public void RegistroMedico(ActionEvent event) throws IOException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/RegistroMedicos.fxml"));
			RegistroMedicosControlador controlRegistroMedicos = new RegistroMedicosControlador();
			loader.setController(controlRegistroMedicos);
			Parent rootRegistroMedico = loader.load();
			Stage stage = new Stage();
			stage.getIcons().add(new Image("/Img/lifeplusplus.png"));
			stage.setTitle("Life++");
			stage.setScene(new Scene(rootRegistroMedico));
			stage.setMinHeight(600);
			stage.setMinWidth(600);
			Stage s_login = (Stage) btnAceptarRegistroPaciente.getScene().getWindow();
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
	 * Metodo para volver a la pantalla de login
	 * 
	 * @exception IOException
	 */
	@FXML
	public void VolverLogin(ActionEvent event) throws IOException {

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
			Stage s_login = (Stage) btnAceptarRegistroPaciente.getScene().getWindow();
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
	 * Metodo para seleccionar sexo de la persona que se registra
	 * 
	 * @exception IOException
	 */
	@FXML
	public void femeninoSeleccionado(ActionEvent event) throws IOException {

		try {
			if (selectFemenino.isSelected()) {
				selectMasculino.setSelected(false);
				
			} else {
				selectMasculino.setSelected(true);
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Metodo para seleccionar sexo de la persona que se registra
	 * 
	 * @exception IOException
	 */
	@FXML
	public void masculinoSeleccionado(ActionEvent event) throws IOException {

		try {
			if (selectMasculino.isSelected()) {
				selectFemenino.setSelected(false);
				
			} else {
				selectFemenino.setSelected(true);
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Metodo para validar el formato del correo
	 * 
	 * @param correo
	 * 
	 * @return boolean
	 */
	private boolean validarCorreo(String correo) {
		Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(correo);
		return m.find();
	}

	/*
	 * Metodo para validar el nombre
	 * 
	 * @param nombre
	 * 
	 * @return boolean
	 */
	private boolean validarNombre(String nombre) {
		Pattern p = Pattern.compile("^([A-Za-z]*((\\s)))+[A-Za-z]*$");
		Matcher m = p.matcher(nombre);
		return m.find();
	}
}