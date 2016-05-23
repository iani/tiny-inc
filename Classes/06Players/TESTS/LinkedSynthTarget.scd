/* 22 May 2016 10:14
Test use of custom groups as targets in LinkedSynth, in interconectingI/O of synths via audio busses.
*/

// 1. Set up the example
~sourceGroup = Group();
//:
~effectGroup = Group.after (~sourceGroup);
//:
Server.default.queryAllNodes;
//:
~bus = Bus.audio;
//:
~source = { | out = 0 |
	Out.ar (out, WhiteNoise.ar (0.1));
}.play (~sourceGroup, args: [\out, ~bus.index]);
//:
~effect = { | in = 0 |
	Resonz.ar (In.ar (in), LFDNoise0.kr (10).range (100, 2000), 0.01);
}.play (~effectGroup, args: [\in, ~bus.index]);
//:
Server.default.queryAllNodes;

//================================================================
// 2. Reproduce the example using LinkedSynth

