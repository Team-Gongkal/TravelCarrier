var weeklyform = [
  {
    id: 1,
    element: $(".thumbnail"),
    title: "썸네일",
    text: `썸네일 이미지를 넣어보세요! <br> 이미지는 메인페이지 지도 옆에서 확인할 수 있어요
    `,
    positionX: "left",
    x: 45,
    positionY: "top",
    y: 30,
    arrow: "left",
    // 문자가 아닌 직접 객체 형식으로 .css() 메서드에 전달하기
    arrowTail: { top: "50%", right: "100%", bottom: "auto", left: "auto" },
  },
  {
    id: 2,
    element: $(".weekly_input"),
    title: "위클리작성",
    text: `각각의 입력칸에 맞게 여행에 대한 정보를 작성해주세요
    <br><br>
    <span class="
    essential">💡꼭 확인해주세요!</span>
    <em>위클리에서 설정한 여행기간만큼 데일리를작성할 수 있으니 처음 작성시 신중하게 선택해주세요</em>
    `,
    positionX: "left",
    x: 17,
    positionY: "top",
    y: 15,
    arrow: "right",
    arrowTail: { top: "45%", right: "auto", bottom: "auto", left: "100%" },
  },
  {
    id: 3,
    element: $(".weekly_input"),
    title: "동행인",
    text: `여행을 함께한 동행인을 선택할 수 있어요
    <br><br><span class="
    essential">💡꼭 확인해주세요!</span>
    동행인을 태그하면 여행기록을 함께 편집관리 하게 됩니다.
    <br>친구와 함께 여행을 추억해보세요!`,
    positionX: "left",
    x: 19,
    positionY: "bottom",
    y:8,
    arrow: "right",
    arrowTail: { top: "auto", right: "auto", bottom: "40%", left: "100%" },
  },
];

// 함수적용--------------------------------------------
var currentIdx = 0;
$(document).ready(function () {
  //초기 설정값 보여주기(object자료명,시작인덱스)
  show(weeklyform,currentIdx);
  //이전, 다음 버튼 클릭시 함수
  prevBtnClick(weeklyform);
  nextBtnClick(weeklyform);
});
closeTutorial(weeklyform);
