import 'package:flutter/material.dart';
import 'package:keeping/widgets/header.dart';
import 'package:keeping/screens/my_page/select_theme_page.dart';
import 'package:keeping/screens/my_page/password_edit_page.dart';
import 'package:keeping/screens/my_page/phonenum_edit_page.dart';
import 'package:keeping/screens/my_page/child_management_page.dart';

import 'package:keeping/widgets/bottom_modal.dart';
import 'package:path/path.dart';

class ParentMyPage extends StatelessWidget {
  const ParentMyPage({Key? key}) : super(key: key);
  final name = 'Parent';

  void gotoSelectTheme(BuildContext context) {
    // 매개변수로 BuildContext 추가
    Navigator.push(
        context, MaterialPageRoute(builder: (context) => SelectThemePage()));
  }

  void gotoEditPwd(BuildContext context) {
    Navigator.push(
        context, MaterialPageRoute(builder: (context) => PasswordEditPage()));
  }

  void gotoEditPhone(BuildContext context) {
    Navigator.push(
        context, MaterialPageRoute(builder: (context) => PhonenumEditPage()));
  }

  void handleChild(BuildContext context) {
    Navigator.push(context,
        MaterialPageRoute(builder: (context) => ChildManagementPage()));
  }

  void logout(BuildContext context) {
    showLogoutConfirmationDialog(context);
  }

// 로그아웃 모달 다이얼로그
  void showLogoutConfirmationDialog(BuildContext context) {
    bottomModal(
      context: context,
      title: '로그아웃',
      content: logoutDescription(),
      button: logoutBtn(context),
    );
  }

  void showDeleteAccountConfirmationDialog(BuildContext context) {
    bottomModal(
      context: context,
      title: '회원 탈퇴',
      content: deleteAccountDescription(),
      button: deleteAccountBtn(context),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: MyHeader(text: '마이페이지', elementColor: Colors.black),
      body: Center(
        child: Container(
          width: 300,
          child: Column(
            children: [
              userInfoBox(),
              _MyPageDetailedFunctionPage(
                context,
                '테마 변경',
                () => gotoSelectTheme(context), // BuildContext를 전달
              ),
              _MyPageDetailedFunctionPage(
                context,
                '비밀번호 변경',
                () => gotoEditPwd(context),
              ),
              _MyPageDetailedFunctionPage(
                context,
                '휴대폰 번호 변경',
                () => gotoEditPhone(context),
              ),
              _MyPageDetailedFunctionPage(
                context,
                '자녀 관리',
                () => handleChild(context),
              ),
              _MyPageDetailedFunctionPage(
                context,
                '로그아웃',
                () => logout(context),
              ),
              SizedBox(
                height: 30,
              ),
              _deleteAccount(context),
            ],
          ),
        ),
      ),
    );
  }

  Widget userInfoBox() {
    return Center(
      child: Container(
        width: 300,
        height: 150,
        decoration: BoxDecoration(
          border: Border.all(
            color: Color.fromARGB(255, 185, 185, 185), // 테두리 색상
            width: 2.0, // 테두리 두께
          ),
          borderRadius: BorderRadius.circular(10.0), // 모서리 반경
        ),
        child: Row(
          children: [
            Image.asset(
              'assets/image/temp_image.jpg',
              width: 100.0,
              height: 100.0,
            ),
            Text('$name 님'),
          ],
        ),
      ),
    );
  }

  Widget logoutBtnModal(BuildContext context) {
    return ElevatedButton(
        onPressed: () {
          bottomModal(
            context: context,
            title: '로그아웃',
            content: logoutDescription(),
            button: logoutBtn(context),
          );
        },
        child: Text('로그아웃'));
  }

  Row logoutBtn(BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        ElevatedButton(
          onPressed: () {
            Navigator.of(context).pop();
            print('로그아웃 중');
          },
          child: Text('확인'),
        ),
        SizedBox(width: 20),
        ElevatedButton(
          onPressed: () {
            Navigator.of(context).pop();
          },
          child: Text('취소'),
        ),
      ],
    );
  }

  Widget logoutDescription() {
    return Text('정말 로그아웃 하시겠어요?');
  }

  Widget _deleteAccount(BuildContext buildContext) {
    return Row(
      children: [
        TextButton(
          onPressed: () {
            showDeleteAccountConfirmationDialog(buildContext);
          },
          child: Text(
            '회원 탈퇴',
            style: TextStyle(
              color: Colors.black,
            ),
          ),
        ),
      ],
    );
  }

  Widget deleteAccountDescription() {
    return Text('정말 회원 탈퇴 하시겠어요?');
  }

  Row deleteAccountBtn(BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        ElevatedButton(
          onPressed: () {
            // 회원 탈퇴 로직을 여기에 추가
            // 예를 들어, 서버에 회원 탈퇴 요청을 보내는 등의 작업
            // 작업이 성공하면 앱을 종료하거나 로그인 페이지로 이동할 수 있습니다.
            Navigator.of(context).pop();
            // Navigator.of(context).pop();
          },
          child: Text('확인'),
        ),
        SizedBox(width: 20),
        ElevatedButton(
          onPressed: () {
            Navigator.of(context).pop();
          },
          child: Text('취소'),
        ),
      ],
    );
  }
}

Widget _MyPageDetailedFunctionPage(
  BuildContext context,
  String title,
  Function function,
) {
  return Padding(
      padding: EdgeInsets.only(top: 15),
      child: ElevatedButton(
        onPressed: () async {
          function();
        },
        style: _detailFunctionBtnStyle(),
        child: Text(title),
      ));
}

ButtonStyle _detailFunctionBtnStyle() {
  return ButtonStyle(
      backgroundColor: MaterialStateProperty.all<Color>(Colors.white),
      foregroundColor:
          MaterialStateProperty.all<Color>(const Color(0xFF8320E7)),
      shape: MaterialStateProperty.all<RoundedRectangleBorder>(
          RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(10),
        side: BorderSide(
          color: const Color(0xFF8320E7), // 테두리 색상 설정
          width: 2.0, // 테두리 두께 설정
        ),
      )),
      fixedSize: MaterialStateProperty.all<Size>(Size(300, 50)));
}