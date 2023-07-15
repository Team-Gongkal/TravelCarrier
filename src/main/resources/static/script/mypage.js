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
var following_content = $(".userProfile_travler > ul");
$("#follower").on("click", function () {
  following_content.removeClass("show");
  following_content.filter(".follower").addClass("show");
  getTravelerPage("tra","follower",1);
});
$("#follow").on("click", function () {
  following_content.removeClass("show");
  following_content.filter(".follow").addClass("show");
  getTravelerPage("tra","following",1);
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

//탭메뉴 활성화 -by윤아
var $scroll = $("#userProfile_wrap > div.userProfile_scroll");
var tab = $(".userProfile_tab > ul > li");
var contents = $(".userProfile_gallery").children("ul,div");
console.log(contents);
var idx = $(".userProfile_tab > ul > li.on").index();
var tab_li = contents.eq(idx).children("li").length;
on_scroll(idx, tab_li); //화면 로드시 실행해서 보여줌

//탭실행
tab.on("click", function () {
  idx = $(this).index(); //현재 클릭한 탭의 idx

  tab.removeClass("on"); //선택한탭 css 제거
  contents.removeClass("show"); // 현재 보여지고있는 콘텐츠 숨기기

  $(this).addClass("on"); //선택한 탭 css추가
  contents.eq(idx).addClass("show"); //idx에 해당하는 콘텐츠 보이기
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



// 검색어를 바탕으로 위클리 검색 ajax - by.서현
$(document).ready(function () {
  $("#search").keypress(function (event) {
    if (event.which === 13) {
      // 엔터 키 눌렀을때 실행
      event.preventDefault();
      var searchKeyword = $(this).val();
      var type = $(".userProfile_tab li.on span").text().substring(0, 3); //tag 또는 dia 보내짐

      if (type == "rev") return;

      $.ajax({
        url: "/TravelCarrier/mypage/search",
        type: "POST",
        data: JSON.stringify({ type: type, keyword: searchKeyword }),
        contentType: "application/json",
        success: function (resp) {
          updateResult(type, resp);
        },
        error: function (error) {
          alert("실패");
        },
      });
    }
  });
});

// 각 탭을 클릭하면 해당 탭의 1페이지를 로드한다 - by.서현
$(".userProfile_tab li").on("click", function (e) {
  var type = $(this).find("span").text().substring(0, 3);
  if (type == "tra") {
    $("#search").attr("placeholder", "검색하기 (이름, 이메일)");
    $("#follower").trigger("click");
    return;
  } else if (type == "rev") {
    $("#search").attr("placeholder", "");
    return;
  } else $("#search").attr("placeholder", "검색하기 (제목, 국가명, 동행인)");

  var data = getPage(type, 1);
  updateResult(type, data);
  $("#search").val(""); // 입력 필드의 값을 빈 문자열로 설정
});

// 타입과 페이지를 파라미터로 해당 페이지를 get - by.서현
function getPage(type, page) {
  $.ajax({
    url: "/TravelCarrier/mypage/page",
    type: "POST",
    data: JSON.stringify({ type: type, page: page }),
    contentType: "application/json",
    success: function (resp) {
      updateResult(type, resp);
    },
    error: function (error) {
      alert("실패");
    },
  });
}
function getTravelerPage(type,detailType,page){
  $.ajax({
    url: "/TravelCarrier/mypage/page",
    type: "POST",
    data: JSON.stringify({ type: type, page: page, detailType : detailType}),
    contentType: "application/json",
    success: function (resp) {
      updateResult(detailType, resp);
    },
    error: function (error) {
      alert("실패");
    },
  });
}
// 결과를 바탕으로 html틀을 할당 - by.서현
function updateResult(type, data) {
  console.log(data);
  if (type == "dia") {
    console.log("dia 실행");
    $(".userProfile_diary").empty();
    if (data == null) return;
    for (var e of data) {
      $(".userProfile_diary").append(diaryHtml(e));
    }
  } else if (type == "tag") {
    console.log("tag 실행");
    $(".userProfile_tagged").empty();
    if (data == null) return;
    for (var e of data) {
      $(".userProfile_tagged").append(taggedHtml(e));
    }
  } else if (type == "following") {
        $(".follow").empty();
        if (data == null) return;
        for (var e of data) {
          $(".follow").append(travlerHtml(e, "following"));
        }
   } else if(type == "follower"){
        $(".follower").empty();
        if (data == null) return;
        for (var e of data) {
          $(".follower").append(travlerHtml(e, "follower"));
        }
    }


  //게시글 갯수에 따른 스크롤 활성화
  idx = $(".userProfile_tab > ul > li.on").index();
  tab_li = contents.eq(idx).children("li").length;
  console.log("탭번호 : " + idx + "/ 자식요소개수 : " + tab_li);
  on_scroll();
}

// 검색결과를 바탕으로 diary탭의 html을 생성 - by.서현
function diaryHtml(data) {
console.log(data);
  var html = `
    <li data-wid="${data.id}" >
      <div class="uP_diary_thumbnail">
        <a href='/TravelCarrier/weekly/${data.id}'>
          <img src="${data.thumbPath}" alt="썸네일" class="moving_bg">
        </a>
      </div>
      <div class="uP_diary_box">
        <div class="uP_diary_companion">
          <ul class="sel_companion">`;

  for (var thumbPath of data.goWithList) {
    html += `<li style="background-image: url(${thumbPath});" class="circle"></li>`;
  }

  html += `
          </ul>
        </div>
        <div class="uP_diary_text">
          <span class="uP_diary_tit">${data.title}</span>
          <span class="uP_diary_period">${data.date.sdate} - ${data.date.edate}</span>
        </div>
        <div class="uP_diary_btn">
          <a href='/TravelCarrier/weekly/${data.id}'>수정하기</a>
          <button type="button" class="weeklyDelBtn" >삭제하기</button>
        </div>
      </div>
    </li>`;

  return html;
}

// 검색결과를 바탕으로 tagged탭의 html을 생성 - by.서현
function taggedHtml(data) {
  var html = `
    <li data-wid="${data.id}">
      <div class="uP_diary_thumbnail">
        <a href='/TravelCarrier/weekly/${data.id}'>
          <img src="${data.thumbPath}" alt="썸네일" class="moving_bg">
        </a>
      </div>
      <div class="uP_diary_box">
        <div class="uP_diary_companion">
          <ul class="sel_companion">`;

  for (var thumbPath of data.goWithList) {
    html += `<li style="background-image: url(${thumbPath});" class="circle"></li>`;
  }

  html += `
          </ul>
        </div>
        <div class="uP_diary_text">
          <span class="uP_diary_tit">${data.title}</span>
          <span class="uP_diary_period">${data.date.sdate} - ${data.date.edate}</span>
        </div>
        <div class="uP_diary_btn">
          <a href='/TravelCarrier/weekly/${data.id}'>수정하기</a>`;

  if(data.hide == true) html += `<button type="button" >보이기</button>`;
  else if(data.hide == false) html += `<button type="button" class="weeklyHideBtn">숨기기</button>`;

  html += `</div>
      </div>
    </li>`;

  return html;
}

// 검색결과를 바탕으로 travler탭의 html을 생성 - by.서현
function travlerHtml(data, type) {
  var html = `<li>
                  <div class="uP_diary_thumbnail">
                    <a href="'TravelCarrier/member/' + ${data.id}">
                      <img src="${
                        data.backgroundThumbPath != null
                          ? data.backgroundThumbPath
                          : "/image/default/default_bg.jpg"
                      }"
                        alt="썸네일" class="moving_bg">
                    </a>
                  </div>
                  <div class="uP_user_box">
                    <div class="uP_user_profileImg">
                      <div class="my_profile_img circle">
                        <a href="TravelCarrier/member/${data.id}">
                          <img
                            src="${data.thumbPath}"
                            src="/image/mypage/profile.jpg" alt="프로필 이미지">
                        </a>
                      </div>
                    </div>

                    <div class="uP_user_text">
                      <span class="uP_user_name">${data.name}</span>
                      <span class="uP_user_added">${data.fdate}</span>
                    </div>`;

                  if(type == "following"){
                    html += `
                    <div class="follower_del_btn">
                       <button><i class="fa-solid fa-user-minus fa-xs fa"></i>친구끊기</button>
                     </div>`;
                  }
                  else if(type == "follower"){
                    html += `<div class="follower_del_btn">
                      <button><i class="fa-solid fa-user-minus fa-xs fa"></i>친구신청</button>
                    </div>`;
                  }
          html += `</div>
                </li>`;

  return html;
}

//친구추가 버튼
function add_friend() {
  var add_friend_btn = $(".add_friend_btn > button");
  add_friend_btn.addClass("completed");
  add_friend_btn.children("span").text("follow");
  add_friend_btn.children("i").attr("class", "fa-solid fa-check fa-xs fa");
}

// 위클리 삭제 클릭 이벤트 - by.서현
$(document).on("click", ".weeklyDelBtn", function () {
  var title = $(this).closest("li").find(".uP_diary_tit").text();
  var wid = $(this).closest("li").data("wid");
  if (confirm("[ " + title + " ] 삭제하시겠습니까?")) deleteWeekly(wid);
});

function deleteWeekly(weeklyId) {
  $.ajax({
    url: "/TravelCarrier/weekly/" + weeklyId,
    type: "DELETE",
    processData: false,
    contentType: false,
    success: function (data) {
      $("li[data-wid='" + weeklyId + "']").remove();
    },
    error: function (error) {
      alert("삭제 실패" + error);
    },
  });
}


// 태그된 위클리 숨기기 이벤트 - by.서현
$(document).on("click", ".weeklyHideBtn", function() {
  var title = $(this).closest("li").find(".uP_diary_tit").text();
  var wid = $(this).closest("li").data("wid");
  if (confirm("[ " + title + " ] 숨김처리 하시겠습니까?")) hideOrShowWeekly(wid,"hide");
});

// 태그된 위클리에 대해 숨김또는 보이기 하는 ajax (숨김:"hide" 보이기:"seek")
function hideOrShowWeekly(weeklyId,type) {
    $.ajax({
        url: "/TravelCarrier/weekly/"+weeklyId,
        type: "PUT",
        data :  JSON.stringify({ "type": type }),
        contentType: "application/json",
        success: function (data) {
            alert("숨김처리 되었습니다.");
            $("li[data-wid='" + weeklyId + "']").remove();
        },
        error: function (error) {
            alert("숨김처리에 실패하였습니다."+error);
        }
    });
}

// 기간 선택 이벤트
  $(".inquire_period li").on("click", function() {
    // 기존에 on 클래스가 붙어있는 li 요소의 on 클래스를 제거
    $(".inquire_period li.on").removeClass("on");
    $(this).addClass("on");

    $("#edate").val("");
    $("#sdate").val("");
  });

// 기간입력시 <기간없음> 자동선택
$("#edate, #sdate").datepicker({
    onSelect: function(dateText, inst) {
        $(".inquire_period li.on").removeClass("on");
        $('.inquire_period li:first').addClass("on")
    }
});
// 기간 검색 이벤트
$(document).on("click","#search_period", function(){
    if (!dateValidate()) return;
    // type, sdate, edate
    var sdate;
    var edate;
    if($('.inquire_period li:first').hasClass("on")) {
        sdate = $('#sdate').val();
        edate = $('#edate').val();
    } else{
        var dateArr = getDate($('.inquire_period li.on').text());
        sdate = dateArr.sdate;
        edate = dateArr.edate;
    }

    var type = $(".userProfile_tab li.on span").text().substring(0, 3);
    searchDate(type, sdate, edate);
});

function searchDate(type, sdate, edate){
    $.ajax({
        url: "/TravelCarrier/mypage/search/date",
        type: "POST",
        data :  JSON.stringify({ type:type, sdate: sdate, edate : edate }),
        contentType: "application/json",
        success: function (resp) {
        console.log(resp);
            $(".period_modal_bg").removeClass("show");
            updateResult(type, resp);
        },
        error: function (error) {
            alert("실패"+error);
        }
    });
}


function dateValidate(){
    // 기간선택 검사
    if($('.inquire_period li:first-child').hasClass("on")) {
        if($('#sdate').val()=="" || $('#edate').val() =="") {
            alert("날짜가 선택되지 않았습니다");
            return false;
        }
    } else{
        if($('.inquire_period li.on').length == 0) return false;
    }

    return true;
}

function getDate(btn){
    var date = new Date();
    var year = date.getFullYear();
    var month = ('0' + (date.getMonth() + 1)).slice(-2);
    var day = ('0' + date.getDate()).slice(-2);
    var edate = `${year}-${month}-${day}`;

    if(btn == "1주일") day = ('0' + (date.getDate()-7)).slice(-2);
    else if(btn == "1개월") month = ('0' + (date.getMonth()+1 - 1)).slice(-2);
    else if(btn == "3개월") month = ('0' + (date.getMonth()+1 -3)).slice(-2);
    else if(btn == "6개월") month = ('0' + (date.getMonth()+1 - 6)).slice(-2);
    else if(btn == "지난 1년") year = date.getFullYear()-1;

    var sdate = `${year}-${month}-${day}`;
    result = {"sdate" : sdate, "edate" : edate};
    return  result;
}