# SchemeDemo
通过注解实现Uri页面跳转  
支持参数自动解析  
  
使用场景：  
1、应用内服务端下发uri进行页面跳转  
2、通知栏点击，携带uri进行页面跳转  
3、其他应用通过uri调起进行页面跳转  
  
注：activity的注解格式：group/path  
group为各个模块的唯一字符串，不同模块不可重复  
  
使用姿势：  
场景1：应用内服务端下发uri进行页面跳转  
1、在需要支持uri跳转的Activity增加注解@SchemePath("{随意填，唯一字符串}")  
2、跳转事件  
  SchemeManager.handleScheme(context, {服务端下发的uri字符串})  
    
注：参数支持  
uri支持参数，如"scheme://ModuleA/Activity?data=1&time=20200714&hasData=true"  
Activity的参数增加@SchemeExtra注解，如  
@SchemeExtra  
var data: Int = 2  
@SchemeExtra  
var time: String? = null   
@SchemeExtra  
var hasData: Boolean = false  
  
通过调用Activity通过SchemeManager.inject(this)注入参数  
  
场景2：通知栏点击，携带uri进行页面跳转    
1、应用首页Activity增加注解@SchemePath("{随意填}")  
2、application调用初始化  
  SchemeManager.initScheme("{你的应用的scheme}", "{首页Activity的注解}")  
3、启动页，通知栏点击入口  
  val data = intent.getStringExtra("data")  
  if (data != null) {  
      SchemeManager.handleScheme(this, data)  
  } else if (isTaskRoot) {  
      startActivity(Intent(this, MainActivity::class.java))   
  }  
  finish()  
    
场景三：其他应用通过uri调起进行页面跳转  
1、注册中转activity  
        <activity  
            android:name="com.archer.scheme.SchemeActivity"  
            android:configChanges="orientation|keyboardHidden|screenSize"   
            android:launchMode="singleTask"  
            android:screenOrientation="behind"  
            android:theme="@style/Translucent">  
            <intent-filter>  
                <action android:name="android.intent.action.VIEW" />  
  
                <category android:name="android.intent.category.BROWSABLE" />  
                <category android:name="android.intent.category.DEFAULT" />  
  
                <data android:scheme="{你的应用的scheme}" />  
            </intent-filter>  
        </activity>  
           
        
   

