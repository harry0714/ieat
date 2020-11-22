$(document).ready(function(){
	$(".bookmark").click(function(){
		var action=$(this).children(".action");
		var store_no=$(this).children(".store_no");
		var icon = $(this).children(".glyphicon");
		var path = $(this).children(".path");
		$.ajax({
			type:"post",
			url: path.val()+"/bookmarkstore/bookmarkstore.do",
			data : {action : action.val(),store_no:store_no.val()},
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