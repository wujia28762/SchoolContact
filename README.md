# 项目名称
SchoolContact
## 项目结构&类型
>`mvc`+`imageLoder`+`afinal`+`jackson`+`lrucache`
>>为沈阳师范大学幼儿园和中小学提供线下交流平台,依托融云IM,完成1对1对话,1对多通知,作业;班级圈.
>>>集成友盟（报告分析）,信鸽推送等SDK.
## 地址
http://zhushou.360.cn/detail/index/soft_id/3127192?recrefer=SE_D_%E5%8D%8E%E5%88%9B%E5%AE%B6%E6%A0%A1%E9%80%9A
## 页面展示
>![](http://p17.qhimg.com/dm/168_300_/t01ca4f745daf209ad8.png "登录LOGO")
>![](http://p16.qhimg.com/dm/168_300_/t015b3e425d8c41431d.png "班级圈")
>![](http://p16.qhimg.com/dm/168_300_/t01a8c6a46edf69aee1.png "设置")
>![](http://p18.qhimg.com/dm/168_300_/t01d505e32a04f2e24a.png "IM")
## 项目缺陷
> 并没有对Activity,fragment,adapter,holder做好`封装`<br>
> `JSON解析`在UI线程<br>
> 后台业务返回没有对view的`生命周期`进行检查<br>
> 因为业务主要依托第三方IM,所以很少使用`服务`,`内容提供者`,`sqlite`等组件<br>
> `mvc`代码冗余高,afinal注解基于`反射`.效率低<br>
> 很少自定义控件
> 班级圈布局，使用一致的item，通过隐藏控件来完成。
## 改善缺陷
>见：
>>https://github.com/wujia28762/MvpDemo
## 不是缺陷
>在低版本的SDK中,在班级圈中,实现了ListView的可见位置局部加载。
```Java
private void updateSingleRow() {

		if (mListView != null) {
			int start = mListView.getFirstVisiblePosition();

			for (int i = start, j = mListView.getLastVisiblePosition(); i <= j; i++) {
				View view = mListView.getChildAt(i - start);
				if (view.getTag() instanceof ViewHolder) {
					ViewHolder view1 = (ViewHolder) (mListView.getChildAt(i
							- start).getTag());
					view1.getmGridViewAdapter().notifyDataSetChanged();
				}

			}
		}
	}
```
> 简单进行了屏幕适配,动态获取了窗口宽度,按权重进行计算每个布局的长度。
> Bitmap使用了弱引用。
> 使用了Lrucache
> 对返回的图片进行了压缩。
