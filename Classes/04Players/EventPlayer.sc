/*
Play an EventStream attached to a TaskPlayer.

Hold EventPattern, create EventStream from it.
Create Events from EventStream and play them by attaching yourself to a TaskPlayer, 
through { | dur | stream.next.put(\dur, dur).play }
*/

EventPlayer {
	var <pattern, <>action;
	var stream;

	*new { | pattern, action |
		^this.newCopyArgs(pattern, action).init;
	}

	init {
		this.reset;
		action ?? {
			action = { | dur |
				stream.next.play(dur);
			}
		}
	}

	reset { this.pattern = pattern }

	pattern_ { | argPattern |
		pattern = argPattern;
		stream = pattern.asStream;
	}

	add { | taskPlayer |
		taskPlayer.actions[this] = action;
	}

	remove { | taskPlayer |
		taskPlayer.actions[this] = nil;
	}
}