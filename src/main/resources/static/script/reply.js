// 댓글 모달창 활성화 - by윤아
$(".diary_viewport").on("click", ".d_slide > .reply_icon", function (e) {
    //var attachNo = $(this).closest(".d_slide").data("attachno");
    var attachNo = $(this).siblings("img").data("attachno");
    var currentImg = $(this).siblings("img").attr("src");
    $(".reply_img img").attr("src", currentImg);
    $(".reply_img img").attr("data-attachNo",attachNo);
    $(".reply_modal").addClass("show");
    currentReplyList(attachNo);
});

$(".reply_modal .close").click(function () {
  $(".reply_modal").removeClass("show");
});

// 댓글리스트 새로고침 ajax- by.서현
function currentReplyList(attachNo){
  $.ajax({
    type: "GET",
    url: "/TravelCarrier/reply/"+attachNo,
    dataType: 'json',
    success: function (replyList) {
      console.log("댓글 List 로드 성공");
      appendReply(replyList);
    },
    error: function (jqXHR, textStatus, errorThrown) {
      alert("댓글 List 로드 실패");
    }
  });
}

// 댓글리스트 ajax후 append해주는 메소드 - by.서현
function appendReply(replyList){
    // replyList : [{attachNo, cdate, origin, originName, replyId, text, thumbPath, udate, userId, userName},{}]
    $('.reply_scroll').empty();
    console.log(replyList);
    $.each(replyList, function(index, obj) {
        if(obj.udate == null) var date = obj.cdate;
        else var date = obj.udate+" (수정됨)";

        if(obj.origin == undefined || obj.origin == null || obj.origin == 0){
            var html = newReplyHtml(obj.thumbPath,date, obj.userName, obj.text,obj.replyId);
            $('.reply_scroll').append(html);
            $(".reply_input input").val('');
        } else{
            // 대댓글달기
            var targetDiv = $(`div[data-reply=${obj.origin}]`);
            var recommentHtml = newReCommentHtml(obj.thumbPath,date, obj.userName, obj.text,obj.replyId, obj.originName);
            targetDiv.append(recommentHtml);
            $(".reply_input input").val('');
        }
    });
}

// 댓글 작성 이벤트 - by.서현
$(".reply_input button").on("click", function(){
    var type = $(".reply_input p").text();
    if(type == "댓글달기") createReply(type);
    else if(type == "댓글수정") modifyReply();
    else if(type.charAt(0) == "@") createReply(type);
    else alert("오류");
    // 댓글 작성/수정/대댓 후 인풋란 초기화
    $(".reply_input div").html("<p>댓글달기</p>");
    $(".cancel").remove();
    $(".reply_input input").val('');
});
$('input').on("keydown", function() {
  if (event.keyCode === 13) {

  }
});

// 댓글 작성 유효성 검사 - by.서현
function validReply(){
    var comment = $(".reply_input input").val();
    if(comment == '') return false;
    else return true;
}

// 새 댓글 등록 ajax - by.서현
function createReply(type){
    if(!validReply()) return;

    // 댓글이면 origin값이 "", 대댓글이면 origin값이 원댓글Id
   if(type == "댓글달기") var originVal = "";
   else if(type.charAt(0) == "@") var originVal = $(".reply_input div span").text();

    // reply 필요조건 : ATTACH_NO, USER_ID, REPLY_TEXT, CDATE, UDATE, REPLY_ORIGIN
   var data = { attachNo : $(".reply_img img").attr("data-attachNo"),
                text : $(".reply_input input").val(),
                cdate : $.datepicker.formatDate('yy-mm-dd', new Date()),
                udate : null,
                origin : originVal };

  $.ajax({
    type: "POST",
    url: "/TravelCarrier/reply/create",
    data: JSON.stringify(data),
    contentType: 'application/json',
    success: function (replyId) {
      console.log("성공 "+replyId);
      var attachNo = $(".reply_img img").attr("data-attachNo");
      currentReplyList(attachNo);
    },
    error: function (jqXHR, textStatus, errorThrown) {
      alert("실패");
    }
  });
}

// 댓글 수정 ajax - by.서현
function modifyReply(){
    if(!validReply()) return;
    // reply 수정 : replyId, text, udate
   var data = { replyId : $(".reply_input div span").text(),
                text : $(".reply_input input").val(),
                udate : $.datepicker.formatDate('yy-mm-dd', new Date()) };

  $.ajax({
    type: "POST",
    url: "/TravelCarrier/reply/modify",
    data: JSON.stringify(data),
    contentType: 'application/json',
    success: function (replyId) {
      console.log("성공 "+replyId);
      var attachNo = $(".reply_img img").attr("data-attachNo");
      currentReplyList(attachNo);
    },
    error: function (jqXHR, textStatus, errorThrown) {
      alert("실패");
    }
  });
}

// 댓글 append 틀 - by.서현
function newReplyHtml(img, date, name, comment, replyId){
    var html = `
    <div class="reply" data-reply="${replyId}">
      <div class="comment">
        <div class="comment_profile">
          <div>
            <img src="${img}" alt="프로필사진">
          </div>
        </div>
        <div class="comment_date">
          <span>${date}</span>
        </div>
        <div class="comment_textbox">
          <span class="comment_id">${name}</span>
          <p class="comment_content">${comment}</p>
        </div>
        <div class="comment_btn">
          <span class="mod_rep">수정하기</span>
          <span class="re_rep">답글달기</span>
        </div>
      </div>
    </div>
    `;

    return html;
};
// 대댓글
function newReCommentHtml(img, date, name, comment, replyId, originName){
    var html = `
      <div class="recomment" data-reply="${replyId}">
        <div class="comment_profile">
          <div>
            <img src="${img}" alt="프로필01">
          </div>
        </div>
        <div class="comment_date">
          <span>${date}</span>
        </div>
        <div class="comment_textbox">
          <span class="comment_id">${name}</span>
          <p class="comment_content">
            <span class="reply_id">@${originName}</span>
            ${comment}
          </p>
        </div>
        <div class="comment_btn">
          <span>수정하기</span>
          <span>답글달기</span>
        </div>
      </div>
    `;

    return html;
};
// 답글/수정 취소 이벤트 - by.서현
$(document).on("click", ".cancel", function(e) {
    $(".reply_input div").html("<p>댓글달기</p>");
    $(".cancel").remove();
});
// 수정버튼 클릭 이벤트 - by.서현
$(document).on("click", ".mod_rep", function(e) {
    var $comment = $(this).closest(".reply");
    $(".cancel").remove();
    $(".reply_input input").val('');
    $comment.find(".comment_btn").append("<span class='cancel'>수정취소</span>");
    var origin = $comment.data("reply");
    var content = $comment.find(".comment_content").text();
    $(".reply_input div").html("<p>댓글수정</p> <span>"+origin+"</span>");
    $(".reply_input input").val(content);
});

// 답글 작성 이벤트  - by.서현
$(document).on("click", ".re_rep", function(e) {
    var $comment = $(this).closest(".reply");
    $(".cancel").remove();
    $(".reply_input input").val('');
    $comment.find(".comment_btn").append("<span class='cancel'>답글취소</span>");
    var origin = $comment.data("reply");
    var name = $comment.find(".comment_id").text();
    $(".reply_input div").html("<p>@"+name+"</p> <span>"+origin+"</span>");
});