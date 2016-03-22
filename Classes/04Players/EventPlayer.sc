
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
		pattern = EventPattern(argPattern ?? (instrument: \default));
		stream = pattern.asStream;
	}
	
	makeAction {
		action ?? {
			action = { | sourceEvent |
				this.filterSourceEvent (sourceEvent).play;
			}
		}
	}

	filterSourceEvent { | sourceEvent |
		var filterEvent, outputEvent;
		filterEvent = stream.next;
		// Do not play if stream has ended.
		// Note: The default plays forever: (instrument: \default)
		if (filterEvent.isNil) { ^nil };
		outputEvent = sourceEvent.copy;
		sourceEvent use: {
			filterEvent keysValuesDo: { | key value |
				outputEvent[key] = value.(filterEvent);
			}
		};
		^outputEvent;
	}

}

////////////////////////////////////////////////////////////////

SimpleEventPlayer : AbstractEventPlayer {
	makeAction {
		action = { | sourceEvent | sourceEvent.play }
	}
}

/* Notes on consturction of filterSourceEvent
//:
~source = EventPattern((freq: 1000, somethingElse: \xxxxx, ser: [1, 1].pseries)).asStream;
~filter = EventPattern((
	freq: { ~freq * 123 },
	dur: 0.2,
	freq2: { ~freq / ~ser },
		xpattern: [-100, -50, 1].pbrown, // stream value in own event stream
	freqFromOwnPattern: { | f | ~freq * f[\xpattern] } // use value from own event stream
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