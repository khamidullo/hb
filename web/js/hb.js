var g_type;
var language = window.navigator.userLanguage || window.navigator.language;
var field_required = 'Please fill out this field';
var field_required_en = 'Please fill out this field';
var field_required_ru = 'Пожалуйста, заполните';
var field_required_uz = 'Iltimos, to`ldiring';

if (language == 'en-US') field_required = field_required_en
else if (language == 'ru-RU') field_required = field_required_ru
else if (language == 'uz-UZ') field_required = field_required_uz
else field_required = field_required_en;

var email_filter = /[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,4}/;
var email_bad = 'You have not given a correct e-mail address';
var email_bad_en = 'You have not given a correct e-mail address';
var email_bad_ru = 'Вы не дали правильный адрес электронной почты';
var email_bad_uz = 'Siz to`g`ri e-mail manzilini bermagansiz';

var number_filter = /^([-]?[0-9]+)([\.]?[0-9]+)*$/;
var number_bad = 'The input value can only contain numeric characters';
var number_bad_en = 'The input value can only contain numeric characters';
var number_bad_ru = 'Входное значение может содержать только цифровые символы';
var number_bad_uz = 'Kiritish qiymati faqat soni belgilarni o`z ichiga olishi mumkin';

var date_bad = 'Date format Error';
var date_bad_en = 'Date format Error';
var date_bad_ru = 'Ошибка в формате дате';
var date_bad_uz = 'Sananing formati noto`g`ri';


var choice_one = 'Please, Choice one';
var choice_one_en = 'Please, Choice one';
var choice_one_ru = 'Пожалуйста, выберите';
var choice_one_uz = 'Iltimos, tanlang';

var default_border = undefined;


if (language == 'en-US') email_bad = email_bad_en
else if (language == 'ru-RU') email_bad = email_bad_ru
else if (language == 'uz-UZ') email_bad = email_bad_uz

var data = [];

function setG_type(g_type){
	this.g_type = g_type;
}

function getG_type(){
	return this.g_type;
}


function require_fields(){
	$('.error').hide();
	var error = false;
	$('input:required,select:required').each(function(index,element){
		var $element = $(element);
		if ($element.val() == ''){
			var $label = $("label[for='" + $element.prop('id') + "']")
			if ($label.length) $element.after("<span class='error'>This Field '"+ $label.text() + "' is required. </span>");
			else $element.after("<span class='error'>This Field '"+ $label.attr('title') + "' is required. </span>");
			$element.focus();
			error = true;
		}
	});
	return ! error;
}

