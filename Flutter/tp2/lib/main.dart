import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Weather App',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.blue),
        useMaterial3: true,
      ),
      home: const WeatherPage(),
    );
  }
}

class WeatherPage extends StatelessWidget {
  const WeatherPage({super.key});

  @override
  Widget build(BuildContext context) {
    // Remplacer ces données par les données réelles de l'API météo.
    final String city = "Chimoio, MZ";
    final String date = "Saturday, Dec 14, 2019";
    final String temperature = "73°F";
    final String weather = "FEW CLOUDS";
    final String wind = "1.9 mi/h";
    final String humidity = "76 %";
    final String tempMin = "73°F";

    return Scaffold(
      appBar: AppBar(
        title: const Text('Weather App'),
        backgroundColor: Colors.white,
        actions: [
          IconButton(
            icon: const Icon(Icons.search, color: Colors.grey),
            onPressed: () {
              // Ajouter la logique de recherche de ville ici.
            },
          ),
        ],
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.start,
          children: <Widget>[
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: TextField(
                decoration: InputDecoration(
                  hintText: 'Enter City Name',
                  prefixIcon: const Icon(Icons.search),
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(10),
                    borderSide: BorderSide(color: Colors.blue.shade300),
                  ),
                ),
              ),
            ),
            Text(
              city,
              style: TextStyle(
                fontSize: 24.0,
                fontWeight: FontWeight.bold,
              ),
            ),
            Text(
              date,
              style: TextStyle(
                color: Colors.grey,
              ),
            ),
            Spacer(),
            Icon(
              Icons.cloud, // Changer l'icône en fonction des données météo.
              size: 100,
              color: Colors.pink,
            ),
            Text(
              temperature,
              style: TextStyle(
                fontSize: 60.0,
                fontWeight: FontWeight.bold,
              ),
            ),
            Text(
              weather,
              style: TextStyle(
                color: Colors.grey,
              ),
            ),
            Spacer(),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                Column(
                  children: [
                    Icon(Icons.wind_power, color: Colors.blue),
                    Text(wind),
                  ],
                ),
                Column(
                  children: [
                    Icon(Icons.water_drop, color: Colors.blue),
                    Text(humidity),
                  ],
                ),
                Column(
                  children: [
                    Icon(Icons.thermostat, color: Colors.blue),
                    Text(tempMin),
                  ],
                ),
              ],
            ),
            Spacer(),
          ],
        ),
      ),
    );
  }
}