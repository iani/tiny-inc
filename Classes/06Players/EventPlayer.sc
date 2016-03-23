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
	var <>action;
	var <name = \player;

	*new { | action, name = \player |
		^this.newCopyArgs(action, name).init;
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

	*new { | pattern, name = \player, action |
		^this.newCopyArgs(action, name, pattern).init;
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
}

////////////////////////////////////////////////////////////////

SimpleEventPlayer : AbstractEventPlayer {
	makeAction {
		action = { | sourceEvent | sourceEvent.play }
	}
}
