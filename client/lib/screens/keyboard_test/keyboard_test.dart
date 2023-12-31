import 'package:flutter/material.dart';
import 'package:keeping/util/display_format.dart';
import 'package:keeping/widgets/bottom_btn.dart';
import 'package:keeping/widgets/header.dart';
import 'package:keeping/widgets/number_keyboard.dart';

class KeyboardTest extends StatelessWidget {
  const KeyboardTest({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        children: [
          MyHeader(
            text: '숫자 키보드 테스트',
            elementColor: Colors.black
          ),
          renderText('1000'),
          NumberKeyboard()
        ]
      ),
      bottomNavigationBar: BottomBtn(
        text: '확인',
        isDisabled: false,
      ),
    );
  }
}

Widget renderText(String amount) {
  String display = '보낼 금액';
  TextStyle style = TextStyle(
    color: Colors.grey,
    fontWeight: FontWeight.bold,
    fontSize: 30.0,
  );

  if (amount.isNotEmpty) {
    display = formattedMoney(int.parse(amount));
    style = style.copyWith(
      color: Colors.black
    );
  }

  return Expanded(
    child: Center(
      child: Text(
        display,
        style: style,
      ),
    ),
  );
}

void onPressed(BuildContext context) {
  Navigator.pop(context);
}