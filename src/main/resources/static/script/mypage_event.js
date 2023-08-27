// mypage의 ajax를 관리하는 이벤트리스너를 등록하는 js파일입니다

$(document).ready(function() {
    // 페이지가 로드될 때 실행되는 함수
    getPage("dia", 1);
});


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
function getFollowPage(type, detailType, page) {
  $.ajax({
    url: "/TravelCarrier/mypage/page",
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

  if (data.hide == true) html += `<button type="button" >보이기</button>`;
  else if (data.hide == false)
    html += `<button type="button" class="weeklyHideBtn">숨기기</button>`;

  html += `</div>
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
                    </div>`;

  if (type == "following") {
    html += `
                    <div class="follower_del_btn">
                       <button><i class="fa-solid fa-user-minus fa-xs fa"></i>친구끊기</button>
                     </div>`;
  } else if (type == "follower") {
    html += `<div class="follower_add_btn">
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
$(document).on("click", ".weeklyHideBtn", function () {
  var title = $(this).closest("li").find(".uP_diary_tit").text();
  var wid = $(this).closest("li").data("wid");
  if (confirm("[ " + title + " ] 숨김처리 하시겠습니까?"))
    hideOrShowWeekly(wid, "hide");
});

// 태그된 위클리에 대해 숨김또는 보이기 하는 ajax (숨김:"hide" 보이기:"seek")
function hideOrShowWeekly(weeklyId, type) {
  $.ajax({
    url: "/TravelCarrier/weekly/" + weeklyId,
    type: "PUT",
    data: JSON.stringify({ type: type }),
    contentType: "application/json",
    success: function (data) {
      alert("숨김처리 되었습니다.");
      $("li[data-wid='" + weeklyId + "']").remove();
    },
    error: function (error) {
      alert("숨김처리에 실패하였습니다." + error);
    },
  });
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
    url: "/TravelCarrier/mypage/search/date",
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

// 프로필 이미지 선택시 checked - by 윤아
// $(`input:radio[name=choose_profile],input:radio[name=choose_bg]`).on(
//   "change",
//   function () {
//     var upload = $("input:radio[name=choose_profile]:checked").val();
//     var uploadbg = $("input:radio[name=choose_bg]:checked").val();

//     if ($(this).is(":checked")) {
//       if ($(this).is("input:radio[name=choose_profile]")) {
//         // choose_profile에 대한 동작 수행
//         $(".choose_profile ul li > label > div").removeClass("on");
//         $(this).siblings("label").children("div").addClass("on");
//       } else if ($(this).is("input:radio[name=choose_bg]")) {
//         // choose_bg에 대한 동작 수행
//         $(".choose_bg > ul > li").removeClass("on");
//         $(this).parents("li").addClass("on");
//         // $(this).siblings("label").children("div").addClass("on");
//       }
//     }
//     if (upload === "upload_profile") {
//       $("#profile_img_change").trigger("click");
//     }
//     if (uploadbg === "upload_bg") {
//       $("#profile_bg_change").trigger("click");
//     }
//   }
// );

// $(".choose_profile, .choose_bg").on("click", "ul > li", function () {
//   if ($(this).parent().hasClass("choose_profile")) {
//     $("#profile_img_change").trigger("click");
//   } else if ($(this).parent().hasClass("choose_bg")) {
//     $("#profile_bg_change").trigger("click");
//   }
// });

// 닉네임 바꾸기(10글자 제한) - by.서현
$(document).on("keyup",".info_text input", function(){
   var length = $(this).val().length;
   $(".msg_cnt").empty();
   $(".msg_cnt").append(`${length}<em>/10</em>`);

   if(length > 10) $(".msg_text").text("별명이 너무 길어요.");
   else if(length == 0) $(".msg_text").text("별명을 입력해주세요.");
   else $(".msg_text").text("사용 가능한 별명이에요!");
});


// 유효성 검사 - by.서현
function profileValidCheck(){
    var nickNameLength = $(".info_text input").val().length;
    if(nickNameLength > 10 || nickNameLength==0 ) return false;

    var data = {
        nickName : $(".info_text input").val()
    };
    return data;
}


// 내정보 수정 저장 - by.서현
$(document).on("click",".infoBtn button", function(){
    alert("클릭!");
    var data = profileValidCheck();
    if(data == false) return;

    //변경된 정보 저장하기
    $.ajax({
        type : "POST",
        url : "/TravelCarrier/member/info",
        data : JSON.stringify(data),
        contentType : "application/json",
        success : function(){
            alert("저장되었습니다.");
            switchNickName(data);
        },
        error : function(jqXHR, textStatus, errorThrown){
            alert("저장 실패");
        }

    })
});

function switchNickName(data){
    console.log(data.nickName);
    $(".info_text input:first").attr("placeholder", data.nickName);
    $(".my_profile_id span:not(#edit_menu)").text(data.nickName);
}