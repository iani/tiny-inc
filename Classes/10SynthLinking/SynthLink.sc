/* 23 Mar 2016 07:01
================ DRAFT ================
*/

SynthLink {
	var <name; // Each instance stored as unique object under this name.
	// The name is also useful for printing, to identify different instances.
	var <server;
	var <rank; // smaller numbers mean earlier synth order
	var <group;  // the actual group. Used as target for player.
	var <inputs; // Dictionary of Inputs (param: input, param2: input)
	var <outputs; // Dictionary of Outputs
	var <player; // SynthPlayer, TaskPlayer, or similar/compatible object
	var <>addAction = \addToHead;
	var <>args;

	*all { | server |
		if (server.isNil) {
			^server.all.asArray.collect ({ | s | this.all (s) }).flat
		}{
			^Registry.allAt (this, server).asArray
		}
	}

	*new { | name = \default, server |
		name = name.asSymbol;
		server ?? { server = Server.default };
		^Registry(this, server, name.asSymbol, { this.newCopyArgs(name, server).init })
	}

	asString { ^format ("% : %", this.class.name, name) }

	init {
		inputs = IdentityDictionary();
		outputs = IdentityDictionary()
	}

	start { | argArgs |
		argArgs !? { args = argArgs };
		player.start(args, group, addAction)
	}

	player_ { | argPlayer |
		this.stop;
		player = argPlayer;
	}
	stop { player.stop }
	release { | dur = 1 | player.release(dur) }
	
	addReader { | reader out = \out in = \in numChannels = 1 |
		// set reader's input bus to my output bus
		
		this.connectWriterReader(
			this, reader, this.getOutputBus(out, numChannels), out, in
		);
		// FOLLOWING IS THE CORRECT CODE - FOR LATER
		/*
		if (this canAddReader: reader) {
		this.connectWriterReader(
			this, reader, this.getOutputBus(out, numChannels), out, in
		);			
			
		}{
			postf("% is a writer of % : cannot add it as reader\n",
				this, reader
			);
		}
		*/
	}
	
	canAddReader { | reader |
		^(reader isIndirectWriterOf: this).not
	}

	isIndirectWriterOf { | synthLink |
		var allReaders;
		// TEST!
		
		if ((allReaders = this.allReaders)) {
			^true
		}{
			allReaders do: { | r |
				if (r isIndirectWriterOf: synthLink) { ^true }
			}
		};
		^false;
	}

	allReaders {
		^outputs.values.asArray.collect({ | o | o.readers.asArray }).flat;
	}

	connectWriterReader { | writer, reader, bus, out = \out, in = \in |
		writer.addOutput (reader, bus, out); // do not set synth's out yet!
		reader.addInput (writer, bus, in); // do not set synth's in yet!
		writer.getGroup; // this also moves readers groups after own
		// Perhaps rewrite this as a bundle message to the server
		// to ensure that both bus changes happen simultaneously:
		writer.finalizeInput (bus, in); // set in in synth after all is prepared!
		writer.finalizeOutput (bus, out); // set out in synth after all is prepared!
	}

	addOutput { | reader, bus, out = \out |
		// Only create the data structure.
		// Do not change group or set synth,
		// because this must be done in a separate order:
		// first the group ordering and then the bus changes in the synths

		// TODO: still unfinished here>
	}

	addInput { | writer, bus, in = \in |
		
	}

	addWriter { | writer out = \out in = \in numChannels = 1  |
		// set writer's output bus to my input bus
		if (writer canAddReader: this) {
			writer.setOutputBus (this.getInputBus(in, numChannels), out);			
		}{
			postf("% is a writer of % : cannot add it as reader\n",
				writer, this
			)
		}
	}

	// ================ INPUTS AND OUTPUTS ================

	getInputBus { | name, numChannels = 1, rate = \audio |
		
	}

	getOutputBus { | name, numChannels = 1, rate = \audio |

	}
	// ================ GROUPS ================

	getGroup {
		if (inputs.size == 0 and: { outputs.size == 0 }) {
			rank = nil;
		}{
			if (inputs.size == 0) {
				rank = 0
			}{
				rank = this.writers.collect(_.rank).maxItem + 1
			};
		};
		this.moveToGroup;
	}

	moveToGroup {
		this.readers do: _.moveAfter(rank);
		this.setGroup;
	}

	moveAfter { | argRank |
		if (rank <= argRank) {
			rank = argRank + 1;
			this.moveToGroup;
		}
	}
	
	setGroup {
		if (rank.isNil) {
			this.group = server.asTarget;
		}{
			this.group = PlayerGroup(server, rank)
		}
	}

	group_ { | argGroup |
		group = argGroup;
		player !? { player.target = group };		
	}

	// called by PlayerGroup when recreating Groups on ServerTree:
	resetGroup { | groups |
		if (rank.isNil) {
			this.group = server.asTarget;
		}{
			this.group = groups [rank]
		}
	}
	
	getArgs {
		
	}

	// ================ SETTING PLAYERS ================

	addEventAsTaskPlayerFilter { | event filterName |

	}

	addEventAsTaskPlayerSource { | event |

	}

	addSynthPlayer { | synthPlayer |

	}
}

Input {
	var <parameter; // name of input parameter
	var <bus;
	var <readerNode; // the SynthLink that has this input
	var <writers;   // set of Outputs that write to this input
}

Output {
	var <parameter; // name of input parameter
	var <bus;
	var <writerNode; // the SynthLink that has this output
	var <readers;   // set of Inputs that read from this output
}

PlayerGroup {
	// A PlayerGroup contains and handles all Groups that are used 
	// by all SynthLinks on one Server.
	var <server, <groups;
	*new { | server, rank = 0 |
		// Only one PlayerGroup per server
		server ?? { server = Server.default };
		^Registry(this, server, { this.newCopyArgs(server, []).init })
		.getGroup(rank);
	}

	init {
		ServerTree.add({ this.remakeGroups }, server);
	}

	remakeGroups {
		groups = { this.makeGroup } ! groups.size;
		SynthLink.all (server) do: _.resetGroup (groups);
		postf ("% made % Groups on %\n", this, groups.size, server);
	}

	makeGroup { ^Group(server, \addToTail) }

	getGroup { | rank |
		rank - groups.size + 1 max: 0 do: {
			groups = groups add: this.makeGroup
		};
		^groups[rank];
	}
}
