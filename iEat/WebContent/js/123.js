


function readFile(){ 
    var file = this.files[0]; 
    if(!/image\/\w+/.test(file.type)){ 
        alert("Error!!!"); 
        return false; 
    } 
   
    var reader = new FileReader(); 
    reader.readAsDataURL(file); 
    reader.onload = function(e){ 

        result.innerHTML ='<img src="'+this.result+'" alt="" width=200 height=200/>'; 
    } 
}
function readFile1(){ 
    var file = this.files[0]; 
    if(!/image\/\w+/.test(file.type)){ 
        alert("Error!!!"); 
        return false; 
    } 
   
    var reader = new FileReader(); 
    reader.readAsDataURL(file); 
    reader.onload = function(e){ 

        result1.innerHTML ='<img src="'+this.result+'" alt="" width=200 height=200/>'; 
    } 
} 

function doFirst(){
	var result = document.getElementById("result"); 
	var input = document.getElementById("fileinput"); 
	var input1 = document.getElementById("fileinput1"); 
	
	if(typeof FileReader==='undefined'){ 
	    result.innerHTML = "no suppor FileReader"; 
	    input.setAttribute('disabled','disabled'); 
	}else{ 
	    input.addEventListener('change',readFile,false); 

	} 
}
window.addEventListener('load',doFirst,false);