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
    text: `시계 아이콘을 클릭하면 친구신청, 글작성, 댓글 등의 알림을 확인 할 수 있어요`,
    positionX: "right",
    x: 0,
    positionY: "top",
    y: 7,
    arrow: "top",
    arrowTail: { top: "-1rem", right: "30%", bottom: "auto", left: "auto" },
  },
  {
    id: 3,
    element: $(".utill_menu"),
    title: "검색",
    text: `돋보기 모양의 아이콘을 클릭하면 검색창이 열려요`,
    positionX: "right",
    x: 0,
    positionY: "top",
    y: 7,
    arrow: "top",
    arrowTail: { top: "-1rem", right: "19%", bottom: "auto", left: "auto" },
  },
  {
    id: 4,
    element: $(".utill_menu"),
    title: "최근 알림",
    text: `프로필 이미지를 클릭할 경우 마이페이지로 이동됩니다
    <br>
    그밖에 계정설정 로그아웃도 가능해요
    `,
    positionX: "right",
    x: 0,
    positionY: "top",
    y: 7,
    arrow: "top",
    arrowTail: { top: "-1rem", right: "4%", bottom: "auto", left: "auto" },
  },
  {
    id: 5,
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
    id: 6,
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
    id: 7,
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
// 함수적용--------------------------------------------
var currentIdx = 0;
$(document).ready(function () {

    // 쿠키가 없다면 튜토리얼을 show하고, 쿠키가 있다면 show하지 않는다.
    const tuto_main = getCookie('tuto_main');
    if (!tuto_main) {
        console.log("쿠키없으니까 튜토리얼을 셋팅할게요");
        document.cookie = "tuto_main=true; max-age=3600";

        $("#tutorial").addClass("show");
        //초기 설정값 보여주기(object자료명,시작인덱스)
          show(main,currentIdx);
          prevBtnClick(main);
          nextBtnClick(main);
    }
});
closeTutorial(main);