function checkRequiredFields(formId, language){
	if (language == 'en') {
		field_required = field_required_en;
		email_bad = email_bad_en;
		number_bad = number_bad_en;
		choice_one = choice_one_en;
		date_bad = date_bad_en;
	} else if (language == 'ru') {
		field_required = field_required_ru;
		email_bad = email_bad_ru;
		number_bad = number_bad_ru;
		choice_one = choice_one_ru;
		date_bad = date_bad_ru;
	} else if (language == 'uz') {
		field_required = field_required_uz;
		email_bad = email_bad_uz;
		number_bad = number_bad_uz;
		choice_one = choice_one_uz;
		date_bad = date_bad_uz;
	} else {
		field_required = field_required_en;
		email_bad = email_bad_en;
		number_bad = number_bad_en;
		choice_one = choice_one_en;
		date_bad = date_bad_en;
	}

	var $form = $('#' + formId);
	
	if ($form.length){
		$('.error').remove();
		
		var error = false;
		
		$('input:required:enabled, select:required:enabled, textarea:required:enabled', $form).each(function(index,element){
			var $element = $(element);
			
			if (default_border != undefined) $element.css("border", default_border);
			else default_border = $element.css("border");
			
			var tagName = $element.prop("tagName");
			
			if (tagName == 'INPUT' || tagName == 'TEXTAREA'){
				if ($element.val() == ''){
					
					var $parent = $element.parent();
					
					if ($parent.hasClass('input-group'))  
							$parent.after("<span class='error' data-bind='" + $element.attr('name') + "'>" + field_required + "</span>");
					else 	$element.after("<span class='error' data-bind='" + $element.attr('name') + "'>" + field_required + "</span>");
							
					$element.css("border", "1px solid red");
					$element.bind("blur", function(){ check(this); });
					$element.focus();
					error = true;
				} else {
					var fieldtype = $element.attr('type');
					if (tagName == 'INPUT' && fieldtype.toLowerCase() == 'email') {
						if (! email_filter.test($element.val())){
							$element.after("<span class='error' data-bind='" + $element.attr('name') + "'>" + email_bad + "</span>");
							$element.css("border", "1px solid red");
							$element.focus();
							error = true;
						}
					} else if (tagName == 'INPUT' && fieldtype.toLowerCase() == 'number') {
						if (! number_filter.test($element.val())){
							$element.after("<span class='error' data-bind='" + $element.attr('name') + "'>" + number_bad + "</span>");
							$element.css("border", "1px solid red");
							$element.focus();
							error = true;
						}
					} 
					if ($element.attr("data-date-format")) {
						var dt = moment($element.val(), $element.attr("data-date-format"));
						if (! dt.isValid()){
							var $parent = $element.parent();
							
							if ($parent.hasClass('input-group'))  
									$parent.after("<span class='error' data-bind='" + $element.attr('name') + "'>" + date_bad + "</span>");
							else 	$element.after("<span class='error' data-bind='" + $element.attr('name') + "'>" + date_bad + "</span>");
							$element.css("border", "1px solid red");
							$element.bind("change", function(){ check(this); });
							$element.focus();
							error = true;
						}
					};
				}
			} else if (tagName == 'SELECT') {
				if ($element.val() == '' || ($element.val() != '' && $element.val() < 0)){
					var $parent = $element.parent();
					
					if ($parent.hasClass('input-group'))  
							$parent.after("<span class='error' data-bind='" + $element.attr('name') + "'>" + field_required + "</span>");
					else 	$element.after("<span class='error' data-bind='" + $element.attr('name') + "'>" + field_required + "</span>");
					
					$element.css("border", "1px solid red");
					$element.focus();
					$element.bind("change", function(){ check(this); });
					error = true;
				}
			}
		});
		$service = $('input.service:checkbox');
		if ($service.length){
			$service_checked = $service.filter(':checked');
			if ($service_checked.length) $('span#service_warn').hide(); else {
				$('span#service_warn').show();
				error = true;
			}
		}
		return ! error;
	}
	return false;
}

