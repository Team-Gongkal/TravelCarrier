$(document).ready(function () {
  // 최근 업데이트창 활성화 - by윤아
  $(".notice").on("click", function () {
    $(".utill_notice ").addClass("show");
    $(".diary_noH .writing").hide();
  });
  $(".update_notice h6 i").on("click", function () {
    $(".utill_notice ").removeClass("show");
    $(".diary_noH .writing").show();
  });
});
