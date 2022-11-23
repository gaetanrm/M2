// importation du paquetage pour utiliser Material Design
import 'package:flutter/material.dart';

void main() => runApp(MyApp()); // point d'entrée
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
          // When navigating to the "/second" route, build the Profile widget.
          '/profile': (context) => const ProfileHomePage(),
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
      appBar: AppBar(
        title: const Center(
          child: Text('Home'),
        ),
      ),
      body: Center(
        child: ElevatedButton(
          style: ElevatedButton.styleFrom(
            elevation: 3,
            shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(32.0)),
            minimumSize: const Size(200, 60), //////// HERE
            textStyle: const TextStyle(fontSize: 20),
          ),
          onPressed: () {
            Navigator.pushNamed(context, '/profile');
          },
          child: const Text('Profile'),
        ),
      ),
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
              onPressed: () {
                Navigator.pushNamed(context, '/');
              },
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