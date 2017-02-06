# 简介

仿照大神的weexone做的一个知乎新闻和日记。

# 使用方法如下：

首先需要：
``` java
npm install
```

装好node_modules之后，就可以使用项目中的脚本weextwo了，先将we文件转换为js，命令如下：
``` java
./weextwo
```

然后将生成的js文件放到android项目中，命令如下：
``` java
./weextwo android
```

这行命令会将生产的js文件放到android项目中的assets/dist中，最后你就可以使用android studio打开项目中android/playground直接把app编译运行起来。

# 运行结果

![新闻列表](http://img.blog.csdn.net/20170206214702556?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvemptMDUxOA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

![新闻详情](http://img.blog.csdn.net/20170206214738150?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvemptMDUxOA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

![日记列表](http://img.blog.csdn.net/20170206214802174?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvemptMDUxOA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

![日记详情](http://img.blog.csdn.net/20170206214852994?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvemptMDUxOA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

还有上拉和下拉刷新，我没有截图下来，大家可以去试试。

# 问题
-  日记保存后并没有立马生效，需要点击退出之后重新进app才能生效，因为weex的生命周期没有像android onresume生命周期，所以我暂时还没修改。
-  长按删除，也是没有立马生效，同上原因。
-  界面没有适配（weex没有dp单位），不同分辨率 界面看起来也不同。








