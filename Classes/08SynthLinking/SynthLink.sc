/* 23 Mar 2016 07:01
================ DRAFT ================
*/

SynthLink {
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

	*new { | name server |
		server ?? { server = Server.default };
		^Registry(this, server, name.asSymbol, { this.newCopyArgs(server).init })
	}

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
	
	addOutputNode { | node out = \out in = \in numChannels = 1  |
		
	}

	getGroup {
		// TODO: REWRITE THIS CORRECTLY.  IT IS NOT RIGHT NOW
		/*
			When there are no inputs and no outputs, there should be
			no rank and no group.
			When there are only outputs, rank should be 0
		*/
		if (inputs.size == 0 and: { outputs.size == 0 }) {
			rank = nil;
		}{
			if (inputs.size == 0) {
				rank = 0
			}{
				rank = this.allWriters.collect(_.rank).maxItem + 1
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
		rank !? { this.group = groups [rank] }
	}
	
	getArgs {
		
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
