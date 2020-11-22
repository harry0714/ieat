$(function() {
    check_table();
	$("#discount_startdate").datepicker({
		minDate: 0,
		dateFormat : "yy-mm-dd",
		onClose: function(selectedDate) {
			$("#discount_enddate").datepicker("option", "minDate", selectedDate);
		}
	});

	$("#discount_enddate").datepicker({
		defaultDate: "+1w",
		dateFormat : "yy-mm-dd",
		onClose: function(selectedDate) {
			$("#discount_startdate").datepicker("option", "maxDate", selectedDate);
		}
	});
	
	function check_table(){
		if($("#discount_meals tbody").children('tr').length==0){
			$("#discount_meals").hide();
		}
		else{
			$("#discount_meals").show();			
		}
	}
	
	function delete_register(){
	    $(".glyphicon-remove").on('click',function(){
 	    	var val = $(this).closest('tr').find('input[name=meal_no]').val();
 	    	$(this).closest('tr').remove();
 	    	$("#meals_menu input[type=checkbox][value="+val+"]").next().show();
 	    	check_table();
 	    })				
	}
	
    $(".meal_option").on('click',function(e){
    	var clicked = $(this).prev().prop('checked');
    	if(clicked){
	    	$(this).css('background','#ccc').prev().prop('checked',false);
    	}
	    else{
	    	$(this).css('background','#5cb85c').prev().prop('checked',true);
	   	}   	
    })    
    
	$("#btn_meal_confirm").on('click',function(){
		$('#meals_menu input:checked').each(function() {
			var meal_no = $(this).val();
	 	       $("#discount_meals tbody").append('<tr><td><img src="ImageMeal?meal_no='+meal_no+'" width="100px"></td><td>'
	 	    		   +$(this).next().find('h4').text()+'<input type="hidden" value="'+meal_no+'" name="meal_no"></td><td>$'
	 	    		   +$(this).next().find('span').text()+'</td><td><div class="input-group" style="width:150px"><div class="input-group-addon">$</div><input class="form-control input-sm" type="number" name="'+meal_no+'" value="0"></div></td><td>'	 
	 	    		   +'<a><i class="glyphicon glyphicon-remove"></i></a></td></tr>'
	 	    	);
 	      	$(this).prop('checked',false).next().css('background','#ccc').hide();
 		});
		
		delete_register();    
	    check_table();
	    
	})
    
	$("#btn_meal_close").on('click',function(){
		$('#meals_menu input:checkbox').prop('checked',false).next().css('background','#ccc');
	})
})  