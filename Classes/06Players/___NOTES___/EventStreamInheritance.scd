/* 22 Mar 2016 16:21
This is code copied from tiny-sc 2014, to look into how events can modify values 
inherited from other events. 

The methods to look into are: 
- applyMods
- inherit
- propagate
- originalPatternEvent
- addPlayerMods
- replacePlayerMods
- replaceEvent

This should enable things like in the follwing "Bourree" piece. 

*/

// Selected lines to look into:
(degree:{if(~degree%3==0){~degree+(0..2)*[2,3].choose}{~degree}})%>\wander;

(
(amp: [0, 0, 0.1, 0.2, 0.05].pseq, degree: { ~degree }) %> \wander;
(amp: [0, 0.1].pseq, degree: { 2 + ~degree }) %> \other;
)

/// Full code of piece: 

//: ================================================================
// Possible coda: "Bourree"


//: Start a named pattern reference in a SynthTree
Edef(\simple, (degree: [1, 2, 3].pseq, amp: 0.3)) => \wander;

//: Change the named pattern Edef.  (Edef(\simple) =< ... also possible)
\simple =< (degree: Pbrown(-5, 5, 3, inf));
//: Modify some more
\simple =< (degree: Pbrown(-5, 5, 3, inf), dur: [(1, 0.95 .. 0.125).pseq1, 0.11 pn: 100].pseq);
//:
\simple =< (dur: 0.125, amp: 0.3);

//:
\simple =< (dur: [0.125, 0.125, 0.125, 0.25, 0.25, 1, 0.5, 0.5].prand, amp: 0.3);

//: And more
\simple =< (instrument: [\bassfoundation, \sine].prand);

//: Many keys in event OK
\simple =< (legato: { 0.4 exprand: 1.5 }.pfunc, amp: [Pn(0.2, 3), 0.1, 0.05, 0.05].pseq);

//: Modify how the synthtree interprets the stream
(degree:{if(~degree%3==0){~degree+(0..2)*[2,3].choose}{~degree}})%>\wander;

//: Add another SynthTree playing from the same stream as \wander
\wander cloneInto: \other;

//: Modify how the second SynthTree plays the stream
(degree: { 20 + ~degree }, ratio: 0.5) %> \other;

//: Like it? Lets have more:
(degree: { (10, 19 .. 32) + ~degree }) %> \other;

//: Hm, up there a little louder:
(amp: { Pbrown(0.5, 2, 0.1, inf).asStream * (~amp ? 0.01) }) %> \other;

//: Or maybe try playing inverse amplitude:
(amp: { 1 - (~amp ? 0.01) / 10 }) %> \other;

//: See if propagation works
Edef(\simple) =< (degree: Pwhite(-10.1, -12, inf));
//: OK
Edef(\simple) =< (degree: (-10..10).pyramid(5).pseq);
//: OK
Edef(\simple) =< (degree: (10..-10).pyramid(5).pseq);

//:
Edef(\simple) =< (degree: [1, 2].pseq);
//: Pauses? Suddenly a whiff of baroque.
(
(amp: [0, 0, 0.1, 0.2, 0.05].pseq, degree: { ~degree }) %> \wander;
(amp: [0, 0.1].pseq, degree: { 2 + ~degree }) %> \other;
)
//: Try some modulation
(
\simple =< (mtranspose: Pstutter(12, (-4..2).pseq),
	ctranspose: Pstutter(24, ((0..5) * 5 % 12).pseq),
	scale: Scale.harmonicMinor
);
)
//: Fade out
\wander.fadeOut(5);
\other.fadeOut(10);

