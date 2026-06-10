package ru.itmo.truffle.lama.nodes;

import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.nodes.Node;

@TypeSystemReference(LamaTypes.class)
public abstract class LamaBaseNode extends Node {
	
}
