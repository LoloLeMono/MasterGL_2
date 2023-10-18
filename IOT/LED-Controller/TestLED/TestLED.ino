#include <Button2.h>

const int ledPin = 33; // Le numéro de broche du LED
bool isClose = false;
Button2 btn(BUTTON_1);

void setup()
{
  pinMode(ledPin, OUTPUT); // Configure la broche comme une sortie
}

void loop()
{
  if (btnCick)
  {
    if (isClose)
    {
      digitalWrite(ledPin, LOW); // Éteint la LED
    } else
    {
      digitalWrite(ledPin, HIGH); // Allume la LED
    }
    isClose = !isClose;
  }

  btn.loop();
  delay(10); // Attend 10 m/s
}

void button_init()
{
  btn.setPressedHandler([](Button2 & b)
  {
    btnCick = true;
  });
}

void setup()
{
  pinMode(ADC_LED, OUTPUT); // Configure la broche comme une sortie
  digitalWrite(ADC_LED, HIGH); // Allume la LED

  button_init();
}