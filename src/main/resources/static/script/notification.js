// 로그인 되었는지 확인 후 되어있으면 SSE를 셋팅하는 메소드 - by.서현
$.ajax({
  type: "GET",
  url: "/member/login/check",
  success: function (resp) {
    if (resp == "true") {
      setSSE();
      //안읽은 알림이 있다면 알림뱃지 활성화
      setNewNoti();
    } else console.log("로그인 안함");
  },
  error: function (jqXHR, textStatus, errorThrown) {
    alert("로그인 실패 : " + textStatus);
  },
});

function setNewNoti() {
  $.ajax({
    type: "GET",
    url: "/notification/notRead",
    success: function (resp) {
      console.log(resp);
      if (resp) $(".notice").addClass("active");
    },
    error: function (jqXHR, textStatus, errorThrown) {
      alert("실패 : " + textStatus);
    },
  });
}

// SSE연결 생성 메소드 = by.서현
function setSSE() {
  console.log("notification() 실행됨!");
  let eventSource = new EventSource("/sub");

  //실시간 알림 발생시 - 알림모달이 on 되어있다면 append
  //실시간 알림 발생시 - 알림모달이 off되어있다면 알림발생 안내만 추가
  eventSource.addEventListener("new", function (e) {
    if ($(".utill_notice").hasClass("show")) {
      var data = JSON.parse(e.data);
      var html;
      if (data.type == "comment") html = commentHtml(data);
      else if (data.type == "recomment") html = recommentHtml(data);
      else if (data.type == "follow") html = addFriendHtml(data);
      else if (data.type == "gowith") html = tagHtml(data);
      $(".update_notice ul").prepend(html);
    } else $(".notice").addClass("active");
  });

  eventSource.addEventListener("error", function (event) {
    eventSource.close();
  });
}

$(document).on("click", ".notice", async function (e) {
    // 알림창 셋팅
    if(!$(".utill_notice").hasClass("show")) {
        console.log("보여줘");
        await getNotification();
        $(".utill_notice").addClass("show");
        //읽음처리
        $(".notice").removeClass("active");
        isReadNotification();
    }
});

// 최근 업데이트창 활성화 및  -by윤아
$(document).mouseup(function (e) {
   //.notice 클릭시 비활성화 무시
   if ($(".notice").has(e.target).length != 0) {
       return;
   }

  //알림창 밖의 요소 클릭시 최근 업데이트창 비활성화
  var alertNew = $(".utill_notice");
  if (alertNew.has(e.target).length == 0) {
    console.log(alertNew + "내가클릭한거 : " + e.target);
    alertNew.removeClass("show");
  }
  //요소내 닫기버튼 클릭시 닫기
  $(".update_notice h6 i").on("click", function () {
    $(".utill_notice ").removeClass("show");
  });
});

// 알림삭제 - by.서현
$(document).on("click", ".notice_del", function () {
  var notification_id = $(this).attr("data-notification");
  $.ajax({
    type: "GET",
    url: "/notification/" + notification_id,
    success: function (resp) {
      console.log("성공");
      $("li button[data-notification='" + notification_id + "']")
        .closest("li")
        .remove();
    },
    error: function (jqXHR, textStatus, errorThrown) {
      alert("실패 : " + textStatus);
    },
  });
});

// 알림목록을 로드하는 함수
async function getNotification() {
  var ajax = $.ajax({
    type: "GET",
    url: "/notification",
    success: function (resp) {
      console.log("성공,앞");
      drawNotice(resp);
    },
    error: function (jqXHR, textStatus, errorThrown) {
      alert("실패 : " + textStatus);
    },
  });

  await ajax.done(function(){
      console.log("앞 종료");
      return;
  });
}

// 알림을 읽음처리하는 함수
function isReadNotification() {
  $.ajax({
    type: "GET",
    url: "/notification/isRead",
    success: function (resp) {
      console.log("읽음처리 성공");
    },
    error: function (jqXHR, textStatus, errorThrown) {
      alert("실패 : " + textStatus);
    },
  });
}

