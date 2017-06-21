# 知乎日报.K

知乎日报.K 是用Android 平台的第三方知乎日报客户端。目前实现了获取最新新闻列表，获取主题日报列表以及查看新闻内容等功能。知乎日报.K 使用 Kotlin 开发（除了生成的 GsonFormat 外），并搭配了 Fresco, RxAndroid, Retrofit 等框架。

![home](captures/home.png) ![theme_list](captures/theme_list.png) 



![info_details](captures/info_details.png)



## 技术点

开发环境为 Android Studio 3.0 Preview 。

在 Kotlin 中如果对象可能为 null 需要 ? 声明

```kotlin
class DetailsActivity: AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    bundle Bundle? = intent.extras
    ...
  }
}
```

对声明为 可空? 的变量，在使用时需使用 ?(safe) 或者 !!(not-null assert) 。区别在于当 object 为空时，前者不会执行操作，后者则会抛出异常。

```kotlin
class DetailsActivity: AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    ...
    mWebView = findViewById(R.id.details_web) as WebView
    mWebView!!.setWebChromeClient(object: WebChromeClient() {
    })
    mWebView!!.settings.javaScriptEnabled = true
    ...
    val bundle: Bundle? = intent.extras
    bundle?.let {
      mStoryId = bundle.getString("id")
      if (!TextUtils.isEmpty(mStoryId)) {
        mWebView!!.loadUrl(STORY_URL + mStoryId)
      } else {
        Log.d(LOG_TAG, "story id is null.")
      }
    }
  }
}
```

在 Java 中我们在 Activity 中经常会使用到这个 Activity 的实例 Activity.this 在 Unity 中我们变成 this@MainActivity 例如在 DailyInfoFragment 中定义了一个 ```fun toActivity(id: String) ```

```kotlin
class DailyInfoFragment: Fragment() {
  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
  savedInstanceState: Bundle?): View? {
    ...
    mAdapter = DailyInfoAdapter(object: DailyInfoAdapter.FragmentCallback {
  		override fun toActivity(id: String) {
    		// this 的用法
    		this@DailyInfoFragment.toActivity(id)
  		}
	})
  }
  
  fun toActivity(id: String) {
    ...
  }
}

```

在 Java 中我们获得 class 的实例如下

```java
public class Hello {
  ...
}
...
Class<?> clazz = Hello.class;
Hello hello = new Hello();
Class<?> clazz2 = hello.getClass();
```

而 Kotlin 中

```
class Hello
val clazz = Hello::class.java
val hello = Hello()
val clazz2 = hello.javaClass
```

实际上 Hello::class 拿到的时 Kotlin 的 KClass，这个是 Kotlin 的类型，如果想拿到 Java 的 Class 实例，那么就 需要```Hello::class.java``` 

## Links

[Twobbble: ](https://github.com/550609334/Twobbble) 一个非常优秀的 Dribbble Android 客户端，使用 Kotlin 编写。

[快速上手 Kotlin 的 11 招](https://zhuanlan.zhihu.com/p/27376096) 很棒的 Kotlin 入门

[From Java To Kotlin - Your Cheat Sheet For Java To Kotlin](https://github.com/MindorksOpenSource/from-java-to-kotlin) 从 Java 到 Kotlin ，强烈推荐

最后还是推荐 Kotlin 的官方 Reference [传送门](https://www.kotlincn.net/docs/reference/)

#### License Copyright 2017 Lian Dongyang

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.