import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class MainWatch {

	public static void watchDirectoryPath(String path) throws IOException {
		File dir = new File(path);
		//		 while(true){
		String[] subdirs=dir.list();
		ArrayList<String> dirList = new ArrayList<String>(Arrays.asList(subdirs)); 
		dirList.add(".");
		for (String d : subdirs ) {
			File f=new File(path+System.getProperty("file.separator")+d);
			if(f.isDirectory())
			{
				Runnable r=new Runnable() {

					@Override
					public void run() {
						setListener(f);
					}
				};
				new Thread(r).start();			}
		}
		//		}
	}

	private static void setListener(File f) {
		Path myDir= f.toPath();
		try 
		{
			Boolean isFolder = (Boolean) Files.getAttribute(myDir,"basic:isDirectory", NOFOLLOW_LINKS);
			if (!isFolder)
			{
				throw new IllegalArgumentException("Path: " + myDir + " is not a folder");
			}
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}

		//        Iterable<FileStore> it=myDir.getFileSystem().getFileStores();
		//        Iterator<FileStore> it1=it.iterator();
		//        while(it1.hasNext()){
		//      	  System.out.println(it1.next());
		//        }
		while(true)
			try {
				System.out.println(1);
				WatchService watcher = myDir.getFileSystem().newWatchService();
				myDir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

				WatchKey watckKey = watcher.take();

				List<WatchEvent<?>> events = watckKey.pollEvents();

				for (WatchEvent event : events) {
					System.out.println(event);
					if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
						System.out.println("Created: " + event.kind().toString());
//						{
//							try {
//								URL url = new URL ("http://usmlvv1sf1643.usmlvv1d0a.smshsc.net:8443/job/EPM-Common-Properties-publish/build"); // Jenkins URL localhost:8080, job named 'test'
//								String user = "bhunas00"; // username
//								String pass = "Passw0rd"; // password or API token
//								String authStr = user +":"+  pass;
//								String encoding = DatatypeConverter.printBase64Binary(authStr.getBytes("utf-8"));
//
//								HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//								connection.setRequestMethod("POST");
//								connection.setDoOutput(true);
//								connection.setRequestProperty("Authorization", "Basic " + encoding);
//								InputStream content = connection.getInputStream();
//								BufferedReader in   =
//										new BufferedReader (new InputStreamReader (content));
//								String line;
//								while ((line = in.readLine()) != null) {
//									System.out.println(line);
//								}
//							} catch(Exception e) {
//								e.printStackTrace();
//							} 
//
//						}
					}
					if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
						System.out.println("Delete: " + event.context().toString());
					}
					if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
						System.out.println("Modify: " + event.context().toString());
					}
				}
			}
		catch (Exception e) 
		{
			System.out.println("Error: " + e.toString());
		}

	}
	public static void main(String[] args) throws IOException,
	InterruptedException {
		File dir = new File("C:\\AccurevWorkspace\\SF-EPM-REPO-DA-TEST\\soarian-financials-repository\\common-properties");
		watchDirectoryPath(dir.toPath().toString());
	}
}