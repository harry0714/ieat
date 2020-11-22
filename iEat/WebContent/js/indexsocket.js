var MyPoint = "/indexScoket";
	var host = window.location.host;
	var path = window.location.pathname;
	var webCtx = path.substring(0, path.indexOf('/', 1));
	var endPointURL = "ws://" + window.location.host + webCtx + MyPoint;

	var webSocket1;
	function init() {
		var chart = c3.generate({
			bindto : '#chart',
			data : {
				columns : [ [ 'data1', 30, 200, 100, 400, 150, 250 ],
						[ 'data2', 50, 20, 10, 40, 15, 25 ] ],
				axes : {
					data2 : 'y2'
				},
				types : {
					data2 : 'bar' // ADD
				}
			},
			axis : {
				y : {
					label : {
						text : 'Y Label',
						position : 'outer-middle'
					}
				},
				y2 : {
					show : true,
					label : {
						text : 'Y2 Label',
						position : 'outer-middle'
					}
				}
			}
		});

		webScoket1 = new WebSocket(endPointURL);
		webScoket1.onopen = function(event) {
			sendMessage();
		};
		webScoket1.onmessage = function(event) {
			
			var ord = document.getElementById("ord");
			var art = document.getElementById("art");
			var allstore = document.getElementById("allstore");
			var allreservation = document.getElementById("allreservation");
			
			
			
			var totalMember = document.getElementById("totalMember");
			var jsonObj = JSON.parse(event.data);
			
			
			totalMember.innerHTML = jsonObj.totalMember;
			ord.innerHTML = jsonObj.ord;
			art.innerHTML = jsonObj.art;
			allstore.innerHTML = jsonObj.allstore;
			allreservation.innerHTML = jsonObj.allreservation;
			
		
		};
		webScoket1.onclose = function(event) {

		};

	}
	function sendMessage() {
		webScoket1.send(JSON.stringify({
			"message" : "後端送出"
		}));
	}
	window.addEventListener("load", init, false);