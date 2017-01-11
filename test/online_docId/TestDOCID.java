package online_docId;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import com.sohu.mrd.videoDocId.utils.FileKit;
import com.sohu.mrd.videoDocId.utils.HttpClientUtil;
/**
 * @author Jin Guopan
   @creation 2017年1月10日
 */
public class TestDOCID {
	private static Logger LOG = Logger.getLogger(TestDOCID.class);
	private static String url ="http://service1.mrd.sohuno.com/news/newsprofile?act=getdata&docid=";
	public static void main(String[] args) {
		final List<String> list=FileKit.read2List("data/20170110DID.txt");
		for( int i=0;i<list.size();i++)
		{
			String docId="";
			try {
				 docId=HttpClientUtil.executeGet(url+list.get(i));
				if(docId!=null)
				{
					FileKit.write2File(docId, "data/docId_20170110DID_10DID", true);
				}
			} catch (Exception e) {
				LOG.error("出错的 docId"+docId);
				String docId1=HttpClientUtil.executeGet(url+list.get(i));
				if(docId1!=null)
				{
					FileKit.write2File(docId1, "data/docId_20170110DID_19DID", true);
				}
			}
			
		
		}
	}
}
