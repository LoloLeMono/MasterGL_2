class WeatherData {
  final String city;
  final double temperature;
  final String description;

  WeatherData({
    required this.city,
    required this.temperature,
    required this.description,
  });

  factory WeatherData.fromJson(Map<String, dynamic> json) {
    return WeatherData(
      city: json['name'],
      temperature: json['main']['temp'],
      description: json['weather'][0]['description'],
    );
  }
}
