var file; 
var fileReader; 

function doFirst() {	
	document.getElementById('upfile1').onchange = fileChange; 
}
function fileChange() {
	file = document.getElementById('upfile1').files[0];
	fileReader = new FileReader(); 
	
	fileReader.onload = function() {
		document.getElementById('pic1').src = fileReader.result;
	}
	
	fileReader.readAsDataURL(file);		
}


window.addEventListener('load', doFirst, false); 