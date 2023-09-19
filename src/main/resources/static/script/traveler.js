// 트래블러의 페이지를 보여주는 이벤트리스너를 관리하는 js입니다


$(document).ready(function() {
    // 페이지가 로드될 때 실행되는 함수
    $('.userProfile_tab li.on').trigger('click'); // 탭 클릭
});


//현재 방문한 트래블러 페이지의 주인의 이메일
let TravelerEmail = $(location).attr('href').substring( $(location).attr('href').indexOf("/member/")+8 );


//팔로우/팔로워 탭 - by 윤아
$("#follower").on("click", function () {
  getFollowPage("tra", "follower", 1);
});
$("#follow").on("click", function () {
  getFollowPage("tra", "following", 1);
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
        url: "/mypage/search",
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
    $("#search").attr("placeholder", "리뷰검색");
    return;
  } else $("#search").attr("placeholder", "검색하기 (제목, 국가명, 동행인)");

  var data = getTravelerPage(type, 1);
  updateResult(type, data);
  $("#search").val(""); // 입력 필드의 값을 빈 문자열로 설정
});

// 타입과 페이지를 파라미터로 해당 페이지를 get - by.서현
function getTravelerPage(type, page) {
  $.ajax({
    url: "/member/"+TravelerEmail+"/page",
    type: "POST",
    data: JSON.stringify({ userEmail:TravelerEmail, type: type, page: page }),
    contentType: "application/json",
    success: function (resp) {
      updateResult(type, resp);
    },
    error: function (error) {
      alert("실패");
    },
  });
}

