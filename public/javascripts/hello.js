if (window.console) {
  console.log("Welcome to your Play application's JavaScript!");
}


       

});

/*area conference role*/
$(function(){
    $("#arearole, #modal-background-arearole, #modal-close-arearole").click(function () {
    $("#modal-content-arearole,#modal-background-arearole,#modal-header-arearole").toggleClass("active");
    $("#modal-load-page-arearole").load("@routes.Application.areaConferenceRole(id,idenv)");
  });
});

/*area booth description*/
$(function(){
    $("#areabooth, #modal-background-areabooth, #modal-close-areabooth").click(function () {
    $("#modal-content-areabooth,#modal-background-areabooth,#modal-header-areabooth").toggleClass("active");
    $("#modal-load-page-areabooth").load("@routes.Application.areaBothDescription(id,idenv)");
  });
});

/* area description*/
$(function(){
    $("#description, #modal-background-description, #modal-close-description").click(function () {
    $("#modal-content-description,#modal-background-description,#modal-header-description").toggleClass("active");
    $("#modal-load-page-description").load("@routes.Application.areaDescription(id,idenv)");
  });
});

/*view description*/
$(function(){
  $("#modal-launcher, #modal-background, #modal-close").click(function () {
    $("#modal-content,#modal-background,#modal-header").toggleClass("active");
    $("#modal-load-page").load("@routes.Application.viewDescription(id,idenv)");
  });
});


/* edit descritpion*/
$(function(){
  $("#edit, #modal-background-edit, #modal-close-edit").click(function () {
    $("#modal-content,#modal-background,#modal-header").removeClass("active");
    $("#modal-content-edit,#modal-background-edit,#modal-header-edit").toggleClass("active");
    $("#modal-load-page-edit").load("@routes.Application.areaDescription(id,idenv)");
  });
});

/*area tab left*/
$(document).ready(function() {
  $('#rootwizard').bootstrapWizard({'tabClass': 'nav nav-tabs'});
});

/*edit area*/
$(function(){
  $("#editarea, #modal-background-editarea, #modal-close-editarea").click(function () {
    $("#modal-content-editarea,#modal-background-editarea,#modal-header-editarea").toggleClass("active");
    $("#modal-load-page-editarea").load("@routes.Application.editArea(id,idenv)");
  });
});

/*show tags area*/
$(function(){
  $("#tag").click(function(){
    $('.area-tags').fadeToggle();
  });
});



//area role

$(document).ready(function(){

	$('#participant,#speaker,#session_chair').bind('click',radbtn);

	function radbtn(){
		var valueChecked = $("input[name=rol]:checked").val();
		if(valueChecked){
			// alert("rrrr");
			if(valueChecked == 2){
				// alert("2");

				$(".p .fa").removeClass("fa-circle-o").addClass("fa-check-circle-o");
				$(".s .fa").removeClass("fa-check-circle-o").addClass("fa-circle-o");
				$(".sc .fa").removeClass("fa-check-circle-o").addClass("fa-circle-o");		
			}

			else if(valueChecked == 1){

				$(".p .fa").removeClass("fa-check-circle-o").addClass("fa-circle-o");
				$(".s .fa").removeClass("fa-circle-o").addClass("fa-check-circle-o");
				$(".sc .fa").removeClass("fa-check-circle-o").addClass("fa-circle-o");			
			}

			else if(valueChecked == 0){
	
				$(".p .fa").removeClass("fa-check-circle-o").addClass("fa-circle-o");
				$(".s .fa").removeClass("fa-check-circle-o").addClass("fa-circle-o");
				$(".sc .fa").removeClass("fa-circle-o").addClass("fa-check-circle-o");
			
			}

		}
	}
});

$(document).ready(function(){
$('#participant,#speaker,#session_chair').bind('click',radval);
	function radval(){

		var valueChecked = $("input[name=rol]:checked").val();
		if(valueChecked){
			// alert("rrrr");
			if(valueChecked == 2){
				// alert("2");
				$('input[name=role]').val('Participant');
				// $('input[name=role]').attr('name', 'participant');

				$('.radio-buttons').fadeToggle();
			}

			else if(valueChecked == 1){

				$('input[name=role]').val('Speaker');
				// $('input[name=role]').attr('name', 'speaker');

				$('.radio-buttons').fadeToggle();
		}

			else if(valueChecked == 0){
				$('input[name=role]').val('Session chair');
				// $('input[name=role]').attr('name', 'session_chair');

				$('.radio-buttons').fadeToggle();
		}

		}
	}
});
