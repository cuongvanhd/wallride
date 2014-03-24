
$(document).ready(function(){
		
		
		// Club Select
		$("body.league div#visual li a").click(function(){
				$("body.league div#visual li a").removeClass();
				$(this).addClass("active");
				$("body.league div#visual div.detail div.map span").removeClass().addClass($(this).attr("rel"));
				return false;
		});
		
		// Input Text
		$("div.search input").focus(function(){
				if(this.value == "選手名やチーム名から検索"){
						$(this).val("").css("color","#333");
				}
		});
		$("div.search input").blur(function(){
				if(this.value == ""){
						$(this).val("選手名やチーム名から検索").css("color","#333");
				}
		});
		
		// Page Scroll
		$("a#pagetop").click(function(){
				$('html,body').animate({ scrollTop: $($(this).attr("href")).offset().top }, 'slow','swing');
				return false;
		});
});



