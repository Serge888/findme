$("#login-form").submit(function () {
    $.ajax({
        url: "login",
        type: "POST",
        data: $(this).serialize(),
        success: function success(data) {
            alert("Welcome");
        },
        error: function error(xhr) {
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
            alert(data.valueOf());
        },
        error: function error(xhr) {
            alert(xhr.responseText);
        }
    });
});