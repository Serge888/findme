$("#login-form").submit(function () {
    debugger;
    $.ajax({
        url: "login",
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



$("#logout-button").click(function () {
    debugger;
    $.ajax({
        url: "logout",
        type: "GET",
        data: {},
        success: function success(data) {
            debugger;
            alert(data.valueOf());
        },
        error: function error(xhr) {
            debugger;
            alert(xhr.responseText);
        }
    });
});