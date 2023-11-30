import 'package:flutter/material.dart';
import '../features/weather_page.dart';
import '../repositories/weather_repository.dart';

class MyApp extends StatelessWidget {
  final WeatherRepository weatherRepository;

  const MyApp({super.key, required this.weatherRepository});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Weather App',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.blue),
        useMaterial3: true,
      ),
      home: WeatherPage(weatherRepository: weatherRepository),
    );
  }
}