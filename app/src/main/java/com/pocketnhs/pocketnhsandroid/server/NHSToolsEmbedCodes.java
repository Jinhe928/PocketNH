package com.pocketnhs.pocketnhsandroid.server;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MacBook Pro on 8/31/2016.
 */

public class NHSToolsEmbedCodes {

    public static Map<String,String> embedCodesMap = new HashMap();


    public static String TOOL_HTML_CODE_BMI = "<div style=\"width:624px;  margin-right: auto; margin-left: auto; \">\n" +
            "  <iframe align=\"middle\" style=\"width:624px;height:470px;\" title=\"BMI Healthy weight calculator\" src=\"http://media.nhschoices.nhs.uk/tools/documents/healthy_weight_v3/healthy_weight.html\" frameborder=\"no\" scrolling=\"no\"></iframe>\n" +
            "  <div id=\"assessment_webpart_branding\" style=\"float:right;\"><a href=\"http://nhs.uk/tools/\" alt=\"content provided by NHS Choices\"><img src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/images/syndication.png\" width=\"264\" height=\"38\" border=\"0\" alt=\"content provided by NHS Choices\" style=\"margin-right:22px;\" /></a></div>\n" +
            "</div>";

    
    public static final String TOOL_HTML_CODE_VTE = "<script language=\"javascript\">" +
            "$(window).on('message', function(e) {\tvar tmp = (eval('(' +e.originalEvent.data+')'));" +
            "\twindow.hasOwnProperty = window.hasOwnProperty || Object.prototype.hasOwnProperty;" +
            "\tif(tmp.hasOwnProperty('nhs_redirect')){" +
            "\t\twindow.location.href = tmp.nhs_redirect;\t}" +
            "});    </script><div id=\"assessment_webpart_wrapper\" style=\"width:342px;\"> " +
            " <iframe style=\"width:100%;height:458px;\" title=\"self assessments\" src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/assessment.html?ASid=71&syndicate=true\" " +
            "frameborder=\"no\" scrolling=\"no\"></iframe>  <div id=\"assessment_webpart_branding\" style=\"float:right;\"><a href=\"http://nhs.uk/tools/\" alt=\"content provided by NHS Choices\">" +
            "<img src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/images/syndication.png\" width=\"264\" height=\"38\" border=\"0\" alt=\"content provided by NHS Choices\" /></a></div></div>";


    public static final String TOOL_HTML_CODE_DIABETES_2 = "<script language=\"javascript\">" +
            "$(window).on('message', function(e) {\tvar tmp = (eval('(' +e.originalEvent.data+')'));" +
            "\twindow.hasOwnProperty = window.hasOwnProperty || Object.prototype.hasOwnProperty;" +
            "\tif(tmp.hasOwnProperty('nhs_redirect')){" +
            "\t\twindow.location.href = tmp.nhs_redirect;\t}});    " +
            "</script>" +
            "<div id=\"assessment_webpart_wrapper\" style=\"width:342px;\"> " +
            "<iframe style=\"width:100%;height:430px;\" title=\"self assessments\" src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/assessment.html?ASid=67&syndicate=true\" frameborder=\"no\" scrolling=\"no\"></iframe>  <div id=\"assessment_webpart_branding\" style=\"float:right;\"><a href=\"http://nhs.uk/tools/\" alt=\"content provided by NHS Choices\"><img src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/images/syndication.png\" width=\"264\" height=\"38\" border=\"0\" alt=\"content provided by NHS Choices\" /></a></div></div>";



