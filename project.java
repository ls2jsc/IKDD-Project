import org.json.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class project {

	public static void main(String[] args) {
		data v1, v2;
		int score1, score2;
		
		v1 = readURL(args[0]);
		v2 = readURL(args[1]);
		score1 = judge(v1.label);
		score2 = judge(v2.label);
		if(v1.viewCount > v2.viewCount) score1 += 10 * Math.round(Math.log(v1.viewCount)/Math.log(v2.viewCount));
		else score2 += 10 * Math.round(Math.log(v2.viewCount)/Math.log(v1.viewCount));
		if((v1.like-v1.dislike) > (v2.like-v2.dislike)) score1 += 10 * Math.round(Math.log(v1.like-v1.dislike)/Math.log(v2.like-v2.dislike));
		else score2 += 10 * Math.round(Math.log(v2.like-v2.dislike)/Math.log(v1.like-v1.dislike));
		if(v1.comment > v2.comment) score1 += 8 * Math.round(Math.log(v1.comment)/Math.log(v2.comment));
		else score2 += 8 * Math.round(Math.log(v2.comment)/Math.log(v1.comment));
		
		System.out.printf("[%d],[%d]\n", score1, score2);
	}
	public static data readURL( String strURL ) {
		data v = new data();
        try {
        	String line = null;
        	
        	URL url = new URL("http://gdata.youtube.com/feeds/api/videos/" + strURL + "?v=2&alt=json");
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            uc.connect();
            InputStream is = uc.getInputStream();  
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            
            while ((line = br.readLine()) != null) {
            	JSONObject j1 = new JSONObject(line);
            	
            	v.label = j1.getJSONObject("entry").getJSONArray("category").get(1).toString();
            	JSONObject j2 = new JSONObject(v.label);
            	v.label = j2.get("term").toString();
            	//System.out.println(v.label);
            	
            	v.viewCount = Integer.parseInt(j1.getJSONObject("entry").getJSONObject("yt$statistics").get("viewCount").toString());
            	//System.out.println(v.viewCount);
            	
            	v.like = Integer.parseInt(j1.getJSONObject("entry").getJSONObject("yt$rating").get("numLikes").toString());
            	//System.out.println(v.like);
            	
            	v.dislike = Integer.parseInt(j1.getJSONObject("entry").getJSONObject("yt$rating").get("numDislikes").toString());
            	//System.out.println(v.dislike);
            	
            	v.comment = Integer.parseInt(j1.getJSONObject("entry").getJSONObject("gd$comments").getJSONObject("gd$feedLink").get("countHint").toString());
            	//System.out.println(v.comment);
            }
            return v;
        }
        catch(Exception e) {
            System.out.println("Exception: " + e);
            System.out.println("Shut down.");
            System.exit(0);
            return v;
        }
	}
	public static int judge(String str) {
		if(str.equals("Comedy") || str.equals("Entertainment")) return 50;
		if(str.equals("Film") || str.equals("Animals") || str.equals("People") || str.equals("Funny") || str.equals("Sports")) return 20;
		else return 0;
	}
}
