<%-- 
    Document   : addC
    Created on : 18-lug-2016, 16.48.56
    Author     : TTm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>
        <script>
            $(document).ready(function () {
                 $.ajax({
                    type: "POST",
                    url: "Insegnamenti",
                    data: {action: "getDipList"},
                    success: function (data) {
                        $("#dip_cap").html(data);
                    },
                    error: function (xhr, status, error) {
                        alert(error);
                    }
                });
            });
            
            $(document).on("change", "#dip_cap", function () {
                dip_cap = $("#dip_cap").val();
                $.ajax({
                    type: "POST",
                    url: "Insegnamenti",
                    data: {action: "getDipList",
                        dip_cap: dip_cap
                    },
                    success: function (data) {
                        $("#dip_cap").html(data);
                    },
                    error: function (xhr, status, error) {
                        alert(error);
                    }
                });
            });
            
            $(document).on("click", "#submitCand", function () {
                dip_cap = 10079;
                $.ajax({
                    type: "POST",
                    url: "Candidature",
                    data: {action: "getCandByCap",
                        dip_cap: dip_cap
                    },
                    success: function (data) {
                        $("#view_cand").html(data);
                    },
                    error: function (xhr, status, error) {
                        alert(error);
                    }
                });
            });
  
        </script>
        <title>Visualizza candidature</title>
    </head>
    <body>
        <h1>Visualizza candidatura</h1>
        <form>
            Scegli dipartimento capofila:
            <select id='dip_cap'></select>
            <br><br>
            <button id='submitCand'>Visualizza</button>
        </form>
        <div id='view_cand'> </div>
    </body>
</html>
