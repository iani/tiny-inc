\test.addNotifier(Server.default, \booted, { | n | n.message.postln });

//:

Server.default addDependant: { | ... args |
	args.postln;
	args[0].serverRunning.postln;
};

{ Server.default.reboot }.defer(3);

//:
Server.default.releaseDependants;

//:
\test.addNotifier(Server.default, \serverRunning, { | n |
	var isRunning;
	isRunning = n.notifier.serverRunning;
	{ isRunning.postln; } ! 5;
	if (isRunning) {
		ServerBootCheck.default.bus.get({ | val |
			postf("yeah the bus value is: %\n", val);
		});
	}{
	};
});