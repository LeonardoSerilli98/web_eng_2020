$(document).ready(function(){

    $("#login").submit( function (e) {

        e.preventDefault();

        $.ajax({url: "/api/auth",
            success: function(response){
                console.log(response)
                $(window.location).attr('href', '/restchannels');
            },
            error: function (request, status, error) {
                console.log(status);
            },
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify({
                email: $("#email").val(),
                password: $("#password").val()
            }),
            type: "POST"
        })

        
    })
})

