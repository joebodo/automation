/**
 * Global application related logic
 */
$(function() {
    var keys = [];

	$('body').keydown(function(e) {
        keys[e.keyCode] = e.type == 'keydown';

		if (e.ctrlKey && e.altKey
				&& e.keyCode == 72)
			location.href = hac.homeLink;
		else if (e.ctrlKey && e.altKey
				&& e.keyCode == 83) {
			$('#searchsuggest').focus();
		}
	});	
	
	/**
	 * Navigation logic
	 * 
	 */
	$('.nav').click(function(e) {
		
		if ($('#' + e.target.id).hasClass('activeTab')) {

			hac.global.hideSubnavigation();
			$('.prepend-top').removeClass('lowered');

		} else {
	
			$('.subnavigation').hide();
			$('.prepend-top').addClass('lowered');
		
			if ($('#nav_' + e.target.id).css('display') == 'none')
				$('#nav_' + e.target.id).show();
			
			$('nav.topnavigation ul li a').removeClass('activeTab');
			$('#' + e.target.id).addClass('activeTab');
		}
	});
	
	/**
	 * sidebar toggle if available
	 */
	if ($('#toggleSidebarButton'))
		$('#toggleSidebarButton').click(
				hac.global.toggleSidebar);
	
	/**
	 * Parsing boolean strings
	 */
	Boolean.parse = function (str) {
		  switch (str.toLowerCase ()) {
		    case "true":
		      return true;
		    case "false":
		      return false;
		    default:
		      throw new Error ("Boolean.parse: Cannot convert string to boolean.");
		  }
		};
		
	/**
	 * Defines isBlank function which allows following checks:
	 * $.isBlank(" ") 		=> true
	 * $.isBlank("") 		=> true
	 * $.isBlank("\n")		=> true
	 * $.isBlank("a")		=> false
	 * $.isBlank(null)		=> true
	 * $.isBlank(undefined)	=> true
	 * $.isBlank(false)		=> true
	 * $.isBlank([])		=> true
	 */			
	(function($){
		  $.isBlank = function(obj){
			    return(!obj || $.trim(obj) === "");
		  };
	})(jQuery);
	
	
	/**
	 * Handle flash messages
	 */
	if ($("#flash").length > 0) {
		var message = $("#flash").attr('data-message');
		var severity = $("#flash").attr('data-severity');
		hac.global.msg(message, severity);
	} 		
	
	/**
	 * Make all "readonly" inputs gray/inactive
	 */
	$(':input[readonly=readonly]').addClass("inputDisabled");
	
	/**
	 * Handle file inputs
	 */
	$('.fileInput > :input[type=file]').change(function() {
		$(this).next('div').next('input').val($(this).val().replace("C:\\fakepath\\", ""));
	});
	
	/**
	 * About information (click on footer)
	 */
	$('#about').click(function() {
		var url = $('#about').attr('data-aboutUrl');
		
		$.ajax({
			url : url,
			type : 'GET',
			headers : {
				'Accept' : 'application/json'
			},		
			success : function(data) {
				$('#aboutContainer').html("");
				$('#aboutContainer').dialog();
				$('#aboutContainer').dialog('option', 'title', 'About hybris platform: ');
			
				$('#aboutContainer').append(renderAboutElement("Platform", data.platform));	
				$('#aboutContainer').append(renderAboutElement("OS", data.os));	
				$('#aboutContainer').append(renderAboutElement("Server", data.server));	
				$('#aboutContainer').append(renderAboutElement("Num CPU", data.numCPU));	
				
				$('#aboutContainer').dialog({'modal': true});
				$('#aboutContainer').dialog('open');
	
				return false;	
			},
			error : hac.global.err
		});
	});	
	
	renderAboutElement = function(topic, el) {
		var container = $('<div />');
		container.append($('<strong />').append(topic).append(": "));
		container.append(el);
		return container;
	};	
});