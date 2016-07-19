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

                // prime 3 chiamate di setup, caricano gli elenchi completi 
                $.ajax({
                    type: "POST",
                    url: "Docenti",
                    data: {action: "getDipList"},
                    success: function (data) {
                        $("#dip_doc").html(data);
                    },
                    error: function (xhr, status, error) {
                        alert(error);
                    }
                });
                $.ajax({
                    type: "POST",
                    url: "Docenti",
                    data: {action: "getRoleList"},
                    success: function (data) {
                        $("#ruolo_doc").html(data);
                    },
                    error: function (xhr, status, error) {
                        alert(error);
                    }
                });
                $.ajax({
                    type: "POST",
                    url: "Docenti",
                    data: {action: "getAllDoc"},
                    success: function (data) {
                        $("#cf_doc").html(data);
                    },
                    error: function (xhr, status, error) {
                        alert(error);
                    }
                });

                //sezione insegnamenti
                $.ajax({
                    type: "POST",
                    url: "Insegnamenti",
                    data: {action: "getDipList"},
                    success: function (data) {
                        $("#dip_ins").html(data);
                    },
                    error: function (xhr, status, error) {
                        alert(error);
                    }
                });

            }); // fine document.ready

            // onChange sui due parametri ruolo e dip di aff
            $(document).on("change", "#ruolo_doc,#dip_doc", function () {
                dip_cod_form = $("#dip_doc").val();
                ruolo_doc_form = $("#ruolo_doc").val();
                $.ajax({
                    type: "POST",
                    url: "Docenti",
                    data: {action: "getFilteredDoc",
                        dip_cod: dip_cod_form,
                        ruolo_cod: ruolo_doc_form
                    },
                    success: function (data) {
                        $("#cf_doc").html(data);
                    },
                    error: function (xhr, status, error) {
                        alert(error);
                    }
                });
            });

            $(document).on("change", "#dip_ins", function () {
                dip_ins_form = $("#dip_ins").val();
                $.ajax({
                    type: "POST",
                    url: "Insegnamenti",
                    data: {action: "getInsByDip",
                        dip_cod: dip_ins_form
                    },
                    success: function (data) {
                        $("#id_ins").html(data);
                    },
                    error: function (xhr, status, error) {
                        alert(error);
                    }
                });
            });
            
            $(document).on("click", "#submit", function () {
                dip_cod_form = $("#dip_doc").val();
                cf_doc_form = $("#cf_doc").val();
                dip_ins_form = $("#dip_ins").val();
                id_ins_form = $("#id_ins").val();
                $.ajax({
                    type: "POST",
                    url: "Candidature",
                    data: {action: "insCand",
                        dip_doc: dip_cod_form,
                        cf_doc: cf_doc_form,
                        dip_cod: dip_ins_form,
                        id_ins: id_ins_form
                    },
                    success: function () {
                        alert("Candidatura inserita correttamente.");
                    },
                    error: function (xhr, status, error) {
                        alert(error);
                    }
                });
            });
  
        </script>
        <title>Aggiungi candidatura</title>
    </head>
    <body>
        <h1>Aggiungi candidatura</h1>
        <form>
            Dipartimento afferenza
            <select id='dip_doc'></select>
            <br>
            Ruolo docente
            <select id='ruolo_doc'></select>
            <br>
            Docente
            <select id='cf_doc'></select>
            <br><br>
            Dipartimento capofila
            <select id='dip_ins'></select>
            <br>
            Insegnamento
            <select id='id_ins'></select>
            <br><br>
            <button id='submit'>Candida</button>
        </form>
    </body>
</html>
