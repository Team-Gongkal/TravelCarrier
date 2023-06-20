// 페이지가 로드 되었을 때 만약크기가 크면 run 크기가 작으면 멈춤

// 화면의 사이즈에 변경이 생겼을 경우
// 페이지의 크기가 크면 run 작우면 복제된 것 지우고 멈춤

//$(window).on("load", function () {
  //이미지가 채 로드되기 전에 스크립트가 실행돼서 너비값이 제대로 측정되질 않음
//   function mouseON() {
//     $(".diary_slides").on("mousemove", function () {
//       // title, period 숨기기
//       $(".diary_titlebox").addClass("hide");
//       // 댓글 아이콘 활성화
//       $(".reply_icon").addClass("show");
//     });
//   }

//   function mouseOFF() {
//     $(".diary_slides").on("mouseleave", function () {
//       // title, period 보이기
//       $(".diary_titlebox").removeClass("hide");
//       // 댓글 아이콘 비활성화
//       $(".reply_icon").removeClass("show");
//     });
//   }
//   //슬라이드의 너비 구하기
//   var slide_width = $(".diary_slides").outerWidth();
//   console.log("슬라이드너비 : " + slide_width);
//   console.log("처음 로드시 화면 너비 : " + $(window).width());

//   // if문 실행
//   if ($(window).width() >= slide_width) {
//     //아무것도 하지말고 마우스 호버만 하기
//     alert("짧음");
//     mouseON();
//     mouseOFF();
//   } else {
//     //슬라이드가 더 클경우
//     alert("복사하고 무브");
//     // 슬라이드 복제하기 (clone-복제 / append-붙여넣기)
//     let clone_slide = $(".diary_slides").eq(0).clone();
//     $(".diary_viewport").append(clone_slide);

//     //위치값이 0,0이라 곂치지 않게 두 슬라이드의 위치 지정
//     $(".diary_slides").eq(0).css("left", "0px");
//     $(".diary_slides")
//       .eq(1)
//       .css("left", slide_width + "px");
//   }

//   var movingDistance = 1; //슬라이드 이동 거리

//   //이동 함수 적용하기
//   var originMove = setInterval(() => {
//     moving(movingDistance, $(".diary_slides").eq(0));
//   }, parseInt(1000 / 100));

//   var cloneMove = setInterval(() => {
//     moving(movingDistance, $(".diary_slides").eq(1));
//   }, parseInt(1000 / 100));

//   //슬라이드 이동함수
//   function moving(x, slide) {
//     let left = parseInt(slide.css("left"));
//     slide.css("left", left - x + "px"); //이동
//     // 위치 리셋 시키기
//     if (slide_width + (left - x) <= 0) {
//       slide.css("left", slide_width + "px");
//     }
//     if ($(".diary_slides").eq(0).outerWidth() + (left - x) <= 0) {
//       slide.css("left", slide_width + "px");
//     }
//   }
// }); //온로드 마침

// $(window).on("load", function () {
//   //이미지가 채 로드되기 전에 스크립트가 실행돼서 너비값이 제대로 측정되질 않음

//   //슬라이드의 너비 구하기
//   var slide_width = $(".diary_slides").outerWidth();
//   console.log("슬라이드너비 : " + slide_width);
//   console.log("처음 로드시 화면 너비 : " + $(window).width());

//   function mouseON() {
//     $(".diary_slides").on("mousemove", function () {
//       // title, period 숨기기
//       $(".diary_titlebox").addClass("hide");
//       // 댓글 아이콘 활성화
//       $(".reply_icon").addClass("show");
//     });
//   }

//   function mouseOFF() {
//     $(".diary_slides").on("mouseleave", function () {
//       // title, period 보이기
//       $(".diary_titlebox").removeClass("hide");
//       // 댓글 아이콘 비활성화
//       $(".reply_icon").removeClass("show");
//     });
//   }

//   function slide_clone() {
//     // 슬라이드 복제하기 (clone-복제 / append-붙여넣기)
//     let clone_slide = $(".diary_slides").eq(0).clone();
//     $(".diary_viewport").append(clone_slide);

//     //위치값이 0,0이라 곂치지 않게 두 슬라이드의 위치 지정
//     $(".diary_slides").eq(0).css("left", "0px");
//     $(".diary_slides")
//       .eq(1)
//       .css("left", slide_width + "px");
//   }

//   function moving(x, slide) {
//     let left = parseInt(slide.css("left"));
//     slide.css("left", left - x + "px"); //이동
//     // 위치 리셋 시키기
//     if (slide_width + (left - x) <= 0) {
//       slide.css("left", slide_width + "px");
//     }
//     if ($(".diary_slides").eq(0).outerWidth() + (left - x) <= 0) {
//       slide.css("left", slide_width + "px");
//     }
//   }
//   var movingDistance = 1;

//   function slide_run() {
//     slide_clone();

//     //이동 함수 적용하기
//     var originMove = setInterval(() => {
//       moving(movingDistance, $(".diary_slides").eq(0));
//     }, parseInt(1000 / 100));

//     var cloneMove = setInterval(() => {
//       moving(movingDistance, $(".diary_slides").eq(1));
//     }, parseInt(1000 / 100));

//     //슬라이드 mouseleave시 슬라이드 재생
//     $(".diary_slides").on("mouseleave", function () {
//       var movingDistance = 1;

