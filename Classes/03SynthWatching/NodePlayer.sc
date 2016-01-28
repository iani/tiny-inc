NodePlayer {
	var <node;

	addNode { | argNode |
		NodeWatcher.register(argNode);
		if (node.isNil) {
			argNode addDependant: { | changer, message |
				switch (message,
					\n_go, { this.changed(\started) },
					\n_end, { this.stopped }
				);
			}
		}{
			node.releaseDependants;
			node.free;
			argNode addDependant: { | changer, message |
				switch (message,
					\n_go, { this.changed(\nodeChanged) },
					\n_end, { this.stopped }
				);
			}
		};
		node = argNode;
	}

	stopped {
		node = nil;
		this.changed(\stopped);
	}
	
	addListener { | listener, onStart, onEnd |
		listener.addNotifier(this, \started, onStart);
		listener.addNotifier(this, \stopped, onEnd);
	}

	isPlaying {
		^node.notNil;
	}
}