    public static final String TOOL_HTML_CODE_CHILD_HEALTH ="<script language=\"javascript\">" +
            "$(window).on('message', function(e) {  var tmp = (eval('(' +e.originalEvent.data+')'));  " +
            "window.hasOwnProperty = window.hasOwnProperty || Object.prototype.hasOwnProperty;  " +
            "if(tmp.hasOwnProperty('nhs_redirect')){   window.location.href = tmp.nhs_redirect;  } });     </script> " +
            "<div id=\"assessment_webpart_wrapper\" style=\"width:342px;\">   " +
            "<iframe style=\"width:100%;height:475px;\" title=\"self assessments\" src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/assessment.html?ASid=61&syndicate=true\" frameborder=\"no\" scrolling=\"no\">" +
            "</iframe>   <div id=\"assessment_webpart_branding\" style=\"float:right;\">" +
            "<a href=\"http://nhs.uk/tools/\" alt=\"content provided by NHS Choices\">" +
            "<img src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/images/syndication.png\" width=\"264\" height=\"38\" border=\"0\" alt=\"content provided by NHS Choices\" /></a></div> </div>";



    public static String TOOL_HTML_CODE_MENTAL_HEALTH_DEPRESSION = "<script language=\"javascript\"> " +
            "$(window).on('message', function(e) {  var tmp = (eval('(' +e.originalEvent.data+')'));  window.hasOwnProperty = window.hasOwnProperty || Object.prototype.hasOwnProperty;  " +
            "if(tmp.hasOwnProperty('nhs_redirect')){   window.location.href = tmp.nhs_redirect;  } });  " +
            "   </script> <div id=\"assessment_webpart_wrapper\" style=\"width:342px;\"> " +
            "  <iframe style=\"width:100%;height:376px;\" title=\"self assessments\" src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/assessment.html?ASid=42&syndicate=true\" frameborder=\"no\" scrolling=\"no\"></iframe>   <div id=\"assessment_webpart_branding\" style=\"float:right;\"><a href=\"http://nhs.uk/tools/\" alt=\"content provided by NHS Choices\"><img src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/images/syndication.png\" width=\"264\" height=\"38\" border=\"0\" alt=\"content provided by NHS Choices\" /></a></div> </div>";



    public static String TOOL_HTML_CODE_FERTILITY = "<script language=\"javascript\"> $(window).on('message', function(e) {  var tmp = (eval('(' +e.originalEvent.data+')'));  window.hasOwnProperty = window.hasOwnProperty || Object.prototype.hasOwnProperty;  if(tmp.hasOwnProperty('nhs_redirect')){   window.location.href = tmp.nhs_redirect;  } }); " +
            "    </script> <div id=\"assessment_webpart_wrapper\" style=\"width:342px;\"> " +
            "  <iframe style=\"width:100%;height:410px;\" title=\"self assessments\" src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/assessment.html?ASid=57&syndicate=true\" frameborder=\"no\" scrolling=\"no\"></iframe>   <div id=\"assessment_webpart_branding\" style=\"float:right;\"><a href=\"http://nhs.uk/tools/\" alt=\"content provided by NHS Choices\"><img src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/images/syndication.png\" width=\"264\" height=\"38\" border=\"0\" alt=\"content provided by NHS Choices\" /></a></div> </div>";

    public static String TOOL_HTML_CODE_PERIOD = "<script language=\"javascript\"> $(window).on('message', function(e) {  var tmp = (eval('(' +e.originalEvent.data+')'));  window.hasOwnProperty = window.hasOwnProperty || Object.prototype.hasOwnProperty;  if(tmp.hasOwnProperty('nhs_redirect')){   window.location.href = tmp.nhs_redirect;  } });   " +
            "  </script> <div id=\"assessment_webpart_wrapper\" style=\"width:342px;\">" +
            "   <iframe style=\"width:100%;height:490px;\" title=\"self assessments\" src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/assessment.html?ASid=54&syndicate=true\" frameborder=\"no\" scrolling=\"no\"></iframe>   <div id=\"assessment_webpart_branding\" style=\"float:right;\"><a href=\"http://nhs.uk/tools/\" alt=\"content provided by NHS Choices\"><img src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/images/syndication.png\" width=\"264\" height=\"38\" border=\"0\" alt=\"content provided by NHS Choices\" /></a></div> </div>";

