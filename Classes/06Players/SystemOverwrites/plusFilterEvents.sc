//  3 May 2016 21:46

+ EventStreamPlayer {

	addFilterEvent { | event, name = \player |
		// error if not already playing!
		 stream.event.addFilterEvent (event, name)
		
	}
	
	addFilterFunc { | function, name = \player |
		 stream.event.addFilterFunc (function, name);
	}

	removeFilter { | name = \player |
		 stream.event.removeFilter (name);
	}

	addKeys { | keyValuePairs |
		var newStream, originalEvent, event;
		originalEvent = originalStream.event;
			if (stream.isNil) { event = () } { event = stream.event };
		keyValuePairs keysValuesDo: { | key value |
			newStream = value.asStream;
			originalEvent [key] = newStream;
			event [key] = newStream;
		}
	}

	addEvent { | inEvent |
		var newStream, originalEvent, event;
		originalEvent = originalStream.event;
		if (stream.isNil) { event = () } { event = stream.event };
		// event = stream.event;
		inEvent keysValuesDo: { | key value |
			newStream = value.asStream;
			originalEvent [key] = newStream;
			event [key] = newStream;
		}
	}
}

+ Event {
	playAndDelta { | cleanup, mute |
		if (mute) { this.put(\type, \rest) };
		cleanup.update(this);
		this.subevents do: _.(this);
		this.play;
		^this.delta;
	}

	addFilterEvent { | event, name = \player |
		var eventStream;
		eventStream = EventPattern (event).asStream;
		this.addFilterFunc (
			{ | inEvent |
				eventStream.next (inEvent).play;
			} , name
		)
	}
	
	addFilterFunc { | function, name = \player |
		var subevents;
		subevents = this [\subevents];
		subevents ?? {
			subevents = ();
			this.put (\subevents, subevents);
		};
		subevents.put (name, function);
	}

	removeFilter { | name = \player |
		var subevents;
		subevents = this [\subevents];
		subevents ?? {
			subevents.put (name, nil);
		}
	}
}