$(document).ready(function () {
  //datpicker 한국어로 사용하기 위해 언어설정
  $.datepicker.setDefualts($.datepicker.regional["ko"]);

  //datepicker
  $(".datepicker").datepicker({
    // showButtonPanel: true,
    // dateFormat: "yy-mm-dd",
    // onclose: function (selectedDate) {},
  });
}); //스크립트 종료
