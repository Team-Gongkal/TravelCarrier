// 각종 경고 메시지 및 알림창 스크립트 파일
function alertModal(text) {
  //클릭한 요소의text가져오기
  //span을 파라미터로 바꿔주기
  var changeText = $(".alert_textbox p").eq(1).children("span");
  changeText.text(text);
  console.log(changeText.text());
  $("#alert_modal").addClass("show");
}
