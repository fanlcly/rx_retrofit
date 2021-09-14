# rx_retrofit2.0版本来啦
# rx_retrofit2.0版本来啦
# rx_retrofit2.0版本来啦

##功能：
###1：响应方式：普通方式（call），rxjava的方式
###2：请求头你可以随意添加
###3，支持多url
###4，已经集成了默认loading，当然你也可以自定义
###5，已经集成了格式化之后logger。
###6，关于retrofit部分请参考retrofit官方文档

------

## 使用

1.将其添加到project的build.gradle中。

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

或者添加maven

	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>

2.添加依赖到APP的build.gradle中。

	dependencies {
		implementation 'com.github.fanlcly:rx_retrofit:2.0.0'
	}
或者

	<dependency>
	    <groupId>com.github.fanlcly</groupId>
	    <artifactId>rx_retrofit</artifactId>
	    <version>2.0.0</version>
	</dependency>