    public static String TOOL_HTML_CODE_SEXUAL_HEALTH = "<script language=\"javascript\"> $(window).on('message', function(e) {  var tmp = (eval('(' +e.originalEvent.data+')'));  window.hasOwnProperty = window.hasOwnProperty || Object.prototype.hasOwnProperty;  if(tmp.hasOwnProperty('nhs_redirect')){   window.location.href = tmp.nhs_redirect;  } }); " +
            "    </script> <div id=\"assessment_webpart_wrapper\" style=\"width:342px;\"> " +
            "  <iframe style=\"width:100%;height:490px;\" title=\"self assessments\" src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/assessment.html?ASid=50&syndicate=true\" frameborder=\"no\" scrolling=\"no\"></iframe>   <div id=\"assessment_webpart_branding\" style=\"float:right;\"><a href=\"http://nhs.uk/tools/\" alt=\"content provided by NHS Choices\"><img src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/images/syndication.png\" width=\"264\" height=\"38\" border=\"0\" alt=\"content provided by NHS Choices\" /></a></div> </div>";

    public static String TOOL_HTML_CODE_MOLE = "<script language=\"javascript\"> $(window).on('message', function(e) {  var tmp = (eval('(' +e.originalEvent.data+')'));  window.hasOwnProperty = window.hasOwnProperty || Object.prototype.hasOwnProperty;  if(tmp.hasOwnProperty('nhs_redirect')){   window.location.href = tmp.nhs_redirect;  } });   " +
            "  </script> <div id=\"assessment_webpart_wrapper\" style=\"width:342px;\">  " +
            " <iframe style=\"width:100%;height:390px;\" title=\"self assessments\" src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/assessment.html?ASid=62&syndicate=true\" frameborder=\"no\" scrolling=\"no\"></iframe>   <div id=\"assessment_webpart_branding\" style=\"float:right;\"><a href=\"http://nhs.uk/tools/\" alt=\"content provided by NHS Choices\"><img src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/images/syndication.png\" width=\"264\" height=\"38\" border=\"0\" alt=\"content provided by NHS Choices\" /></a></div> </div>";

    public static String TOOL_HTML_CODE_KIDNEY = "<script language=\"javascript\"> $(window).on('message', function(e) {  var tmp = (eval('(' +e.originalEvent.data+')'));  window.hasOwnProperty = window.hasOwnProperty || Object.prototype.hasOwnProperty;  if(tmp.hasOwnProperty('nhs_redirect')){   window.location.href = tmp.nhs_redirect;  } });    " +
            " </script> <div id=\"assessment_webpart_wrapper\" style=\"width:342px;\"> " +
            "  <iframe style=\"width:100%;height:435px;\" title=\"self assessments\" src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/assessment.html?ASid=64&syndicate=true\" frameborder=\"no\" scrolling=\"no\"></iframe>   <div id=\"assessment_webpart_branding\" style=\"float:right;\"><a href=\"http://nhs.uk/tools/\" alt=\"content provided by NHS Choices\"><img src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/images/syndication.png\" width=\"264\" height=\"38\" border=\"0\" alt=\"content provided by NHS Choices\" /></a></div> </div>";




    public static String TOOL_HTML_CODE_LONG_TERM = "<script language=\"javascript\"> $(window).on('message', function(e) {  var tmp = (eval('(' +e.originalEvent.data+')'));  window.hasOwnProperty = window.hasOwnProperty || Object.prototype.hasOwnProperty;  if(tmp.hasOwnProperty('nhs_redirect')){   window.location.href = tmp.nhs_redirect;  } });     </script> " +
            "<div id=\"assessment_webpart_wrapper\" style=\"width:342px;\">  " +
            " <iframe style=\"width:100%;height:434px;\" title=\"self assessments\" src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/assessment.html?ASid=59&syndicate=true\" frameborder=\"no\" scrolling=\"no\"></iframe>   <div id=\"assessment_webpart_branding\" style=\"float:right;\"><a href=\"http://nhs.uk/tools/\" alt=\"content provided by NHS Choices\"><img src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/images/syndication.png\" width=\"264\" height=\"38\" border=\"0\" alt=\"content provided by NHS Choices\" /></a></div> </div>";

