# TopNews app

The handy android news parser application. 

## Features

1) Application is fully written on Kotlin programming language.

2) The arch is used in the app is similar to mvp but view has no any reference to presenter. Data message is going only via livedata from new architecture components so any view(activity or fragment) needs to be registered itself as a listener therefore it is passive.

3) All Android architecture components: livedata, viewmodel, and room database are used.

4) In application extensively used extension functions as an alternative to ordinal inheritance. For instance [glide image loading](https://github.com/aztek-god/TopNews/blob/master/app/src/main/java/dv/serg/topnews/exts/funs.kt#LC233) for ImageView providing asynchronous image loading.

5) Every data request to network or to sqlite db happen in separate thread(In this case as other ones is done by rxjava). 

## Frameworks and libraries are used:

* [Dagger 2](https://google.github.io/dagger/) - Dependency injection framework from google in conjuction with kotlin kapt annotation processor for easy development.
* [RxJava 2](https://github.com/ReactiveX/RxJava) - is used primary with RxAndroid for heavily background job.
* [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/index.html) - The general platform of applicaiton.
* [Retrofit 2](http://square.github.io/retrofit/) - Square's library that makes life easier when there is a place in dealing with rest services. 
* [Glide 2](https://github.com/bumptech/glide) - the good reason for using this lib is allowing to load images via one line of code.

## News source

The news loads https://newsapi.org which gives simple api and authorization.

[![](https://goo.gl/DxpbJK)](https://play.google.com/store/apps/details?id=dv.serg.topnews "")
