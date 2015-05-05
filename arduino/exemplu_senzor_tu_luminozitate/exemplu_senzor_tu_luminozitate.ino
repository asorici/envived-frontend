#include <DHT11.h>
int pin=4;
DHT11 dht11(pin); 
int lightPin = 0;
int ledPin=11;
void setup()
{
  pinMode(ledPin,OUTPUT);
   Serial.begin(9600);
  while (!Serial) {
      ; // wait for serial port to connect. Needed for Leonardo only
    }
}

void loop()
{
  int err;
  analogWrite(ledPin, analogRead(lightPin)/4);
  float temp, humi;
  if((err=dht11.read(humi, temp))==0)
  {
    Serial.print("temperature:");
    Serial.print(temp);
    Serial.print(" humidity:");
    Serial.print(humi);
    Serial.println();
    Serial.print(" Luminozitate:");
/
    Serial.println();
  }
  else
  {
    Serial.println();
    Serial.print("Error No :");
    Serial.print(err);
    Serial.println();    
  }
  delay(DHT11_RETRY_DELAY); //delay for reread
}



