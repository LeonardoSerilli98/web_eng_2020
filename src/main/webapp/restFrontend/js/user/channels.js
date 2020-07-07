$(document).ready(function(){

    $("#getCanali").on("click", function (e) {

        e.preventDefault();

        $.ajax({url: "/api/canali",
            success: function(response){
                response.forEach(function (element, i){
                    var div = `<div class="singolo_canale">${element.url}</div>`;
                    $("#canali").append(div);
                
                });
                $(".singolo_canale").each(function (){
                    var canale = this;
                    $(this).on("click", function (){
                        $.ajax({url: $(this).text().replace("http://localhost:8080", ""),
                        success: function(response){
                            console.log(response)
                            var div = `<div>${response.nome}</div>`;
                            $(canale).append(div);
                            response.palinsesti.forEach(function (p){
                                var div = `<div class="singolo_palinsesto"><div>${p.data}</div><div>${p.programma.nome}</div><div>${p.inizio}</div></div>`;
                                $(canale).append(div);
                            });
                        },
                        error: function (request, status, error) {
                            console.log(status);
                        },
                        dataType: "json",
                        type: "GET"
                        })                   
                    })
                })
                console.log(response);
            },
            error: function (request, status, error) {
                console.log(status);
            },
            dataType: "json",
            type: "GET"
        })

        
    })
})

