
+ Dictionary {
	asArgs {
		var args;
		args = Array (this.size);
		this keysValuesDo: { | key, value | args add: key; args add: value };
		^args;
	}
}