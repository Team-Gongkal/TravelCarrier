// 모달 비활성화 - by.서현
$(document).on("click", ".close", function () {
  closeModal();
});
console.log(url);
function closeModal() {
  $(".keyword_modal_bg").removeClass("show");
  $(".keyword_card").empty();
}

// 모달 활성화시 이전의 데이터 표시 - by.서현
function openModal() {
  $(".keyword_card").empty();
  $(".keyword_modal_bg").addClass("show");
  console.log(key_index);
  console.log(all_keywords[key_index]);
  if (all_keywords[key_index]) {
    var keyword_list = all_keywords[key_index];
    $.each(keyword_list, function (index, text) {
      // 등록된 키워드 세팅
      var newKeyword = $("<li></li>").addClass("card");
      var textspan = $("<span>").text(text);
      var span = $("<span>")
        .addClass("card_del")
        .append($("<i>").addClass("xi-close"));
      newKeyword.append(textspan);
      newKeyword.append(span);
      $(".keyword_card").append(newKeyword);
    });
  }
}

var url = window.location.href; //현재문서의 url가져오기

$(document).ready(function () {
  //위클리에서 데일리로 넘어가는 경로 설정 - by.윤아
  $(".daily_path").attr("href", url + "/daily");
  // text 줄바꿈처리 - by.서현
  // HTML 엔티티 변환 함수
  function htmlentities(str) {
    return String(str)
      .replace(/&/g, "&amp;")
      .replace(/"/g, "&quot;")
      .replace(/'/g, "&#39;")
      .replace(/</g, "&lt;")
      .replace(/>/g, "&gt;");
  }
  // 태그 제거 및 HTML 엔티티 변환
  var originText = $(".weekly_addText p").text();
  var filteredText = htmlentities(originText);
  var changeText = filteredText.replace(/\\n/g, "<br>");
  $(".weekly_addText p").html(changeText);

  // 사진 크기에 따라 클래스명 변경 - by.서현
  $(".longBox img").each(function (index) {
    const $img = $(this);
    const img = new Image();
    img.src = $(this).attr("src");
    console.log("---------------");
    img.onload = function () {
      var ratio = img.width / img.height;
      console.log(ratio);
      if (0.9 <= ratio && ratio <= 1.1) {
        $img.parent().addClass("w-sqr");
      } else if (ratio < 0.9) {
        $img.parent().addClass("w-rectL");
      } else if (1.1 < ratio) {
        $img.parent().addClass("w-rectW");
      }
    };
  });

  // 수정모드 - 이전에 추가한 동행인목록 로드 -by.서현
  sgowiths.forEach((fid) => {
    $('li[data-fid="' + fid + '"] input[type="checkbox"]').prop(
      "checked",
      true
    );
  });
  const checkboxes = document.querySelectorAll(
    '.serch_friends input[type="checkbox"]:checked'
  );
  checkboxes.forEach((checkbox) => {
    const li = checkbox.closest("li");
    document.querySelector(".checked_friends").appendChild(li);

    //이벤트 리스너 달아주기
    checkbox.addEventListener("change", (e) => {
      if (e.target.checked) {
        document.querySelector(".checked_friends").appendChild(li);
      } else {
        document.querySelector(".serch_friends").appendChild(li);
      }
    });
  });
});

// input 작성후 엔터시 키워드 추가 - by.서현
$('input[name="keyword"]').on("keydown", function (event) {
  if (event.key === "Enter") {
    // li가 9개면 실행 X
    if ($(".keyword_card li").length === 5) {
      alert("5개까지만 등록할 수 있습니다");
    } else {
      // 작성한 키워드를 저장 및 input 비우기
      var myKeyword = $('input[name="keyword"]').val();
      var $li = $(
        '<li class="card">' +
          "<span>" +
          myKeyword +
          "</span>" +
          '<span class="card_del"><i class="xi-close"></i></span></li>'
      );
      $(".keyword_card").append($li);
      $('input[name="keyword"]').val("");
    }
  }
});

// 삭제버튼 클릭시 키워드 삭제 - by.서현
$(document).on("click", ".card_del", function () {
  $(this).closest(".card").remove();
});

// 키워드를 배열로 관리 - by.서현
var key_index = -1;
var dailyId = -1;
// 키워드 추가 클릭할때마다 현재 키워드입력창의 index 저장 - by.서현
$(document).on("click", ".keyword", function () {
  key_index = $(this).closest(".weekly_wrap").index() - 1;
  dailyId = $(this).data("daily");
  openModal();
});

// all_keywords[key_index]에 저장 - by.서현
// [ [k1,k2,k3],[k4,k5,k6],[k7,k8,k9],[] ]
$(document).on("click", "#keyword_save", function (event) {
  //event.preventDefault();
  const keyword_list = [];
  $(".keyword_card li").each(function () {
    keyword_list.push($(this).text().trim());
  });
  all_keywords[key_index] = keyword_list;

  //ajax로 DB에 키워드 저장
  $.ajax({
    url: "/TravelCarrier/weekly/saveKeyword",
    type: "POST",
    data: JSON.stringify({ dailyId: dailyId, kwordList: keyword_list }),
    contentType: "application/json",
    success: function (data) {
      // 서버로부터 응답받은 데이터 처리
      closeModal();
      updateKeyword(keyword_list);
    },
    error: function (jqXHR, textStatus, errorThrown) {
      // 에러 처리
      alert("Error: " + textStatus + ": " + errorThrown);
    },
  });
});

// 키워드 리스트를 업데이트 - by.서현
function updateKeyword(keyword_list) {
  var keywordBox = $(".weekly_wrap").eq(key_index).find(".keywordBox");
  keywordBox.empty();

  if (keyword_list.length > 0) {
    $.each(keyword_list, function (index, text) {
      var ul = $("<ul>");
      var li = $("<li>").addClass("keyword").text(text);
      /*        var span = $("<span>").addClass("card_del").html('<i class="xi-close"></i>');
        li.append(span);*/
      ul.append(li);
      keywordBox.append(ul);
    });
  } else {
    var span = $("<span>").addClass("keyword").text("키워드 입력");
    keywordBox.append(span);
  }
}

var thumb_status = "ORIGIN"; //ORIGIN:원래파일 DELETE:파일삭제
// 이미지 삭제버튼 메소드 by서현
$(document).on("click", ".removeBtn", function (event) {
  thumb_status = "DELETE";
});

// 위클리 수정 ajax - by.서현
$(document).on("click", ".updateWeekly", function (event) {
  event.preventDefault();
  console.log("/TravelCarrier/weekly/" + weeklyId + "/update");
  // 제출전 유효성 검사, false면 제출 X
  if (!checkValidation()) return;

  // nation file sdate edate title text gowiths[] status
  var formData = new FormData();
  if ($(".thumbnail_img.circle img").attr("src") !=
    "/image/default/weekly_default_thumbnail.png") {
    formData.append("file", $("#thumbnail_change")[0].files[0]);
  }
  formData.append("nation", $('select[name="nation"] option:selected').attr("value"));
  formData.append("sdate", $("#sdate").val());
  formData.append("edate", $("#edate").val());
  if ($("div.title input").val() === "") {
    formData.append("title", $('select[name="nation"] option:selected').text());
  } else {
    formData.append("title", $("div.title input").val());
  }
  formData.append("text", $("#addText").val());

  $("#sc li:not(:last)").each(function () {
    formData.append("gowiths[]", $(this).data("fid"));
    console.log("fid: " + $(this).data("fid"));
  });
  formData.append("status", $("input[name='status']:checked").val());
  console.log("status : " + formData.get("status"));
  formData.append("thumbStatus", thumb_status);

  //버튼 저장중 처리
  var clickBtn = $(".updateWeekly");
  clickBtn.attr("disabled",true);
  clickBtn.toggleClass("btn_disable btn");
  clickBtn.html("저장중..");

  $.ajax({
    type: "POST",
    url: "/TravelCarrier/weekly/" + weeklyId + "/update",
    data: formData,
    processData: false,
    contentType: false,
    success: function (data) {
      alert("성공");
      clickBtn.attr("disabled",false);
      clickBtn.toggleClass("btn_disable btn");
      clickBtn.html("저장하기");
      location.replace("/TravelCarrier/weekly/" + data);
    },
    error: function (jqXHR, textStatus, errorThrown) {
      alert("실패");
    },
  });
});

//addText textarea높이 유동적으로 조절
var addTextHeight = $(".weekly_addText textarea").prop("scrollHeight");
var htmlfont = parseFloat($("html").css("font-size"));
var rem = 5 * htmlfont; //rem을 px로 변환
if (addTextHeight >= rem) {
  $(".weekly_addText textarea").css("height", "6rem");
}

// 위클리의 개수에 따라 goToFirst레이아웃 변경 - by윤아
var remain = $(".weekly_wrap > div").length % 2;
console.log("위클리갯수나누기" + remain);
if (remain === 0) {
  $(".weekly_finish_even").hide();
  $(".weekly_finish_odd").show();
} else if (remain === 1) {
  $(".weekly_finish_odd").hide();
  $(".weekly_finish_even").show();
}

//가로 슬라이드 구현 - by윤아
$(window).on("load", function () {
  var weekly_scroll = $(".weekly_scroll");
  var window_width = $(window).outerWidth();
  var header_width = $("header").outerWidth();
  console.log("보여지는 화면" + window_width); //1514화면의 너비

  //스크롤 포함 전체 너비 구하기
  var scroll_width = weekly_scroll.prop("scrollWidth");
  console.log("스크롤 전체너비" + scroll_width);

  //스크롤 양 체크하기
  var scroll = weekly_scroll.scrollLeft();
  console.log("스크롤 양" + scroll);

  let num = 0; //첫페이지에서 스크롤되는 횟수(첫페이지 동영상 크기 변환) -스크롤을 할 때마다 증가하며 그에 따라서 스크롤 양을 증가시키기

  let delta; //스크롤의 방향을 판별하기 위한 변수로 할당은 스크롤이벤트 안에서 함

  let moving_cnt = (scroll_width - window_width + header_width) / 100; //무빙카운트, 3114 - 화면너비1514 -헤더너비 = 1600/50 해야됨
  console.log(moving_cnt); //32번

  //mousewheel DOMMouseScroll 스크롤 이벤트
  $(window).on("wheel DOMMouseScroll", scrollHandler);

  //위클리 추가글 위에 mouseover시 스크롤 비활성화
  $(".weekly_addText > textarea, .utill_notice").on(
    "mouseenter mouseleave",
    function (event) {
      if (event.type === "mouseenter") {
        $(window).off("wheel DOMMouseScroll", scrollHandler);
      } else {
        $(window).on("wheel DOMMouseScroll", scrollHandler);
      }
    }
  );

  function scrollHandler(e) {
    delta = e.originalEvent.wheelDelta || e.originalEvent.delta * -1;

    if (delta < 0) {
      //마우스 휠 방향아래
      if (num <= moving_cnt) {
        num++;
        $(weekly_scroll)
          .stop()
          .animate({ scrollLeft: num * 100 }, 500, "easeOutCubic");
        console.log("+" + num);
      }
    } else if (delta > 0) {
      //마우스 휠 방향이 위일떄
      if (num >= 0) {
        num--;
        $(weekly_scroll)
          .stop()
          .animate({ scrollLeft: num * 100 - 100 }, 500, "easeOutCubic");
        console.log("-" + num);
      }
    }
  }

  //위클리 수정 모달 활성화
  $(".weekly_edit").on("click", function () {
    $(".weekly_modal_bg").addClass("show");
    console.log("모달활성화");
    $(window).off("wheel DOMMouseScroll", scrollHandler);
  });
  $(".modal_title > .close").on("click", function () {
    $(".weekly_modal_bg").removeClass("show");
    $(window).on("wheel DOMMouseScroll", scrollHandler);
  });
});
