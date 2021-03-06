package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.api.Function;
import org.dynjs.api.Scope;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynAtom;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.RT;

import java.util.List;

import static me.qmx.jitescript.CodeBlock.newCodeBlock;
import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class FunctionStatement implements Statement {

    private final DynThreadContext context;
    private final String identifier;
    private final List<String> args;
    private final Statement block;

    public FunctionStatement(final DynThreadContext context, final List<String> args, final Statement block) {
        this(context, null, args, block);
    }

    public FunctionStatement(final DynThreadContext context, final String identifier, final List<String> args, final Statement block) {
        this.context = context;
        this.identifier = identifier;
        this.args = args;
        this.block = block != null ? block : new EmptyStatement();
    }

    @Override
    public CodeBlock getCodeBlock() {
        final Integer slot = context.store(block.getCodeBlock());
        // put arguments on stack
        CodeBlock codeBlock = newCodeBlock();

        codeBlock = codeBlock
                .bipush(args.size())
                .anewarray(p(String.class))
                .astore(4);

        for (int i = 0; i < args.size(); i++) {
            codeBlock = codeBlock
                    .aload(4)
                    .bipush(i)
                    .ldc(args.get(i))
                    .aastore();
        }

        codeBlock = codeBlock
                .aload(1)
                .bipush(slot)
                .invokedynamic("dynjs:compile:lookup", sig(CodeBlock.class, DynThreadContext.class, int.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS)
                .astore(5)
                .aload(1)
                .invokevirtual(p(DynThreadContext.class), "getRuntime", sig(DynJS.class))
                .aload(5)
                .aload(4)
                .invokedynamic("dynjs:compile:function", sig(Function.class, DynJS.class, CodeBlock.class, String[].class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);

        if (identifier != null) {
            codeBlock = codeBlock
                    .astore(3)
                    .aload(2)
                    .ldc(identifier)
                    .aload(3)
                    .invokedynamic("dynjs:scope:define", sig(void.class, Scope.class, String.class, DynAtom.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);
        }

        return codeBlock;
    }

}
