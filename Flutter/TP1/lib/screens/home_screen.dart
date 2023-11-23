import 'package:flutter/material.dart';
import '../widgets/profile_card.dart';

class ProfileHomePage extends StatelessWidget {
  const ProfileHomePage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Profile Home Page"),
        centerTitle: true,
      ),
      body: Container(
        alignment: Alignment.center,
        child: ProfileCard(),
      ),
    );
  }
}
