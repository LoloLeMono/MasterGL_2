import 'package:flutter/material.dart';
import 'package:tp2/repositories/weather_repository.dart';
import 'package:tp2/services/open_weather_map_api.dart';
import 'app/Weather_app.dart';

void main() {
  runApp(MyApp(
    weatherRepository: WeatherRepository(
      OpenWeatherMapApi("TOKEN"),
    ),
  ));
}