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
      ".keyword_modal_bg, #login_wrap , .reply_modal , .period_modal_bg, .edit_menu, .companion_modal_bg"
    ).removeClass("show");
  }
});

//모달을 위한 쿠키확인 메소드 - by서현
function getCookie(key) {
  // 모든 쿠키 가져오기
  const cookies = document.cookie.split(";");
  // 쿠키 목록 순회
  for (let i = 0; i < cookies.length; i++) {
    const cookie = cookies[i].trim();
    // 쿠키를 "="로 분할하여 key와 value를 얻습니다.
    const cookieParts = cookie.split("=");
    const cookieName = cookieParts[0];
    const cookieValue = cookieParts[1];

    // 원하는 key와 일치하는 쿠키를 찾으면 해당 값을 반환합니다.
    if (cookieName === key) {
      return decodeURIComponent(cookieValue);
    }
  }
  // 원하는 key를 찾지 못한 경우 null을 반환합니다.
  return null;
}

//메인화면  header옆 left-border제거
$(document).ready(function () {
  $("main#main_map")
    .siblings("header")
    .css("border-right", "1.5px solid transparent");
});
