package com.robjonesdev.pf2elootsie

import android.util.Log
import io.mockk.MockKException
object UnitTestUtils {
    val CLASS_TAG = "UnitTestUtils"

    fun verifyMockkExceptionDidNotOccur(testToWrap: suspend () -> Unit) {
        try {
            kotlinx.coroutines.runBlocking {
                testToWrap()
            }
        } catch (mockkException: MockKException) {
            Log.e(CLASS_TAG, "A test failed to mock a required object, error says: [ ${mockkException.localizedMessage}]")
            throw mockkException
        }
    }
}