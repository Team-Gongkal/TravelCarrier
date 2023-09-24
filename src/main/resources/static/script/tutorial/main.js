console.log("얏호");
var main = [
  {
    id: 1,
    element: $("header"),
    title: "왼쪽 네비게이션",
    text: "각각 클릭시 메인화면, 게시판, 컨텍트페이지로 이동할 수 있어요",
    positionX: "left",
    x: 5,
    positionY: "top",
    y: 15,
    arrow: "left",
    // 문자가 아닌 직접 객체 형식으로 .css() 메서드에 전달하기
    arrowTail: { top: "50%", right: "100%", bottom: "auto", left: "auto" },
  },
  {
    id: 2,
    element: $(".utill_menu"),
    title: "최근 알림",
    text: `왼쪽의 시계 아이콘을 클릭하면 친구신청, 글작성, 댓글 등의 알림을 확인 할 수 있어요
    <br><br>
    오른쪽의 프로필 이미지를 클릭할 경우 마이페이지로 이동됩니다
    `,
    positionX: "right",
    x: 0,
    positionY: "top",
    y: 7,
    arrow: "top",
    arrowTail: { top: "-1rem", right: "17%", bottom: "auto", left: "auto" },
  },
  {
    id: 3,
    element: "",
    title: "지도",
    text: "위클리를 작성하면 여행한 나라에 여행사진이 쏙 들어가요",
    positionX: "left",
    x: 5,
    positionY: "top",
    y: 15,
    arrow: "",
    arrowTail: { top: "50%", right: "100%", bottom: "auto", left: "auto" },
  },
  {
    id: 4,
    element: $(".weekly_thumbnails"),
    title: "위클리 작성 아이콘",
    text: "이 플러스 버튼을 클릭하면 위클리를 작성할 수 있어요",
    positionX: "right",
    x: 7,
    positionY: "top",
    y: 22,
    arrow: "right",
    arrowTail: { top: "40%", right: "auto", bottom: "auto", left: "100%" },
  },
  {
    id: 5,
    element: $("header"),
    title: "도움말",
    text: "홈페이지 이용이 어려울 땐 물음표 아이콘을 눌러주세요",
    positionX: "left",
    x: 3,
    positionY: "bottom",
    y: 0,
    arrow: "left",
    arrowTail: { top: "76%", right: "100%", bottom: "auto", left: "auto" },
  },
];

var currentIdx = 0;

function show(id) {
  data = main[id];
  //[1]진행도 표시(id)
  $(".tt_prograss em").text(data.id + " / " + main.length);
  //[2]제목 (title)
  $(".tt_title > h4").text(data.title);
  //[3]설명글(text)
  $(".tt_explain > p").html(data.text);
  //[4-1]모든 element에 tuto클래스 제거하기
  main.forEach(function (all) {
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

$(document).ready(function () {
  //초기 설정값 보여주기
  show(currentIdx);

  //이전버튼 클릭시
  $(".tt_prev").click(function (event) {
    currentIdx--;
    if (currentIdx < 0) {
      currentIdx = 0;
      event.preventDefault();
      return false;
    }
    show(currentIdx);
  });

  //다음버튼 클릭시
  $(".tt_next").click(function (event) {
    currentIdx++;
    if (currentIdx >= main.length) {
      currentIdx = main.length - 1;
      event.preventDefault();
      return false;
    }
    show(currentIdx);
  });
});

// 모달 닫기
$(document).on("click", "#close_tutorial", function () {
  $("#tutorial").removeClass("show");
  //모든 element에 tuto클래스 제거하기
  main.forEach(function (all) {
    //all:각요소 순회
    $(all.element).removeClass("tuto");
  });
});
