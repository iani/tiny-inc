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
	
	setGroup {
		group = PlayerGroup(server, rank);
		player !? { player.target = group };
	}

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
	var <server, <groups;
	*new { | server, rank = 0 |
		^Registry(this, server, { this.newCopyArgs(server, []).init })
		.getGroup(rank);
	}

	init {
		server // initGraph ...
	}

	getGroup { | rank |
		var root;
		root = server.rootNode;
		rank - groups.size + 1 max: 0 do: {
			groups = groups add: Group.tail(root);
		};
		^groups[rank];
	}
}
