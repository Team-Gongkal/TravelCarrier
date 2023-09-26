// var currentIdx = 0;
function show(page,id) {
  data = page[id];
  //[1]진행도 표시(id)
  $(".tt_prograss span").text(data.id + " / " + page.length);
  //[2]제목 (title)
  $(".tt_title > h4").text(data.title);
  //[3]설명글(text)
  $(".tt_explain > p").html(data.text);
  //[4-1]모든 element에 tuto클래스 제거하기
  page.forEach(function (all) {
    //all:각요소 순회
    $(all.element).removeClass("tuto");
  });
  //[4-2]해당 element에 tuto클래스 추가하기
  $(data.element).addClass("tuto");
  //[5]위치값 적용 방법(x,y사용)
  $(".tooltip").css({
    //방향이 해당되면 값을 적용, 일치하지 않을 경우 auto값 적용
    left: data.positionX === "left" ? data.x + "%" : "auto",
    right: data.positionX === "right" ? data.x + "%" : "auto",
    top: data.positionY === "top" ? data.y + "%" : "auto",
    bottom: data.positionY === "bottom" ? data.y + "%" : "auto",
  });
  //[6]말풍선 꼬리 위치 지정
  $("#arrow").attr("class", data.arrow);
  var tail = data.arrow;
  if ($("#arrow").hasClass(tail)) {
    // $("#arrow").css({ [data.arrowTail] });
    $("#arrow").css(data.arrowTail);
  }
}
function prevBtnClick(page){
  $(".tt_prev").click(function (event) {
    currentIdx--;
    if (currentIdx < 0) {
      currentIdx = 0;
      event.preventDefault();
      return false;
    }
    show(page,currentIdx);
  });
  }

function nextBtnClick(page){
    //다음버튼 클릭시
    $(".tt_next").click(function (event) {
      currentIdx++;
      if (currentIdx >= page.length) {
        currentIdx = page.length - 1;
        event.preventDefault();
        return false;
      }
      show(page,currentIdx);
    });
}

function closeTutorial(page){
  // 모달 닫기
  $(document).on("click", "#close_tutorial", function () {
    $("#tutorial").removeClass("show");
    //모든 element에 tuto클래스 제거하기
    page.forEach(function (all) {
      //all:각요소 순회
      $(all.element).removeClass("tuto");
    });
  });
}
