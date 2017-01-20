package online_docId;
import java.util.List;
import com.sohu.mrd.videoDocId.model.NewsInfo;
import com.sohu.mrd.videoDocId.service.GenerateNewsDocIdService;
import com.sohu.mrd.videoDocId.utils.FileKit;
/**
 * @author Jin Guopan
   @creation 2017年1月17日
 */
public class TestNewsDocId {
	/**
	 * http://service.k.sohu.com/server/interface/getnews.go?newsids=138802780
	 * 00002bbbd3a462ca-676f857a2444d6f0
	 f:k.sohu.com:138135918     timestamp=1464747136756, value=\x00                                         
	 f:k.sohu.com:138226359     timestamp=1464760233040, value=\x00                                         
	 f:k.sohu.com:138802780     timestamp=1464848872067, value=\x00                                         
	 f:m.sohu.com:452460450     timestamp=1464848872067, value=\x00
	 http://service.k.sohu.com/server/interface/getnews.go?newsids=135467597
column=f:k.sohu.com:135455646, timestamp=1464071884452, value=\x00          
 44ed3b0                                                                                                
 column=f:k.sohu.com:135467597, timestamp=1464075177847, value=\x00          
 44ed3b0      
http://service.k.sohu.com/server/interface/getnews.go?newsids=26401773

###
http://service.k.sohu.com/server/interface/getnews.go?newsids=132702651 
http://service.k.sohu.com/server/interface/getnews.go?newsids=132703111 
http://service.k.sohu.com/server/interface/getnews.go?newsids=132724430 
http://service.k.sohu.com/server/interface/getnews.go?newsids=132822648
http://service.k.sohu.com/server/interface/getnews.go?newsids=448367003
	 * @param args
	 */
	public static void main(String[] args) {
		List<String> list=FileKit.read2List("data/simdata");
		GenerateNewsDocIdService  generateNewsDocIdService =GenerateNewsDocIdService.getInstance();
		String title1=list.get(0);
		String content1=list.get(1);
		
		NewsInfo newsInfo1 = new NewsInfo();
		newsInfo1.setContent(content1);
		newsInfo1.setUrl("http://www.baidu25.com/");
		newsInfo1.setTitle(title1);
		String docId1=generateNewsDocIdService.getDocId(newsInfo1);
		System.out.println("docId1 "+docId1);
		
		String title2=list.get(2);
		String content2=list.get(3);
		
		NewsInfo newsInfo2 = new NewsInfo();
		newsInfo2.setContent(content2);
		newsInfo2.setUrl("http://www.sohu25.com/");
		newsInfo2.setTitle(title2);
		String docId2=generateNewsDocIdService.getDocId(newsInfo2);
		System.out.println("docId2 "+docId2);
		
		String title3=list.get(4);
		String content3=list.get(5);
		
		
		NewsInfo newsInfo3 = new NewsInfo();
		newsInfo3.setContent(content3);
		newsInfo3.setUrl("http://www.wangye25.com/");
		newsInfo3.setTitle(title3);
		String docId3=generateNewsDocIdService.getDocId(newsInfo3);
		System.out.println("docId3 "+docId3);
		
		String title4=list.get(6);
		String content4=list.get(7);
		NewsInfo newsInfo4 = new NewsInfo();
		newsInfo4.setContent(content4);
		newsInfo4.setUrl("http://www.wangye225.com/");
		newsInfo4.setTitle(title4);
		String docId4=generateNewsDocIdService.getDocId(newsInfo4);
		System.out.println("docId4 "+docId4);
		
	}
}
