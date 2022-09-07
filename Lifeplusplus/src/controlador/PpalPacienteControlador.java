package controlador;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Locale;
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
import com.jfoenix.controls.JFXTextField;

import application.Paciente;
import bbdd.conexionesBBDD;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/*
 * CONTROLADOR PANTALLA PRINCIPAL PACIENTE
 */
public class PpalPacienteControlador implements SerialPortEventListener {

	@FXML
	private MenuButton BotonMenu;
	@FXML
	private JFXButton BtnCerrarSesion, ModificarPerfil;
	@FXML
	private JFXButton certificadoCovidBtn, PedirAyudaBtn, RespuestaAyuda, PedirAyuda;
	@FXML
	private JFXTextField txtDniLogin;
	@FXML
	private ResourceBundle resources;
	@FXML
	private Label txtNombre, txtCorreo, txtDni, txtFechaNacimiento, txtPulsamin, txtOx;
	@FXML
	private URL location;
	@FXML
	protected GoogleMapView mapView;
	
	@FXML
	protected Slider slideOxigeno, slidePulso;

	/*
	 * Instanciar servicios
	 */

	private Paciente yo;
	private GoogleMap map;

	private DecimalFormat formatter = new DecimalFormat("###.00000");

	
	//COPIAR ESTO PARA QUE FUNCIONE LA SQL
	String sql;
	Connection conexion = null;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;
	
	public SerialPort serialPort;
	/** The port we’re normally going to use. */
	private static final String PORT_NAMES[] = {
	"/dev/tty.usbserial-A9007UX1", // Mac OS X
	"/dev/ttyUSB0", // Linux
	"COM3", // Windows
	};

	public static BufferedReader input;
	public static OutputStream output;
	/** Milliseconds to block while waiting for port open */
	public static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	public static final int DATA_RATE = 115200;
	
