+ Node {
	onStart { | listener, action |
		NodeWatcher.register(this);
		listener.addNotifierOneShot(this, \n_go, action);
	}

	onEnd { | listener, action |
		NodeWatcher.register(this);
		//		this.isPlaying = true; // dangerous
		listener.addNotifierOneShot(this, \n_end, action);
	}
}
