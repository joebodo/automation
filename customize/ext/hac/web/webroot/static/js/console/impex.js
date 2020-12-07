/**
 * Script responsible for getting export result message and passing it to hac.global notification 
 * and changing content form action value when "validate" is clicked.
 */

var typeAttrUrl;
var allTypesUrl;
var dataSeparator = ";";
var stringWrapper = '"';
var styleEnabled = true;

$(function() {
	var mouseX = 0;
	var mouseY = 0;
	var editor;
	typeAttrUrl = $("#content").attr("typeAttr-Url");
	allTypesUrl = $("#content").attr("allTypes-Url");
//	dataSeparator = $("#dataSeparator").val();
//	stringWrapper = $("#stringWrapper").val();
	
	hac.global.messageFromTag($("#impexResult"));
	hac.global.messageFromTag($('#validationResultMsg'));
	
	$(document).mousemove(function(e){
		mouseX = e.pageX;
		mouseY = e.pageY;
	});
	
	var tch = Math.max($(window).height()  - 450, 200);
	$("#textarea-container").resizable().height(tch).width("auto");
	
	CodeMirror.commands.autocomplete = function(cm) {
		CodeMirror.showHint(cm, CodeMirror.impexHint);
    }
	
	editor = CodeMirror.fromTextArea(document.getElementById("script"), {
		mode: "text/x-impex",
		lineNumbers: true,
		autofocus: true,
		extraKeys: {
	        "F11": function(cm) {
	        	setFullScreen(cm, !isFullScreen(cm));
	        },
	        "Esc": function(cm) {
	        	if (isFullScreen(cm)) setFullScreen(cm, false);
	        },
	        "Ctrl-Space": "autocomplete"
	    }
	});
	
	$("#tabsNoSidebar").tabs();
	
	$("#settings").accordion({collapsible: true, active: false});
	
	// >>> Tooltips for Variables. works only with impexhighlight.js and impex-hint.js
	$("body").on("mouseover",'.cm-impex_var', function(e){
		var varName = e.target.innerHTML.trim();
		$("#tooltip").css("top", mouseY + 7 + "px");
		$("#tooltip").css("left", mouseX + 7 + "px");
		if(variables[varName] === "" || variables[varName] === "=" || variables[varName] == undefined) { 
			$("#tooltip").html("<span style='font-style:italic'>'undefined'</span>");
		}
		else{
			$("#tooltip").html(variables[varName.trim()].substring(1));
		}
		$("#tooltip").show();
	});
	
	$("body").on("mouseout",'.cm-impex_var', function(e){
		$("#tooltip").html("");
		$("#tooltip").hide();		
	});
	// <<< Tooltips
	
	$("#syntaxHighlighting").change(function(){
		if($("#syntaxHighlighting").is(":checked")){
			editor.setOption("mode", "text/x-impex");
			editor.refresh();
			styleEnabled = true;
		}
		else{
			editor.setOption("mode", null);
			editor.removeOverlay("mode", "text/x-impex");
			editor.refresh();
			styleEnabled = false;
		}
	});
	
	$('#validate').click(function() {
		$('#contentForm').attr('action', $('#contentForm').attr('data-validateUrl'));
		$('#contentForm').submit();		
	});
	
	$('#clearScriptContent').click(function() {
		editor.setValue("");
		return false;
	});
	
	$("#stringWrapper").change(function(){
		stringWrapper = $("#stringWrapper").val();
		var code = editor.getValue();
		editor.setValue("");
		editor.setValue(code);
	});
	$("#dataSeparator").change(function(){
		dataSeparator = $("#dataSeparator").val();
		var code = editor.getValue();
		editor.setValue("");
		editor.setValue(code);
	});
});