function getFollowPage(type, detailType, page) {
  $.ajax({
    url: "/member/"+TravelerEmail+"/page",
    type: "POST",
    data: JSON.stringify({ type: type, page: page, detailType: detailType }),
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
  if (type == "dia") {
    $('#diary_num').text("("+data.length+")");
    $(".userProfile_diary").empty();
    if (data == null) return;
    for (var e of data) {
      $(".userProfile_diary").append(diaryHtml(e));
    }
  } else if (type == "tag") {
    console.log("tag 실행");
    $(tagged_num).text("(" + 100 + ")");
    $(".userProfile_tagged").empty();
    if (data == null) return;
    for (var e of data) {
     console.log("태그데이터!! ");
      console.log(e);
      $(".userProfile_tagged").append(taggedHtml(e));
    }
  } else if (type == "following") {
    $('#traveler_num').text("("+data.length+")");
    $(".follow").empty();
    if (data == null) return;
    for (var e of data) {
      $(".follow").append(travelerHtml(e, "following"));
    }
  } else if (type == "follower") {
    $(".follower").empty();
    if (data == null) return;
    for (var e of data) {
      $(".follower").append(travelerHtml(e, "follower"));
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
        <a href='/weekly/${data.id}'>
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
      </div>
    </li>`;

  return html;
}

// 검색결과를 바탕으로 tagged탭의 html을 생성 - by.서현
function taggedHtml(data) {
  var html = `
    <li data-wid="${data.id}">
      <div class="uP_diary_thumbnail">
        <a href='/weekly/${data.id}'>
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
      </div>
    </li>`;

  return html;
}

// 검색결과를 바탕으로 traveler탭의 html을 생성 - by.서현
function travelerHtml(data, type) {
  var html = `<li>
                  <div class="uP_diary_thumbnail">
                    <a href="${data.email}">
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
                        <a href="${data.email}">
                          <img
                            src="${data.thumbPath}"
                            src="/image/mypage/profile.jpg" alt="프로필 이미지">
                        </a>
                      </div>
                    </div>

                    <div class="uP_user_text">
                      <span class="uP_user_name">${data.name}</span>
                      <span class="uP_user_added">${data.fdate}</span>
                    </div></li>`;

  return html;
}

// 기간 선택 이벤트
$(".inquire_period li").on("click", function () {
  // 기존에 on 클래스가 붙어있는 li 요소의 on 클래스를 제거
  $(".inquire_period li.on").removeClass("on");
  $(this).addClass("on");

  $("#edate").val("");
  $("#sdate").val("");
});

// 기간입력시 <기간없음> 자동선택
$("#edate, #sdate").datepicker({
  onSelect: function (dateText, inst) {
    $(".inquire_period li.on").removeClass("on");
    $(".inquire_period li:first").addClass("on");
  },
});
// 기간 검색 이벤트
$(document).on("click", "#search_period", function () {
  if (!dateValidate()) return;
  // type, sdate, edate
  var sdate;
  var edate;
  if ($(".inquire_period li:first").hasClass("on")) {
    sdate = $("#sdate").val();
    edate = $("#edate").val();
  } else {
    var dateArr = getDate($(".inquire_period li.on").text());
    sdate = dateArr.sdate;
    edate = dateArr.edate;
  }

  var type = $(".userProfile_tab li.on span").text().substring(0, 3);
  searchDate(type, sdate, edate);
});

function searchDate(type, sdate, edate) {
  $.ajax({
    url: "/mypage/search/date",
    type: "POST",
    data: JSON.stringify({ type: type, sdate: sdate, edate: edate }),
    contentType: "application/json",
    success: function (resp) {
      console.log(resp);
      $(".period_modal_bg").removeClass("show");
      updateResult(type, resp);
    },
    error: function (error) {
      alert("실패" + error);
    },
  });
}

function dateValidate() {
  // 기간선택 검사
  if ($(".inquire_period li:first-child").hasClass("on")) {
    if ($("#sdate").val() == "" || $("#edate").val() == "") {
      alert("날짜가 선택되지 않았습니다");
      return false;
    }
  } else {
    if ($(".inquire_period li.on").length == 0) return false;
  }

  return true;
}

function getDate(btn) {
  var date = new Date();
  var year = date.getFullYear();
  var month = ("0" + (date.getMonth() + 1)).slice(-2);
  var day = ("0" + date.getDate()).slice(-2);
  var edate = `${year}-${month}-${day}`;

  if (btn == "1주일") day = ("0" + (date.getDate() - 7)).slice(-2);
  else if (btn == "1개월") month = ("0" + (date.getMonth() + 1 - 1)).slice(-2);
  else if (btn == "3개월") month = ("0" + (date.getMonth() + 1 - 3)).slice(-2);
  else if (btn == "6개월") month = ("0" + (date.getMonth() + 1 - 6)).slice(-2);
  else if (btn == "지난 1년") year = date.getFullYear() - 1;

  var sdate = `${year}-${month}-${day}`;
  result = { sdate: sdate, edate: edate };
  return result;
}

// 친구추가 이벤트
$('.add_friend_btn > button').on('click', function(){
    add_friend();
})

//친구추가 버튼
function add_friend() {
  var add_friend_btn = $(".add_friend_btn > button");
  if (add_friend_btn.hasClass("completed")) { //친구일때 친구취소시 동작
        unfollowTarget();
        add_friend_btn.removeClass("completed");
        add_friend_btn.children("span").text("follow");
        add_friend_btn.children("i").attr("class", "fa-solid fa-check fa-xs fa");
  } else { // 친구 아닐때 친구신청시
        followingTarget();
        add_friend_btn.addClass("completed");
        add_friend_btn.children("span").text("following");
        add_friend_btn.children("i").attr("class", "fa-solid fa-check fa-xs fa");
  }
}

function followingTarget() {
  $.ajax({
    url: "/member/following/"+TravelerEmail,
    type: "GET",
    success: function (resp) {
      console.log("성공");
    },
    error: function (error) {
      alert("실패" + error);
    },
  });
}

function unfollowTarget() {
  $.ajax({
    url: "/member/unfollow/"+TravelerEmail,
    type: "GET",
    success: function (resp) {
      console.log("성공");
    },
    error: function (error) {
      alert("실패" + error);
    },
  });
}