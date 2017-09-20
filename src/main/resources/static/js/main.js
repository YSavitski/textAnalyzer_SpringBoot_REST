$(document).ready(function () {

    $("#btnSubmit").click(function (event) {
        fire_ajax_submit();
    });

    $("#textAnalyzerButton").click(function (event) {
        getTop10Words();
    })

    $("#bracketsAnalyzerButton").click(function (event) {
        checkBrackets();
    })
});

function fire_ajax_submit() {

    // Get form
    var form = $('#fileUploadForm')[0];

    var data = new FormData(form);

    data.append("CustomField", "This is some extra data, testing");

    $("#btnSubmit").prop("disabled", true);

    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/upload",
        data: data,
        processData: false,
        contentType: false,
        cache: false,
        timeout: 600000,
        success: function (data) {

            $("#result").text(data);
            console.log("SUCCESS : ", data);
            $("#btnSubmit").prop("disabled", false);

        },
        error: function (e) {

            $("#result").text(e.responseText);
            console.log("ERROR : ", e);
            $("#btnSubmit").prop("disabled", false);

        }
    });

}

function getTop10Words () {
    $.ajax({
        type: 'GET',
        url: "/textAnalyzer",
        dataType: 'json',
        async: false,
        success: function (content) {
            $('#result').html(JSON.stringify(content))
        },
        error: function (jgXHR, textStatus, errorThrown) {
            $('#result').html(JSON.stringify(igXHR))
        }
    });
};

function checkBrackets () {
    $.ajax({
        type: 'GET',
        url: "/bracketsAnalyzer",
        dataType: 'json',
        async: false,
        success: function (content) {
            $('#result').html(JSON.stringify(content))
        },
        error: function (jgXHR, textStatus, errorThrown) {
            $('#result').html(JSON.stringify(igXHR))
        }
    });
}