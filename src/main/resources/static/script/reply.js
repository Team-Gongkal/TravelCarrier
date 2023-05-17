// 댓글 모달창 활성화 - by윤아
$(".diary_viewport").on("click", ".d_slide > .reply_icon", function (e) {
    var attachNo = $(this).closest(".d_slide").data("attachno");
    var currentImg = $(this).siblings("img").attr("src");
    $(".reply_img img").attr("src", currentImg);
    $(".reply_img img").attr("data-attachNo",attachNo);
    $(".reply_modal").addClass("show");
    currentReplyList(attachNo);
});

$(".reply_modal .close").click(function () {
  $(".reply_modal").removeClass("show");
});

// 댓글리스트 새로고침
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


function appendReply(replyList){
    // replyList : [{attachNo, cdate, origin, originName, replyId, text, thumbPath, udate, userId, userName},{}]
    $('.reply_scroll').empty();
    console.log(replyList);
    $.each(replyList, function(index, obj) {
        var comment = obj.text;
        var img = obj.thumbPath;
        if(obj.udate == null) var date = obj.cdate;
        else var date = obj.udate+" (수정됨)";
        var name = obj.userName;

        var html = newReplyHtml(img,date,name,comment);
        $('.reply_scroll').append(html);
        $(".reply_input input").val('');
    });
}


$(".reply_input button").on("click", function(){
    createReply();
});

$('input').on("keydown", function() {
  if (event.keyCode === 13) {
    createReply();
  }
});

function validReply(){
    var comment = $(".reply_input input").val();
    if(comment == '') return false;
    else return true;
}

function createReply(){
    if(!validReply()) return;
    // reply 필요조건 : ATTACH_NO, USER_ID, REPLY_TEXT, CDATE, UDATE, REPLY_ORIGIN
   var data = { attachNo : $(".reply_img img").attr("data-attachNo"),
                text : $(".reply_input input").val(),
                cdate : $.datepicker.formatDate('yy-mm-dd', new Date()),
                udate : null,
                origin : "" };
  $.ajax({
    type: "POST",
    url: "/TravelCarrier/reply/create",
    data: JSON.stringify(data),
    contentType: 'application/json',
    success: function (resp) {
      console.log("성공");
      var attachNo = $(".reply_img img").attr("data-attachNo");
      currentReplyList(attachNo);
    },
    error: function (jqXHR, textStatus, errorThrown) {
      alert("실패");
    }
  });
}


function newReplyHtml(img, date, name, comment){
    var html = `
    <div class="reply">
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
          <p class="comment_content">
            ${comment}
          </p>
        </div>
        <div class="comment_btn">
          <span>수정하기</span>
          <span>답글달기</span>
        </div>
      </div>
    </div>
    `;

    return html;
}