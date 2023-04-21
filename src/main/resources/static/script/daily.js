// 시작 폼 모달창 띄우기 - by윤아
$(".writing").on("click", function () {
  $(".daily_form_bg").addClass("show");
});
$(".daily_btn").on("click", function () {
  $(".daily_form_bg").removeClass("show");
});

//daily 슬라이드 구현 - by윤아
$(window).on("load", function () {
  // slide / hover시 애니메이션 멈춤
  $(".diary_list").on("mouseenter", function () {
    $(this).css("animation-play-state", "paused");
  });
  $(".diary_list").on("mouseleave", function () {
    $(this).css("animation-play-state", "running");
  });
  setRollingImg();
});

function setRollingImg() {
  const $wrap = $(".diary_slides");
  const $list = $(".diary_slides .diary_list");
  let wrapWidth = ""; //$wrap의 가로 크기
  let listWidth = ""; //$list의 가로 크기
  const speed = 150; //1초에 몇픽셀 이동하는지 설정

  //리스트 복제
  let $clone = $list.clone();
  $wrap.append($clone);
  flowBannerAct();

  //반응형 :: 디바이스가 변경 될 때마다 배너 롤링 초기화
  let oldWChk =
    window.innerWidth > 1279 ? "pc" : window.innerWidth > 767 ? "ta" : "mo";
  $(window).on("resize", function () {
    let newWChk =
      window.innerWidth > 1279 ? "pc" : window.innerWidth > 767 ? "ta" : "mo";
    if (newWChk != oldWChk) {
      oldWChk = newWChk;
      flowBannerAct();
    }
  });

  //배너 실행 함수
  function flowBannerAct() {
    //배너 롤링 초기화
    if (wrapWidth != "") {
      $wrap.find(".diary_list").css({ animation: "none" });
      $wrap.find(".diary_list").slice(2).remove();
    }
    wrapWidth = $wrap.width();
    listWidth = $list.width();

    //무한 반복을 위해 리스트를 복제 후 배너에 추가
    if (listWidth < wrapWidth) {
      const listCount = Math.ceil((wrapWidth * 2) / listWidth);
      for (let i = 2; i < listCount; i++) {
        $clone = $clone.clone();
        $wrap.append($clone);
      }
    }
    $wrap
      .find(".diary_list")
      .css({ animation: `${listWidth / speed}s linear infinite imgRolling` });
  }
}

// daily 일기화면(hover) - by.윤아

$(".d_slide").on("mouseenter", function (e) {
  //1. title, period 숨기기
  $(".diary_titlebox").addClass("hide");

  //2. 백그라운드 바꾸기
  var img_src = e.target.src;
  console.log(img_src);
  $(".diary_noH").css({
    "background-image": `url(${img_src})`,
    "background-repeat": "no-repeat",
    "background-position": "center center",
    "background-size": "cover",
  });

  //3.배경 어둡게
  $(".filter").addClass("on");

  //4. 일기글 보이기
  $(".diary_textbox").addClass("on");
});

$(".d_slide").on("mouseleave", function (e) {
  //1. title, period 보이기
  $(".diary_titlebox").removeClass("hide");

  //2. 백그라운드 바꾸기
  var img_src = e.target.src;
  $(".diary_noH").css({
    background: "#efeee9",
  });

  //3.배경 흐리게 제거
  $(".filter").removeClass("on");

  //4.일기 숨기기
  $(".diary_textbox").removeClass("on");
});
