/*
EventPattern can be embedded in another Pattern, 
in similar way as Pdef can be embedded in a Pseq (see Pdef help entry).

IZ Mon, Apr 21 2014, 09:58 EEST

EventPattern((dur: 0.1, degree: [-5, 12, 1].pbrown + [0, [-2, 3]].prand)).play;

shortcut: 



*/

EventPattern : Pattern {
	var <>event; // contains patterns

	*new { | event | ^this.newCopyArgs(event ?? { () }) }

	asStream { ^EventStream(event) }

	pattern { ^this }
}

EventStream : Stream {
	var <>event; // contains streams
	*new { | event |
		^super.new.initEventStream(event);
	}

	initEventStream { | inEvent |
		event = ();
		inEvent keysValuesDo: { | key, value | event[key] = value.asStream(this); };
	}

	next {
		var outEvent, outValue;
		outEvent = ();
		event keysValuesDo: { | key, value |
			outValue = value.next(this);
			if (outValue.isNil) { ^nil };
			outEvent[key] = outValue;
		};
		^outEvent;
	}

	/* Needed to embed an EventPattern in a Stream as in: 
		Pseq([EventPattern((degree: (1..8).pseq(2)))]).play;
	*/
	embedInStream { arg inval;
		var outval;
		// this.changed(\started); // Put this in when the need arises.
		while {
			outval = this.next;
			outval.notNil;
		}{
			outval.yield;
		};
		// this.changed(\stopped); // Put this in when the need arises.
		nil;
	}	
}


