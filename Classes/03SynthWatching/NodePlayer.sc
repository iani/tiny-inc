NodePlayer {
	var <node;

	addNode { | argNode |
		NodeWatcher.register(argNode);
		argNode addDependant: { | changer, message |
			switch (message,
				\n_go, { this.changed(\started) },
				\n_end, { this.changed(\stopped) }
			);
		};
		node !? { node.releaseDependants };
		node = argNode;
	}

	addListener { | listener, onStart, onEnd |
		listener.addNotifier(this, \started, onStart);
		listener.addNotifier(this, \stopped, onEnd);
	}
}