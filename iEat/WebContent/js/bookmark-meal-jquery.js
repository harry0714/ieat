$(document).ready(function(){
	$(".bookmark").click(function(){
		var action=$(this).children(".action");
		var meal_no=$(this).children(".meal_no");
		var icon = $(this).children(".glyphicon");
		var path = $(this).children(".path");
		$.ajax({
			type:"post",
			url: path.val()+"/bookmarkmeal/bookmarkmeal.do",
			data : {action : action.val(),meal_no:meal_no.val()},
			dataType:"text",
			success : function(data){
				if(data == "add"){
					action.val("deleteAjax");
					icon.removeClass("glyphicon-heart-empty");
					icon.addClass("glyphicon-heart");
					icon.prop('title', '移除最愛');
					return;
				}
				if(data == "delete"){
					action.val("addAjax");
					icon.removeClass("glyphicon-heart");
					icon.addClass("glyphicon-heart-empty");
					icon.prop('title', '加入最愛');
					return;
				}
				if(data == "error"){
					alert(data);
				}
			},
			error:function(){
				alert("error");
			}
		});
	});
});