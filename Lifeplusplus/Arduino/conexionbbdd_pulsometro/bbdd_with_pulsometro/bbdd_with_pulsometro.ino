#include <Wire.h>
#include "MAX30100_PulseOximeter.h" //library initialization
 
#define REPORTING_PERIOD_MS     1000 //update frequency 1000ms
PulseOximeter pox;
uint32_t tsLastReport = 0;
 
void onBeatDetected()
{
    Serial.println("Beat!");
}
///////////////////////////////////////////////////////////////////////
#if ! (ESP8266 || ESP32 )
  #error This code is intended to run on the ESP8266/ESP32 platform! Please check your Tools->Board setting
#endif

#include "Credentials.h"

#define MYSQL_DEBUG_PORT      Serial

// Debug Level from 0 to 4
#define MYSQL_LOGLEVEL      1

#include <MySQL_Generic.h>

#define USING_HOST_NAME     false

#if USING_HOST_NAME
  // Optional using hostname, and Ethernet built-in DNS lookup
  char server[] = "your_account.ddns.net"; // change to your server's hostname/URL
#else
  IPAddress server(195, 235, 211, 197);
#endif

uint16_t server_port = 3306;    //3306;

char default_database[] = "prilofepp";           //"test_arduino";
char default_table[]    = "hello_arduino";          //"test_arduino";

String default_value    = "Hello, Arduino!"; 

// Sample query
String INSERT_SQL = "INSERT INTO prilifepp.datos_pulsometro (heart_rate, bpm) VALUES ('"+String(pox.getHeartRate(), 2)+"', '"+pox.getSpO2()+"') ";

MySQL_Connection conn((Client *)&client);

MySQL_Query *query_mem;


void setup()
{    
  Serial.begin(115200);
  while (!Serial);

  MYSQL_DISPLAY1("\nStarting Basic_Insert_ESP on", ARDUINO_BOARD);
  MYSQL_DISPLAY(MYSQL_MARIADB_GENERIC_VERSION);

  // Begin WiFi section
  MYSQL_DISPLAY1("Connecting to", ssid);
  
  WiFi.begin(ssid, pass);
  
  while (WiFi.status() != WL_CONNECTED) 
  {
    delay(500);
    MYSQL_DISPLAY0(".");
  }

  // print out info about the connection:
  MYSQL_DISPLAY1("Connected to network. My IP address is:", WiFi.localIP());

  MYSQL_DISPLAY3("Connecting to SQL Server @", server, ", Port =", server_port);
  MYSQL_DISPLAY5("User =", user, ", PW =", password, ", DB =", default_database);
  //Inicia el pulsoximetro

  
  Serial.print("Initializing pulse oximeter..");
 
    // Initialize the PulseOximeter instance
    // Failures are generally due to an improper I2C wiring, missing power supply
    // or wrong target chip
    if (!pox.begin()) {
        Serial.println("FAILED");
        for(;;);
    } else {
        Serial.println("SUCCESS");
    }
     pox.setIRLedCurrent(MAX30100_LED_CURR_7_6MA);
 
    // Register a callback for the beat detection
    pox.setOnBeatDetectedCallback(onBeatDetected); 
}


void runInsert()
{
  // Initiate the query class instance
  MySQL_Query query_mem = MySQL_Query(&conn);

  if (conn.connected())
  {
    MYSQL_DISPLAY(INSERT_SQL);
    
    // Execute the query
    // KH, check if valid before fetching
    if ( !query_mem.execute(INSERT_SQL.c_str()) )
    {
      MYSQL_DISPLAY("Insert error");
    }
    else
    {
      MYSQL_DISPLAY("Data Inserted.");
    }
  }
  else
  {
    MYSQL_DISPLAY("Disconnected from Server. Can't insert.");
  }
}

void loop()
{
   int id = 1;
   // Make sure to call update as fast as possible
    pox.update();
    if (millis() - tsLastReport > REPORTING_PERIOD_MS) {
        float heart = pox.getHeartRate();
        int o2 = pox.getSpO2();
        Serial.print("id");
        Serial.print("Heart rate:");
        Serial.print(heart);
        Serial.print("bpm / SpO2:");
        Serial.print(o2);
        Serial.println("%");  

        tsLastReport = millis();

        MYSQL_DISPLAY("Connecting...");
        INSERT_SQL = "INSERT INTO prilifepp.datos_pulsometro (heart_rate, bpm) VALUES ('"+String(heart, 2)+"', '"+o2+"') ";
        
        //if (conn.connect(server, server_port, user, password))
        if ((heart!=0 || o2!=0) && (conn.connected() || conn.connectNonBlocking(server, server_port, user, password) != RESULT_FAIL))
        {
          runInsert();
        } 
        else 
        {
          MYSQL_DISPLAY("\nConnect failed. Trying again on next iteration.");
        }
    }  
  }
