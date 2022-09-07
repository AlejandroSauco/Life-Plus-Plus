package application;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bbdd.conexionesBBDD;
import javafx.scene.image.Image;
import controlador.LoginControlador;
import controlador.PpalMedicoControlador;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
//import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Login.fxml"));
			primaryStage.getIcons().add(new Image("/Img/lifeplusplus.png"));
			LoginControlador LoginControl = new LoginControlador();
			loader.setController(LoginControl);
			Parent root = loader.load();
			//primaryStage.initStyle(StageStyle.UNDECORATED); BORDERLESS
			primaryStage.setTitle("LIFE++");
			primaryStage.setScene(new Scene(root));
			primaryStage.setMinHeight(600);
			primaryStage.setMinWidth(600);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
	}

}
