<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>

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
        <META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8"/>
        <LINK REL="stylesheet" type="text/css" href="./css/style.css" TITLE="Default">

        <script src="//ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <link rel="stylesheet" type="text/css" href="./css/jquery.datetimepicker.css"/>

        <script src="//www.amcharts.com/lib/3/amcharts.js"></script>
        <script src="//www.amcharts.com/lib/3/serial.js"></script>
        <script src="//www.amcharts.com/lib/3/plugins/export/export.min.js"></script>
        <link rel="stylesheet" href="//www.amcharts.com/lib/3/plugins/export/export.css" type="text/css" media="all"/>
        <script src="//www.amcharts.com/lib/3/themes/light.js"></script>
        <script src="//www.amcharts.com/lib/3/plugins/dataloader/dataloader.min.js" type="text/javascript"></script>
        <script src="amcharts/plugins/export/export.min.js"></script>
        <link href="amcharts/plugins/export/export.css" rel="stylesheet" type="text/css"/>

        <link rel="stylesheet" href="http://vjs.zencdn.net/7.0.0/video-js.css">
        <link href="http://vjs.zencdn.net/7.4.1/video-js.css" rel="stylesheet">
        <script src="http://vjs.zencdn.net/7.4.1/video.js"></script>

        <style>
            #video-container {
                position: relative;
            }

            #overlay {
                position: absolute; 
                top: 0;
                background-color: rgba(153, 153, 153, 0.6);
                color: #fff;
                text-align: left;
                font-size: 20px;
                width: 220px;
                z-index: 100;
                pointer-events: none;
            }

            #aquarium-video {
                position: absolute;
                top: 0;
                z-index: 1;
            }
        </style>

        <script>
            var chart1 = AmCharts.makeChart("chartdiv", {
                "type": "serial",
                "theme": "light",

                "dataLoader": {
                    "url": "rest/podatki/tempslajson?datumOd=${formaBean.datumOd}:00&datumDo=${formaBean.datumDo}:00",
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
                    "url": "rest/podatki/tempslajson?datumOd=${formaBean.datumOd}:00&datumDo=${formaBean.datumDo}:00",
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
        <form id="form" action="FormServlet" method="post">
            <table style="margin: 0 0 0 0;" border="0" cellpadding="0" cellspacing="0" width="100%">

                <tr height="50">
                    <td>
                        <table style="margin: 0 0 0 0;" border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
                            <tbody>
                                <tr height="50">
                                    <td class="topsmall" valign="botom" align="left"><a href="http://www.fpp.uni-lj.si/" target="_blank"><img
                                                src="./images/261x84logofpp.png"></img></a></td>
                                    <td class="topsmall" valign="center" align="center"><b><h1>Video nadzor podvodnega okolja s sprotnimi
                                                meritvami temperature in slanosti</td>
                                                <td class="topsmall" valign="botom" align="right"><a href="//www.nib.si/mbp/sl/" target="_blank"><img
                                                            src="./images/405x84logonib.png"></img></a></td>
                                                </tr>
                                                <tr height="20">
                                                    <td></td>
                                                    <td valign="center" align="center">Peter Valenčič, 2017/2018, Magistrsko delo, pomorsko inženirstvo</td>
                                                    <td></td>
                                                </tr>
                                                </tbody>
                                                </table>
                                                </td>
                                                </tr>

                                                <tr height="30">
                                                    <td>
                                                        <table style="margin: 0 0 0 0;" border="0" cellpadding="0" cellspacing="0" width="100%" height="30">
                                                            <tbody>
                                                                <tr>
                                                                    <td nowrap="" class="navItem">&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                                                    <td class="navItem" onmouseover="this.className = 'navItemOver'" onmouseout="this.className = 'navItem'"><a
                                                                            style="text-decoration: none;" href="index.jsp?id=1">PRIKAZ PODATKOV</a></td>
                                                                    <td class="navItem" onmouseover="this.className = 'navItemOver'" onmouseout="this.className = 'navItem'"><a
                                                                            style="text-decoration: none;" href="index.jsp?id=2">PRIKAZ VIDEO ZAJEMA</a></td>

                                                                    <td class="navItem" onmouseover="this.className = 'navItemOver'" onmouseout="this.className = 'navItem'"><a
                                                                            style="text-decoration: none;" href="index.jsp?id=3">O PROJEKTU</a></td>
                                                                    <td class="navItem" width="70%" align="right"></td>
                                                                </tr>
                                                            </tbody>
                                                        </table>
                                                    </td>
                                                </tr>


                                                <!-- graf -->
                                                <tr>
                                                    <td>

                                                        <table class="table_top" border="0" cellpadding="0" cellspacing="0" width="100%">
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
                                                                                        <input type="Submit" id="ok" value="Poišči"/>

                                                                                    </td>

                                                                                </tr>
                                                                            </tbody>
                                                                        </table>
                                                                    </td>
                                                                    <td>
                                                                        <table border="0" width="100%">
                                                                            <% if (id.equalsIgnoreCase("1") | id.equalsIgnoreCase("null")) { %>
                                                                            <tr>
                                                                                <td>
                                                                                    <div id="chartdiv"></div>
                                                                                </td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td>
                                                                                    <div id="slanostdiv"></div>
                                                                                </td>
                                                                            </tr>
                                                                            <% } %>
                                                                            <% if (id.equalsIgnoreCase("2")) { %>
                                                                            <tr>
                                                                                <td >
                                                                                    <iframe
                                                                                        src="https://player.twitch.tv/?channel=peterv6i&muted=true"
                                                                                        height="506"
                                                                                        width="900"
                                                                                        frameborder="0"
                                                                                        scrolling="no"
                                                                                        allowfullscreen="true">
                                                                                    </iframe>
                                                                                    <br>
                                                                                   
                                                                                    <hr>

                                                                                    <div id="video-container">
                                                                                        <div id="overlay">T: <code id="overlay-data-temp">---</code>
                                                                                            <br>
                                                                                            S: <code id="overlay-data-sal">---</code>
                                                                                            <br>
                                                                                            C: <code id="overlay-data-con">---</code>
                                                                                        </div>
                                                                                        <video id="aquarium-video" class="video-js" width="900" height="506" autoplay muted
                                                                                               poster="" data-setup='{}'>
                                                                                            <source src="https://prenosi.arnes.si/8735-2019-2-14-4eb0/arnes/playlist.m3u8" type="application/x-mpegURL">
                                                                                            <p class='vjs-no-js'>
                                                                                                To view this video please enable JavaScript, and consider upgrading to a web browser that
                                                                                                <a href='https://videojs.com/html5-video-support/' target='_blank'>supports HTML5 video</a>
                                                                                            </p>
                                                                                        </video>
                                                                                    </div>                                                                            




                                                                                </td>
                                                                            </tr>
                                                                            <% } %>
                                                                            <% if (id.equalsIgnoreCase("3")) { %>
                                                                            <tr>
                                                                                <td align="center">
                                                                                    Spletna stran je namenjena prikazu izmerjenih podatkov temperature in slanosti.<br>
                                                                                    Nastala je kot pripomoček za pregled podatkov in prikaz video vsebine<br>
                                                                                    Za merjenje temperature se uporablja analogna sonda SeaBird Scientific model SBE-3 ter za merjenje
                                                                                    prevodnosti - slanosti SeaBird Scientific SBE-4<br>
                                                                                    Meritve se opravljajo s pomočjo mikrokrmilnika Arduino uno na katerega je nameščen izdelan modul, ki
                                                                                    skrbi za pretvorbo analognega signala (frekvence) v digitalne impulze<br>
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
                        format: 'Y-m-d H:i'
                    });
                    jQuery('#datumdo').datetimepicker({
                        format: 'Y-m-d H:i'
                    });

<% if (id.equalsIgnoreCase("2")) { %>
                    function UpdateData() {
                        var url = "rest/podatki/getTCS";
                        $.ajax(url)
                                .done((data) => {
                                    if (data === null){
                                        return;
                                    }
                                    var temp = $(data).find("root>temp>value").text() + " °C";
                                    var sal = $(data).find("root>sal>value").text() + " PSU";
                                    var cond = $(data).find("root>cond>value").text() + " S/m";
                                    $('#overlay-data-temp').text(temp);
                                    $('#overlay-data-sal').text(sal);
                                    $('#overlay-data-con').text(cond);
                                })
                                .fail((error) => {
                                    console.log('error', error);
                                })
                                .always(() => {
                                    console.log('DONE');
                                });
                    }

                    $(document).ready(function () {
                        var player = videojs('aquarium-video');
                        player.ready(function () {
                            var promise = player.play();

                            if (promise !== undefined) {
                                promise.then(function () {
                                    // Autoplay started!
                                    console.log('STARTED');
                                }).catch(function (error) {
                                    // Autoplay was prevented.
                                    console.log('NOT STARTED', error.message);
                                });
                            }
                        });

                        setInterval(UpdateData, 2000);
                    });

<% } %>
                                                </script>
                                                </html>
