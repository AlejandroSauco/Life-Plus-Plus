//package application;
//
//
///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.util.Enumeration;
//
//import controlador.PpalPacienteControlador;
//
//import java.io.IOException;
//
//public class ConexionSerial
//{
//public static BufferedReader input;
//public static OutputStream output;
//
//public static synchronized void writeData(String data) {
//System.out.println("Sent: " + data);
//try {
//output.write(data.getBytes());
//} catch (Exception e) {
//System.out.println("could not write to port");
//}
//}
//
//public static void main(String[] ag)
//{
//
//try
//{
//PpalPacienteControlador obj = new PpalPacienteControlador();
//int c=0;
//obj.initialize();
//input = JavaSerialArduino.input;
//output = JavaSerialArduino.output;
//InputStreamReader Ir = new InputStreamReader(System.in);
//BufferedReader Br = new BufferedReader(Ir);
//
//String inputLine=input.readLine();
//System.out.println(inputLine);
//
//obj.close();
//
//}
//catch(Exception e){}
//
//}
//}