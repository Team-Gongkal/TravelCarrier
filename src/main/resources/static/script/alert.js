// 각종 경고 메시지 및 알림창 스크립트 파일

function alertModal(text) {
  var changeText = $(".alert_textbox p").eq(1).children("span");
  //span을 파라미터로 바꿔주기
  changeText.text(text);
  //모달창 활성화 하기(show)
  $("#alert_modal").addClass("show");
}

function alertModal2(text) {
  var changeText = $(".alert_textbox p").eq(1).children("span");
  //span을 파라미터로 바꿔주기
  changeText.text(text);
  //모달창 활성화 하기(show)
  $("#alert_modal2").addClass("show");
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

//위클리 삭제-----------------------------------
// 위클리 삭제 클릭 이벤트 - by.서현
$(document).on("click", ".weeklyDelBtn", function () {
  var title = $(this).closest("li").find(".uP_diary_tit").text();
  var wid = $(this).closest("li").data("wid");

  //'네'버튼에 id부여
  $(".confirm_btn").attr("id", "deleteWeekly");
  $(".confirm_btn").attr("data-wid", wid);
  alertModal("["+title+"]을 삭제");
});

$(document).on("click", "#deleteWeekly", function () {
   var wid = $(this).data("wid");
   deleteWeekly(wid);
   closeAlert();
});

function deleteWeekly(weeklyId) {
  $.ajax({
    url: "/TravelCarrier/weekly/" + weeklyId,
    type: "DELETE",
    processData: false,
    contentType: false,
    success: function (data) {
      $("li[data-wid='" + weeklyId + "']").remove();
    },
    error: function (error) {
      alert("삭제 실패" + error);
    },
  });
}

//댓글삭제-------------------------------------
// 삭제 이벤트  - by.서현
$(document).on("click", ".del_btn", function (e) {
  var $comment = $(this).closest(".rep");
  var reply = $comment.data("reply");


  //'네'버튼에 id부여
  $(".confirm_btn").attr("id", "deleteReply");
  $(".confirm_btn").attr("data-reply", reply);

  alertModal("댓글을 삭제");

});

$(document).on("click", "#deleteReply", function () {
   var reply = $(this).data("reply");
   deleteReply(reply);
   closeAlert();
});

function deleteReply(reply){
    var data = {
        replyId: reply,
        ddate: $.datepicker.formatDate("yy-mm-dd", new Date()),
    };

    $.ajax({
      type: "POST",
      url: "/TravelCarrier/reply/delete",
      data: JSON.stringify(data),
      contentType: "application/json",
      success: function (resp) {
        alert("삭제되었습니다.");
        var attachNo = $(".reply_img img").attr("data-attachNo");
        currentReplyList(attachNo);
      },
      error: function (jqXHR, textStatus, errorThrown) {
        alert("댓글 삭제 실패");
      },
    });
}

//탈퇴-----------------------------------------

//아이디 중복확인-----------------------------------------
function email_check_alert(valid){
    //valid가 true면 사용 가능, false면 불가하다는 의미
    if(valid) alertModal2("사용 가능");
    else alertModal2("사용 불가능");
}

//알림창 닫기====================================
function closeAlert() {
  $("#alert_modal").removeClass("show");
}
function closeAlert2() {
  $("#alert_modal2").removeClass("show");
}
