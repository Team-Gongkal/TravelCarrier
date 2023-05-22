$(document).ready(function () {
  window.onresize = function () {
    document.location.reload();
  };
  var input1 = $(".login form div input[type=text]"),
    input2 = $(".login form div input[type=password]");

  $(input1).on("propertychange change paste input", function () {
    x = input1.val().length;
    if (x !== 0) {
      $(input1).prev("label").addClass("on");
    } else {
      $(input1).prev("label").removeClass("on");
    }
  });

  $(input2).on("propertychange change paste input", function () {
    x = input2.val().length;
    if (x !== 0) {
      $(input2).prev("label").addClass("on");
    } else {
      $(input2).prev("label").removeClass("on");
    }
  });

/*  //빈칸있을 경우 경고창띄우기
  $("button[type=submit]").click(function chkValue() {
    // 필수입력사항에 누락이 있으면 진행금지
    if (!validCheck()) return;
    form.submit();
  });*/
});

function validCheck() {
        // 공통입력폼내의 모든 입력오브젝트
        var inputObjs = $("form input");
        // 미입력여부(경우에 따라 사용)
        var bEmpty = true;
        var focus;

        // 각 오브젝트에 대해 입력체크
        inputObjs.each(function (index) {
          if ($(this).val() == "") {
            focus = $(this);
            bEmpty = false;

            alert($(this).attr("title") + "를 입력해주세요.");
            focus.focus();

          }
        });
        return bEmpty;
}