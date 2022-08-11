package com.liskovsoft.youtubeapi.common.helpers;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ScriptableObject;

public final class JavaScript {
    private JavaScript() {
    }

    public static String run(final String source,
                             final String functionName,
                             final String... parameters) {
        try {
            final Context context = Context.enter();
            context.setOptimizationLevel(-1);
            final ScriptableObject scope = context.initSafeStandardObjects();

            context.evaluateString(scope, source, functionName, 1, null);
            final Function jsFunction = (Function) scope.get(functionName, scope);
            final Object result = jsFunction.call(context, scope, scope, parameters);
            return result.toString();
        } finally {
            Context.exit();
        }
    }

    public static String evaluate(final String source) {
        try {
            final Context context = Context.enter();
            context.setOptimizationLevel(-1);
            final ScriptableObject scope = context.initSafeStandardObjects();

            final Object result = context.evaluateString(scope, source, "functionName", 1, null);
            return result.toString();
        } finally {
            Context.exit();
        }
    }
}
