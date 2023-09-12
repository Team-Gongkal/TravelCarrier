$(".save_btn button").on("click", async function(e){
    e.preventDefault();
    //console.log("결과 : " + await validCheckPromise());
    if(!await validCheckPromise()) {
        alert("조건을 충족하지 못했습니다. 다시 작성해주세요.");
        return;
    }

    //회원가입 ajax
    var email = $("input[type='email']").val();
    var password = $("input[type='password']").val();
    var name = $("input[type='text']").val();
    $.ajax({
            type: "POST",
            url: "/member/sign/create",
            data: { email: email,
                                    password : password,
                                    name : name},
              //contentType: "application/json",
              success: function (resp) {
                $(".alert_btnBox button").attr("id", "sign_ok");
                alertModal2("회원가입 ","되었습니다.");

              },
              error: function (jqXHR, textStatus, errorThrown) {
                alert("회원가입 실패 \n다시 시도해주세요");
              },
        });
});

// 제출 전 마지막 유효성 검사 (이메일 재검사 / 빈 칸 없어야 함 / 체크박스)
async function validCheckPromise() {
    // 빈 칸 검사
    var email = $("input[type='email']").val();
    var password = $("input[type='password']").val();
    var name = $("input[type='text']").val();
    if (email.length == 0 || password.length == 0 || name.length == 0) return false;

    // 비밀번호가 형식에 맞는지
    var pwCheck1 = false;
    if ("사용 가능한 비밀번호 입니다." == $(".info_text.right p").text()) pwCheck1 = true;

    // 비밀번호 확인이 됐는지
    var pwCheck2 = false;
    if ("올바른 비밀번호 입니다." == $(".info_text.pw2 p").text()) pwCheck2 = true;

    // 이름의 길이가 맞는지
    var nameCheck = false;
    if ("사용 가능한 별명 입니다." == $(".info_text.name p").text()) nameCheck = true;

    if(!pwCheck1 || !pwCheck2 || !nameCheck) return false;
    // 이메일 중복 체크 했는지
    var emailCheck = false;
    var emailPromise = $.ajax({
        type: "POST",
        url: "/member/sign/validCheck",
        data: JSON.stringify({ email: $("input[type='email']").val() }),
        contentType: "application/json"
    });

    await emailPromise.done(function (emailResult) {
        console.log("promise " + emailResult);
        emailCheck = emailResult;
    });

    //console.log("check " + emailCheck + pwCheck1 + pwCheck2 + nameCheck);
    if (!emailCheck) return false;

    return true;
}


$(document).on("keyup", ".signUp_pw1 input[type='password']", function () {
  var length = $(this).val().length;

  if (length > 20) $(".info_text.right p").text("비밀번호가 너무 길어요.");
  else if (length < 6 && length>0) $(".info_text.right p").text("비밀번호가 너무 짧아요.");
  else if (length == 0) $(".info_text.right p").text("6~20자 이내로 구성해주세요");
  else $(".info_text.right p").text("사용 가능한 비밀번호 입니다.");
});

$(document).on("keyup", "input[type='text']", function () {
  var length = $(this).val().length;

  if (length > 10) $(".info_text.name p").text("별명이 너무 길어요.");
  else if (length == 0) $(".info_text.name p").text("1~10자 이내로 구성해주세요");
  else $(".info_text.name p").text("사용 가능한 별명 입니다.");
});

//이메일 중복확인
$(".dubble_check button").on("click", function(e){
    e.preventDefault();
    var data = $("input[type='email']").val();
    $.ajax({
          type: "POST",
          url: "/member/sign/validCheck",
          data: JSON.stringify({ email : data}),
          contentType: "application/json",
          success: function (resp) {
            email_check_alert(resp);
          },
          error: function (jqXHR, textStatus, errorThrown) {
            alert("실패");
          },
    });
});

$(document).on("keyup", ".signUp_pw2 input[type='password']", function () {
  var pw = $(".signUp_pw1 input[type='password']").val();
  var pw2 = $(this).val();

  if (pw == pw2) {
    $(".info_text.pw2 p").text("올바른 비밀번호 입니다.");
  }
  else if(pw2.length==0) $(".info_text.pw2 p").text("");
  else $(".info_text.pw2 p").text("비밀번호를 다시 입력해주세요.");
});

$(document).on("click", "#sign_ok", function(){
    window.location.href = "/";
})
