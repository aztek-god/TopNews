package dv.serg.topnews

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Test
import java.util.concurrent.TimeUnit


class RxTest {

    @Test
    fun testTypeOf() {

        var counter = 0

    }

    @Test
    fun testFlatMap() {
        val dataList = listOf(
                "str", "true", "developer", "false"
        )


        val obs = Observable
                .just("str", "true", "developer", "false")

        val switchObs = obs.switchMap {
            Observable.just(it).delay(5000, TimeUnit.NANOSECONDS)
        }

        switchObs.subscribe {
            println(it)
        }

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
    fun groupByTest() {
        val list = listOf("s", "ss", "sss", "ss")

        list.groupBy { it.length }.forEach { println(it) }
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

    @Test
    fun replayTest() {
        val autoConnect: Observable<Int> = Observable.fromIterable(listOf(1, 2, 3)).replay(2).autoConnect(2)

        autoConnect.subscribe {
            println(it)
        }

        autoConnect.subscribe {
            println(it)
        }
    }

    @Test
    fun testTo() {
        Observable.just(1, 2, 3).blockingForEach { println(it) }
        Observable.just(1, 2, 3).to { t: Observable<Int> -> t.subscribe() }.dispose()
    }

    @Test
    fun anyTest() {
        val any: Single<Boolean> = Observable.just(1, 2, 3).any { it < 5 }
        any.subscribe { t1, t2 -> println(t1) }
    }

    @Test
    fun ambTest() {
        val observables = listOf(Observable.just(1, 2, 3).delay(10, TimeUnit.NANOSECONDS), Observable.just(4, 5, 6))

        Observable.amb(observables).subscribe { println(it) }
    }

    @Test
    fun allTest() {
        val all: Single<Boolean> = Observable.just(1, 2, 3, 4, 5, 6).all { it < 10 }

        Observable.just(1, 2, 3, 4, 5, 6).all { it < 10 }.subscribe { t1: Boolean?, t2: Throwable? -> println(t1) }
    }
}