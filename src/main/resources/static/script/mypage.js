//탭메뉴 활성화 -by윤아
var tab = $(".userProfile_tab > ul > li");
var contents = $(".userProfile_scroll > ul");
var count = tab.length;
var $scroll = $(".userProfile_left");

function open(i) {
  tab.removeClass("on");
  tab.eq(i).addClass("on");
  contents.removeClass("show");
  contents.eq(i).addClass("show");
  $scroll.scrollTop(0); //스크롤 초기화
}
for (let i = 0; i < count; i++) {
  tab.eq(i).click(function () {
    open(i);
  });
}

//계정관리 모달창 활성화 - by 윤아
$("#edit_profile").on("click", function () {
  $(".edit_profile").addClass("show");
});

$(".edit_profile ul li")
  .last()
  .on("click", function () {
    $(".edit_profile").removeClass("show");
  });

//스크롤 변화에 따른 top높이 설정 - by윤아
addEventListener("mousewheel", (e) => {
  const direction = e.deltaY > 0 ? "Scroll Down" : "Scroll Up";

  // 방향과 현 스크롤 위치
  console.log(direction, $scroll.scrollY);
});
