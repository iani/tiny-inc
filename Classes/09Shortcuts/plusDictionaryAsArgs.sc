
+ Dictionary {
	asArgs {
		var args;
		args = Array (this.size * 2);
		this keysValuesDo: { | key, value | args add: key; args add: value };
		^args;
	}
}

/*

a = ();
(1..1000) do: { | i | a[format("%key", i).asSymbol] = i };

a.asArgs.asCompileString;

a.asArgs.size;

a.asArgs.select({ | i | i.isKindOf(Integer) }).maxItem;

a.asArgs.select({ | i | i.isKindOf(Integer) }).sort.reverse;
*/