package controlador;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.dlsc.gmapsfx.GoogleMapView;
import com.dlsc.gmapsfx.javascript.event.GMapMouseEvent;
import com.dlsc.gmapsfx.javascript.event.UIEventType;
import com.dlsc.gmapsfx.javascript.object.GoogleMap;
import com.dlsc.gmapsfx.javascript.object.LatLong;
import com.dlsc.gmapsfx.javascript.object.MapOptions;
import com.dlsc.gmapsfx.javascript.object.MapTypeIdEnum;
import com.jfoenix.controls.JFXButton;

import application.Medico;
import application.Paciente;
import application.Solicitud;
import bbdd.conexionesBBDD;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/*
 * CONTROLADOR PANTALLA PRINCIPAL MEDICO
 */
public class PpalMedicoControlador implements Initializable {

	@FXML
	private JFXButton BtnCerrarSesion_, PedirAyuda, BtnDescargar, EditarPerfil, RespuestaAyuda, DescargadeHistorial;
	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;
	@FXML
	private Label labelP0, labelP1, labelP2, labelP3, labelP4, labelP5, labelP6, labelP7, nombreMedico;
	@FXML
	private JFXButton botonP0, botonP1, botonP2, botonP3, botonP4, botonP5, botonP6, botonP7;
	@FXML
	private TextField busquedaPacientes;
	// Pulsaciones
	@FXML
	private CategoryAxis x;
	@FXML
	private NumberAxis y;
	@FXML
	private LineChart<?, ?> LineChart;
	// Saturación en sangre
	@FXML
	private CategoryAxis XS;
	@FXML
	private NumberAxis YS;
	@FXML
	private LineChart<?, ?> LCSangre;
	@FXML
	private Label txtNombre, txtDni, txtSexo, txtFecha, txtContacto, textoCentral, tituloOxigenacion, tituloPulsaciones;
	@FXML
	protected GoogleMapView mapView;
	@FXML
	protected Pane Foto;

	private Medico yo;
	private int contador = 200;
	private int[] ubiPacientes = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
	private Label[] labelesPacientes = new Label[8];
	private JFXButton[] botonesPacientes = new JFXButton[8];
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	private GoogleMap map;
	private DecimalFormat formatter = new DecimalFormat("###.00000");
	private Paciente elegidoP = null;
	
	//COPIAR ESTO PARA QUE FUNCIONE LA SQL
	String sql;
	Connection conexion = null;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;
	java.util.List<Paciente> pacientes;
	XYChart.Series seriebpm = new XYChart.Series();
	XYChart.Series serieheart = new XYChart.Series();
	String dni_;
	
	public PpalMedicoControlador(String dni_) {
		this.dni_ = dni_;		
	}
	
