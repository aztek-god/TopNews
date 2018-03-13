package dv.serg.topnews

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Test
import java.util.concurrent.TimeUnit

class RxTest {

    @Test
    fun testTypeOf() {
        val dataList = listOf(
                "str", 1, true, "developer", false
        )

        var counter = 0

        Observable
                .fromIterable(dataList)
                .ofType(Boolean::class.java)

                .subscribe { println("output is $it") }
    }

    @Test
    fun testFlatMap() {
        val dataList = listOf(
                "str", "true", "developer", "false"
        )

//        var counter = 0

        val obs = Observable
                .just("str", "true", "developer", "false")

        val switchObs = obs.switchMap {
            Observable.just(it).delay(5000, TimeUnit.NANOSECONDS)
        }

        switchObs.subscribe {
            println(it)
        }
//                .doOnNext {
//                    counter++
//                }
//                .switchMap {
//                    Observable.just("$it flatted")
//                }
//                .subscribe { println("output is $it, counter = $counter") }

    }

    @Test
    fun testSample() {
        Observable.range(1, 2442500)
                .sample(1, TimeUnit.NANOSECONDS)
                .subscribe { println(it) }
    }

    @Test
    fun testTimeInterval() {
        Observable.range(1, 9).timeInterval().subscribe { println(it) }
    }

    @Test
    fun publisherTest() {
        val subject: Subject<String> = PublishSubject.create()

        subject.subscribe { println(it) }
        subject.subscribe { println(it.hashCode()) }

        Observable.just("1", "2").share()
                .doOnNext {
                    subject.onNext(it)
                }.subscribe()
    }
}