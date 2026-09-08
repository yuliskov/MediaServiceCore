# The stable artifact intentionally compiles, but does not package, the legacy
# J2V8 backend. It is unreachable while JavaScriptEngine is the selected
# implementation, so consumers may safely ignore its optional class references.
-dontwarn com.eclipsesource.v8.**
