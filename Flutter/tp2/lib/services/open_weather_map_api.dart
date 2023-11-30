import 'package:http/http.dart' as http;
import 'dart:convert';
import 'weather_api.dart';
import 'package:tp2/models/weather_data.dart';

class OpenWeatherMapApi implements WeatherApi {
  final String apiKey;

  OpenWeatherMapApi(this.apiKey);

  @override
  Future<WeatherData> fetchWeather(String city) async {
    final response = await http.get(
      Uri.parse('https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey&units=metric'),
    );

    if (response.statusCode == 200) {
      return WeatherData.fromJson(json.decode(response.body));
    } else {
      throw Exception('Failed to load weather data');
    }
  }
}
