// importation du paquetage pour utiliser Material Design
import 'package:flutter/material.dart';
import 'package:tp1_flutter/profile.dart';
import 'package:tp1_flutter/quiz.dart';

void main() => runApp(const MyApp()); // point d'entrée
// Le widget racine de notre application
class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return MaterialApp( // une application utilisant Material Design
        theme: ThemeData(
          colorScheme: ColorScheme.fromSwatch(primarySwatch: Colors.blueGrey),
          textTheme: const TextTheme(bodyText2: TextStyle(color: Colors.white)),
        ),
        initialRoute: '/',
        routes: {
          // When navigating to the "/" route, build the Home widget.
          '/': (context) => const HomePage(),
          // When navigating to the "/Profile" route, build the ProfileHomePage widget.
          '/Profile': (context) => const ProfileHomePage(),
          // When navigating to the "/Quizz" route, build the QuizzHomePage widget.
          '/Quiz': (context) => const QuizHomePage(),
          // données relatives au thème choisi
        }
    );
  }
}

class HomePage extends StatelessWidget {
  const HomePage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: buildAppBar(),
      body: Center(
        child: Column(
          children: <Widget>[
            buildSizedBox(200),
            buildElevatedButton(context, 'Profile'),
            buildSizedBox(150),
            buildElevatedButton(context, 'Quiz'),
          ],
        )
      ),
    );
  }

  SizedBox buildSizedBox(double? spaceSize) {
    return SizedBox(
      height: spaceSize,
    );
  }

  AppBar buildAppBar() {
    return AppBar(
      automaticallyImplyLeading: false,
      title: const Center(
        child: Text('Home'),
      ),
    );
  }

  ElevatedButton buildElevatedButton(BuildContext context, String buttonName) {
    return ElevatedButton(
        style: buildStyleFrom(),
        onPressed: () {
          Navigator.pushNamed(context, '/$buttonName');
        },
        child: Text(buttonName),
      );
  }

  ButtonStyle buildStyleFrom() {
    return ElevatedButton.styleFrom(
        elevation: 3,
        shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(32.0)),
        minimumSize: const Size(200, 60), //////// HERE
        textStyle: const TextStyle(fontSize: 20),
      );
  }
}