	/*
	 * Metodo para pasar datos del pulsometro por los linechart
	 * */
	Runnable actualizarPulsometroLinechart = new Runnable() {
		public void run() {
			try {
				Platform.runLater(() -> {
					
					conexion = conexionesBBDD.conectar();
					try {
			            preparedStatement = conexion.prepareStatement("SELECT ID,heart_rate, bpm FROM datos_pulsometro ORDER BY ID DESC LIMIT 14");			            
			            preparedStatement.setString(1,"");
			            resultSet = preparedStatement.executeQuery();
			                        
			            if (resultSet.next()) {
			            	LineChart.setTitle(resultSet.getString("heart_rate"));
			            	LCSangre.setTitle(resultSet.getString("bpm"));
			            	contador += 100;
			            	if(seriebpm.getData().size() >= 15)
			            	{
			            		seriebpm.getData().remove(0);
			            	}
			            	if(seriebpm.getData().size() >= 15)
			            	{
			            		seriebpm.getData().remove(0);
			            	}
			            	seriebpm.getData().add(new XYChart.Data(String.valueOf(contador), Integer.parseInt(resultSet.getString("bpm"))));
			            	serieheart.getData().add(new XYChart.Data(String.valueOf(contador), Integer.parseInt(resultSet.getString("heart_rate"))));
			            	//seriebpm.getChart();
			            	//series.getData().remove(0);
			            	LCSangre.getData().add(seriebpm);
			            	LineChart.getData().add(serieheart);
			            	LCSangre.setTitle(resultSet.getString("heart_rate"));
			            	LCSangre.setTitle(resultSet.getString("bpm"));
			            	//Barras debajo de datos dinamicos			            	
			            }else {
			            	System.out.println("ERROR");
			            }
			        } catch (Exception e2) {
			        	System.out.println(e2);
			        } finally {
			        	try {
							conexion.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        }
				});
			} catch (Exception e) {
				System.out.println("Error en los hilos del bloqueo del login");
				// e.printStackTrace();
			}
		}
	};

	ScheduledExecutorService executor;

	
	
	
	
	/*
	 * Metodo para cerrar sesion y volver a pantalla login
	 * 
	 * @exception IOException
	 */
	@FXML
	public void CerrarSesion(ActionEvent event) throws IOException {

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
			Stage s_login = (Stage) BtnCerrarSesion_.getScene().getWindow();
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
	 * Metodo para editar perfil como medico
	 */
	public void EditarPerfil() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/EditarUsuario.fxml"));
			EditarUsuarioControlador editarUsuarioControlador = new EditarUsuarioControlador(dni_);
			loader.setController(editarUsuarioControlador);
			Parent rootLogin = loader.load();
			Stage stage = new Stage();
			stage.getIcons().add(new Image("/Img/lifeplusplus.png"));
			stage.setTitle("Life++");
			stage.setScene(new Scene(rootLogin));
			editarUsuarioControlador.setMedicoEditable(dni_);
			editarUsuarioControlador.setMedicoRegreso(yo);
			editarUsuarioControlador.OcultarBaja();
			stage.setMinHeight(600);
			stage.setMinWidth(600);
			Stage s_login = (Stage) BtnCerrarSesion_.getScene().getWindow();
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
	 * Metodo para solicitar ayuda como medico
	 * 
	 * @exception IOException
	 */
	public void PedirAyuda(ActionEvent event) throws IOException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/SolicitarAyudaPaciente.fxml"));
			SolicitarAyuda solicitarAyuda = new SolicitarAyuda(dni_, 2);
			loader.setController(solicitarAyuda);
			Parent rootLogin = loader.load();
			Stage stage = new Stage();
			stage.getIcons().add(new Image("/Img/lifeplusplus.png"));
			stage.setTitle("Life++");
			stage.setScene(new Scene(rootLogin));
			solicitarAyuda.getSolucionArea().setVisible(false);
			stage.setMinHeight(600);
			stage.setMinWidth(600);
			Stage s_login = (Stage) BtnCerrarSesion_.getScene().getWindow();
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
	 * Metodo para cargar listado de pacientes en la pantalla de medico
	 * 
	 * @exception IOException
	 */
	public void CargarPacientes(KeyEvent event) throws IOException {
		
		pacientes = new ArrayList();

		conexion = conexionesBBDD.conectar();
		try {
            preparedStatement = conexion.prepareStatement("SELECT * FROM PACIENTES WHERE IDmedico = " + dni_);        
            preparedStatement.setString(1, this.dni_);
            
            resultSet = preparedStatement.executeQuery();
            
        	while(resultSet.next())
        	{
        		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        		Date fn = sdf.parse(resultSet.getString("FechaNacim"));
        		boolean s = resultSet.getInt("Sexo")==1?true:false;
        		// (String dni, String nombre, String password, String correo, Date nacim, boolean sexo)
        		Paciente pacienteAux = new Paciente(resultSet.getString("DNI"), resultSet.getString("Nombre"), resultSet.getString("Pass"), resultSet.getString("Correo"), fn, s);
        		pacientes.add(pacienteAux);
        	}      
            conexion.close();
        } catch (Exception e2) {
        	System.out.println(e2);
        }
		
		if (busquedaPacientes.getText().isEmpty()) {
			for (int i = 0; i < labelesPacientes.length; i++) {
				try {
					labelesPacientes[i].setVisible(true);
					labelesPacientes[i].setText(pacientes.get(i).getNombre());
					botonesPacientes[i].setVisible(true);

				} catch (Exception e) {

					labelesPacientes[i].setVisible(false);
					botonesPacientes[i].setVisible(false);
					System.out.println("Hay menos de 8 pacientes. Label[" + i + "] se oculta");
					// e.printStackTrace();
				}
			}
		} else {
			String textoBusquedaP = busquedaPacientes.getText().toLowerCase();
			int longBusquedaP = busquedaPacientes.getText().length();
			int correccionP = 0;

			for (int i = 0; i < labelesPacientes.length; i++) {
				labelesPacientes[i].setVisible(false);
				botonesPacientes[i].setVisible(false);
				labelesPacientes[0].setVisible(true);
				labelesPacientes[0].setText("No hay resultados");
			}

			for (int i = 0; i < pacientes.size(); i++) {
				try {
					boolean coincideP = true;

					for (int j = 0; j < longBusquedaP; j++) {
						if (textoBusquedaP.charAt(j) != pacientes.get(i).getNombre().toLowerCase().charAt(j)) {
							coincideP = false;
						}
					}
					if (coincideP == true) {
						labelesPacientes[i - correccionP].setVisible(true);
						botonesPacientes[i - correccionP].setVisible(true);
						labelesPacientes[i - correccionP].setText(pacientes.get(i).getNombre());
					} else {
						correccionP++;
					}

				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * Metodo para mostrar los datos del paciente seleccionado por el medico
	 * 
	 * @exception IOException
	 */
	public void mostrarDatos(ActionEvent event) throws IOException {

		JFXButton sourceButton = (JFXButton) event.getSource();
		textoCentral.setText("Ubicación actual");
		tituloOxigenacion.setVisible(true);
		tituloPulsaciones.setVisible(true);
		LCSangre.setVisible(true);
		mapView.setVisible(true);
		LineChart.setVisible(true);
		txtNombre.setVisible(true);
		txtDni.setVisible(true);
		txtSexo.setVisible(true);
		txtFecha.setVisible(true);
		txtContacto.setVisible(true);
		Foto.setVisible(true);
		DescargadeHistorial.setVisible(true);
		
		for (int i = 0; i < botonesPacientes.length; i++) {
			if (sourceButton == botonesPacientes[i]) {
				Paciente p = pacientes.get(i);
				txtNombre.setText(p.getNombre());
				txtContacto.setText(p.getCorreo());
				txtDni.setText(p.getDni());
				txtSexo.setText((p.getSexo()==true)?"Masculino":"Femenino");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				txtFecha.setText(sdf.format(p.getNacimiento()));
				
			}
		}
	}

	
	
	/*
	 * Metodo para descargar historial medico del paciente
	 * 
	 * @exception IOException
	 */
	@FXML
	public void Descargar(ActionEvent event) throws IOException {
		try {
			File path = new File(System.getProperty("user.dir") + "//certificados//" + elegidoP.getDni() + ".pdf");// Indicar
																													// la
																													// ruta
																													// correspondiente(relativa)
			Desktop.getDesktop().open(path);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * Metodo para editar datos
	 * 
	 * @exception IOException
	 */
	public void EditarUsuario(ActionEvent event) throws IOException {
		if (elegidoP != null) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/EditarUsuario.fxml"));
				EditarUsuarioControlador editarUsuarioControlador = new EditarUsuarioControlador(dni_);
				loader.setController(editarUsuarioControlador);
				Parent rootLogin = loader.load();
				Stage stage = new Stage();
				stage.getIcons().add(new Image("/Img/lifeplusplus.png"));
				stage.setTitle("Life++");
				stage.setScene(new Scene(rootLogin));
				//editarUsuarioControlador.setPacienteEditable(elegidoP);
				editarUsuarioControlador.setMedicoRegreso(yo);
				editarUsuarioControlador.OcultarBaja();
				stage.setMinHeight(600);
				stage.setMinWidth(600);
				Stage s_login = (Stage) BtnCerrarSesion_.getScene().getWindow();
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		mapView.setKey("AIzaSyAjkcplqD8c3KVD_KvYQzoV6uiaeXN0Veg");
		
		executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(actualizarPulsometroLinechart, 1, 10, TimeUnit.SECONDS);
		
		txtNombre.setVisible(false);
		txtDni.setVisible(false);
		txtSexo.setVisible(false);
		txtFecha.setVisible(false);
		txtContacto.setVisible(false);
		Foto.setVisible(false);
		DescargadeHistorial.setVisible(false);
		
		try {
			conexion = conexionesBBDD.conectar();
			try {
		        preparedStatement = conexion.prepareStatement("SELECT MEDICOS.Nombre FROM MEDICOS WHERE DNI = ?");		        
		        preparedStatement.setString(1, this.dni_);	        
		       
		        resultSet = preparedStatement.executeQuery();
		                    
		        if (resultSet.next()) {
		        	nombreMedico.setText(resultSet.getString("Nombre"));
		        }else {
		            //JOptionPane.showMessageDialog(null, "Codigo de materia no encontrado");
		        }
		        conexion.close();

		    } catch (Exception e2) {
		        //JOptionPane.showMessageDialog(null,"Ocurrio un error con el acceso a la base de datos " );
		    }
			
		} catch (Exception e) {
			System.out.println("No se especifica bien el medico");
		}
		
		labelesPacientes[0] = labelP0;
		labelesPacientes[1] = labelP1;
		labelesPacientes[2] = labelP2;
		labelesPacientes[3] = labelP3;
		labelesPacientes[4] = labelP4;
		labelesPacientes[5] = labelP5;
		labelesPacientes[6] = labelP6;
		labelesPacientes[7] = labelP7;

		botonesPacientes[0] = botonP0;
		botonesPacientes[1] = botonP1;
		botonesPacientes[2] = botonP2;
		botonesPacientes[3] = botonP3;
		botonesPacientes[4] = botonP4;
		botonesPacientes[5] = botonP5;
		botonesPacientes[6] = botonP6;
		botonesPacientes[7] = botonP7;
		
		tituloOxigenacion.setVisible(false);
		tituloPulsaciones.setVisible(false);
		LCSangre.setVisible(false);
		mapView.setVisible(false);
		LineChart.setVisible(false);
		mapView.addMapInitializedListener(() -> configureMap());
		
		try {
			CargarPacientes(null);
		} catch (Exception e) {
			System.out.println("Problemas al cargar medicos o pacientes en el initialize()");
		}
	}

	public double Latitud() {
		double latitud=0;
		conexion = conexionesBBDD.conectar();
		try {
            preparedStatement = conexion.prepareStatement("SELECT datos_gps.Latitud FROM datos_gps ORDER BY Fecha desc limit 1");
            
            resultSet = preparedStatement.executeQuery();          
            
            conexion.close();
            
            if (resultSet.next()) {
            	latitud=resultSet.getDouble("Latitud");

            }else {
            	System.out.println("ERROR");
            }
            

        } catch (Exception e2) {
        	System.out.println(e2);
        }
		
		return latitud;
	}
	
	public double Longitud() {
		double longitud=0;
		conexion = conexionesBBDD.conectar();
		try {
            preparedStatement = conexion.prepareStatement("SELECT datos_gps.Longitud FROM datos_gps ORDER BY Fecha desc limit 1");
            
            resultSet = preparedStatement.executeQuery();          
            
            conexion.close();
            
            if (resultSet.next()) {
            	longitud=resultSet.getDouble("Longitud");

            }else {
            	System.out.println("ERROR");
            }
            

        } catch (Exception e2) {
        	System.out.println(e2);
        }
		
		return longitud;
	}
	
	/*
	 * Configuracion del mapa
	 */
	protected void configureMap() {
		MapOptions mapOptions = new MapOptions();

		mapOptions.center(new LatLong(Latitud(), Longitud())).mapType(MapTypeIdEnum.ROADMAP).zoom(15);
		map = mapView.createMap(mapOptions, false);

		map.addMouseEventHandler(UIEventType.click, (GMapMouseEvent event) -> {
			LatLong latLong = event.getLatLong();
		});
	}

	/*
	 * Metodo para ver la respuesta del developer
	 */
	@FXML
	void verRespuesta(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/SolicitarAyudaPaciente.fxml"));
			VerRespuesta verrespuesta = new VerRespuesta(dni_, 2);
			loader.setController(verrespuesta);
			Parent rootLogin = loader.load();
			Stage stage = new Stage();
			stage.getIcons().add(new Image("/Img/lifeplusplus.png"));
			stage.setTitle("Life++");
			stage.setScene(new Scene(rootLogin));
			verrespuesta.getBotonEnviar().setVisible(false);
			verrespuesta.BloquearLabels();
			conexion = conexionesBBDD.conectar();
			Solicitud ayudaelegida = new Solicitud(null, null);
			try {
	            preparedStatement = conexion.prepareStatement("SELECT * FROM Ayudas");        
	            preparedStatement.setString(1, this.dni_);
	            
	            resultSet = preparedStatement.executeQuery();
	            
	            ayudaelegida = new Solicitud(resultSet.getString("IDayuda"), resultSet.getString("DniUsuario"));
  
	            conexion.close();
	        } catch (Exception e2) {
	        	System.out.println(e2);
	        }

			stage.setMinHeight(600);
			stage.setMinWidth(600);
			Stage s_login = (Stage) BtnCerrarSesion_.getScene().getWindow();
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