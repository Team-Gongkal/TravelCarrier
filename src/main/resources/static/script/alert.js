// 각종 경고 메시지 및 알림창 스크립트 파일

function alertModal(text) {
  var changeText = $(".alert_textbox p").eq(1).children("span");
  //span을 파라미터로 바꿔주기
  changeText.text(text);
  //모달창 활성화 하기(show)
  $("#alert_modal").addClass("show");
}

//로그아웃-------------------------------------
function confirmLogout(event) {
  event.preventDefault(); // 폼 제출 방지
  alertModal("로그아웃");
  //'네'버튼에 logout-id부여
  $(".confirm_btn").attr("id", "logout");
}

$(document).on("click", "#logout", function () {
  document.getElementById("logoutForm").submit(); // 폼 제출
});
//댓글삭제-------------------------------------
//팔로우 및 팔로워 삭제-------------------------
//탈퇴-----------------------------------------

//알림창 닫기====================================
function closeAlert() {
  $("#alert_modal").removeClass("show");
}
