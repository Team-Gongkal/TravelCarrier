// 시작 폼 모달창 버튼
$(".writing").on("click", function () {
  $(".daily_form_bg").addClass("show");
});
$(".daily_btn").on("click", function () {
  $(".daily_form_bg").removeClass("show");
});

//일기 슬라이드
//client rolling banner
window.onload = function () {
  var bannerLeft = 0;
  var first = 1;
  var last;
  var imgCnt = 0;
  var $img = $(".diary_slide li");
  var $first;
  var $last;

  $img.each(function () {
    // 10px 간격으로 배너 처음 위치 시킴
    $(this).css("left", bannerLeft);
    bannerLeft += $(this).width() + 10;
    $(this).attr("id", "banner" + ++imgCnt); // img에 id 속성 추가
  });

  if (imgCnt >= 6) {
    //배너 5개 이상이면 이동시킴

    last = imgCnt;

    setInterval(function () {
      $img.each(function () {
        $(this).css("left", $(this).position().left - 50); // 5px씩 왼쪽으로 이동
      });
      $first = $("#banner" + first);
      $last = $("#banner" + last);
      if ($first.position().left < -200) {
        // 제일 앞에 배너 제일
        last++;
        if (last > imgCnt) {
          last = 1;
        } //뒤로 옮김
        $first.css("left", $last.position().left + $last.width() + 10); //10px 간격 남기고 붙이기
        first++;
        if (first > imgCnt) {
          first = 1;
        }
      }
    }, 1000); //여기 값을 조정하면 속도를 조정할 수 있다.(위에 1px 이동하는 부분도 조정하면

    //깔끔하게 변경가능하다
  }
};

// daily 일기화면(hover후)
$(".d_slide").on("hover", function () {
  console.log("뿅");
  // 1. 백그라운드 바꾸기
  //2. title, period 숨기기
  //3.배경 어둡게
  //4. 일기글 보이기
  //5.슬라이드 멈춤
  //6.
}),
  function () {};
