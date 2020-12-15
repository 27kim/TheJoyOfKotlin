import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun main() {
//    dependentJob()
//    jobHierarchy()
    var isDoorOpen = false

    println("Unlocking the door..please wait \n")

    GlobalScope.launch {
        delay(3000)
        isDoorOpen = true
    }

    GlobalScope.launch {
        repeat(4){
            println("trying to open the door..\n")
            delay(800)
            if(isDoorOpen){
                println("Opened the door! \n")
            }else{
                println("The door is still locked\n")
            }
        }
    }
    Thread.sleep(5000)
}

private fun dependentJob() {
    val job1 = GlobalScope.launch(start = CoroutineStart.LAZY) {
        delay(200)
        println("pong")
        delay(200)
    }
    GlobalScope.launch {
        println("ping")
        job1.join()
        println("ping")
        delay(200)
    }
    Thread.sleep(1000)
}

private fun jobHierarchy() {
    with(GlobalScope) {
        val parentJob = launch {
            delay(200)
            println("I'm the parent")
            delay(200)
        }

        launch(context = parentJob) {
            delay(200)
            println("I'm a child")
            delay(200)
        }

        if (parentJob.children.iterator().hasNext()) {
            println("The Job has children ${parentJob.children}")
        } else {
            println("the Job has no children")
        }

        Thread.sleep(2000)

    }
}