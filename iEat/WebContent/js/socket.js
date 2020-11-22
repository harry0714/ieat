var MyPoint1 = "/ReportScoket";
	var host1 = window.location.host;
	var path1 = window.location.pathname;
	var webCtx1 = path1.substring(0, path1.indexOf('/', 1));
	var endPointURL1 = "ws://" + window.location.host + webCtx1 + MyPoint1;

	var webSocket;
	function init1() {
		

		webScoket = new WebSocket(endPointURL1);
		webScoket.onopen = function(event) {
			sendMessage1();
		};
		webScoket.onmessage = function(event) {
			var orUntreated = document.getElementById("orUntreated");
//			var ord = document.getElementById("ord");
//			var art = document.getElementById("art");
//			var allstore = document.getElementById("allstore");
//			var allreservation = document.getElementById("allreservation");
			
			
			var arUntreated = document.getElementById("arUntreated");
			var arrUntreated = document.getElementById("arrUntreated");
			var crUntreated = document.getElementById("crUntreated");
			var srUntreated = document.getElementById("srUntreated");
			var rrUntreated = document.getElementById("rrUntreated");
			var storeUntreated = document.getElementById("storeUntreated");
			var totalUntreated = document.getElementById("totalUntreated");
			//var totalMember = document.getElementById("totalMember");
			var jsonObj = JSON.parse(event.data);
			
			
//			totalMember.innerHTML = jsonObj.totalMember;
//			ord.innerHTML = jsonObj.ord;
//			art.innerHTML = jsonObj.art;
//			allstore.innerHTML = jsonObj.allstore;
//			allreservation.innerHTML = jsonObj.allreservation;
			
			orUntreated.innerHTML = jsonObj.orUntreated;
			arUntreated.innerHTML = jsonObj.arUntreated;
			arrUntreated.innerHTML = jsonObj.arrUntreated;
			crUntreated.innerHTML = jsonObj.crUntreated;
			srUntreated.innerHTML = jsonObj.srUntreated;
			rrUntreated.innerHTML = jsonObj.rrUntreated;
			storeUntreated.innerHTML = jsonObj.storeUntreated;
			totalUntreated.innerHTML = jsonObj.totalUntreated;
			storetotalUntreated.innerHTML = jsonObj.storetotalUntreated;
		};
		webScoket.onclose = function(event) {

		};

	}
	function sendMessage1() {
		webScoket.send(JSON.stringify({
			"message" : "後端送出"
		}));
	}
	window.addEventListener("load", init1, false);