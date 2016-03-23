/* 23 Mar 2016 07:01
================ DRAFT ================
*/

LinkedNode {
	var <server;
	var <rank = 0; // smaller numbers mean earlier synth order
	var <group;  // the actual group. Used as target for player.
	var <inputs; // Dictionary of Inputs (param: input, param2: input)
	var <outputs; // Dictionary of Outputs
	var <player; // SynthPlayer, TaskPlayer, or similar/compatible object

	*all { | server |
		if (server.isNil) {
			^server.all.asArray.collect ({ | s | this.all (s) }).flat
		}{
			^Registry.allAt (this, server)
		}
	}

	*new { | server name |
		server ?? { server = Server.default };
		^Registry(this, server, name.asSymbol, { this.newCopyArgs(server) })
	}

	getGroup {
		if (inputs.isNil and: { outputs.isNil }) {
			rank = 0
		}{
			rank = this.allWriters.collect(_.rank).maxItem + 1;
			this.moveToGroup;
		};
		
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
	
	setGroup { this.group = PlayerGroup(server, rank) }

	group_ { | argGroup |
		group = argGroup;
		player !? { player.target = group };		
	}

	// called by PlayerGroup when recreating Groups on ServerTree:
	resetGroup { | groups | this.group = groups [rank] }
	
	getArgs {
		
	}
}

Input {
	var <parameter; // name of input parameter
	var <bus;
	var <readerNode; // the LinkedNode that has this input
	var <writers;   // set of Outputs that write to this input
}

Output {
	var <parameter; // name of input parameter
	var <bus;
	var <writerNode; // the LinkedNode that has this output
	var <readers;   // set of Inputs that read from this output
}

PlayerGroup {
	// A PlayerGroup contains and handles all Groups that are used 
	// by all LinkedNodes on one Server.
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
		LinkedNode.all (server) do: _.resetGroup (groups);
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
