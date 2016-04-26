/* 23 Mar 2016 07:01
================ DRAFT ================
*/

SynthLink {
	var <name; // Each instance stored as unique object under this name.
	// The name is also useful for printing, to identify different instances.
	var <server;
	var <player; // SynthPlayer, TaskPlayer, or similar/compatible object
	var <rank; // smaller numbers mean earlier synth order
	var <group;  // the actual group. Used as target for player.
	var <inputs; // Dictionary of Inputs (param: input, param2: input)
	var <outputs; // Dictionary of Outputs
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

	restart { | ... argArgs |
		player.restart (argArgs, group, addAction);
		//		player.restart (argArgs);
	}

	start { | ... argArgs |
		//		postf ("%, %, % isplaying: %\n", this, thisMethod.name, this, this.isPlaying);
		// if (this.isPlaying) { ^this }; 
		// argArgs !? { args = argArgs };
		player.start(argArgs, group, addAction)
	}

	stop { player.stop }

	addSource { | source, name = \player |
		// When new source (Function, Event is added,
		// then current player must decide:
		// add source and (optionally) restart
		// or exchange self with new object and (Opionally) restart
		// The optional restart is done by the player
		//	this.player.source = source;
		player = player.addSource (source, name)
	}

	player_ { | argPlayer |
		// The optional restart is done y the plyer
		// no need to tepeat it here
		//	var restart;
		//restart = this.isPlaying;
		//this.stop;
		player = argPlayer; //  argPlayer.makePlayerFor (this);
		//if (restart) { this.start }
	}

	isPlaying { ^player.isPlaying }
	
	release { | dur = 1 | player.release(dur) }
	
	addReader { | reader out = \out in = \in numChannels = 1 |
		// set reader's input bus to my output bus
		
		Output (this, out, numChannels).addReader (reader, in);
		
		// FOLLOWING IS THE CORRECT CODE - FOR LATER
		/*
		if (this canAddReader: reader) {
		
			Output (this, out, numChannels).addReader (reader, in);
		
			
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
		
		if ((allReaders = this.readers)) {
			^true
		}{
			allReaders do: { | r |
				if (r isIndirectWriterOf: synthLink) { ^true }
			}
		};
		^false;
	}

	readers {
		^outputs.values.asArray.collect({ | o | o.readers.asArray }).flat;
	}

	// TODO: following 1 up to 3 methods should move to IOBus and its subclasses
	connectWriterReader { | writer, reader, bus, out = \out, in = \in |
		writer.addOutput (reader, bus, out); // do not set synth's out yet!
		reader.addInput (writer, bus, in); // do not set synth's in yet!
		writer.getGroup; // this also moves readers groups after own
		// Perhaps rewrite this as a bundle message to the server
		// to ensure that both bus changes happen simultaneously:
		writer.finalizeInput (bus, in); // set in in synth after all is prepared!
		writer.finalizeOutput (bus, out); // set out in synth after all is prepared!
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
		// First move all readers after me, and then set my group. 
		// This ensures that writers stay before readers at all times.
		this.readers do: _.moveAfter(rank); // move my readers after me
		this.setGroup;                      // then set my group
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

	//================ ARGUMENTS ================
 
	getArgs {
		
	}

	// ================ SETTING PLAYERS ================
	/*
	addEventAsTaskPlayerFilter { | event filterName |

	}
	*/
   
	addEventAsTaskPlayerSource { | event |
		//	var isPlaying;
		//isPlaying = this.isPlaying;
		if (player isKindOf: PatternTaskPlayer) {
			//	postf ("% doing % will add to existing PatternTaskPlayer\n", this, thisMethod.name);
			player.pattern = event;
		}{
			// postf ("% doing % will create new PatternTaskPlayer\n", this, thisMethod.name);
			player = event.asPlayer;
		};

		//postf ("% doing % monitoring: isPlaying? %\n", this, thisMethod.name, isPlaying);
	}

}