    public static String TOOL_HTML_CODE_BLADDER = "<script language=\"javascript\"> $(window).on('message', function(e) {  var tmp = (eval('(' +e.originalEvent.data+')'));  window.hasOwnProperty = window.hasOwnProperty || Object.prototype.hasOwnProperty;  if(tmp.hasOwnProperty('nhs_redirect')){   window.location.href = tmp.nhs_redirect;  } });     </script> " +
            "<div id=\"assessment_webpart_wrapper\" style=\"width:342px;\">  " +
            " <iframe style=\"width:100%;height:410px;\" title=\"self assessments\" src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/assessment.html?ASid=56&syndicate=true\" frameborder=\"no\" scrolling=\"no\"></iframe>   <div id=\"assessment_webpart_branding\" style=\"float:right;\"><a href=\"http://nhs.uk/tools/\" alt=\"content provided by NHS Choices\"><img src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/images/syndication.png\" width=\"264\" height=\"38\" border=\"0\" alt=\"content provided by NHS Choices\" /></a></div> </div>";

    public static String TOOL_HTML_CODE_ASTHMA = "<script language=\"javascript\"> $(window).on('message', function(e) {  var tmp = (eval('(' +e.originalEvent.data+')'));  window.hasOwnProperty = window.hasOwnProperty || Object.prototype.hasOwnProperty;  if(tmp.hasOwnProperty('nhs_redirect')){   window.location.href = tmp.nhs_redirect;  } });    " +
            " </script> <div id=\"assessment_webpart_wrapper\" style=\"width:342px;\"> " +
            "  <iframe style=\"width:100%;height:430px;\" title=\"self assessments\" src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/assessment.html?ASid=52&syndicate=true\" frameborder=\"no\" scrolling=\"no\"></iframe>   <div id=\"assessment_webpart_branding\" style=\"float:right;\"><a href=\"http://nhs.uk/tools/\" alt=\"content provided by NHS Choices\"><img src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/images/syndication.png\" width=\"264\" height=\"38\" border=\"0\" alt=\"content provided by NHS Choices\" /></a></div> </div>";




    public static String TOOL_HTML_CODE_MOOD =
    "<script language=\"javascript\">\n"+
            "$(window).on('message', function(e) {\n"+
            "\tvar tmp = (eval('(' +e.originalEvent.data+')'));\n"+
            "\twindow.hasOwnProperty = window.hasOwnProperty || Object.prototype.hasOwnProperty;\n"+
            "\tif(tmp.hasOwnProperty('nhs_redirect')){\n"+
            "\t\twindow.location.href = tmp.nhs_redirect;\n"+
            "\t}\n"+
            "});    \n"+
            "</script>\n"+
            "<div id=\"assessment_webpart_wrapper\" style=\"width:342px;\">\n"+
            "  <iframe style=\"width:100%;height:500px;\" title=\"self assessments\" src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/assessment.html?ASid=44&syndicate=true\" frameborder=\"no\" scrolling=\"no\"></iframe>\n"+
            "  <div id=\"assessment_webpart_branding\" style=\"float:right;\"><a href=\"http://nhs.uk/tools/\" alt=\"content provided by NHS Choices\"><img src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/images/syndication.png\" width=\"264\" height=\"38\" border=\"0\" alt=\"content provided by NHS Choices\" /></a></div>\n"+
            "</div>";