	///Hilos
	Runnable actualizarPulsometro = new Runnable() {
		public void run() {
			try {
				Platform.runLater(() -> {
					
					conexion = conexionesBBDD.conectar();
					try {
			            preparedStatement = conexion.prepareStatement("SELECT ID, heart_rate, bpm FROM datos_pulsometro ORDER BY ID DESC");
			            
			            preparedStatement.setString(1,"");
			            resultSet = preparedStatement.executeQuery();
			                        
			            if (resultSet.next()) {
			            	txtPulsamin.setText(resultSet.getString("heart_rate"));
			            	txtOx.setText(resultSet.getString("bpm") + " %");
			            	
			            	//Barras debajo de datos dinamicos
			            	slidePulso.setValue(Integer.parseInt(resultSet.getString("heart_rate")));
			            	slideOxigeno.setValue(Integer.parseInt(resultSet.getString("bpm")));

			            }else {
			            	System.out.println("ERROR");
			            }
			            
			            

			        } catch (Exception e2) {
			        	System.out.println(e2);
			        } finally {
			        	try {
							conexion.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
			        }
					System.out.println("Hilo funcionando");
					
				});
			} catch (Exception e) {
				System.out.println("Error en los hilos del bloqueo del login");
				// e.printStackTrace();
			}
		}
	};

	ScheduledExecutorService executor;

	
	String dni_;
	
	public PpalPacienteControlador(String dni_) {
		this.dni_ = dni_;
	}
	
	/*
	 * Metodo para mostrar datos del paciente al entrar a la pantalla principal
	 */
	
	@FXML
	public void mostrarDatos() {
		conexion = conexionesBBDD.conectar();
		try {
            preparedStatement = conexion.prepareStatement("SELECT PACIENTES.Nombre, PACIENTES.Correo, PACIENTES.DNI, PACIENTES.FechaNacim FROM PACIENTES WHERE DNI = ?");
            
            preparedStatement.setString(1, this.dni_);
            
            resultSet = preparedStatement.executeQuery();
                        
            if (resultSet.next()) {
            	txtNombre.setText(resultSet.getString("Nombre"));
            	txtCorreo.setText(resultSet.getString("Correo"));
            	txtDni.setText(resultSet.getString("DNI"));
            	txtFechaNacimiento.setText(resultSet.getString("FechaNacim"));

            }else {
            	System.out.println("ERROR");
            }
            
            conexion.close();

        } catch (Exception e2) {
        	System.out.println(e2);
        }
	}

	/*
	 * Metodo para editar datos como paciente
	 * 
	 * @exception IOException
	 */
	public void EditarUsuario(ActionEvent event) throws IOException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/EditarUsuario.fxml"));
			EditarUsuarioControlador editarUsuarioControlador = new EditarUsuarioControlador(dni_);
			loader.setController(editarUsuarioControlador);
			Parent rootLogin = loader.load();
			Stage stage = new Stage();
			stage.getIcons().add(new Image("/Img/lifeplusplus.png"));
			stage.setTitle("Life++");
			stage.setScene(new Scene(rootLogin));
			editarUsuarioControlador.setPacienteEditable(dni_);
			editarUsuarioControlador.setPacienteRegreso(yo);
			stage.setMinHeight(600);
			stage.setMinWidth(600);
			Stage s_login = (Stage) BtnCerrarSesion.getScene().getWindow();
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
	 * Metodo para solicitar ayuda como paciente
	 * 
	 * @exception IOException
	 */
	public void PedirAyuda(ActionEvent event) throws IOException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/SolicitarAyudaPaciente.fxml"));
			SolicitarAyuda solicitarAyuda = new SolicitarAyuda(dni_, 1);
			loader.setController(solicitarAyuda);
			Parent rootLogin = loader.load();
			Stage stage = new Stage();
			stage.getIcons().add(new Image("/Img/lifeplusplus.png"));
			stage.setTitle("Life++");
			stage.setScene(new Scene(rootLogin));
			solicitarAyuda.getSolucionArea().setVisible(false);
			stage.setMinHeight(600);
			stage.setMinWidth(600);
			Stage s_login = (Stage) BtnCerrarSesion.getScene().getWindow();
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
	 * Metodo para cerrar sesion y volver a pantalla de login
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
			Stage s_login = (Stage) BtnCerrarSesion.getScene().getWindow();
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
	 * Metodo para subir el certificado covid como pdf
	 */
	@FXML
	public void subirCertificado(ActionEvent event) {
		conexion = conexionesBBDD.conectar();
		FileInputStream fis;
		try {
			FileChooser fc = new FileChooser();
			fc.getExtensionFilters().add(new ExtensionFilter("PDF Files", "*.pdf"));
			File f = fc.showOpenDialog(null);
			String origen = f.getAbsolutePath();
			String destino = System.getProperty("user.dir") + "//certificados//" + this.dni_+ ".pdf";
			copyFile(origen, destino);
			File image = new File(destino);
			fis = new FileInputStream(image);
			
			preparedStatement = conexion.prepareStatement("UPDATE PACIENTES SET PasaporteCovid = ? WHERE Dni = ?");
			preparedStatement.setString(2, this.dni_);
			preparedStatement.setBinaryStream(1, (InputStream)fis, (int)(image.length()));
			int s = preparedStatement.executeUpdate();
            if(s>0) {
                System.out.println("Uploaded successfully !");
            }
            else {
                System.out.println("unsucessfull to upload image.");
            } 
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("No se ha podido cargar el archivo");
		} finally {
			try {
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

	/*
	 * Metodo para descargar el historial medico
	 * 
	 * @exception IOException
	 */
	@FXML
	public void Descargar(ActionEvent event) throws IOException {
		conexion = conexionesBBDD.conectar();
	    InputStream input = null;
	    FileOutputStream output = null;
	    try {
	        preparedStatement = conexion.prepareStatement("SELECT PasaporteCovid FROM PACIENTES WHERE DNI=?");
	        preparedStatement.setString(1, this.dni_);
	        ResultSet rs = preparedStatement.executeQuery();
	        String destino = System.getProperty("user.dir") + "//certificados//" + this.dni_+ ".pdf";
	        File file = new File(destino);
	        output = new FileOutputStream(file);
	
	        if (rs.next()) {
	            input = rs.getBinaryStream("PasaporteCovid");
	            System.out.println("Leyendo archivo desde la base de datos...");
	            byte[] buffer = new byte[1024];
	            while (input.read(buffer) > 0) {
	                output.write(buffer);
	            }
	            System.out.println("> Archivo guardado en : " + file.getAbsolutePath());
	            try {
	                File path = new File (file.getAbsolutePath());
	                Desktop.getDesktop().open(path);
	           }catch (IOException ex) {
	                ex.printStackTrace();
	           }
	        }
	        
	    } catch (SQLException | IOException ex) {
	        System.err.println(ex.getMessage());
	    } finally {
	        try {
	            if (input != null) {
	                input.close();
	            }
	            if (output != null) {
	                output.close();
	            }
	            if (preparedStatement != null) {
	            	preparedStatement.close();
	            }
	        } catch (IOException | SQLException ex) {
	            System.err.println(ex.getMessage());
	        }
	    }
	}

	/*
	 * Metodo para escribir lo que hay en el fichero de origen en el de destino a
	 * traves de la ruta
	 * 
	 * @param fromFile
	 * 
	 * @param toFile
	 * 
	 * @return boolean
	 */
	private boolean copyFile(String fromFile, String toFile) {
		File origin = new File(fromFile);
		File destination = new File(toFile);
		if (origin.exists()) {
			try {
				InputStream in = new FileInputStream(origin);
				OutputStream out = new FileOutputStream(destination);
				// We use a buffer for the copy (Usamos un buffer para la copia).
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
				return true;
			} catch (IOException ioe) {
				ioe.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}

	@FXML
	public
	void initialize() {
		slidePulso.setMin(0);
		slidePulso.setMax(160);
		slideOxigeno.setMin(50);
		slideOxigeno.setMax(100);
		executor = Executors.newScheduledThreadPool(10);
		executor.scheduleAtFixedRate(actualizarPulsometro, 1, 5, TimeUnit.SECONDS);
		
		mapView.setKey("AIzaSyAjkcplqD8c3KVD_KvYQzoV6uiaeXN0Veg");
		mapView.addMapInitializedListener(() -> configureMap());
		
		//DatosPulsometro();
		mostrarDatos();
		
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		//First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
		CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
		for (String portName : PORT_NAMES) {
		if (currPortId.getName().equals(portName)) {
		portId = currPortId;
		break;
		}
		}
		}
		if (portId == null) {
		System.out.println("Could not find COM port.");
		return;
		}

		try {
		// open serial port, and use class name for the appName.
		serialPort = (SerialPort) portId.open(this.getClass().getName(),
		TIME_OUT);

		// set port parameters
		serialPort.setSerialPortParams(DATA_RATE,
		SerialPort.DATABITS_8,
		SerialPort.STOPBITS_1,
		SerialPort.PARITY_NONE);

		// open the streams
		input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
		output = serialPort.getOutputStream();
		char ch = 1;
		output.write(ch);

		// add event listeners
		serialPort.addEventListener(this);
		serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
		System.err.println(e.toString());
		}
		
		//PpalPacienteControlador main = new PpalPacienteControlador();
		//main.initialize();
		Thread t=new Thread() {
		public void run() {
		//the following line will keep this app alive for 1000 seconds,
		//waiting for events to occur and responding to them (printing incoming messages to console).
		try {
			Thread.sleep(10000);
		writeData("2");} catch (InterruptedException ie) {}
		}
		};
		t.start();
		System.out.println("Started");
		t.stop();
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
	 * Configuracion mapa
	 */
	protected void configureMap() {
		mapView.setKey("AIzaSyAjkcplqD8c3KVD_KvYQzoV6uiaeXN0Veg");
		
		MapOptions mapOptions = new MapOptions();

		mapOptions.center(new LatLong(Latitud(), Longitud())).mapType(MapTypeIdEnum.ROADMAP).zoom(15);
		map = mapView.createMap(mapOptions, false);

		map.addMouseEventHandler(UIEventType.click, (GMapMouseEvent event) -> {
			LatLong latLong = event.getLatLong();
		});
		
	}

	/*
	 * Metodo para ver respuesta de solicitud del developer
	 */
	@FXML
	void verRespuesta(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/SolicitarAyudaPaciente.fxml"));
			VerRespuesta verrespuesta = new VerRespuesta(dni_, 1);
			loader.setController(verrespuesta);
			Parent rootLogin = loader.load();
			Stage stage = new Stage();
			stage.getIcons().add(new Image("/Img/lifeplusplus.png"));
			stage.setTitle("Life++");
			stage.setScene(new Scene(rootLogin));
			verrespuesta.getBotonEnviar().setVisible(false);
			stage.setMinHeight(600);
			stage.setMinWidth(600);
			Stage s_login = (Stage) BtnCerrarSesion.getScene().getWindow();
			verrespuesta.BloquearLabels();
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
	
	public synchronized void close() {
		if (serialPort != null) {
		serialPort.removeEventListener();
		serialPort.close();
		}
		}

		public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
		try {
		String inputLine=input.readLine();
		System.out.println(inputLine);
		conexion = conexionesBBDD.conectar();
		try {
            preparedStatement = conexion.prepareStatement("SELECT * FROM PACIENTES WHERE uid = ?");
            
            preparedStatement.setString(1, inputLine);
            
            resultSet = preparedStatement.executeQuery();
                        
            if (resultSet.next()) {
            	String aux = resultSet.getString("Dni");
            	System.out.println(aux);
            	Descargar(null);
            }else {
            	System.out.println("ERROR");
            }
            
            conexion.close();

        } catch (Exception e2) {
        	System.out.println(e2);
        }
		} catch (Exception e) {
		System.err.println(e.toString());
		}
		}

		}

		public static synchronized void writeData(String data) {
		System.out.println("Sent: " + data);
		try {
		output.write(data.getBytes());
		} catch (Exception e) {
		System.out.println("could not write to port");
		}
		}
}