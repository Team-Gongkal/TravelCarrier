//프로필 이미지 hover시 메뉴 활성화 - by윤아
$(window).load(function () {
  $(".profile > a, .user_menu").on("mouseenter", function (e) {
    $(".user_menu").addClass("on");
  });
  $(".profile, .user_menu").on("mouseleave", function () {
    $(".user_menu").removeClass("on");
  });
});

//검은 배경 클릭시 모달 비활성화 -by윤아
$(window).click(function (e) {
  if ($(e.target).is($(".modal_bg"))) {
    console.log(e.target);
    $(
      ".weekly_modal_bg, .keyword_modal_bg, .edit_profile, #login_wrap , .reply_modal , .daily_form_bg .period_modal_bg"
    ).removeClass("show");
  }
});

//모달창 활성화시 스크롤 비활성화 하기 -by윤아
// 스크롤 비활성화
