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
      ".keyword_modal_bg, #login_wrap , .reply_modal , .daily_form_bg, .period_modal_bg, .edit_menu, .companion_modal_bg"
    ).removeClass("show");
  }
});
