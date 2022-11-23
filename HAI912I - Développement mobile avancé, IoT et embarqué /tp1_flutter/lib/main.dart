// importation du paquetage pour utiliser Material Design
import 'package:flutter/material.dart';

void main() => runApp(MyApp()); // point d'entrée
// Le widget racine de notre application
class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return MaterialApp( // une application utilisant Material Design
        title: 'My First Flutter App',
        theme: ThemeData(
          colorScheme: ColorScheme.fromSwatch(primarySwatch: Colors.blueGrey),
          textTheme: const TextTheme(bodyText2: TextStyle(color: Colors.white)),
        ), // données relatives au thème choisi
        home: const ProfileHomePage(), // le widget de la page d'accueil
    );
  }
}
// Le widget de notre page d'accueil
class ProfileHomePage extends StatelessWidget {
  const ProfileHomePage({Key? key}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
          title: const Center(
            child: Text('Profile'),
          ),

          centerTitle: false,
      ),
      body: BodyApp(),
    );
  }
/*  Container _getCard() {}
  Container _getAvatar() {}*/
}
 
class BodyApp extends StatelessWidget {
  BodyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Column(
          children: <Widget>[
            const SizedBox(
              height: 50,
            ),
            const ProfileImg(),
            const MyDividerWidget(),
            Row(
              children: <Widget>[
                Icon(
                  Icons.person,
                  color: Theme.of(context).colorScheme.primary,
                  size: 30.0,
                ),
                const SizedBox(
                  width: 50,
                ),
                const BodyCard(strToPrint: 'Gaetan Romero'),
              ],
            ),
            Row(
              children: <Widget>[
                Icon(
                  Icons.email,
                  color: Theme.of(context).colorScheme.primary,
                  size: 30.0,
                ),
                const SizedBox(
                  width: 50,
                ),
                const BodyCard(strToPrint: 'gaetan.romero@etu.umontpellier.fr'),
              ],
            ),
            const SizedBox(
              height: 50,
            ),
            ElevatedButton(
              style: ElevatedButton.styleFrom(
                elevation: 3,
                shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(32.0)),
                minimumSize: const Size(200, 60), //////// HERE
                textStyle: const TextStyle(fontSize: 20),
              ),
              onPressed: () {},
              child: const Text('Retour'),
            ),
          ]
      ),
    );
  }
}


class BodyCard extends StatelessWidget {
  final String? strToPrint;
  const BodyCard({Key? key, required this.strToPrint}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Card(
        color: const Color(0x59c6e5c7),
        shape: const RoundedRectangleBorder(
          borderRadius: BorderRadius.all(Radius.circular(12)),
        ),
        child: SizedBox(
          width: 300,
          height: 100,
          child: Center(child: Text(
            strToPrint!,
            style: const TextStyle(fontSize: 18),
          )),
        ),
      ),
    );
  }
}

class ProfileImg extends StatelessWidget {
  const ProfileImg({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return const CircleAvatar(
      backgroundImage: AssetImage('assets/images/perceval.png'),
      radius: 100,
    );
  }
}

class MyDividerWidget extends StatelessWidget {
  const MyDividerWidget({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return const Divider(
      height: 10,
      thickness: 2,
      indent: 20,
      endIndent: 20,
    );
  }
}