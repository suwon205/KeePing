// import 'package:flutter/material.dart';
// import 'package:keeping/provider/request_money_provider.dart';
// import 'package:keeping/widgets/render_field.dart';
// import 'package:provider/provider.dart';

// Widget requestMoneyHowMuch(BuildContext context) {
//   TextEditingController _money = TextEditingController();

//   void handleRequestMoney(dynamic value) {
//     if (value is String) {
//       Provider.of<RequestMoneyProvider>(context, listen: false)
//           .updateRequestMoney(money: value);
//       print('용돈 수정 중: $value');
//     } else {
//       Provider.of<RequestMoneyProvider>(context, listen: false)
//           .updateRequestMoney(money: value);
//     }
//   }

//   return Container(
//     child: Column(
//       children: [
//         // Text('얼마를 조를까요?'),
//         Builder(
//           builder: (BuildContext context) {
//             // Builder를 사용하여 새로운 빌드 컨텍스트를 얻습니다.
//             return renderTextFormField(
//               label: '얼마를 조를까요?',
//               controller: _money,
//               onChange: handleRequestMoney,
//               isNumber: true,
//             );
//           },
//         ),
//       ],
//     ),
//   );
// }