//       setTimeout(function () {
//         originMove = setInterval(() => {
//           moving(movingDistance, $(".diary_slides").eq(0));
//         }, parseInt(1000 / 100));
//         cloneMove = setInterval(() => {
//           moving(movingDistance, $(".diary_slides").eq(1));
//         }, parseInt(1000 / 100));
//       }, 0);
//       mouseOFF();
//     });

//     //슬라이드 mouseenter시 슬라이드 정지
//     $(".diary_slides").on("mousemove", function () {
//       clearInterval(originMove);
//       clearInterval(cloneMove);
//       mouseON();
//     });
//   }

//   function resize_slide() {
//     if ($(window).width() >= slide_width) {
//       $(".diary_slide").eq(1).remove();
//       alert("remove()했으니까 멈춰!!");
//       $(".diary_slides").on("mousemove", function () {
//         mouseON();
//       });
//       $(".diary_slides").on("mouseleave", function () {
//         mouseOFF();
//       });
//     } else {
//       alert("움직여!!");
//       slide_run();
//     }
//   }
//   //기본실행
//   resize_slide();

//   //브라우저 크기 리사이징
//   $(window).resize(function () {
//     console.log("사이즈가 바뀌었어요!!");
//     console.log("리사이징 후 화면 너비 : " + $(window).width());
//     slide_width = $(".diary_slides").outerWidth();
//     resize_slide(); //if문 실행
//   });
//   jㅔ;
// }); //onload 슬라이드 끝

// //데일리 이미지 슬라이드 구현 - by윤아
// window.onload = function () {
//   //   var slide_left = 0;
//   //   var first = 1;
//   //   var last;
//   //   var slideCnt = 0;
//   //   var $slide = $("ul.diary_list li.d_slide");
//   //   var $first;
//   //   var $last;
//   //   var slide_width = $(".diary_slides").outerWidth();

//   //   $slide.each(function () {
//   //     $(this).attr("id", "slide" + ++slideCnt); //id속성추가;
//   //   });

//   //   if (slide_width > $(window).width()) {
//   //     last = slideCnt;
//   //     setInterval(function () {
//   //       $slide.each(function () {
//   //         $(this).css("left", $(this).position().left - 1);
//   //       });

//   //       $first = $("#slide" + first);
//   //       $last = $("#slide" + last);
//   //       if ($first.position().left < -200) {
//   //         $first.css("left", $last.position().left);
//   //         first++;
//   //         last++;
//   //         if (last > slideCnt) {
//   //           last = 1;
//   //         }
//   //         if (last > slideCnt) {
//   //           first = 1;
//   //         }
//   //       }
//   //     }, 50);
//   //   }

  // 슬라이드 수정=========================================
  $(window).on("load", function () {
  // let origin_slide = document.querySelector("ul.diary_list");
  // origin_slide.id = "slide1";
  // // $(clone_slide).attr("id", "slide1");

  // let clone_slide = origin_slide.cloneNode(true); //(true)로 자식요소까지 복제해서 변수에 할당함
  // clone_slide.id = "slide2";
  // document.querySelector("div.diary_slides").appendChild(clone_slide); //자식요소로 붙여넣음

  // document.querySelector("#slide1").style.left = "0px";
  // document.querySelector("#slide2").style.left =
  //   document.querySelector("ul.diary_list").offsetWidth + "px"; // offsetWidth : 마진값을 제외한 너비값을 계산

  // origin_slide.classList.add("original");
  // clone_slide.classList.add("clone");

  $("html,body")
  .stop()
  .animate(
    {
      scrollTop: $(window).height() * -1,
    },
    1000
  );
console.log($(window).height() * idx);
  });//

// daily 일기화면(hover) - by.윤아
$(".diary_viewport").on("mouseenter", "li.d_slide > img", function (e) {
  // 복제된 diary_slides에는 mouseenter 이벤트가 적용되지 않아 위임 방식을 사용하여, 부모 요소인 .diary_viewport에 이벤트를 바인딩함

  //1. 백그라운드 바꾸기
  var img_src = e.target.src;
  $(".diary_noH").css({
    "background-image": `url(${img_src})`,
    "background-repeat": "no-repeat",
    "background-position": "center center",
    "background-size": "cover",
  });

  //1.제목숨기기 title, period 숨기기
  $(".diary_titlebox").addClass("hide");

  //2. 일기글 보이기
  $(".diary_textbox").addClass("on");

  //3. 댓글 아이콘 활성화
  $(".reply_icon").addClass("show");

  //4.필터 활성화
  $(".filter").addClass("on");
  $(".diary_viewport").addClass("black");
});

//슬라이드 전체 호버시 효과(제목 및 댓글 아이콘 따로 처리)
$(".diary_slides").on("mouseleave", function () {
  //배경화면 색 변경하기
  $(".diary_noH").css({
    background: "#efeee9",
  });

  // title, period 보이기
  $(".diary_titlebox").removeClass("hide");

  // 댓글 아이콘 비활성화
  $(".reply_icon").removeClass("show");

  //2. 일기글 숨기기
  $(".diary_textbox").removeClass("on");

  //4.필터 비활성화
  $(".filter").removeClass("on");
  $(".diary_viewport").removeClass("black");
});

