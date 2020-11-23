var editor;
var lastIndex = 0;
var zTree;
var rMenu;
var zNodes; //defined in scripting.jsp

$(document).ready(function() {
    var token = $("meta[name='_csrf']").attr("content");

    hac.global.messageFromTag($("#uploadResult"));
    setting = {
        check: {
            chkboxType: {"Y":"ps","N":"ps"},
            chkStyle: "checkbox",
            enable: false,
            nocheckInherit: true
        },
        callback: {
            onRightClick: displayContextMenu
        }
    };
    zTree = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
    rMenu = $("#rMenu");
    $('#saveButton').click(function(){
        if($('#code').val().trim()==""){
            alert('Please enter script code');
            return false;
        }
        return true;
    });

    $('#loadButton').click(function(){
        hideRMenu();
        var nodes = zTree.getSelectedNodes();
        if (nodes && nodes.length>0) {
            $('#code').val(nodes[0].name);
            $('#nav-tabs-edit').click();
            loadScript();
        }
    });
    $('#deleteButton').click(function(){
        deleteScript();
    });
	$("#textarea-container").resizable().height("500px").width("100%");
	editor = CodeMirror.fromTextArea(document.getElementById("script"), {
		mode: "text/x-groovy",
		lineNumbers: true,
		styleActiveLine: true,
		autofocus: true
	});
	$("#scriptType").change(function(){
		switch($("#scriptType option:selected").text()){
			case "beanshell": editor.setOption("mode", "text/x-java");break;
			case "javascript": editor.setOption("mode", "text/javascript");break;
			case "groovy": editor.setOption("mode", "text/x-groovy");break;
			default: editor.setOption("mode", $("#scriptType").val());break;
		}
	});

    $("#commitCheckbox").change(function() {
        var isInCommitMode = $("#commitCheckbox").is(':checked');

        var msg = "Rollback Mode: script results <strong>won't be persisted</strong> in the system!";
        if (isInCommitMode === true) {
            msg = "Commit Mode: script results <strong>will be persisted</strong> in the system!";
        }

        $("#modeWarningMsg").html(msg);
    });

	var tabs = $("#tabsNoSidebar").tabs();
	
	$("#tabsNoSidebar").bind("tabsselect", function(event, ui){
		switch(lastIndex){
			case 0:
				$("#nav-tabs-edit").css("font-weight", "normal");
				break;
			;
			case 1:
				$("#nav-tabs-result").css("font-weight", "normal");
				break;
			;
			case 2:
				$("#nav-tabs-output").css("font-weight", "normal");
				break;
			;
            case 3:
                $("#nav-tabs-stacktrace").css("font-weight", "normal");
                break;
            ;
            case 4:
                $("#nav-tabs-browse").css("font-weight", "normal");
                break;
            ;
		}
		lastIndex = ui.index;
	});
	activateLastActiveTab();

	$("#executeButton").click(function(event) {

        $('#spinnerWrapper').show();
		$.ajax({
			type : "POST",
			url : $('#executeButton').attr('data-executorUrl'),
			data : {
				script : editor.getValue(),
				scriptType : $('#scriptType').val(),
				commit: $("#commitCheckbox").is(':checked')
			},
			dataType : 'json',
            headers : {
                'Accept' : 'application/json',
                'X-CSRF-TOKEN' : token
            },
			success : function(data) {
				$('#spinnerWrapper').hide();

				$('#output').text("");
				$('#result').text("");
				$('#stacktrace').text("");

                var executedInCommitMode = $("#commitCheckbox").is(':checked');

                var executedMsg = "Script was executed in a <strong>rollback mode</strong>. Results <strong>weren't persisted</strong> in the system!<hr/>";
                if (executedInCommitMode === true) {
                    executedMsg = "Script was executed in a <strong>commit mode</strong>. Results <strong>were persisted</strong> in the system!<hr/>";
                }
                $('.executedModeMsg').html(executedMsg);

				if (data.executionResult.length > 0) {
					if (data.executionResult != "null") {
						$("#tabsNoSidebar").tabs( "option", "active", 0 );
					}
					$('#result').text(data.executionResult);
					$('#result').fadeIn();
					$("#nav-tabs-result").css("font-weight", "bold");
					
				} else {
					$('#result').fadeOut();
				}
				
				if (data.outputText.length > 0) {
					$("#tabsNoSidebar").tabs( "option", "active", 2);
					$('#output').text(data.outputText);
                    $('#output').fadeIn();
					$("#nav-tabs-output").css("font-weight", "bold");
				} else {
					$('#output').fadeOut();
				}				

				if (data.stacktraceText.length > 0) {
					$('#stacktrace').fadeIn();
					$('#stacktrace').text(data.stacktraceText);
					$("#tabsNoSidebar").tabs( "option", "active", 4);
					$("#nav-tabs-stacktrace").css("font-weight", "bold");
				} else {
					$('#stacktrace').fadeOut();
				}
			},

			error : hac.global.err

		});
	});

	function loadScript(event) {
        $('#spinnerWrapper').show();
        $.ajax({
            type : "POST",
            url : $('#loadButton').attr('data-executorUrl'),
            data : {
                code: $('#code').val()
            },
            dataType : 'json',
            headers : {
                'Accept' : 'application/json',
                'X-CSRF-TOKEN' : token
            },
            success : function(data) {
                $('#spinnerWrapper').hide();
                if(data.exception!=null){
                    hac.global.error(data.exception,2000,function(){
                        //
                    });
                }else{
                    hac.global.notify("Script loaded.",2000,function(){
                        //
                    });
                    editor.setValue(data.content.content);
                    $('#scriptType').filter(function() {
                        return true;
                    }).prop('selected', false);
                    $('#scriptType option').filter(function() {
                        return ($(this).text() == data.content.engineName);
                    }).prop('selected', true);
                }
            },
            error : hac.global.err
        });
    }

    function deleteScript(event) {
        $('#spinnerWrapper').show();
        var nodes = zTree.getSelectedNodes();
        $.ajax({
            type : "POST",
            url : $('#deleteButton').attr('data-executorUrl'),
            data : {
                code: nodes[0].name
            },
            dataType : 'json',
            success : function(data) {
                $('#spinnerWrapper').hide();
                removeTreeNode();
                if(data.exception!=null){
                    hac.global.error(data.exception,2000,function(){
                        //
                    });
                }else{
                    hac.global.notify("Script deleted.",2000,function(){
                        //
                    });
                }
            },
            error : hac.global.err
        });
    }

    $('#spinnerWrapper').hide().ajaxStart(function() {
        $(this).show();
    }).ajaxStop(function() {
        $(this).hide();
    });

    function displayContextMenu(event, treeId, treeNode){
        if(treeNode && !treeNode.isParent && (treeNode.getParentNode()!=zTree.getNodes()[0])){
            zTree.selectNode(treeNode);
            showRMenu("node", event.clientX, event.clientY, treeNode);
        } else if (treeNode && !treeNode.noR) {
            zTree.cancelSelectedNode();
            //showRMenu("root", event.clientX, event.clientY);
        }
    }
    function hideRMenu() {
        if (rMenu) rMenu.css({"visibility": "hidden"});
        $("body").unbind("mousedown", onBodyMouseDown);
    }
    function showRMenu(type, x, y, treeNode) {
        $("#rMenu ul").show();
        if (type=="root") {
            $("#reloadButton").show();
            $("#loadButton").hide();
            $("#deleteButton").hide();
        } else {
            $("#reloadButton").hide();
            $("#loadButton").show();
            $("#deleteButton").show();
        }
        rMenu.css({"top":y+"px", "left":x+"px", "visibility":"visible"});
        $("body").bind("mousedown", onBodyMouseDown);
    }
    function onBodyMouseDown(event){
        if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length>0)) {
            rMenu.css({"visibility" : "hidden"});
        }
    }
    function removeTreeNode() {
        hideRMenu();
        var nodes = zTree.getSelectedNodes();
        if (nodes && nodes.length>0) {
            if (nodes[0].children && nodes[0].children.length > 0) {
                var msg = "If you delete this node will be deleted along with sub-nodes. \n\nPlease confirm!";
                if (confirm(msg)==true){
                    zTree.removeNode(nodes[0]);
                }
            } else {
                zTree.removeNode(nodes[0]);
            }
        }
    }

   function activateLastActiveTab(){
       switch($("#lastActiveTab").text()){
           case "upload": $('#nav-tabs-browse').click(); break;
           default: $('#nav-tabs-edit').click(); break;
       }

   }

});