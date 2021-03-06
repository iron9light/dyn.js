package org.dynjs.runtime;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.api.Function;
import org.dynjs.api.Scope;
import org.dynjs.compiler.DynJSCompiler;

public class CodeBlocksTest {

    private DynJSCompiler compiler = new DynJSCompiler();
    private Scope scope = new DynObject();
    private DynThreadContext context = new DynThreadContext();

    public void testDynPropGet() {
        Function function = compiler.compile(new DynFunction(new String[]{"target", "name"}) {
            @Override
            public CodeBlock getCodeBlock() {
                return CodeBlocks.GETPROP;
            }
        });
        DynAtom propertyName = new DynString("constructor");
        DynObject object = new DynObject();
        DynAtom result = function.call(context, scope, new DynAtom[]{object, propertyName});
    }

}
