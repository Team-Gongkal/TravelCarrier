// var weekly_scroll = document.querySelector("main.weekly_wrap");
// weekly_scroll.addEventListener("wheel", function (event) {
//   event.preventDefault();
//   마우스 휠의 deltaX 속성은 가로 스크롤 양을 나타냅니다.
//   const scrollX = event.deltaX;
//   console.log(scrollX);

//   container 요소의 scrollLeft 속성을 이용해 가로 스크롤을 조작합니다.
//   weekly_scroll.scrollLeft -= scrollX;
// });

//키워드 모달창 띄우기 -by윤아
$(".keywordBox > span").click(function () {
  $(".keyword_modal_bg").addClass("show");
});
$(".close, .keyword_modal > button").click(function () {
  $(".keyword_modal_bg").removeClass("show");
});

//위클리에서 데일리로 넘어가는 경로 설정 - by.윤아
var url = window.location.href; //현재문서의 url가져오기
document.getElementById("daily_path").href = url + "/daily";
