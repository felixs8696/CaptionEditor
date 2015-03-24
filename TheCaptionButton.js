function createFile() {
	var textFile = null,
  	makeTextFile = function (text) {
	    var data = new Blob([text], {type: 'srt/plain'});

	    // If we are replacing a previously generated file we need to
	    // manually revoke the object URL to avoid memory leaks.
	    if (textFile !== null) {
	      window.URL.revokeObjectURL(textFile);
		}

	    textFile = window.URL.createObjectURL(data);

	    return textFile;
  	};

	var create = document.getElementById('create'),
	textbox = document.getElementById('textbox');
	
	create.addEventListener('click', function () {
	    var link = document.getElementById('downloadlink');
	    link.href = makeTextFile(textbox.value);
	    link.style.display = 'block';
		}, false);
}

var	clsStopwatch = function() {
		// Private vars
		var	startAt	= 0;	// Time of last start / resume. (0 if not running)
		var	lapTime	= 0;	// Time on the clock when last stopped in milliseconds

		var	now	= function() {
				return (new Date()).getTime(); 
			}; 
 
		// Public methods
		// Start or resume
		this.start = function() {
				startAt	= startAt ? startAt : now();
			};

		// Stop or pause
		this.stop = function() {
				// If running, update elapsed time otherwise keep it
				lapTime	= startAt ? lapTime + now() - startAt : lapTime;
				startAt	= 0; // Paused
			};

		// Reset
		this.reset = function() {
				lapTime = startAt = 0;
			};

		// Duration
		this.time = function() {
				return lapTime + (startAt ? now() - startAt : 0); 
			};
	};

var x = new clsStopwatch();
var $time;
var clocktimer;
var blank = true;
var recentFrame;
var inPressed = false;
var outPressed = true;
var index = 1;

function pad(num, size) {
	var s = "0000" + num;
	return s.substr(s.length - size);
}

function formatTime(time) {
	var h = m = s = ms = 0;
	var newTime = '';

	h = Math.floor( time / (60 * 60 * 1000) );
	time = time % (60 * 60 * 1000);
	m = Math.floor( time / (60 * 1000) );
	time = time % (60 * 1000);
	s = Math.floor( time / 1000 );
	ms = time % 1000;

	newTime = pad(h, 2) + ':' + pad(m, 2) + ':' + pad(s, 2) + ',' + pad(ms, 3);
	return newTime;
}

function show() {
	$time = document.getElementById('time');
	update();
}

function update() {
	$time.innerHTML = formatTime(x.time());
}

function start() {
	clocktimer = setInterval("update()", 1);
	x.start();
}

function stop() {
	x.stop();
	clearInterval(clocktimer);
}

function reset() {
	stop();
	x.reset();
	update();
}

function writeIn() {
	if (outPressed) {
		if (blank) {
			document.getElementById('textbox').innerHTML = index + "\n" + formatTime(x.time()) + " --> ";
			blank = false;
		}
		else {
			document.getElementById('textbox').innerHTML += index + "\n" + formatTime(x.time()) + " --> ";
		}
		recentFrame = x.time();
		inPressed = true;
		outPressed = false;
		index += 1;
	}
}

function writeOut() {
	if (inPressed) {
		document.getElementById('textbox').innerHTML += formatTime(x.time()) + "\n" + "\n";
		recentFrame = x.time();
		inPressed = false;
		outPressed = true;
	}
}

function returnFrame() {
	$time.value = recentFrame;
	$time.innerHTML = formatTime(recentFrame());
}

