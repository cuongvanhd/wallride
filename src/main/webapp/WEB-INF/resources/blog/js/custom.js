
/* Flex slider */

$(window).load(function() {
  $('#full-slider .flexslider').flexslider({
    easing: "easeInOutSine",
    directionNav: false,
    animationSpeed: 1500,
    slideshowSpeed: 5000
  });

  $('#banner-slider .flexslider').flexslider({
    animation: "slide",
    easing: "swing",
    selector: "ul.slides li",
    animationLoop: true,
    itemWidth: 130,
    itemMargin: 3,
    minItems: 1,
    maxItems: 8,
    controlNav: false,
    directionNav: true,
    move: 2
  });
});

/* Projects carousel */

$('.carousel').carousel();

/* Slider 1 - Parallax slider*/

// $(function() {
// 	$('#da-slider').cslider({
// 		autoplay	: true,
// 		interval : 9000
// 	});

// });
/* prettyPhoto Gallery */

jQuery(".prettyphoto").prettyPhoto({
   overlay_gallery: false, social_tools: false
});

/* Isotope */
$(function() {

	// cache container
	var $container = $('#portfolio');
	// initialize isotope
	$container.isotope({
	  // options...
	});

	// filter items when filter link is clicked
	$('#filters a').click(function(){
	  var selector = $(this).attr('data-filter');
	  $container.isotope({ filter: selector });
	  return false;
	});

/* Image block effects */

      $('ul.hover-block li').hover(function(){
        $(this).find('.hover-content').animate({top:'-3px'},{queue:false,duration:500});
      }, function(){
        $(this).find('.hover-content').animate({top:'125px'},{queue:false,duration:500});
      });

/* Slide up & Down */
	$(".dis-nav a").click(function(e){
		e.preventDefault();
		var myClass=$(this).attr("id");
		$(".dis-content ."+myClass).toggle('slow');
	});


	/* Image slideshow */

	// $('#s1').cycle({
	//     fx:    'fade',
	//     speed:  2000,
	//     timeout: 300,
	//     pause: 1
	//  });

	/* Support */

	$("#slist a").click(function(e){
	   e.preventDefault();
	   $(this).next('p').toggle(200);
	});

	$('#accordion').on('shown', function () {
	   $(".icon-chevron-down").removeClass("icon-chevron-down").addClass("icon-chevron-right");
	});

	$('#accordion').on('hidden', function () {
	   $(".icon-chevron-right").removeClass("icon-chevron-right").addClass("icon-chevron-down");
	});

	$("#nav-category").navgoco({
		caret: '<span class="caret"></span>',
		accordion: false,
		openClass: 'open',
		save: true,
		cookie: {
			name: 'navgoco',
			expires: false,
			path: '/'
		},
		slide: {
			duration: 400,
			easing: 'swing'
		}
	});
});


