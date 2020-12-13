import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis

val job = Job()
val dispatcher = Dispatchers.IO
val exceptionHandler = CoroutineExceptionHandler { _, t ->
    println(t.localizedMessage)
}
val coroutineContext = job + dispatcher + exceptionHandler

fun main() {
//    launchAndJoin()
//    launchAndCancel()
//    runSuspendFun()
//    launchAsync()
//    launchCoroutineInExplicitContext()
//    cancelOnlySuspend()
//    checkCancelManually()
//    cancelByTimeout()
    clearOnFinally()
}


/**
 * Cancellation
 * */

fun cancelOnlySuspend() = runBlocking {
    val cancelable = launch {
        repeat(10) {
            suspendWork()
        }
    }

    val notCancelable = launch {
        repeat(10) {
            notSuspendWork()
        }
    }

    delay(25)
    cancelable.cancel()
    notCancelable.cancel()
}

suspend fun suspendWork() {
    println("suspendWork")
    delay(10)
}

fun notSuspendWork(source : String = "notSuspendWork") {
    println(source)
    Thread.sleep(10)
}

fun checkCancelManually() = runBlocking {
    val checkIsActive = launch {
        repeat(10) {
//            ensureActive()
            if (isActive) {
                notSuspendWork("checkIsActive")
            }
        }
    }

    val callYield = launch {
        repeat(10) {
            notSuspendWork("callYield")
            yield()
        }
    }
    delay(25)

    checkIsActive.cancel()
    callYield.cancel()
}

fun cancelByTimeout() = runBlocking {
    withTimeout(25){
        repeat(10){
            suspendWork()
        }
    }

    // returns null instead of throwing an exception
    withTimeoutOrNull(25) {
        repeat(10) {
            suspendWork()
        }
    }
}

fun clearOnFinally() = runBlocking {
    val cancelable = launch {
        try {
            repeat(10) {
                suspendWork()
            }
        }catch (e: Exception){
            println("exception : ${e.localizedMessage}")
        } finally {
            // use only non suspending functions
            withContext(NonCancellable) {
                // here you can use suspending functions
            }
        }
    }
    delay(25)
    cancelable.cancel() // throw a CancellationException
}

/**
 * Scope
 */

class someClass {
    lateinit var scope: CoroutineScope

    fun create() {
        scope = CoroutineScope(Dispatchers.Main)

        scope.launch {
            //now it's possinle to run a coroutine
        }
    }

    fun destroy() {
        scope.cancel()
    }
}


class ScopeActivity : CoroutineScope by MainScope() {

    fun onDestroy() {
        cancel()
    }
}

/**
 * Context
 */
fun launchCoroutineInExplicitContext() = runBlocking {
    launch(coroutineContext) {
        //some work
        withContext(Dispatchers.Main) {
            //some UI related work
        }
    }
}

fun launchCoroutinesOnDifferentThreads() = runBlocking {
    launch { }
    launch(Dispatchers.IO) { }
    launch(Dispatchers.Main) { }
    launch(Dispatchers.Unconfined) {}
    launch(newSingleThreadContext("OwnThread")) { }
}

/**
 * launch / async
 */
fun runSuspendFun() = runBlocking {

    val times = measureTimeMillis {
        val resultA = suspendFunA()
        val resultB = suspendFunB()

        val finalResult = "result : $resultA $resultB"
        println("runSuspendFun finalResult $finalResult")
    }
    println("runSuspendFun timeMillis : $times")
}

fun launchAsync() = runBlocking {
    val timeMillis = measureTimeMillis {
        val defferedA = async {
            return@async suspendFunA()
        }

        val defferedB = async {
            return@async suspendFunB()
        }

        val finalResult = "launchAsync ${defferedA.await()} ${defferedB.await()}"
        println(finalResult)
    }
    println("launchAsync timeMillis : $timeMillis")
}

fun launchAndJoin() = runBlocking {
    val job = launch {
        val result = suspendFun()
    }

    job.join()
}


fun launchAndCancel() = runBlocking {
    println("starting launchAndCancel")
    val parentJob = launch {
        val jobA = launch {
            delay(100)
            println("launchAndCancel - jobA")
        }
        val jobB = launch {
            delay(300)
            println("launchAndCancel - jobB")

        }
    }

    delay(200)

    parentJob.cancel()
}

suspend fun suspendFun() {
    delay(1000)
    println("suspendFun delay 1000 ms")
}

suspend fun suspendFunA(): String {
    delay(200)
    return "result A"
}

suspend fun suspendFunB(): String {
    delay(200)
    return "result B"
}

private fun test1() {
    runBlocking {
        val job1 = launch(Dispatchers.IO) {
            (1..10).forEach {
                println("job1 : ${Thread.currentThread().name} : $it")
            }
        }
        val job2 = launch(Dispatchers.IO) {
            (1..10).forEach {
                println("job2 : ${Thread.currentThread().name} : $it")
            }
        }
        job1.join()
    }
}

fun runALotOfCoroutines() = GlobalScope.launch {
    (1..1_000).forEach {
        launch(Dispatchers.IO) {
            println("${Thread.currentThread().name} : $it")
        }
    }
}
