import 'package:flutter/material.dart';

// 하단 네모난 버튼 클래스
class BottomBtn extends StatelessWidget {
  final String text;
  final Color bgColor;
  final Color textColor;
  final void Function(BuildContext) onPressed; // 버튼 클릭 후 실행될 함수

  BottomBtn(
      {super.key,
      required this.text,
      this.bgColor = const Color(0xFF8320E7),
      this.textColor = Colors.white,
      required this.onPressed});

  // 특정 페이지로 가던지, 데이터를 보내던지, 이전 페이지로 돌아가던지 구분해서 로직 짜기

  @override
  Widget build(BuildContext context) {
    return BottomAppBar(
      color: bgColor,
      height: 70.0,
      child: TextButton(
        onPressed: () {
          onPressed(context);
        },
        style: bottomBtnStyle(textColor),
        child: Text(text),
      ),
    );
  }
}

// 버튼 스타일
ButtonStyle bottomBtnStyle(Color textColor) {
  return ButtonStyle(
    backgroundColor: MaterialStateProperty.all<Color>(Colors.transparent),
    foregroundColor: MaterialStateProperty.all<Color>(textColor),
    textStyle: MaterialStateProperty.all<TextStyle>(
      TextStyle(fontSize: 25.0, color: textColor),
    ),
  );
}
