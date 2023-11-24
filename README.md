1. Extend your app class
@HiltAndroidApp
class App : CoreApplication() {
    override fun isDebug(): Boolean {
        return true
    }
}
  
2. Extend your base activity class
class MainActivity : CoreActivity<ActivityMainBinding>() {
    override fun bindingView(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }
}

To prevent that split for language files we need to add extra lines in our build.gradle file inside the app folder like below.
android {
    //...
    //... removed for brevity
    bundle {
        language {
            enableSplit = false
        }
    }
}

