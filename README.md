# StatusMonitor

### 功能列表

* [x] 修改密码
* [ ] 考勤提醒
* [x] 上下班人脸打卡
* [ ] 工作时长统计
* [ ] 疲劳检测
* [ ] 疲劳提醒
* [x] 查看员工考勤信息
* [ ] 查看员工信息
* [x] 员工信息录入、修改和删除
* [ ] 部门信息录入、修改和删除
* [ ] 上下班时间设定

### 项目环境

* [x] python3.3+
* [x] java1.8
* [x] Window或者macOS

### 安装步骤
1、安装dlib

2、安装其它python依赖

```
# requirements.txt在根目录下
pip3 install -r requirements.txt
```

3、导入数据库文件

```
# 先连接mysql
# 然后执行下面语句
source src/main/resources/statusmonitor.sql
```

### 启动步骤

1、启动mian.py

```
main.py在src/main/python文件下
```

2、启动StartApplication.java

```
StartApplication.java在src/main/java/com/whut下
```

### QT前端代码

如果要和后端进行联调，需要安装QT

`https://github.com/15207135348/device-agent`

下面展示对接后的整体效果

1、登录界面

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gfs1kpfpgwj30iw0ig3z4.jpg" alt="6DE2E47F-B700-4112-BA4D-B91F6A9A69CE" style="zoom: 50%;" />

2、录入员工信息界面

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gfs1o1vxomj30ve0j4wjx.jpg" alt="549EA618-1EEB-445A-B5F9-7BE6C88F7E60" style="zoom:44%;" />



3、人脸打卡和疲劳检测界面

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gfs1pv9jyhj311a0mwter.jpg" alt="7774A07C-5DFF-46A5-8493-197D2003B677" style="zoom:50%;" />





