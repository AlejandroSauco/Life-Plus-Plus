#include <Wire.h>
#include <TinyGPS++.h>
#include "Credentials.h"
#include <MySQL_Generic.h>

#define RXD2 16
#define TXD2 17
#define MYSQL_DEBUG_PORT      Serial
#define MYSQL_LOGLEVEL      1
#define USING_HOST_NAME     false



HardwareSerial neogps(1);

TinyGPSPlus gps;



#if USING_HOST_NAME
  // Optional using hostname, and Ethernet built-in DNS lookup
  char server[] = "your_account.ddns.net"; // change to your server's hostname/URL
#else
  IPAddress server(195, 235, 211, 197);
#endif
double lat, lng;
uint16_t server_port = 3306;    //3306;

char default_database[] = "prilifepp";           //"test_arduino";
char default_table[]    = "datos_gps";          //"test_arduino";


String default_value    = "Hello, Arduino!"; 

// Sample query
String INSERT_SQL = "INSERT INTO prilifepp.datos_gps (latitud, longitud) VALUES ('"+String(lat, 2)+"', '"+lng+"') ";

MySQL_Connection conn((Client *)&client);

MySQL_Query *query_mem;







void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);
  //Begin serial communication Arduino IDE (Serial Monitor)

  //Begin serial communication Neo6mGPS
  neogps.begin(9600, SERIAL_8N1, RXD2, TXD2);





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






void loop() {
  // put your main code here, to run repeatedly:

    boolean newData = false;
    for (unsigned long start = millis(); millis() - start < 1000;)
    {
      while (neogps.available())
      {
        if (gps.encode(neogps.read()))
        {
          newData = true;
        }
      }
    }
  
    //If newData is true
    if(newData == true)
    {
      newData = false;
      Serial.println(gps.location.lat(), 6); // Latitude in degrees (double)
      Serial.println(gps.location.lng(), 6); // Longitude in degrees (double)
      lat=gps.location.lat();
      lng=gps.location.lng();

      INSERT_SQL = "INSERT INTO prilifepp.datos_gps (Latitud, Longitud) VALUES ('"+String(lat, 2)+"', '"+lng+"') ";

    if ((lat!=0 || lng!=0) && (conn.connected()||(conn.connectNonBlocking(server, server_port, user, password) != RESULT_FAIL)))
  {
    runInsert();
    delay(50000);
   
    
  } 
  else 
  {
    MYSQL_DISPLAY("\nConnect failed. Trying again on next iteration.");
  }
      /*Serial.print(gps.location.rawLat().negative ? "-" : "+");
      Serial.println(gps.location.rawLat().deg); // Raw latitude in whole degrees
      Serial.println(gps.location.rawLat().billionths);// ... and billionths (u16/u32)
      Serial.print(gps.location.rawLng().negative ? "-" : "+");
      Serial.println(gps.location.rawLng().deg); // Raw longitude in whole degrees
      Serial.println(gps.location.rawLng().billionths);// ... and billionths (u16/u32)
      Serial.println(gps.date.value()); // Raw date in DDMMYY format (u32)
      Serial.println(gps.date.year()); // Year (2000+) (u16)
      Serial.println(gps.date.month()); // Month (1-12) (u8)
      Serial.println(gps.date.day()); // Day (1-31) (u8)
      Serial.println(gps.time.value()); // Raw time in HHMMSSCC format (u32)
      Serial.println(gps.time.hour()); // Hour (0-23) (u8)
      Serial.println(gps.time.minute()); // Minute (0-59) (u8)
      Serial.println(gps.time.second()); // Second (0-59) (u8)
      Serial.println(gps.time.centisecond()); // 100ths of a second (0-99) (u8)
      Serial.println(gps.speed.value()); // Raw speed in 100ths of a knot (i32)
      Serial.println(gps.speed.knots()); // Speed in knots (double)
      Serial.println(gps.speed.mph()); // Speed in miles per hour (double)
      Serial.println(gps.speed.mps()); // Speed in meters per second (double)
      Serial.println(gps.speed.kmph()); // Speed in kilometers per hour (double)
      Serial.println(gps.course.value()); // Raw course in 100ths of a degree (i32)
      Serial.println(gps.course.deg()); // Course in degrees (double)
      Serial.println(gps.altitude.value()); // Raw altitude in centimeters (i32)
      Serial.println(gps.altitude.meters()); // Altitude in meters (double)
      Serial.println(gps.altitude.miles()); // Altitude in miles (double)
      Serial.println(gps.altitude.kilometers()); // Altitude in kilometers (double)
      Serial.println(gps.altitude.feet()); // Altitude in feet (double)
      Serial.println(gps.satellites.value()); // Number of satellites in use (u32)
      Serial.println(gps.hdop.value()); // Horizontal Dim. of Precision (100ths-i32)*/
    }    
}
