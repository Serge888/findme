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
        type: "PUT",
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