function check(obj){
	$element = $(obj);
	var tagName = $element.prop("tagName");
	$span = $('span.error[data-bind="' + $element.attr('name') + '"]');

	if ($element.val() != ''){
		if (tagName == 'INPUT' || tagName == 'TEXTAREA'){
			var fieldtype = $element.attr('type');
			
			if (tagName == 'INPUT' && fieldtype.toLowerCase() == 'email') {
				if (! email_filter.test($element.val())){
					if (! $span.length) $element.after("<span class='error' data-bind='" + $element.attr('name') + "'>" + email_bad + "</span>");
					$element.css("border", "1px solid red");
					error = true;
				} else {
					$element.css("border", default_border);
					$("span[data-bind='"+ $element.attr('name') + "']").remove();
				}
			} else if (tagName == 'INPUT' && fieldtype.toLowerCase() == 'number') {
				if (! number_filter.test($element.val())){
					if (! $span.length)  $element.after("<span class='error' data-bind='" + $element.attr('name') + "'>" + number_bad + "</span>");
					$element.css("border", "1px solid red");
					error = true;
				} else {
					$element.css("border", default_border);
					$("span[data-bind='"+ $element.attr('name') + "']").remove(); 
				}
			} else {
				$element.css("border", default_border);
				$("span[data-bind='"+ $element.attr('name') + "']").remove();
			}
			if ($element.attr("data-date-format")) {
				var dt = moment($element.val(), $element.attr("data-date-format"));
				if (! dt.isValid()){
					if (! $span.length) {
						var $parent = $element.parent();
						
						if ($parent.hasClass('input-group'))  
								$parent.after("<span class='error' data-bind='" + $element.attr('name') + "'>" + date_bad + "</span>");
						else 	$element.after("<span class='error' data-bind='" + $element.attr('name') + "'>" + date_bad + "</span>");
					}
					$element.css("border", "1px solid red");
					error = true;
				} else {
					$element.css("border", default_border);
					$("span[data-bind='"+ $element.attr('name') + "']").remove(); 
				}
			};
		} else {
			$element.css("border", default_border);
			$("span[data-bind='"+ $element.attr('name') + "']").remove();
		}
	} else {
		if (! $span.length) {
			if (tagName == 'INPUT') {
				$parent = $element.parent();

				if ($parent.hasClass('input-group'))
					$parent.after("<span class='error' data-bind='" + $element.attr('name') + "'>" + field_required + "</span>");
				else
					$element.after("<span class='error' data-bind='" + $element.attr('name') + "'>" + field_required + "</span>");
				
			} else if (tagName == 'SELECT') {
				$element.after("<span class='error' data-bind='" + $element.attr('name') + "'>" + choice_one + "</span>");
			}
		}
		$element.css("border", "1px solid red");
	}
}

function findErrMsg(formId){
	var $form = $('#' + formId);
	var $span = $('span.error', $form).filter(':first');
	if ($span.length){
		$tab = $span.parents('div.tab-pane');
		if ($tab.length && ! $tab.hasClass('active')){
			$active_tab = $('div.tab-pane.active', $tab.parent());
			
			if ($active_tab.length) $active_tab.removeClass('active');
			
			$tab.addClass('active');
			
			$header = $('a[href="#' + $tab.attr('id')+ '"]', $form);
			if ($header.length){
				$header.parent().siblings('li.active').removeClass('active');
				$header.parent().addClass('active');
			}
		}
	}
}


// file input style changer
$(document).on('change', '.btn-file :file', function() {
	var $input = $(this);
	var numFiles = $input.get(0).files ? $input.get(0).files.length : 1;
	var label = $input.val().replace(/\\/g, '/').replace(/.*\//, '');
	$input.trigger('fileselect', [ numFiles, label ]);
});

$(document).ready(function() {
	$('.btn-file :file').on('fileselect',
		function(event, numFiles, label) {
			var input = $(this).parents('.input-group').find(':text');
			var log = numFiles > 1 ? numFiles + ' files selected' : label;
			if (input.length) {
				input.val(log);
			}
	});
	moveTop();
});


function moveTop() {
	// Hide the toTop button when the page loads.
	 $("#toTop").css("display", "none");
	 
	 // This function runs every time the user scrolls the page.
	 $(window).scroll(function(){
		// Check weather the user has scrolled down (if "scrollTop()"" is more than 0)
		if($(window).scrollTop()){
			// If it's more than or equal to 0, show the toTop button.
//			 console.log("is more");
			 $("#toTop").fadeIn("slow");
		} else {
			 // If it's less than 0 (at the top), hide the toTop button.
//			 console.log("is less");
			 $("#toTop").fadeOut("slow");
		}
	 });
	 
	// When the user clicks the toTop button, we want the page to scroll to the top.
	 $("#toTop").click(function(){
		// Disable the default behaviour when a user clicks an empty anchor link.
		 // (The page jumps to the top instead of // animating)
		 event.preventDefault();
		 
		// Animate the scrolling motion.
		 $("html, body").animate({ scrollTop:0 },"slow");
	 });
}

