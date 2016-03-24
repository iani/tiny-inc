
// Notes on consturction of filterSourceEvent
//:
~source = EventPattern((freq: 1000, somethingElse: \xxxxx, ser: [1, 1].pseries)).asStream;
~filter = EventPattern((
	freq: { ~freq * 123 },
	dur: 0.2,
	freq2: { ~freq / ~ser },  // Filter on value inherited from source pattern
		xpattern: [-100, -50, 1].pbrown, // Stream value in own event stream
	freqFromOwnPattern: { | f | ~freq * f[\xpattern] } // Filter value from own event stream
)).asStream;
//:
// ~filter.next;
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
