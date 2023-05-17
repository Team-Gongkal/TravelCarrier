// 2022.05.14 ==================================================================
$(window).on("load", function () {
  var slide_width = $(".diary_slides").outerWidth();
  console.log(
    "비교 : 윈도우 :" + $(window).width() + "  /  슬라이드 :" + slide_width
  );
  //슬라이드 이동함수
  function moving(x, slide) {
    let left = parseInt(slide.css("left"));
    slide.css("left", left - x + "px"); //이동
    // 위치 리셋 시키기
    if (slide_width + (left - x) <= 0) {
      slide.css("left", slide_width + "px");
    }
    if ($(".diary_slides").eq(0).outerWidth() + (left - x) <= 0) {
      slide.css("left", slide_width + "px");
    }
  }
  if (slide_width > $(window).width()) {
    alert("복사하고 무브");

    // 슬라이드 복제하기 (clone-복제 / append-붙여넣기)
    let clone_slide = $(".diary_slides").eq(0).clone();
    $(".diary_viewport").append(clone_slide);

    //위치값이 0,0이라 곂치지 않게 두 슬라이드의 위치 지정
    $(".diary_slides").eq(0).css("left", "0px");
    $(".diary_slides")
      .eq(1)
      .css("left", slide_width + "px");

    //이동 거리
    var movingDistance = 1;
    //이동 함수 적용하기
    var originMove = setInterval(() => {
      moving(movingDistance, $(".diary_slides").eq(0));
    }, parseInt(1000 / 100));

    var cloneMove = setInterval(() => {
      moving(movingDistance, $(".diary_slides").eq(1));
    }, parseInt(1000 / 100));

    // mouseenter시 슬라이드 일시정지
    $(".diary_slides").on("mousemove", function () {
      clearInterval(originMove);
      clearInterval(cloneMove);
      movingDistance = 0;
    });

    //mouseleave시 슬라이드 재생
    $(".diary_slides").on("mouseleave", function () {
      movingDistance = 1;
      //이동 함수 적용하기
      originMove = setInterval(() => {
        moving(movingDistance, $(".diary_slides").eq(0));
      }, parseInt(1000 / 100));

      cloneMove = setInterval(() => {
        moving(movingDistance, $(".diary_slides").eq(1));
      }, parseInt(1000 / 100));
    });
  } else {
  }
  // 댓글 모달창 활성화 - by윤아
  $(".diary_viewport").on("click", ".d_slide > .reply_icon", function (e) {
    $(".reply_modal").addClass("show");
    var reply_img = $(e.target).parent().parent().find("img").attr("src");
    $(".reply_img img").attr("src", `${reply_img}`);
    $(".diary_noH .writing").hide();

    // 댓글 모달창 세로선 자동 생성 및 길이 수정
    var reply_height = $(".reply_scroll").height();
    console.log("댓글 스크롤 길이" + reply_height);
    $(".reply_screen::before").css("height", (reply_height / 80) * 100 + "%");
  });
  //댓글모달창비활성화
  $(".reply_modal .close").click(function () {
    $(".reply_modal").removeClass("show");
    $(".diary_noH .writing").show();
  });

  //슬라이드 호버시
  $(".diary_slides").on("mousemove", function () {
    // title, period 숨기기
    $(".diary_titlebox").addClass("hide");
    // 댓글 아이콘 활성화
    $(".reply_icon").addClass("show");
    //2.배경 어둡게
    $(".filter").addClass("on");
    $(".diary_viewport").addClass("black");
    $(".diary_noH .writing").hide();
  });
  $(".diary_slides").on("mouseleave", function () {
    // title, period 보이기
    $(".diary_titlebox").removeClass("hide");
    // 댓글 아이콘 비활성화
    $(".reply_icon").removeClass("show");
    //슬라이드 양끝 흐림효과
    $(".diary_noH").css({
      background: "#efeee9",
    });
    //3.배경 흐리게 제거
    $(".filter").removeClass("on");
    //슬라이드 양끝 흐림효과
    $(".diary_viewport").removeClass("black");
    $(".diary_noH .writing").show();
  });
});
// daily 일기화면(hover) - by.윤아
$(".diary_viewport").on("mouseenter", ".d_slide", function (e) {
  // 복제된 diary_slides에는 mouseenter 이벤트가 적용되지 않아 위임 방식을 사용하여, 부모 요소인 .diary_viewport에 이벤트를 바인딩함

  //1. 백그라운드 바꾸기
  var img_src = e.target.src;
  console.log(e.target);
  $(".diary_noH").css({
    "background-image": `url(${img_src})`,
    "background-repeat": "no-repeat",
    "background-position": "center center",
    "background-size": "cover",
  });

  //3. 일기글 보이기
  $(".diary_textbox").addClass("on");
  var diary_title = $(e.target).parent().data("title");
  var diary_text = $(e.target).parent().data("text");

  $(".diary_textbox h6").html(diary_title);
  $(".diary_textbox p").html(diary_text);

  $(".d_slide").on("mouseleave", function (e) {
    //4.일기 숨기기
    $(".diary_textbox").removeClass("on");
  });
});

// 슬라이드 사진 크기에 따라 클래스명 변경 - by.서현
$(document).ready(function () {
  $(".d_slide > img").each(function (index) {
    const $img = $(this);
    const img = new Image();
    img.src = $(this).attr("src");
    img.onload = function () {
      var ratio = img.width / img.height;
      console.log(ratio);
      if (0.9 <= ratio && ratio <= 1.1) {
        $img.parent().addClass("sqr");
      } else if (ratio < 0.9) {
        $img.parent().addClass("rectL");
      } else if (1.1 < ratio) {
        $img.parent().addClass("rectW");
      }
    };
  });
});

//스크롤시 한 페이지씩 이동 구현 - by윤아
// window.addEventListener(
//   "wheel",
//   function (e) {
//     e.preventDefault();
//   },
//   { passive: false }
// );
