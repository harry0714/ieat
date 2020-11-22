



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
	var booking = document.getElementById("store_book_amt"); 
	
}
window.addEventListener('load',doFirst,false);