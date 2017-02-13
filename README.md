# 简介

完全使用weex来做的一个知乎新闻和日记功能的app，学习weex的入门级app。

# 使用方法如下：

首先需要：
``` java
npm install
```

装好node_modules之后，第一次命令如下：
``` java
npm run build
```
然后用studio打开playground代码，把MainActivity.java中的代码
``` java
    @Override
    protected String getHost() {
        return "10.12.65.114";
    }
```
中的return的string修改成你pc的ip就可以了。

把MainActivity.java中的代码
``` java
@Override
    protected String setHotRefreshUrl() {
        return "main";
    }
```
中return的string修改成你要调试的we文件的名字就可以了。

启动watch服务以及debug服务命令如下：
``` java
npm run dev
```

最新增加hot reload功能，修改完代码后，可以在android上直接看到效果。
打开命令行，进入目录src下，执行（weex后面增加的是你要调试的文件名，如果你修改了文件名，只要把下面对应的文件名修改一下就可以了）
``` java
weex main.js
```

-  使用android studio打开playground，运行app，修改代码，就可以直接看到效果了。
-  如果更改代码没有效果，点一下app右上角的刷新按钮，也可以立即看到效果。

# 运行结果

<img src="http://img.blog.csdn.net/20170206214702556?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvemptMDUxOA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast" width="200px" height="400px"/>

<img src="http://img.blog.csdn.net/20170206214738150?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvemptMDUxOA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast" width="200px" height="400px"/>

<img src="http://img.blog.csdn.net/20170206214802174?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvemptMDUxOA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast" width="200px" height="400px"/>

<img src="http://img.blog.csdn.net/20170206214852994?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvemptMDUxOA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast" width="200px" height="400px"/>

![新闻详情](http://img.blog.csdn.net/20170206214738150?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvemptMDUxOA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

![日记列表](http://img.blog.csdn.net/20170206214802174?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvemptMDUxOA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

![日记详情](http://img.blog.csdn.net/20170206214852994?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvemptMDUxOA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

还有上拉和下拉刷新，我没有截图下来，大家可以去试试。

# 问题
-  日记保存后并没有立马生效，需要点击退出之后重新进app才能生效，因为weex的生命周期没有像android onresume生命周期，所以我暂时还没修改。
-  长按删除，也是没有立马生效，同上原因。
-  界面没有适配（weex没有dp单位），不同分辨率 界面看起来也不同。








