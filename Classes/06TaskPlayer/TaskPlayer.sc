TaskPlayer {
	var <durs, <clock, <task, <dur;
	var <players;
	var <task, <atEnd = false;

	*new { | durs, clock |
		^this.newCopyArgs(durs, clock).init;
	}

	init {
		players = Set();
		task = Task({
			
		}, clock);
		//		task = Task()
	}
	
	start {
		//	if (task)
	}	
}
