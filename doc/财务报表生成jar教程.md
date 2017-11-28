## 财务报表生成jar教程

***本教程适用于intelliJ IDEA***

### 1. File-->Project Structure-->Artifacts

### 2. 点击加号-->Add JAR-->From modules with dependencies

 ![无标题](\\goodnight\Intern\侯立\无标题.png)



###3. 填写项目目标模块、主类、引用库文件及生成MF配置文件的路径 

![无标题1](\\goodnight\Intern\侯立\无标题1.png)

###4. 填写classpath
./lib/fastjson-1.2.7.jar
./lib/gson-2.3.1.jar
./lib/hamcrest-core-1.3.jar
./lib/junit-4.12.jar
./lib/poi-3.9.jar

`点击确定`
###5. build-->Build Artifacts-->选取刚才设置的Artifact