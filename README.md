# KIISGateway
KIISGateway adalah salah satu komponen dalam KIIS (Kepri Integrated Information System)

## Mekanisme Otentikasi Rest KIIS Gateway 
Untuk mengakses layanan KIISBigData harus melakukan otentikasi terlebih dahulu, yang mana hasil outputnya dalam bentuk json. contoh implementasi dalam beberapa bahasa pemrograman.
### Java

```
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
public class SendHeaderHTTP {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
			sendGet();
	}
	
	public static void sendGet () throws Exception {
		String url = "";
		
		URL obj = new URL(url);
		
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");
		
		//add request header
		con.setRequestProperty("Username", "");
		con.setRequestProperty("Password", "");
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));	
		String jsonText = readAll(in);
		JSONObject json = null;
		json = new JSONObject(jsonText);
		int conn = (Integer)json.get("connection");
		if (conn>0) {
			System.out.print("sukses");
		}
		else {
			System.out.print("gagal");
		}
	}	
	public static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

}
```
