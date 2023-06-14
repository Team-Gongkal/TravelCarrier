$(window).click(function (e) {
  console.log($(e.target));
  if ($(e.target).is($(".modal_bg"))) {
    $(
      ".weekly_modal_bg, .keyword_modal_bg, .edit_profile, #login_wrap , .reply_modal , .daily_form_bg"
    ).removeClass("show");
  }
});
