// Le widget de notre page d'accueil
import 'package:flutter/material.dart';

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
      body: const BodyApp(),
    );
  }
/*  Container _getCard() {}
  Container _getAvatar() {}*/
}

class BodyApp extends StatelessWidget {
  const BodyApp({Key? key}) : super(key: key);

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
            buildRow(context, "Gaetan Romero"),
            buildRow(context, 'gaetan.romero@etu.umontpellier.fr'),
            const SizedBox(
              height: 50,
            ),
            buildElevatedButton(context),
          ]
      ),
    );
  }

  ElevatedButton buildElevatedButton(BuildContext context) {
    return ElevatedButton(
            style: ElevatedButton.styleFrom(
              elevation: 3,
              shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(32.0)),
              minimumSize: const Size(200, 60), //////// HERE
              textStyle: const TextStyle(fontSize: 20),
            ),
            onPressed: () {
              // Navigator.pushNamed(context, '/');
              Navigator.pop(context);
            },
            child: const Text('Retour'),
          );
  }

  Row buildRow(BuildContext context, String str) {
    return Row(
            children: <Widget>[
              Icon(
                Icons.person,
                color: Theme.of(context).colorScheme.primary,
                size: 30.0,
              ),
              const SizedBox(
                width: 50,
              ),
              BodyCard(strToPrint: str),
            ],
          );
  }
}


class BodyCard extends StatelessWidget {
  String? strToPrint;
  BodyCard({Key? key, required this.strToPrint}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Center(
      child: buildCard(),
    );
  }

  Card buildCard() {
    return Card(
      color: const Color(0x59c6e5c7),
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.all(Radius.circular(12)),
      ),
      child: buildSizedBox(),
    );
  }

  SizedBox buildSizedBox() {
    return SizedBox(
        width: 300,
        height: 100,
        child: Center(child: Text(
          strToPrint!,
          style: const TextStyle(fontSize: 18),
        )),
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