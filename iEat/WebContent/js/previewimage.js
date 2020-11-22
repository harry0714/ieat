function init(){
	img = document.getElementsByName("img");
	upFile = document.getElementsByName("mem_photo");
	for(var i=0;i<upFile.length;i++){
		upFile[i].addEventListener("change",doclick,false);
	}
}
function doclick(){
	var file = event.srcElement;
	for(var i=0;i<upFile.length;i++){
		if(file == upFile[i]){
			index = i;
			break;
		}
	}
	if(file.value == ""){
		img[index].src="images/user_default_full.jpg";
		return;
	}
	var fReader = new FileReader();
	fReader.readAsDataURL(file.files[0]);
	fReader.onloadend = function(event){
		img[index].src = event.target.result;
	}
}
window.onload=init;