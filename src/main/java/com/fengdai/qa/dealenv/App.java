package com.fengdai.qa.dealenv;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import com.fengdai.qa.util.sendmailUtil;

/**
 * Hello world!
 *
 */
public class App {
	
	
	
	
	public static List<String> result= new ArrayList<>();
	public static void printpath(ZooKeeper zk,String path) throws Exception, InterruptedException{
		List<String> paths = zk.getChildren(path, false);
		for (String p : paths) {
			printpath(zk, path + "/" + p);
			//System.out.println(path + "/" + p);
			result.add(path + "/" + p);
		}
	}

	public static void delPath(ZooKeeper zk, String path) throws Exception {
		List<String> paths = zk.getChildren(path, false);
		for (String p : paths) {
			delPath(zk, path + "/" + p);
			System.out.println(path + "/" + p);
		}
		for (String p : paths) {
			zk.delete(path + "/" + p, -1);
		}
	}

	public static void main(String[] args) throws Exception {
		ZooKeeper zk = new ZooKeeper("172.30.249.243", 3000, new Watcher() {
			@Override
			public void process(WatchedEvent event) {

			}
		});
//		delPath(zk, "/dubbo");
		printpath(zk, "/dubbo");
		Set<String> checkproviderset = new HashSet<String>();
		Set<String> chongfuset = new HashSet<String>();
		
		for(String temp:result){
			if(temp.contains("providers")){
				String var= temp.split("/")[2];
				if(checkproviderset.contains(var)&&temp.split("/").length==5){
					chongfuset.add(var);
				}
				checkproviderset.add(var);
			}
		}
		String emailbody="";
		StringBuffer emailcontent = new StringBuffer(); 
		Set<String> chongfuDubboSet = new HashSet<String>();
		System.out.println("重复的provider是");
		for(String temp:chongfuset){
			System.out.println(temp);
			emailbody+=temp;
			emailbody+="<br/>";
			String servicename=temp;
			String[] vattemp=servicename.split("\\.");
			if(servicename.contains("com.fengdai")){
				chongfuDubboSet.add("com.fengdai."+servicename.split("\\.")[2]);
			}
		}
		System.out.println("重复的dubbo服务是：");
		emailbody+="<span class=\"test\">测试环境dubbo重复服务</span><br />";
		for(String temp:chongfuDubboSet){
			System.out.println(temp);
			emailbody+=temp;
			emailbody+="<br/>";
		}
		emailbody+="<span class=\"test\">测试环境解决方法</span><br />";
		emailbody+="砸了它<br/>";
		emailbody+="关机重启<br/>";
		emailbody+="发个红包 联系Harrison Wells<br/>";
		emailbody+="最后一个最靠谱了，快点给我发红包！！！<br/>";
	
		
		emailcontent.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">")  
        .append("<html>")  
        .append("<head>")  
        .append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")  
        .append("<title>dubbo环境问题</title>")  
        .append("<style type=\"text/css\">")  
        .append(".test{font-family:\"Microsoft Yahei\";font-size: 18px;color: red;}")  
        .append(".body{font-family:\"Microsoft Yahei\";font-size: 10px;color: black;}")  
        .append("</style>")  
        .append("</head>")  
        .append("<body>")  
        .append("<span class=\"test\">测试环境dubbo重复provier节点</span><br />")  
        .append(emailbody)
        .append("</body>")  
        .append("</html>"); 
		
		List<String> receviedAccouts= new ArrayList<>();
		receviedAccouts.add("****@qq.com");
		receviedAccouts.add("hzzyl@*.com");
		sendmailUtil.sendmail(emailcontent.toString(),receviedAccouts);
		
	}

}
