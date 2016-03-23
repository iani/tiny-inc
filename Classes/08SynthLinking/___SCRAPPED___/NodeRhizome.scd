/*
Manage a grass-forest of NodeSources, where any NodeSource can have
multiple NodeSources as inputs and outputs. 

- Sort the NodeSources in Groups so that they are in the correct Synth order.
- Allocate and set in/out busses. 
- Create groups as needed at Server boot time.
- Create and start signal-copying synths at boot time.

*/
NodeRhizome {
	var <server;

	var <layers, <groups;

	*initClass {
		StartUp add: {
			this.new(Server.default);
		}
	}

	*new { | server |
		^this.newCopyArgs(server.asTarget.server).init;
	}

	init {
		layers = List();
		// MUST ALSO DO THIS ON COMMAND PERIOD!!!!!!!!!!!!!!!!
		this.addNotifier(server, \booted, {
			this.makeLayers;
			this.makeGroups;
			this.makeBusses;
		});
		ServerTree.add({ this.makeGroups }, server);
	}

	makeLayers {
		var layer, newLayer;
		layer = SynthPlayer.all.select({ | n | n.inputs.size == 0 }).asSet;
		while { layer.size > 0 } {
			this removeFromPreviousLayers: layer;
			layers = layers add: layer;
			newLayer = Set();
			layer do: { | nodeSource |
				nodeSource.outputs do: newLayer.add(_);
			};
			layer = newLayer;
		};
	}

	removeFromPreviousLayers { | layer |
		layer do: { | nodeSource | layers do: _.remove(nodeSource); }
	}

	makeGroups {
		var group;
		groups = layers.collect({ Group(server) }).reverse;
		layers do: { | layer, index |
			group = groups[index];
			layer do: _.setTarget(group);
		}
	}

	makeBusses { layers do: { | l | l do: _.makeBusses; } }

}