function drawNotice(data) {
  // console.log(data);
  // console.log($(".update_notice ul"));
  $(".update_notice ul").empty();
  for (var e of data) {
    var html;
    if (e.type == "comment") html = commentHtml(e);
    else if (e.type == "recomment") html = recommentHtml(e);
    else if (e.type == "follow") html = addFriendHtml(e);
    else if (e.type == "gowith") html = tagHtml(e);
    $(".update_notice ul").append(html);
  }
}

function commentHtml(data) {
  var title;
  if (
    typeof data.title == "undefined" ||
    data.title == null ||
    data.title == ""
  )
    title = "제목없음";
  else title = data.title;

  var html;
  html =
    `
        <li class="notice_comment">
          <div class="notice_profile circle">
            <img src="${data.senderThumbPath}" alt="프로필 이미지">
          </div>
          <div class="notice_textbox">
            <p>
              <a href="#none" class="notice_name">${data.senderName}</a>님이 <a href="${data.url}" class="notice_writing">` +
    title +
    `</a> 글에 댓글을
              남겼습니다.
            </p>
            <span class=" update_date"><i class="xi-time-o"></i>${data.time}</span>
          </div>
          <button data-notification="${data.id}" class="notice_del">
            <i class="xi-close"></i>
          </button>
        </li>
        `;
  return html;
}

function recommentHtml(data) {
  var html;
  html = `
        <li class="notice_recomment">
          <div class="notice_profile circle">
            <img src="${data.senderThumbPath}" alt="프로필 이미지">
          </div>
          <div class="notice_textbox">
            <p>
              <a href="#none" class="notice_name">${data.senderName}</a>님이 <a href="${data.url}" class="notice_writing">${data.title}</a> 댓글에 답댓글을
              남겼습니다.
            </p>
            <span class=" update_date"><i class="xi-time-o"></i>${data.time}</span>
          </div>
          <button data-notification="${data.id}" class="notice_del">
            <i class="xi-close"></i>
          </button>
        </li>
        `;
  return html;
}

function addFriendHtml(data) {
  var html;
  html = `
    <li class="notice_addFriend">
      <div class="notice_profile circle">
        <img src="${data.senderThumbPath}" alt="프로필 이미지">
      </div>
      <div class="notice_textbox">
        <p>
          <a href="${data.url}" class="notice_name">${data.senderName}</a>님이 회원님을 팔로우 합니다.
        </p>
        <span class="update_date"><i class="xi-time-o"></i>${data.time}</span>
      </div>
      <button data-notification="${data.id}" class="notice_del">
        <i class="xi-close"></i>
      </button>
    </li>
        `;
  return html;
}

/*function beFriendHtml(data) {
  var html;
  html = `
    <li class="notice_beFriend">
      <div class="notice_profile circle">
        <img src="${data.senderThumbPath}" alt="프로필 이미지">
      </div>
      <div class="notice_textbox">
        <p>
          <a href="${data.url}" class="notice_name">${data.senderName}</a>님이 회원님을 팔로우합니다.
        </p>
        <span class="update_date"><i class="xi-time-o"></i>${data.time}</span>
      </div>
      <button data-notification="${data.id}" class="notice_del">
        <i class="xi-close"></i>
      </button>
    </li>
        `;
  return html;
}*/

function tagHtml(data) {
  var html;
  html = `
    <li class="notice_tag">
      <div class="notice_profile circle">
        <img src="${data.senderThumbPath}" alt="프로필 이미지">
      </div>
      <div class="notice_textbox">
        <p>
          <a href="${data.url}" class="notice_name">${data.senderName}</a>님이 <a href="${data.url}" class="notice_writing">${data.title}</a>에 회원님을
          태그했습니다.
        </p>
        <span class=" update_date"><i class="xi-time-o"></i>${data.time}</span>
      </div>
      <button data-notification="${data.id}" class="notice_del">
        <i class="xi-close"></i>
      </button>
    </li>

        `;
  return html;
}
