// 로그인 되었는지 확인 후 되어있으면 SSE를 셋팅하는 메소드 - by.서현
$.ajax({
  type: "GET",
  url: "/TravelCarrier/member/login/check",
  success: function (resp) {
    if(resp == "loginUser") setSSE();
  },
  error: function (jqXHR, textStatus, errorThrown) {
    alert("로그인 실패 : "+textStatus);
  }
});

// SSE연결 생성 메소드 = by.서현
function setSSE() {
	console.log("notification() 실행됨!");
	let eventSource = new EventSource("http://localhost:8080/sub");

	eventSource.addEventListener("reply", function(event) {
		let message = event.data;
        alert(message);
	});
    eventSource.addEventListener("re-reply", function(event) {
        let message = event.data;
        alert(message);
    });

	eventSource.addEventListener("error", function(event) {
		eventSource.close();
	});
}

$(document).ready(function () {
  // 최근 업데이트창 활성화 - by윤아
  $(".notice").on("click", function () {
    // 알림창 셋팅
    getNotification();
    $(".utill_notice ").addClass("show");
    $(".diary_noH .writing").hide();

  });
  // 최근 업데이트창 비활성화 - by윤아
  $(".update_notice h6 i").on("click", function () {
    $(".utill_notice ").removeClass("show");
    $(".diary_noH .writing").show();
  });

  // 알림삭제 - by.서현
  $(document).on("click", ".notice_del", function () {
    var notification_id = $(this).attr("data-notification");
    $.ajax({
      type: "GET",
      url: "/TravelCarrier/notification/"+notification_id,
      success: function (resp) {
        console.log("성공");
        $("li button[data-notification='" + notification_id + "']").closest("li").remove();
      },
      error: function (jqXHR, textStatus, errorThrown) {
        alert("실패 : "+textStatus);
      }
    });
  });

});



// 알림목록을 로드하는 함수
function getNotification(){
    $.ajax({
      type: "GET",
      url: "/TravelCarrier/notification",
      success: function (resp) {
        console.log("성공");
        drawNotice(resp);
      },
      error: function (jqXHR, textStatus, errorThrown) {
        alert("실패 : "+textStatus);
      }
    });
}

function drawNotice(data){
    console.log(data);
    console.log($(".update_notice ul"));
    $(".update_notice ul").empty();

   for (var e of data) {
        var html;
        if(e.type == "comment") html = commentHtml(e);
        else if(e.type == "recomment") html = recommentHtml(e);
        else if(e.type == "f_req") html = addFriendHtml(e);
        else if(e.type == "f_com") html = beFriendHtml(e);
        else if(e.type == "gowith") html = tagHtml(e);
        $(".update_notice ul").append(html);
   };
}


function commentHtml(data) {
  var html;
    html = `
        <li class="notice_comment">
          <div class="notice_profile circle">
            <img src="${data.senderThumbPath}" alt="프로필 이미지">
          </div>
          <div class="notice_textbox">
            <p>
              <a href="#none" class="notice_name">${data.senderName}</a>님이 <a href="http://localhost:8080/TravelCarrier${data.url}" class="notice_writing">${data.title}</a> 글에 댓글을
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
              <a href="#none" class="notice_name">${data.senderName}</a>님이 <a href="http://localhost:8080/TravelCarrier${data.url}" class="notice_writing">${data.title}</a> 댓글에 답댓글을
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
          <a href="${data.url}" class="notice_name">${data.senderName}</a>님이 팔로우를 요청했습니다.
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

function beFriendHtml(data) {
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
}

function tagHtml(data) {
  var html;
    html = `
    <li class="notice_tag">
      <div class="notice_profile circle">
        <img src="${data.senderThumbPath}" alt="프로필 이미지">
      </div>
      <div class="notice_textbox">
        <p>
          <a href="${data.url}" class="notice_name">${data.senderName}</a>님이 <a href="http://localhost:8080/TravelCarrier${data.url}" class="notice_writing">${data.title}</a>에 회원님을
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