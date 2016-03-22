
AbstractEventPlayer {
	var <>action;

	*new { | action |
		^this.newCopyArgs(action, pattern).init;
	}

	init { this.makeAction }

	add { | taskPlayer |
		taskPlayer.actions[this] = action;
	}

	remove { | taskPlayer |
		taskPlayer.actions[this] = nil;
	}
}

/* This can become part of EventPlayer,
when I figure out how to merge the stream's event with the args event.

args.put(\dur, dur);
stream.next keysValuesDo: { | key, value |
	args.put(key, value.valueEnvir())
}


*/
EventFilterPlayer : AbsractEventPlayer {
	makeAction {
		action = { | dur args |
			args.put(\dur, dur).play
		}
	}
}

/*
Play an EventStream attached to a TaskPlayer.

Hold EventPattern, create EventStream from it.
Create Events from EventStream and play them by attaching yourself to a TaskPlayer, 
through { | dur | stream.next.put(\dur, dur).play }
*/

EventPlayer : AbsractEventPlayer {
	var <pattern;
	var stream;

	*new { | pattern, action |
		^this.newCopyArgs(action, pattern).init;
	}

	init {
		this.reset;
		super.init;
	}

	reset { this.pattern = pattern }
	
	pattern_ { | argPattern |
		pattern = EventPattern(argPattern);
		stream = pattern.asStream;
	}

	makeAction {
		action ?? {
			action = { | dur | stream.next.put(\dur, dur).play }
		}
	}

}

