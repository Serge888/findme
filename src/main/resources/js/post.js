// add post
$("#form-postCreator").submit(function () {
    debugger;
    $.ajax({
        url: "/save-post",
        type: "POST",
        data: $(this).serialize(),
        success: function success(data) {
            debugger;
            alert(data.valueOf());
        },
        error: function error(xhr) {
            debugger;
            alert(xhr.responseText);
        }
    });
    return false;
});