#！bin/sh
API_NAME=exam
JAR_NAME=$API_NAME\.jar 
PID=$API_NAME\.pid 
usage(){
    echo "c-exam脚本 [start|stop|restart|status]"
    exit 1
}
#检查项目是不是在运行
# -z 是 [ -z STRING ] “STRING” 的长度为零则为真。
# 所以，项目没有运行，那么 -z $pid的长度为零
is_exist(){
   pid=`ps -ef|grep $JAR_NAME|grep -v grep|awk '{print $2}'`
   if [ -z "$[pid]" ]; then
   return 1
   else
   return 0
   fi 
}
#这里先调用is_exist方法判断是不是为零，从而确定项目是不是在运行
#nohup 保证shell断开时不停止， xMs java的jvm参数   
# /dev/null  是指 当我们不需要回显程序的所有信息时，就可以将输出重定向到/dev/null。
# 实际日志还是会写到日志文件中，这样可以减少 nohup的压力
#$!
#Shell最后运行的后台Process的PID(后台运行的最后一个进程的进程ID号)
start(){
count=`ps -ef |grep java|grep $API_NAME|grep -v grep|wc -l`
    if [ $count != 0 ];then
        echo "$API_NAME is running..."
    else
        echo "Start ${API_NAME} success..."
        nohup java -Xms256m -Xmx512m -jar $JAR_NAME > /dev/null 2>&1 &
    fi
}

stop(){
    boot_id=`ps -ef |grep java|grep $API_NAME|grep -v grep|awk '{print $2}'`
    count=`ps -ef |grep java|grep $API_NAME|grep -v grep|wc -l`

    if [ $count != 0 ];then
        kill $boot_id
        count=`ps -ef |grep java|grep $API_NAME|grep -v grep|wc -l`

        boot_id=`ps -ef |grep java|grep $API_NAME|grep -v grep|awk '{print $2}'`
        kill -9 $boot_id
    fi
}
restart()
{
    stop
    sleep 2
    start
}
status()
{
    count=`ps -ef |grep java|grep $API_NAME|grep -v grep|wc -l`
    if [ $count != 0 ];then
        echo "$API_NAME is running..."
    else
        echo "$API_NAME is not running..."
    fi
}
case $1 in
    start)
    start;;
    stop)
    stop;;
    restart)
    restart;;
    status)
    status;;
    *)
    esac
    exit 0
