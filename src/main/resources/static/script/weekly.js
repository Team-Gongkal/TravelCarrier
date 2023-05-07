// 키워드 모달창 띄우기 - by.윤아
/*$(document).on("click", ".keyword", function () {
  openModal();
});*/

$(document).on("click", ".close", function () {
  closeModal();
});

console.log(url);
// 모달 비활성화 - by.서현
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
      var newKeyword = $("<li></li>").addClass("card").text(text);
      var span = $("<span>")
        .addClass("card_del")
        .append($("<i>").addClass("xi-close"));
      newKeyword.append(span);
      $(".keyword_card").append(newKeyword);
    });
  }
}

var url = window.location.href; //현재문서의 url가져오기


$(document).ready(function () {
  //위클리에서 데일리로 넘어가는 경로 설정 - by.윤아
  $(".daily_path").attr("href", url + "/daily");
  console.log(url);

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
    img.onload = function () {
      const width = img.width;
      const height = img.height;
      if (width > height) {
        $img.parent().addClass("w-rectW");
      } else if (height > width) {
        $img.parent().addClass("w-rectL");
      } else {
        $img.parent().addClass("w-sqr");
      }
    };
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
          myKeyword +
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
$(document).on("click", ".btn", function (event) {
  event.preventDefault();
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
      ul.append(li);
      keywordBox.append(ul);
    });
  } else {
    var span = $("<span>").addClass("keyword").text("키워드 입력");
    keywordBox.append(span);
  }
}
