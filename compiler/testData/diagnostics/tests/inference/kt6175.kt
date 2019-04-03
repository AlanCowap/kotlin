// !WITH_NEW_INFERENCE
// !DIAGNOSTICS: -UNUSED_PARAMETER -UNUSED_ANONYMOUS_PARAMETER

fun <T, R : Any> foo(body: (R?) -> T): T = fail()

fun test1() {
    <!OI;TYPE_INFERENCE_NO_INFORMATION_FOR_PARAMETER!>foo<!> {
        <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH, NI;CONSTANT_EXPECTED_TYPE_MISMATCH, NI;CONSTANT_EXPECTED_TYPE_MISMATCH!>true<!>
    }
    <!NI;UNREACHABLE_CODE!><!OI;TYPE_INFERENCE_NO_INFORMATION_FOR_PARAMETER!>foo<!> { <!OI;CANNOT_INFER_PARAMETER_TYPE!>x<!> ->
        <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH, NI;CONSTANT_EXPECTED_TYPE_MISMATCH, NI;CONSTANT_EXPECTED_TYPE_MISMATCH!>true<!>
    }<!>
}


fun <T, R> bar(body: (R) -> T): T = fail()

fun test2() {
    <!NI;IMPLICIT_NOTHING_AS_TYPE_PARAMETER, OI;TYPE_INFERENCE_NO_INFORMATION_FOR_PARAMETER!>bar<!> {
        <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH, NI;CONSTANT_EXPECTED_TYPE_MISMATCH, NI;CONSTANT_EXPECTED_TYPE_MISMATCH!>true<!>
    }
    <!NI;UNREACHABLE_CODE!><!NI;IMPLICIT_NOTHING_AS_TYPE_PARAMETER, OI;TYPE_INFERENCE_NO_INFORMATION_FOR_PARAMETER!>bar<!> { <!OI;CANNOT_INFER_PARAMETER_TYPE!>x<!> ->
        <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH, NI;CONSTANT_EXPECTED_TYPE_MISMATCH, NI;CONSTANT_EXPECTED_TYPE_MISMATCH!>true<!>
    }<!>
}

fun <T, R> baz(body: (List<R>) -> T): T = fail()

fun test3() {
    <!NI;IMPLICIT_NOTHING_AS_TYPE_PARAMETER, OI;TYPE_INFERENCE_NO_INFORMATION_FOR_PARAMETER!>baz<!> {
        <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH, NI;CONSTANT_EXPECTED_TYPE_MISMATCH, NI;CONSTANT_EXPECTED_TYPE_MISMATCH!>true<!>
    }
    <!NI;UNREACHABLE_CODE!><!NI;IMPLICIT_NOTHING_AS_TYPE_PARAMETER, OI;TYPE_INFERENCE_NO_INFORMATION_FOR_PARAMETER!>baz<!> { <!OI;CANNOT_INFER_PARAMETER_TYPE!>x<!> ->
        <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH, NI;CONSTANT_EXPECTED_TYPE_MISMATCH, NI;CONSTANT_EXPECTED_TYPE_MISMATCH!>true<!>
    }<!>
}

fun <T, R : Any> brr(body: (List<R?>) -> T): T = fail()

fun test4() {
    <!OI;TYPE_INFERENCE_NO_INFORMATION_FOR_PARAMETER!>brr<!> {
        <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH, NI;CONSTANT_EXPECTED_TYPE_MISMATCH, NI;CONSTANT_EXPECTED_TYPE_MISMATCH!>true<!>
    }
    <!NI;UNREACHABLE_CODE!><!OI;TYPE_INFERENCE_NO_INFORMATION_FOR_PARAMETER!>brr<!> { <!OI;CANNOT_INFER_PARAMETER_TYPE!>x<!> ->
        <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH, NI;CONSTANT_EXPECTED_TYPE_MISMATCH, NI;CONSTANT_EXPECTED_TYPE_MISMATCH!>true<!>
    }<!>
}

fun fail(): Nothing = throw Exception()