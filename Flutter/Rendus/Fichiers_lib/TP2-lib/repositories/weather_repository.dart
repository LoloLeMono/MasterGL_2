import 'package:tp2/services/weather_api.dart';
import '../models/weather_data.dart';

class WeatherRepository {
  final WeatherApi api;

  WeatherRepository(this.api);

  Future<WeatherData> getWeather(String city) {
    return api.fetchWeather(city);
  }
}