    public static String TOOL_HTML_CODE_YOUR_BLOOD_PREASURE_READING2 = "<iframe id=\"bp-frame\" data-source=\"example\" seamless=\"seamless\" scrolling=\"no\" frameBorder=\"0\" width=\"100%\" style=\"margin-bottom:12px;\">" +
            "</iframe>      <!-- Beginwebpart -->    " +
            "  <script>    if (!window.jQuery) { document.write('<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.2.6/jquery.min.js\"><\\\\/script>'); }   </script>   " +
            " <script type=\"text/javascript\">   $(window).load(function(){    setsource($('#bp-frame').attr('data-source'));      });    " +
            "function setsource(syn)   {    if($('#bp-frame').attr('src') != '')    {     var frameUrl = 'http://nhs.uk/tools/documents/bloodpressure/index.html';          if(syn != '' && syn != undefined) " +
            "    {      frameUrl += '?syn=' + syn;     }          var location = window.location.href;      location = location.replace('http://','').replace('https://','');          if(location.indexOf('nhs.uk') == -1)   " +
            "  {      var querySym = frameUrl.indexOf('?') == -1 ? '?' : '&'      frameUrl += querySym + 'location=' + location;     }          $('#bp-frame').attr('src',frameUrl);    }   }  " +
            "    function handleMessage(e) {      var messageObject = e.data.split(\":\");      var action = messageObject[0];   var height = messageObject[1];      if (action == 'RESIZE') {      " +
            " var iFrame = document.getElementById('bp-frame');    iFrame.style.height = height + \"px\";    }         if(action == 'SLICKRESIZE')   {    var targetHeight = height;    $('#bp-frame').animate({height:targetHeight},1000);       }   } " +
            "  if(window.addEventListener)   {    window.addEventListener(\"message\", handleMessage, false);   }   else if(window.attachEvent)   {    window.attachEvent(\"onmessage\",handleMessage);   }  </script>";


    public static String TOOL_HTML_CODE_YOUR_BLOOD_PREASURE_READING =
            "<iframe id=\"bp-frame\" data-source=\"example\" seamless=\"seamless\" scrolling=\"no\" frameBorder=\"0\" width=\"100%\" style=“margin-bottom:12px;”></iframe>\n" +
                    "\t\t\n" +

