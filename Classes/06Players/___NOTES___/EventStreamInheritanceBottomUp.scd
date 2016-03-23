/*
	Building EventStream inheritance / filtesrs, from the bottom up.
*/

a = EventPattern((freq: 1000, somethintElse: \xxxxx, ser: [1, 1].pser)).asStream;
b = EventPattern((freq: { ~freq * 123 }, dur: 0.2, freq2: { ~freq + ~ser.next })).asStream;

c = a.next;
d = b.next;
e = c.copy;
//:
c use: {
	d keysValuesDo: { | key value |
		// key.postln;
		// value.postln;
		// value.value.postln;
		e[key] = value.value;
	}
};
e;
//:
a = EventPattern((freq: 1000, somethintElse: \xxxxx, ser: [1, 1].pseries)).asStream;
b = EventPattern((freq: { ~freq * 123 }, dur: 0.2, freq2: { ~freq / ~ser.next })).asStream;
//:
{
	c = a.next;
	d = b.next;
	e = c.copy;
	c use: {
		d keysValuesDo: { | key value |
			e[key] = value.value;
		}
	};
}.value;
e;
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
//:
Pser

//: Ehhh??????
{
	c = a.next;
	d = b.next;
	e = c.copy;
	c use: {
		d keysValuesDo: { | key value |
			e[key] = value.value.value
		}
	};
}.value;
d;
//:
c;
//:

d.play;

().play;

d.inspect;
c.inspect;

//:
c.play;

//:
x = Pfunc({ 100.rand + [0, 4].prand.asStream }).asStream;

x.next.value.value;