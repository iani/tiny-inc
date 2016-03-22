
AbstractEventPlayer {
	var <>action;

	*new { | action |
		^this.newCopyArgs(action).init;
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
EventFilterPlayer : AbstractEventPlayer {
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

EventPlayer : AbstractEventPlayer {
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

	/* TODO: Put this filter mechanism in the default action: 
//:
~source = EventPattern((freq: 1000, somethingElse: \xxxxx, ser: [1, 1].pseries)).asStream;
~filter = EventPattern((
	freq: { ~freq * 123 },
	dur: 0.2,
	freq2: { ~freq / ~ser },
	xpattern: [-100, -50, 1].pbrown,
	freqFromOwnPattern: { | f | ~freq * f[\xpattern] } // cannot use like this
)).asStream;
//:
~filter.next;
//:
{
	var sourceEvent, filterEvent, outputEvent;
	sourceEvent = ~source.next;
	filterEvent = ~filter.next;
	outputEvent = sourceEvent.copy;
	sourceEvent use: {
		filterEvent keysValuesDo: { | key value |
			outputEvent[key] = value.(filterEvent);
		}
	};
	outputEvent;
}.value;
	*/
	makeAction {
		action ?? {
			action = { | dur args |
				this.filterSourceEvent (dur, args).play;
			}
		}
	}

	filterSourceEvent { | dur, sourceEvent |
		var filterEvent, outputEvent;
		outputEvent = (sourceEvent ?? { () }).copy.put (\dur, dur);
		filterEvent = stream.next;
		sourceEvent use: {
			filterEvent keysValuesDo: { | key value |
				outputEvent[key] = value.(filterEvent);
			}
		};
		^outputEvent;
	}

}