                    " <div id=\"assessment_webpart_branding\" style=\"float:right;\"><a href=\"http://nhs.uk/tools/\" alt=\"content provided by NHS Choices\"><img src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/images/syndication.png\" width=\"264\" height=\"38\" border=\"0\" alt=\"content provided by NHS Choices\" style=\"margin-right:22px;\" /></a></div>\n" +
                    "\t\t<!-- Beginwebpart -->\n" +
                    "\t\t\n" +
                    "\t\t<script>\n" +
                    "\t\t\tif (!window.jQuery) { document.write('<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.2.6/jquery.min.js\"><\\/script>'); }\n" +
                    "\t\t</script>\t\n" +
                    "\t\t<script type=\"text/javascript\">\n" +
                    "\t\t$(window).load(function(){\n" +
                    "\t\t\tsetsource($('#bp-frame').attr('data-source'));\n" +
                    "\t\t\n" +
                    "\t\t});\n" +
                    "\n" +
                    "\t\tfunction setsource(syn)\n" +
                    "\t\t{\n" +
                    "\t\t\tif($('#bp-frame').attr('src') != '')\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\tvar frameUrl = 'http://nhs.uk/tools/documents/bloodpressure/index.html';\n" +
                    "\t\t\t\t\n" +
                    "\t\t\t\tif(syn != '' && syn != undefined)\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\tframeUrl += '?syn=' + syn;\n" +
                    "\t\t\t\t}\n" +
                    "\t\t\t\t\n" +
                    "\t\t\t\tvar location = window.location.href;\n" +
                    "\n" +
                    "\t\t\t\tlocation = location.replace('http://','').replace('https://','');\n" +
                    "\t\t\t\t\n" +
                    "\t\t\t\tif(location.indexOf('nhs.uk') == -1)\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\tvar querySym = frameUrl.indexOf('?') == -1 ? '?' : '&'\n" +
                    "\t\t\t\t\tframeUrl += querySym + 'location=' + location;\n" +
                    "\t\t\t\t}\n" +
                    "\t\t\t\t\n" +
                    "\t\t\t\t$('#bp-frame').attr('src',frameUrl);\n" +
                    "\t\t\t}\n" +
                    "\t\t}\n" +
                    "\t\t\n" +
                    "\t\tfunction handleMessage(e) {\n" +
                    "\t\t\n" +
                    "\t\tvar messageObject = e.data.split(\":\");\n" +
                    "\t\t\n" +
                    "\t\tvar action = messageObject[0];\n" +
                    "\t\tvar height = messageObject[1];\n" +
                    "\t\t\n" +
                    "\t\tif (action == 'RESIZE') {\n" +
                    "\t\t\n" +
                    "\t\t\tvar iFrame = document.getElementById('bp-frame');\n" +
                    "\t\t\tiFrame.style.height = height + \"px\";\n" +
                    "\t\t\t}\n" +
                    "\t\t\n" +
                    "\t\t\n" +
                    "\t\tif(action == 'SLICKRESIZE')\n" +
                    "\t\t{\n" +
                    "\t\t\tvar targetHeight = height;\n" +
                    "\t\t\t$('#bp-frame').animate({height:targetHeight},1000);\t\n" +
                    "\t\t\n" +
                    "\t\t}\n" +
                    "\t\t}\n" +
                    "\t\t\n" +
                    "\t\t\n" +
                    "\t\tif(window.addEventListener)\n" +
                    "\t\t{\n" +
                    "\t\t\twindow.addEventListener(\"message\", handleMessage, false);\n" +
                    "\t\t}\n" +
                    "\t\telse if(window.attachEvent)\n" +
                    "\t\t{\n" +
                    "\t\t\twindow.attachEvent(\"onmessage\",handleMessage);\n" +
                    "\t\t}\n" +
                    "\n" +
                    "\t</script>" ;



    public static final String TOOL_HTML_CODE_ALLERGY_MYTH_BUSTER ="<object width=\"546\" height=\"346\"><param name=\"movie\" value=\"http://media.nhschoices.nhs.uk/Tools/Documents/myth_food_allergy.swf\">" +
            "</param><param name=\"allowFullScreen\" value=\"true\"><param name=\"flashvars\" value=\"XMLpath=http://media.nhschoices.nhs.uk/Tools/Documents/\">" +
            "</param></param><param name=\"wmode\" value=\"transparent\"></param></param><param name=\"quality\" value=\"high\"></param>" +
            "<embed src=\"http://media.nhschoices.nhs.uk/Tools/Documents/myth_food_allergy.swf\" type=\"application/x-shockwave-flash\" flashvars=\"XMLpath=http://media.nhschoices.nhs.uk/Tools/Documents/\" wmode=\"transparent\" allowfullscreen=\"true\" quality=\"high\" width=\"546\" height=\"346\">" +
            "</embed></object>";

    public static String TOOL_HTML_CODE_HAIR_LOSS = "<object width=\"546\" height=\"346\">" +
            "<param name=\"movie\" value=\"http://media.nhschoices.nhs.uk/Tools/Documents/myth_hair.swf\">" +
            "</param><param name=\"allowFullScreen\" value=\"true\"><param name=\"flashvars\" value=\"XMLpath=http://media.nhschoices.nhs.uk/Tools/Documents/\">" +
            "</param></param><param name=\"wmode\" value=\"transparent\"></param></param><param name=\"quality\" value=\"high\"></param>" +
            "<embed src=\"http://media.nhschoices.nhs.uk/Tools/Documents/myth_hair.swf\" type=\"application/x-shockwave-flash\" flashvars=\"XMLpath=http://media.nhschoices.nhs.uk/Tools/Documents/\" wmode=\"transparent\" allowfullscreen=\"true\" quality=\"high\" width=\"546\" height=\"346\"></embed></object> ";


}
