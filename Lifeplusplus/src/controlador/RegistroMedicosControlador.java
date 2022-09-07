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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/*
 * CONTROLADOR PANTALLA REGISTRO MEDICOS
 */
public class RegistroMedicosControlador {
	@FXML
	private JFXButton BtnAtras, btnAceptarRegistro;
	@FXML
	private JFXTextField txtNombreMedico, txtDniMedico, txtCorreoMedico, txtInstitucionMedico;
	@FXML
	private JFXTextField txtRolMedico, txtIdentificacionMedico;
	@FXML
	private JFXPasswordField txtPasswordMedico, txtConfirmPasswordMedico;
	@FXML
	private DatePicker FechaNacimientoMedico;
	@FXML
	private Label txtError;

	/*
	 * Metodo para aceptar el registro como medico Comprobacion de los campos
	 * correctamente completados
	 */
	@FXML
	public void ConfirmarRegistroMedico(ActionEvent event) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nombre = txtNombreMedico.getText();
		String dni = txtDniMedico.getText();
		String correo = txtCorreoMedico.getText();
		String institucion = txtInstitucionMedico.getText();
		String rol = txtRolMedico.getText();
		String identificacion = null;
		try {
			identificacion = txtIdentificacionMedico.getText();
		} catch (Exception e) {

		}
		String fecha = null;
		Date fechaAux = null;
		try {
			fecha = FechaNacimientoMedico.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			fechaAux = sdf.parse(fecha);
		} catch (Exception e) {

		}
		String password = txtPasswordMedico.getText();
		String confirmPassword = txtConfirmPasswordMedico.getText();

		if (nombre.isEmpty()) {
			txtError.setText("El campo de nombre está vacío");
		} else if (!validarNombre(nombre)) {
			txtError.setText("El nombre tiene que tener Nombre y Apellidos");
		} else if (dni.isEmpty()) {
			txtError.setText("El campo de DNI está vacío");
		} /*else if (sm.checkIfExist(dni)) {
			txtError.setText("El paciente ya está registrado");
		} */ else if (institucion.isEmpty()) {
			txtError.setText("El campo de institución está vacío");
		} else if (rol.isEmpty()) {
			txtError.setText("El campo de rol está vacío");
		} else if (identificacion == null) {
			txtError.setText("El campo de identificación no es correcto");
		} else if (correo.isEmpty()) {
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
				conexionesBBDD.crearMedicos(dni, confirmPassword, nombre, correo, fecha, rol, identificacion, " ");
				//Medico m = new Medico(dni, nombre, password, correo, sdf.parse(fecha), institucion, rol, identificacion);
				//sm.addSolicitud(m);
				//sm.saveSolicitudes();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
				controlLogin.ExplicarSituacion("Solicitud de registro enviada. Espere confirmación");
				stage.setMinHeight(600);
				stage.setMinWidth(600);
				Stage s_login = (Stage) btnAceptarRegistro.getScene().getWindow();
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
	 * Metodo para volver a la pantalla de registro de pacientes
	 * 
	 * @exception IOException
	 */
	@FXML
	public void VolverRegistroPacientes(ActionEvent event) throws IOException {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/RegistroPacientes.fxml"));
			RegistroPacientesControlador controlRegistroPaciente = new RegistroPacientesControlador();
			loader.setController(controlRegistroPaciente);
			Parent rootRegistroPaciente = loader.load();
			Stage stage = new Stage();
			stage.getIcons().add(new Image("/Img/lifeplusplus.png"));
			stage.setTitle("Life++");
			stage.setScene(new Scene(rootRegistroPaciente));
			stage.setMinHeight(600);
			stage.setMinWidth(600);
			Stage s_login = (Stage) btnAceptarRegistro.getScene().getWindow();
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