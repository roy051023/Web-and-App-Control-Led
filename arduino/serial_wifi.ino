//connect
String SID = "Awin-Lab";
String PWD = "123698745";
String IP = "140.120.13.180";
String file = "HW5/receive.php";
String swi = "HW5/switch_check.php";

boolean upload = false;//是否要上傳
String str = ""; //http 回覆狀態
int Pin = 2;
int Val = 0;
int LED = 13;

char inString[32]; // string for incoming serial data
int stringPos = 0; // string index counter
boolean startRead = false; // is reading?

void setup() {
    // enable debug serial
    Serial.begin(115200);
    pinMode(LED, OUTPUT);
    digitalWrite(LED,HIGH);
    init_wifi();  //設定Serial,改變模式,連線wifi
}

void loop() {

  Val = analogRead(Pin);

  String resp="",ans="";
  // convert to string
  // TCP connection
  String cmd = "AT+CIPSTART=\"TCP\",\"";
  cmd += IP; //host
  cmd += "\",80";
  Serial.println(cmd);
  delay(2000);

  // prepare GET string
  String getStr = "GET /"+swi;
  getStr +=" HTTP/1.1\r\nHost:"+IP+":80";
  getStr += "\r\n\r\n";

  // send data length
  cmd = "AT+CIPSEND=";
  cmd += String(getStr.length());
  Serial.println(cmd);
  
  if(Serial.find(">")){
    Serial.print(getStr);
    while(Serial.available())
      {    
        // The esp has data so display its output to the serial window
        resp = Serial.readString(); // read the next character.
      } 
    ans = catch_word(resp);
    Serial.println(resp);
    if(ans == "on"){
      Serial.println("switch on");
      digitalWrite(LED,HIGH);
    }else if(ans == "off"){
      Serial.println("switch off");
      digitalWrite(LED,LOW);
    }else{
      Serial.println(resp);
    }
  }
  else{
     Serial.println("AT+CIPCLOSE");
     //init_wifi();
  }

  // 傳送command給esp8266
  if(ans =="on")//uplao
  {
     Serial.println("|---Start Upload---|");
     upload = true;
  }
  else if(ans == "off")
  {
     Serial.println("|---Stop Upload---|");
     upload = false;
  }
  else  {
     Serial.println("no use switch");
     upload = false;
  }
     
   
  // 是否上傳
  if(upload)
  { 
     uploadData();
  }
  
}


/*
 *  Setting Serial
*/
void init_wifi(){
  Serial.println("=======================================");
  Serial.println("|---  Serial Setting  ---|\n");
  sendCommand("AT+RST",5000); // reset module
  sendCommand("AT+CWMODE=1",2000); // configure as access point
  sendCommand("AT+CWJAP=\""+SID+"\",\""+PWD+"\"",5000);
  sendCommand("AT+CIPMUX=0",2000); // configure for single connections
  Serial.println("\n|---  Setting Finish  ---|");
  Serial.println("=======================================");
}

/*
 *  Setting Serial (Send Command)
*/
void sendCommand(String command, const int timeout)
{
    String response = "";    
    Serial.println(command); // send the read character to the Serial   
    long int time = millis();   
    while( (time+timeout) > millis())
    {
      while(Serial.available())
      {    
        // The esp has data so display its output to the serial window 
        response = Serial.readString(); // read the next character.
      }
    } 
    Serial.println(response);   
    delay(100);
}


String catch_word(String pageread){
  //read the page, and capture & return everything between '<' and '>'
  stringPos = 0;
  memset( &inString, 0, 32 ); //clear inString memory
  int i ;
  for(i = 0;i < pageread.length() ; i++){
      char c = pageread.c_str()[i];
      if (c == '^' ) { //'<' is our begining character
        startRead = true; //Ready to start reading the part 
      }else if(startRead){
        if(c != '!'){ //'>' is our ending character
          inString[stringPos] = c;
          stringPos ++;
        }else{
          //got what we need here! We can disconnect now
          startRead = false;
          return inString;
        }
      }
    }  
}
// upload data
void uploadData()
{
  // convert to string
  // TCP connection
  String upload_cmd = "AT+CIPSTART=\"TCP\",\"";
  upload_cmd += IP; //host
  upload_cmd += "\",80";
  Serial.println(upload_cmd);
   
  if(Serial.find("Error")){
    Serial.println("AT+CIPSTART error");
    return;
  }

  // prepare GET string
  String upload_getStr = "GET /"+file+"?value=";
  upload_getStr += String(Val);
  upload_getStr +=" HTTP/1.1\r\nHost:"+IP;
  upload_getStr += "\r\n\r\n";

  // send data length
  upload_cmd = "AT+CIPSEND=";
  upload_cmd += String(upload_getStr.length());
  Serial.println(upload_cmd);

  if(Serial.find(">")){
    Serial.print(upload_getStr);
  }
  else{
    Serial.println("AT+CIPCLOSE");
    // alert user
    Serial.println("AT+CIPCLOSE");
  }
  delay(1000);
}
