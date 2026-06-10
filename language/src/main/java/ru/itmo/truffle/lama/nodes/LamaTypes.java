package ru.itmo.truffle.lama.nodes;

import com.oracle.truffle.api.dsl.TypeSystem;

import ru.itmo.truffle.lama.runtime.LamaArray;
import ru.itmo.truffle.lama.runtime.LamaFunction;
import ru.itmo.truffle.lama.runtime.LamaSExp;
import ru.itmo.truffle.lama.runtime.LamaString;

@TypeSystem({long.class, LamaString.class, LamaSExp.class, LamaArray.class, LamaFunction.class})
public class LamaTypes {

}