/*
This version does not work correctly when cloning multiple Idefs from same Edef. 
Fix is underway in Idef2.  Wed, Apr 30 2014, 19:04 EEST
Idef applies inheritance/modification operations on the originalStream event.
Bdef plays inside SynthDef by broadcasting its event and letting 
Prototype: 
//:
a = EventPattern((degree: [1, 2, 3].pseq(inf))).play;
//:
a.originalStream.event[\degree] = Pbrown(-5, 5, 3, inf).asStream;
//:
a.originalStream.event[\dur] = 0.1;
//: Testing play into synthtree:
a = Idef.fromEvent((degree: [10, 12].pseq, dur: 0.2));
a.play;
a.inspect;
*/
Idef : EventStreamPlayer { // NamedInheritingEventStreamPlayer
	var <>name;
	var <parent;
	var <children;
	var <mods; // locally modified elements: apply these on inherited pattern
	
	*new { | name, parent, protoEvent |
		^NameSpace(\Idef, name, {
			super.new(parent.asStream, protoEvent).initIdef(name, parent);
		});
	}

	initIdef { | argName argParent |
		name = argName;
		this.parent = argParent;
		children = Set();
	}

	parent_ { | argParent |
		parent = argParent;
		parent !? { parent.addChild(this); };
	}

	addChild { | child | children = children add: child }

	*fromEvent { | event, protoEvent |
		^this.new(nil, Edef(nil, event), protoEvent)
	}

	addEvent { | event, fromPattern = false |
		var newEvent;
		newEvent = if (fromPattern) { 
			this.originalPatternEvent;
		}{
			originalStream.event.copy;
		};
		newEvent use: {
			event keysValuesDo: { | key value | newEvent[key] = value.value.asStream; };
		};
		this.applyMods(newEvent);
	}

	replaceEvent { | event |
		event = event.copy;
		this.applyMods(
			event keysValuesDo: { | key value | event[key] = value.asStream; }
		);
	}

	replacePlayerMods { | event |
		// replace mods with event, apply to stream from original pattern
		mods = event;
		this.applyMods(this.pattern.asStream.event);
	}

	addPlayerMods { | event, reset = false |
		var newEvent;
		mods ?? { mods = () };
		event keysValuesDo: { | key value | mods[key] = value };
		this.applyMods(if (reset) { this.originalPatternEvent }  { nil });
	}

	originalPatternEvent { ^this.pattern.asStream.event.copy }
	pattern { ^parent.pattern }

	propagate { | inEvent | children do: _.inherit(inEvent) }
	inherit { | inEvent | this.applyMods(inEvent); }

	applyMods { | inEvent |
		inEvent = (inEvent ?? { originalStream.event; }).copy;
		mods !? { 
			inEvent use: {
				mods keysValuesDo: { | key value | inEvent[key] = value.value } 
			};
		};
		inEvent keysValuesDo: { | key value | inEvent[key] = value.asStream };
		inEvent[\dur] ?? { inEvent[\dur] = 1 };
		originalStream.event = inEvent;
		this.propagate(inEvent);
	}

	asIdef { | edef |
		// ignore edef: no switching parent edefs!
		^this
	}
}

Bdef : Idef {

	init {
		if (originalStream.event.dur.isNil) { originalStream.event.dur = 1 };
		super.init;
	}

	prNext { arg inTime;
		var nextTime;
		var outEvent = stream.next(event.copy);
		if (outEvent.isNil) {
			streamHasEnded = stream.notNil;
			cleanup.clear;
			this.removedFromScheduler;
			this.changed(\taskStopped);
			^nil
		}{
			// Instead of playAndDelta, use broadcastAndDelta.
			nextTime = outEvent.broadcastAndDelta(cleanup, muteCount > 0, this);
			if (nextTime.isNil) { this.removedFromScheduler; ^nil };
			nextBeat = inTime + nextTime;	// inval is current logical beat
			^nextTime
		};
	}

}

+ Event {
	broadcastAndDelta { | cleanup, mute, streamPlayer |
		if (mute) { this.put(\type, \rest) };
		cleanup.update(this);
		// this.play;  // instead of this, use "changed".
        // instead of playing, broadcast, with "changed":
        streamPlayer.changed(\event, this);
		^this.delta;
	}
}
