// PROBLEM: none
fun test(s: String?) {
    val x: Any = if (s == null)
        ""
    else
        Unit<caret>
}
