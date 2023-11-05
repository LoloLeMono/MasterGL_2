// #define WIFI_SSID "Bbox-46D5F041"
// #define WIFI_PASSWORD "sdS7RDTR33awhGKHAT"

#include <WiFi.h>
#include <WebServer.h>
#include <ArduinoJson.h>

#define WIFI_SSID "Bbox-46D5F041"
#define WIFI_PASSWORD "sdS7RDTR33awhGKHAT"

WebServer server(80);
const int LED_PIN = 26;
bool ledState = false;

void sendJson() {
  StaticJsonDocument<200> doc;
  doc["LED"] = ledState ? "ON" : "OFF";

  String jsonStr;
  serializeJson(doc, jsonStr);

  server.send(200, "application/json", jsonStr);
}

void handlePOST() {
  String request = server.arg("plain");
  DynamicJsonDocument doc(200);
  DeserializationError error = deserializeJson(doc, request);

  if (error) {
    server.send(400, "application/json", "Bad Request");
  } else {
    ledState = doc["LED"] == "ON";
    digitalWrite(LED_PIN, ledState ? HIGH : LOW);
    sendJson();
  }
}

void setup() {
  Serial.begin(115200);
  pinMode(LED_PIN, OUTPUT);

  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  while (WiFi.status() != WL_CONNECTED) {
    delay(100);
    Serial.print(".");
  }
  Serial.println(" Connected!");
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());

  server.on("/json", HTTP_GET, sendJson);
  server.on("/json", HTTP_POST, handlePOST);

  server.begin();
  Serial.println("HTTP server started");
}

void loop() {
  server.handleClient();
}