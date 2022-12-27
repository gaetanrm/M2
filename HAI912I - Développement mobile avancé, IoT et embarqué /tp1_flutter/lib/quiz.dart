import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';

/*class QuizHomePage extends StatelessWidget {
  const QuizHomePage({super.key});


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Center(
          child: Text('Quiz'),
        ),
      ),
      body: const ManageQuiz(),
    );
  }
}*/

class QuizBody extends StatelessWidget {

  final List<Map<String, Object>> questions;
  final List<Map<String, Object>> questionsImage;
  final int questionIndex;
  final Function answerQuestion;

  const QuizBody({
    Key? key,
    required this.questions,
    required this.questionsImage,
    required this.answerQuestion,
    required this.questionIndex,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {

    Object? imageName = questionsImage[questionIndex]['name'];

    return Column(
      children: [

        Image.asset(
            'assets/images/$imageName',
            height: 200,
            width: 200,
          ),

        Question(
          questions[questionIndex]['questionText'].toString(),
        ), //Question

        ...(questions[questionIndex]['answers'] as List<Map<String, Object>>)
            .map((answer) {
          return Center(
              child: Column(
                children: <Widget>[
                  const SizedBox(
                    height: 50,
                  ),
                buildAnswerButton(answer),
                ],
              )
          );
        }).toList()
      ],
    ); //Column
  }

  SizedBox buildAnswerButton(Map<String, Object> answer) {
    return SizedBox(
      child: ElevatedButton(
        style: ElevatedButton.styleFrom(
          elevation: 3,
          shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(32.0)),
              minimumSize: const Size(200, 60), //////// HERE
              textStyle: const TextStyle(fontSize: 20),
            ),
        onPressed: () { answerQuestion(answer['score']); },
        child: Text(answer['text'].toString()),
      ),
    );
  }
}


class QuizHomePage extends StatefulWidget {
  const QuizHomePage({super.key});


  @override
  State<QuizHomePage> createState() {
        return _QuizHomeState();
  }
}

class _QuizHomeState extends State<QuizHomePage> {

  final _questions = const [
    {
      'questionText': 'Q1. Quelle année sommes nous ?',
      'answers': [
        {'text': '1997', 'score': 0},
        {'text': '2002', 'score': 0},
        {'text': '2022', 'score': 1},
        {'text': '3567', 'score': 0},
      ],
    },
    {
      'questionText': 'Q2. Qu\'est ce que Flutter ?',
      'answers': [
        {'text': 'Un kit de développement Android', 'score': 0},
        {'text': 'Un kit de développement IOS', 'score': 0},
        {'text': 'Un kit de développement Web', 'score': 0},
        {
          'text':
          'Un SDK pour créer de magnifiques applications natives IOS, Android, Web et Bureau',
          'score': 1
        },
      ],
    },
    {
      'questionText': ' Q3. Les pizzas à l\'ananas sont elles acceptables ?',
      'answers': [
        {'text': 'Oui', 'score': 0},
        {'text': 'Non', 'score': 1},
      ],
    },
    {
      'questionText': 'Q4. Qui a créé Flutter ?',
      'answers': [
        {'text': 'Lars Bak and Kasper Lund', 'score': 1},
        {'text': 'Brendan Eich', 'score': 0},
        {'text': 'Bjarne Stroustrup', 'score': 0},
        {'text': 'Jeremy Ashkenas', 'score': 0},
      ],
    },
    {
      'questionText': 'Q5. Montpellier se situe-t-il en France ?',
      'answers': [
        {'text': 'Oui', 'score': 1},
        {'text': 'Non', 'score': 0},
      ],
    },
  ];

  final _questionsImage = const [
    {
      'name': 'year.png'
    },
    {
      'name': 'flutter.png'
    },
    {
      'name': 'pineapple.png'
    },
    {
      'name': 'flutter.png'
    },
    {
      'name': 'montpellier.png'
    },
  ];

  var _questionIndex = 0;
  var _totalScore = 0;

  void _resetQuiz() {
    setState(() {
      _questionIndex = 0;
      _totalScore = 0;
    });
  }

  void _answerQuestion(int score) {
    _totalScore += score;

    setState(() {
      _questionIndex = _questionIndex + 1;
    });

    if (kDebugMode) {
      print(_questionIndex);
      print(_questions.length);
    }
    if (_questionIndex < _questions.length) {

      if (kDebugMode) {
        print('There are still some other questions !');
      }
    } else {

      if (kDebugMode) {
        print('No more questions !');
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Center(
          child: Text('Quiz'),
        ),
      ),
      body: appBehavior(), //Padding
    );
  }

  Padding appBehavior() {
    return Padding(
        padding: const EdgeInsets.all(30.0),
        child: _questionIndex < _questions.length
            ? QuizBody(
          answerQuestion: _answerQuestion,
          questionIndex: _questionIndex,
          questions: _questions,
          questionsImage: _questionsImage,
        ) //Quiz
            : Result(_totalScore, _resetQuiz),
      );
  }
}

class Question extends StatelessWidget {
  final String questionText;

  const Question(this.questionText, {Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      margin: const EdgeInsets.all(10),
      child: Text(
        questionText,
        style: const TextStyle(fontSize: 28, color: Colors.black),
        textAlign: TextAlign.center,
      ), //Text
    ); //Container
  }
}

class Result extends StatelessWidget {
  final int resultScore;
  final Function resetHandler;

  const Result(this.resultScore, this.resetHandler, {Key? key})
      : super(key: key);

//Remark Logic
  String get resultPhrase {
    String resultText;
    if (resultScore == 5) {
      resultText = 'You are godlike !';
      Text('$resultScore');
    } else if (resultScore == 4) {
      resultText = 'Pretty Good !';
      Text('$resultScore');
    } else if (resultScore == 3) {
      resultText = 'You need to work more !';
    } else if (resultScore >= 1) {
      resultText = 'You need to work hard!';
    } else {
      resultText = 'You are really bad !';
      Text('$resultScore');
    }
    return resultText;
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Text(
            resultPhrase,
            style: const TextStyle(fontSize: 26, fontWeight: FontWeight.bold, color: Colors.black),
            textAlign: TextAlign.center,
          ), //Text
          Text(
            'Score ' '$resultScore',
            style: const TextStyle(fontSize: 36, fontWeight: FontWeight.bold, color: Colors.black),
            textAlign: TextAlign.center,
          ), //Text
          const SizedBox(
            height: 100,
          ),
          ElevatedButton(
            style: ElevatedButton.styleFrom(
              elevation: 3,
              shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(32.0)),
              minimumSize: const Size(200, 60), //////// HERE
              textStyle: const TextStyle(fontSize: 20),
            ),
            onPressed: () { resetHandler(); },
            child: const Text('Restart Quiz'),
          )
        ], //<Widget>[]
      ), //Column
    ); //Center
  }
}

