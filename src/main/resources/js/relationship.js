// add request
$("#form-saveRequest").submit(function () {
    debugger;
    $.ajax({
        url: "/add-relationship",
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

// change status
$("#form-confirmRequest").submit(function () {
    debugger;
    $.ajax({
        url: "/update-relationship",
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

// cancel request
$("#form-cancelRequest").submit(function () {
    debugger;
    $.ajax({
        url: "/cancel-relationship",
        type: "POST",
        data: $(this).serialize(),
        success: function success() {
        debugger;
            alert("Your request to was canceled.");
        },
        error: function error(xhr) {
        debugger;
            alert(xhr.responseText);
        }
    });
    return false;
});
