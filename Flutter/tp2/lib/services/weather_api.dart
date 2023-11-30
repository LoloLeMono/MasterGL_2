import '../models/weather_data.dart';

abstract class WeatherApi {
  Future<WeatherData> fetchWeather(String city);
}
