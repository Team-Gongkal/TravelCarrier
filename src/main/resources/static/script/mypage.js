// mypage의 기본적 css동작을 관장하는 리스너를 등록하는 js파일 입니다.

//계정관리 모달창 활성화 - by 윤아
$("#edit_menu").on("click", function () {
  $(".edit_menu").addClass("show");
});
$(".edit_menu ul li").on("click", function () {
  $(".edit_menu").removeClass("show");
});

//기간검색 모달창 활성화 - by 윤아
$(".search_period").on("click", function () {
  $(".period_modal_bg").addClass("show");
});

$(".period_modal_bg .close").on("click", function () {
  $(".period_modal_bg").removeClass("show");
});

//프로필 편집 모달창 활성화 - by 윤아
$("#edit_profile").on("click", function () {
  $(".edit_modal").addClass("show");
});

$(".clos").on("click", function () {
  $(".edit_modal").removeClass("show");
});

//팔로우/팔로워 탭 - by 윤아
var following_content = $(".userProfile_traveler > ul");
$("#follower").on("click", function () {
  following_content.removeClass("show ani");
  following_content.filter(".follower").addClass("show");
  following_content.addClass("ani");
  //getTravelerPage("tra", "follower", 1);
});
$("#follow").on("click", function () {
  following_content.removeClass("show ani");
  following_content.filter(".follow").addClass("show");
  following_content.addClass("ani");
  //getTravelerPage("tra", "following", 1);
});

//친구목록 버튼 활성화
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
// 스크롤 이벤트 핸들러
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

userProfileScroll.on("scroll", function () {
  const scrollTop = userProfileScroll.scrollTop();
  console.log("스크롤 양:", scrollTop);
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

//메인 탭메뉴 활성화 -by윤아
var $scroll = $("#userProfile_wrap > div.userProfile_scroll");
var tab = $(".userProfile_tab > ul > li");
var contents = $(".userProfile_gallery").children("ul,div");
var idx = $(".userProfile_tab > ul > li.on").index();
var tab_li = contents.eq(idx).children("li").length;
on_scroll(idx, tab_li); //화면 로드시 실행해서 보여줌

//탭실행
tab.on("click", function () {
  idx = $(this).index(); //현재 클릭한 탭의 idx

  tab.removeClass("on"); //선택한탭 css 제거
  contents.removeClass("show ani"); // 현재 보여지고있는 콘텐츠 숨기기+애니메이션 클래스 제거

  $(this).addClass("on"); //선택한 탭 css추가
  contents.eq(idx).addClass("show ani"); //idx에 해당하는 콘텐츠 보이기
});

//li개수에 따라 스크롤 생성
function on_scroll() {
  $scroll.addClass("hide"); //스크롤 전체 제거

  if (idx < 3) {
    console.log("3번이하선택");
    $(".travlar_option").removeClass("show");
    $(".search_period").addClass("show");
    if (tab_li >= 5) {
      console.log("게시글이 5이하");
      $scroll.removeClass("hide");
    }
    $(".search_period").addClass("show");
  } else if (idx == 3) {
    console.log("트레블러 선탣");
    $(".search_period").removeClass("show");
    $(".travlar_option").addClass("show");
    if (tab_li >= 12) {
      console.log("친구없음 나가");
      $scroll.removeClass("hide");
    }
  } else if (idx > 16) {
    // 게시글 개수에 따른 더보기버튼 활성화,,
    $("#more_btn").addClass("show");
  }
}

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

//(⚙️ 설정)--------------------------------------
//탭메뉴 활성화
var settingTab = $(".edit_modal_nav > ul> li"); //탭메뉴 리스트
var settingCon = $(".edit_modal_con > ul > li"); //탭메뉴에 해당하는 컨텐츠

//탭실행
settingTab.each((idx) => {
  $(settingTab)
    .eq(idx)
    .on("click", function () {
      console.log(idx);
      //선택된 탭메뉴 리스트 표시
      settingTab.removeClass("on");
      $(this).addClass("on");
      //해당 콘텐츠 보이기
      $(settingCon).removeClass("show");
      $(settingCon).eq(idx).addClass("show");
    });
});

//(설정-1) 프로필 이미지 변경
var choose_profile = $(
  ".choose_profile > ul.edit_profile_padding > li > label > div"
);
var choose_background = $(".choose_bg > ul.edit_profile_padding > li");

//[1] 선택한 라디오버튼 표시하기
function chooseOn() {
  console.log(this.value + "라디오선택됨");
  if ($(this).is("input:radio[name=choose_profile]")) {
    // (1)선택된 라디오버튼이 프로필 변경일 경우
    choose_profile.removeClass("on");
    $(this).siblings("label").children("div").addClass("on");
  }
  if ($(this).is("input:radio[name=choose_bg]")) {
    // (2)선택된 라디오버튼이 배경 변경일 경우
    choose_background.removeClass("on");
    $(this).parents("li").addClass("on");
  }
}
// [1]번 실행하긴
$(".edit_profile input[type=radio]").on("change", chooseOn);

//[2]프로필과 배경이미지 변경 라디오버튼 선택시
function clickUpload() {
  console.log("업로드선택함");

  //(1)프로필 이미지 변경
  if (this.value === "upload_profile") {
    console.log("프로필 이미지 바껴라");
    // 크로퍼 열기 + 파일선택 인풋창 실행
    $("#profile_img_change").click();
  } else if (this.value === "upload_bg") {
    //(2) 배경 이미지 변경
    console.log("배경 이미지 바껴라");
    $("#profile_bg_change").click();
  }
}
// [2]번 실행하기
$(".edit_profile input[type=radio]").on("click", clickUpload);
