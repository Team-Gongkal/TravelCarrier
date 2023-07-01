//계정관리 모달창 활성화 - by 윤아
$("#edit_profile").on("click", function () {
  $(".edit_profile").addClass("show");
});
$(".edit_profile ul li")
  .last()
  .on("click", function () {
    $(".edit_profile").removeClass("show");
  });

//기간검색 모달창 활성화 - by 윤아
$(".search_period").on("click", function () {
  $(".period_modal_bg").addClass("show");
});

$(".period_modal_bg .close, #search_period").on("click", function () {
  $(".period_modal_bg").removeClass("show");
});

//친구목록 활성화
$(".travlar_option button").on("click", function (e) {
  $(e.target).addClass("on");
  $(e.target).siblings("button").removeClass("on");
});

//스크롤 변화에 따른 top높이 설정 - by윤아
addEventListener("mousewheel", (e) => {
  const direction = e.deltaY > 0 ? "Scroll Down" : "Scroll Up";

  // 방향과 현 스크롤 위치
  console.log(direction, $("div.userProfile_scroll").scrollY);
});

// 스크롤에 따른 드롭다운 메뉴 구현 -by 윤아
const userProfileScroll = $(".userProfile_scroll");
let drop_menu = $(".drop_menu");
let top_height = $(".userProfile_top").height();
let drop_height = top_height - drop_menu.height();
console.log(
  "top높이 : " +
    top_height +
    " - 드롭메뉴높이 : " +
    drop_menu.height() +
    "= drop높이 : " +
    drop_height
);
// 스크롤 이벤트 핸들러
userProfileScroll.on("scroll", function () {
  const scrollTop = userProfileScroll.scrollTop();
  console.log("스크롤 양:", scrollTop);
  console.log(
    "top높이 : " +
      top_height +
      " - 드롭메뉴높이 : " +
      drop_menu.height() +
      "= drop높이 : " +
      drop_height
  );
  $(".moving_bg").css("objectPosition", `center ${50 + scrollTop / 100}%`);
  $(".userProfile_bg")
    .stop()
    .animate({ height: top_height - scrollTop }, 0);

  if (scrollTop >= drop_height) {
    drop_menu.addClass("drop");
    $(".userProfile_bg").css("height", drop_menu.height());
    $(".utill_menu").addClass("hide");
    $(".gap").addClass("on");
  }
  if (scrollTop < drop_height) {
    drop_menu.removeClass("drop");
    $(".utill_menu").removeClass("hide");
    $(".gap").removeClass("on");
  }
  if (scrollTop == 0) {
    $(".userProfile_bg").css("height", "40%");
  }
});

//탭메뉴 활성화 -by윤아
var tab = $(".userProfile_tab > ul > li");
var contents = $(".userProfile_gallery > ul");
var $scroll = $("#userProfile_wrap > div.userProfile_scroll");

tab.click(function () {
  var idx = $(this).index();

  tab.removeClass("on");
  contents.removeClass("show");
  $scroll.addClass("hide");
  $(".search_period, .travlar_option").removeClass("show");
  console.log(idx + "왜이래");

  $(this).addClass("on");
  contents.eq(idx).addClass("show");

  if (idx < 3) {
    if (contents.eq(idx).children("li").length >= 5) {
      $scroll.eq(idx).removeClass("hide");
    }
    $(".search_period").addClass("show");
    console.log("idx0-2 = " + idx);
  } else if (idx == 3) {
    if (contents.eq(idx).children("li").length >= 12) {
      $scroll.eq(idx).removeClass("hide");
    }
    $(".travlar_option").addClass("show");
    console.log("idx3 = " + idx);
  }
});

// datepicker 설정 및 옵션 변경 - by윤아
$(document).ready(function () {
  $.datepicker.setDefaults($.datepicker.regional["ko"]);
  $("#sdate").datepicker({
    // showOn: 'both',
    // buttonImage: "/", // 버튼 이미지
    // buttonImageOnly: true, // 버튼에 있는 이미지만 표시한다.
    changeMonth: true,
    changeYear: true,
    nextText: "다음달",
    prevText: "이전달",
    dayNames: [
      "일요일",
      "월요일",
      "화요일",
      "수요일",
      "목요일",
      "금요일",
      "토요일",
    ],
    dayNamesMin: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
    monthNamesShort: [
      "1월",
      "2월",
      "3월",
      "4월",
      "5월",
      "6월",
      "7월",
      "8월",
      "9월",
      "10월",
      "11월",
      "12월",
    ],
    monthNames: [
      "1월",
      "2월",
      "3월",
      "4월",
      "5월",
      "6월",
      "7월",
      "8월",
      "9월",
      "10월",
      "11월",
      "12월",
    ],
    dateFormat: "yy-mm-dd",
    showMonthAfterYear: true,
    showOtherMonths: true,
    yearRange: "1960:2023", //연도 범위
    todayHighlight: true, //오늘 날짜 표시
    maxDate: 0, //선택할 수 있는 최소 날짜로 (0: 오늘 이후 날짜 선택 불가하도록 함)
    onClose: function (selectedDate) {
      //시작일(startDate) datepicker가 닫힐때
      //종료일(endDate)의 선택할수있는 최소 날짜(minDate)를 선택한 시작일로 지정
      $("#edate").datepicker("option", "minDate", selectedDate);
      if ($("#edate").val() != "") $("#periodValidation").text("");
    },
  });
  //end Date
  $("#edate").datepicker({
    // showOn: 'both',
    // buttonImage: "/", // 버튼 이미지
    // buttonImageOnly: true, // 버튼에 있는 이미지만 표시한다.
    changeMonth: true,
    changeYear: true,
    nextText: "다음달",
    prevText: "이전달",
    dayNames: [
      "일요일",
      "월요일",
      "화요일",
      "수요일",
      "목요일",
      "금요일",
      "토요일",
    ],
    dayNamesMin: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
    monthNamesShort: [
      "1월",
      "2월",
      "3월",
      "4월",
      "5월",
      "6월",
      "7월",
      "8월",
      "9월",
      "10월",
      "11월",
      "12월",
    ],
    monthNames: [
      "1월",
      "2월",
      "3월",
      "4월",
      "5월",
      "6월",
      "7월",
      "8월",
      "9월",
      "10월",
      "11월",
      "12월",
    ],
    dateFormat: "yy-mm-dd",
    showMonthAfterYear: true,
    showOtherMonths: TransformStreamDefaultController,
    yearRange: "1960:2023", //연도 범위
    todayHighlight: true, //오늘 날짜 표시
    maxDate: 0, //선택할 수 있는 최소 날짜로 (0: 오늘 이후 날짜 선택 불가하도록 함)
    onClose: function (selectedDate) {
      //시작일(startDate) datepicker가 닫힐때
      //종료일(endDate)의 선택할수있는 최소 날짜(minDate)를 선택한 시작일로 지정
      $("#sdate").datepicker("option", "maxDate", selectedDate);
      if ($("#sdate").val() != "") $("#periodValidation").text("");
    },
  });
  //input태그 옆의 이미지 클릭시 datepicker열기 - by윤아
  $(".dateClick").on("click", function (e) {
    $(e.target).prev("input").focus();
    console.log(e.target);
  });
});
