/*
Play events produced by an EventStream, one at a time.
The timing is not produced by the EventPlayer.  
Instead, the event player gets and plays the next event from the EventStream, 
in response to calls from a TaskPlayer. The TaskPlayer times the events. 

EventPlayer filters the events given by the TaskPlayer at each call.
It combines them with its own events, and can use its own event
to modify values received from the TaskPlayer, before playing.

*/

AbstractEventPlayer {
	var <name = \player;
	var <>action;

	*new { | name = \player, action |
		^this.newCopyArgs(name, action).init;
	}

	init { this.makeAction }

	/*
	add { | taskPlayer |
		taskPlayer.actions[this] = action;
	}
	*/

	addTo { | taskPlayer | taskPlayer.add(this) }
	removeFrom { | taskPlayer | taskPlayer.remove(this) }

	play { | args | action.(args, this) }
}

/*
Play an EventStream attached to a TaskPlayer.

Hold EventPattern, create EventStream from it.
Create Events from EventStream and play them by attaching yourself to a TaskPlayer.

*/

EventPlayer : AbstractEventPlayer {
	var <pattern;
	var stream;

	*new { | name = \player, pattern, action |
		^super.new(name, action).init(pattern);
	}

	init { | inPattern |		
		super.init;
		this.pattern = inPattern;
	}

	pattern_ { | argPattern |
		pattern = EventPattern(argPattern ?? (instrument: \default));
		stream = pattern.asStream;
	}

	reset { this.pattern = pattern }
	
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
		// Do not play if own stream has ended.
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

	addEvent { | inEvent | pattern.addEventContents(inEvent, stream.event) }
}

////////////////////////////////////////////////////////////////

SimpleEventPlayer : AbstractEventPlayer {
	makeAction {
		action = { | sourceEvent | sourceEvent.play }
	}
}
