<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>

<jsp:useBean id="formaBean" class="si.nib.mbp.akvarij.poc.pojo.FormaBean" scope="session"/>


<%
    String id = request.getParameter("id");
    if (id == null) {
        id = "1";
    }
%>

<html>
    <HEAD>
        <TITLE>FPP/NIB MBP - prikaz meritev</TITLE>  
        <META HTTP-EQUIV="Content-Type"  CONTENT="text/html; charset=UTF-8"/>
        <LINK REL="stylesheet" type="text/css" href="./css/style.css" TITLE="Default">

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <link rel="stylesheet" type="text/css" href="./css/jquery.datetimepicker.css"/>

        <script src="https://www.amcharts.com/lib/3/amcharts.js"></script>
        <script src="https://www.amcharts.com/lib/3/serial.js"></script>
        <script src="https://www.amcharts.com/lib/3/plugins/export/export.min.js"></script>
        <link rel="stylesheet" href="https://www.amcharts.com/lib/3/plugins/export/export.css" type="text/css" media="all" />
        <script src="https://www.amcharts.com/lib/3/themes/light.js"></script>
        <script src="http://www.amcharts.com/lib/3/plugins/dataloader/dataloader.min.js" type="text/javascript"></script>
        <script src="amcharts/plugins/export/export.min.js"></script>
        <link href="amcharts/plugins/export/export.css" rel="stylesheet" type="text/css"/>

        <script>
            var chart1 = AmCharts.makeChart("chartdiv", {
                "type": "serial",
                "theme": "light",

                "dataLoader": {
                    "url": "./rest/podatki/tempslajson?datumOd=${formaBean.datumOd}:00&datumDo=${formaBean.datumDo}:00",
                    "format": "json"
                },
                "valueAxes": [{
                        "position": "left",
                        "title": "Temperatura v °C"
                    }],
                "graphs": [{
                        "valueField": "temperatura",
                        "type": "line",
                        "useLineColorForBulletBorder": true,
                        "balloonText": "<div style='margin:10px; text-align:left;'><span style='font-size:10px'>[[datum]]</span><br><span style='font-size:12px'>Temperatura:[[value]] °C</span>",
                        "bullet": "none",
                    }],
                "titles": [
                    {
                        "id": "title",
                        "size": 18,
                        "text": "Temperatura morske vode"
                    }
                ],
                "categoryField": "datum",
                "dataDateFormat": "YYYY-MM-DD JJ:NN:SS",
                "categoryAxis": {
                    "minPeriod": "ss",
                    "parseDates": true,
                    "dashLength": 1,
                    "minorGridEnabled": true
                },
                "chartScrollbar": {
                    "autoGridCount": true,
                    "graph": "g1",
                    "scrollbarHeight": 40
                },
                "chartCursor": {
                    "categoryBalloonDateFormat": "JJ:NN, DD MMMM",
                    "cursorPosition": "mouse"
                },
                "export": {
                    "enabled": true,
                    "libs": {
                        "path": "amcharts/plugins/export/libs/"
                    }
                }
            });


            var chart2 = AmCharts.makeChart("slanostdiv", {
                "type": "serial",
                "theme": "light",

                "dataLoader": {
                    "url": "./rest/podatki/tempslajson?datumOd=${formaBean.datumOd}:00&datumDo=${formaBean.datumDo}:00",
                    "format": "json"
                },
                "valueAxes": [{
                        "position": "left",
                        "title": "Slanost morske vode [PSU]"
                    }],
                "graphs": [{
                        "valueField": "slanost",
                        "type": "line",
                        "useLineColorForBulletBorder": true,
                        "balloonText": "<div style='margin:10px; text-align:left;'><span style='font-size:10px'>[[datum]]</span><br><span style='font-size:12px'>Slanost:[[value]] PSU</span>",
                        "bullet": "none",
                    }],
                "titles": [
                    {
                        "id": "title",
                        "size": 18,
                        "text": "Slanost morske vode"
                    }
                ],
                "categoryField": "datum",
                "dataDateFormat": "YYYY-MM-DD JJ:NN:SS",
                "categoryAxis": {
                    "minPeriod": "ss",
                    "parseDates": true,
                    "dashLength": 1,
                    "minorGridEnabled": true
                },
                "chartScrollbar": {
                    "autoGridCount": true,
                    "graph": "g1",
                    "scrollbarHeight": 40
                },
                "chartCursor": {
                    "categoryBalloonDateFormat": "JJ:NN, DD MMMM",
                    "cursorPosition": "mouse"
                },
                "export": {
                    "enabled": true,
                    "libs": {
                        "path": "../libs/"
                    }
                }
            });




        </script>
    </HEAD>

    <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
        <form id="form" action="./FormServlet" method="post">
            <table style="margin: 0 0 0 0;" border="0" cellpadding="0" cellspacing="0" width="100%">

                <tr height ="50">
                    <td>
                        <table style="margin: 0 0 0 0;" border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
                            <tbody>
                                <tr height ="50">
                                    <td class="topsmall" valign="botom" align="left"><a href="http://www.fpp.uni-lj.si/" target="_blank"><img src="./images/261x84logofpp.png"></img></a></td>
                                    <td class="topsmall" valign="center" align="center" ><b><h1>Video nadzor podvodnega okolja s sprotnimi meritvami temperature in slanosti</td>
                                                <td class="topsmall" valign="botom" align="right" ><a href="http://www.nib.si/mbp/sl/" target="_blank"><img src="./images/405x84logonib.png"></img></a></td>
                                                </tr>
                                                <tr height="20">
                                                    <td></td>
                                                    <td valign="center" align="center">Peter Valenčič, 2017/2018, Magistrsko delo, pomorsko inženirstvo </td>
                                                    <td></td>
                                                </tr>
                                                </tbody>
                                                </table>
                                                </td>
                                                </tr>

                                                <tr height = "30">
                                                    <td>
                                                        <table style="margin: 0 0 0 0;" border="0" cellpadding="0" cellspacing="0" width="100%"  height="30"  >
                                                            <tbody>
                                                                <tr >
                                                                    <td nowrap="" class="navItem">&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                                                    <td class="navItem" onmouseover="this.className = 'navItemOver'" onmouseout="this.className = 'navItem'"><a style="text-decoration: none;" href="index.jsp?id=1">PRIKAZ PODATKOV</a></td>
                                                                    <td class="navItem" onmouseover="this.className = 'navItemOver'" onmouseout="this.className = 'navItem'"><a style="text-decoration: none;" href="index.jsp?id=2">PRIKAZ VIDEO ZAJEMA</a></td>

                                                                    <td class="navItem" onmouseover="this.className = 'navItemOver'" onmouseout="this.className = 'navItem'"><a style="text-decoration: none;" href="index.jsp?id=3">O PROJEKTU</a></td>
                                                                    <td class="navItem" width="70%" align="right"></td>
                                                                </tr>
                                                            </tbody>
                                                        </table>
                                                    </td>
                                                </tr>


                                                <!-- graf -->
                                                <tr>
                                                    <td>

                                                        <table  class="table_top" border="0" cellpadding="0" cellspacing="0" width="100%" >
                                                            <tbody>
                                                                <tr>
                                                                    <td class="table1" valign="top" width="300">
                                                                        <table width="100%" border="0" cellpadding="2" cellspacing="0">
                                                                            <tbody>
                                                                                <tr>
                                                                                    <td colspan="2" class="table_top">
                                                                                        <table border="0" cellspacing="0" cellpadding="0" width="100%">
                                                                                            <tbody>
                                                                                                <tr>
                                                                                                    <td><b>Nastavitve</b>
                                                                                                    </td>
                                                                                                    <td align="right">
                                                                                                        <!-- lahko dam gumbe-->
                                                                                                    </td>
                                                                                                </tr>

                                                                                            </tbody>
                                                                                        </table>

                                                                                    </td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td>
                                                                                        Datum od:
                                                                                    </td>
                                                                                    <td align="right">
                                                                                        <input id="datumod" type="text" name="datumOd" value="${formaBean.datumOd}">
                                                                                       <!-- <input id="datumod" type="datetime-local" style="width: 200;" name="datumOd" value="${formaBean.datumOd}"/> -->
                                                                                    </td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td>
                                                                                        Datum do:
                                                                                    </td>
                                                                                    <td align="right">
                                                                                        <input id="datumdo" type="text" name="datumDo" value="${formaBean.datumDo}">
                                                                                      <!--  <input id="datumdo" type="datetime-local" style="width: 200;" name="datumDo" value="${formaBean.datumDo}"/> -->
                                                                                    </td>

                                                                                </tr>

                                                                                <tr>
                                                                                    <td colspan="2">
                                                                                        <input type="Submit" id="ok" value="Poišči" />

                                                                                    </td>

                                                                                </tr>
                                                                            </tbody>
                                                                        </table>
                                                                    </td>
                                                                    <td>
                                                                        <table border="0"  width="100%" >
                                                                            <% if (id.equalsIgnoreCase("1") | id.equalsIgnoreCase("null")) { %>
                                                                            <tr>
                                                                                <td><div id="chartdiv"></div></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td><div id="slanostdiv"></div></td>
                                                                            </tr>
                                                                            <% } %>
                                                                            <% if (id.equalsIgnoreCase("2")) { %>
                                                                            <tr>
                                                                                <td align="center">
                                                                                    <iframe
                                                                                        src="https://player.twitch.tv/?channel=peterv6i&muted=true"
                                                                                        height="720"
                                                                                        width="1280"
                                                                                        frameborder="0"
                                                                                        scrolling="no"
                                                                                        allowfullscreen="true">
                                                                                    </iframe>

                                                                                </td>
                                                                            </tr>
                                                                            <% } %>
                                                                            <% if (id.equalsIgnoreCase("3")) { %>
                                                                            <tr>
                                                                                <td align="center">
                                                                                    Spletna stran je namenjena prikazu izmerjenih podatkov temperature in slanosti.<br>
                                                                                    Nastala je kot pripomoček za pregled podatkov in prikaz video vsebine<br>
                                                                                    Za merjenje temperature se uporablja analogna sonda SeaBird Scientific model SBE-3 ter za merjenje prevodnosti - slanosti SeaBird Scientific SBE-4<br>
                                                                                    Meritve se opravljajo s pomočjo mikrokrmilnika Arduino uno na katerega je nameščen izdelan modul, ki skrbi za pretvorbo analognega signala (frekvence) v digitalne impulze<br>
                                                                                    Frekvenca se nato izmeri in preračuna v temperaturo, prevodnost ter slanost.<br>
                                                                                    Za shranjevanje podatkov se uporablja podatkovna baza MySQL.<br>
                                                                                    <p>
                                                                                    </p>

                                                                                </td>
                                                                            </tr>
                                                                            <% } %>
                                                                        </table>
                                                                    </td>
                                                                </tr>
                                                            </tbody>
                                                        </table>

                                                    </td>
                                                </tr>
                                                <% if (id.equalsIgnoreCase("1") | id.equalsIgnoreCase("null")) { %>
                                                <% }%>
                                                </table>
                                                </form>
                                                </body>
                                                 <script src="./build/jquery.datetimepicker.full.min.js"></script>
                                                <script>
                                                    $.datetimepicker.setLocale('sl');
                                                    jQuery('#datumod').datetimepicker({
                                                         format:'Y-m-d H:i'
                                                    });
                                                    jQuery('#datumdo').datetimepicker({
                                                         format:'Y-m-d H:i'
                                                    });
                                                  
                                                    var d1;
                                                    var d2;


                                                    //form submit funkcije
                                                    $("#form").submit(function (event) {

                                                        if ($("#datumod").val().length == 0) {
                                                            alert('Datum od ne sme biti prazen');
                                                            return false;
                                                        } else
                                                        {
                                                            d1 = new Date($("#datumod").val());
                                                        }
                                                        ;

                                                        if ($("#datumdo").val().length == 0) {
                                                            alert('DatumDo ne sme biti prazen');
                                                            return false;
                                                        } else
                                                        {
                                                            d2 = new Date($("#datumdo").val());
                                                        }
                                                        ;

                                                        if (d2.getTime() < d1.getTime())
                                                        {
                                                            alert('Datum do ne sme biti manjši od datuma od');
                                                            return false;
                                                        }

                                                    });

                                                    
                                                </script>
                                                </html>
