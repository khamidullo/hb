package uz.hbs.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.beans.IdByteAndName;

public class FaxClientUtil {
	private static final Logger _logger = LoggerFactory.getLogger(FaxClientUtil.class);
	private String link;
	private int connect_timeout;
	private int socket_timeout;
	
	private static final byte UnSuccess = 0;
	private static final byte Success = 1;
	
	private FaxClientUtil(){
		link = new BundleUtil().configValue("fax.server.link");
		connect_timeout = Integer.parseInt(new BundleUtil().configValue("fax.server.link.timeout.connect"));
		socket_timeout = Integer.parseInt(new BundleUtil().configValue("fax.server.link.timeout.socket"));
	}
	
	public FaxClientUtil getInstance(){
		return new FaxClientUtil();
	}
	
	public boolean sendRequest(String recipient, String content) throws Exception {
        URL url = new URL(link);
        
        String data = createRequest(recipient, content);

        URLConnection connect = getConnect(url, "POST");
        connect.setConnectTimeout(connect_timeout);
        connect.setReadTimeout(socket_timeout);

        connect.setDoOutput(true);
        connect.setDoInput(true);
        DataOutputStream dis = new DataOutputStream(connect.getOutputStream());
        dis.write(data.getBytes());
        dis.close();

        _logger.debug("Order sent to server, data = \n" + data);

        InputStream is = connect.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int read;
        byte[] part = new byte[1024];
        while ((read = is.read(part, 0, part.length)) != -1) {
            baos.write(part, 0, read);
        }
        baos.flush();

        byte[] body = baos.toByteArray();
        
        IdByteAndName response = parseOrderResponse(new String(body));
        
        is.close();
        baos.close();
        
        _logger.debug("Response: " + new String(body));
        return (response.getId() == Success);
	}
	
	private URLConnection getConnect(URL url, String method) throws Exception {
        if (url.getProtocol().equalsIgnoreCase("https")) {
                TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {  // Create a trust manager that does not validate certificate chains

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                    }
                            
                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    
                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                } };
                
                SSLContext sc = SSLContext.getInstance("SSL"); // Install the all-trusting trust manager
                sc.init(null, trustAllCerts, new java.security.SecureRandom());

                HostnameVerifier allHostsValid = new HostnameVerifier() { // Create all-trusting host name verifier
					@Override
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
                };
                        
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setSSLSocketFactory(sc.getSocketFactory());
                conn.setHostnameVerifier(allHostsValid);
                conn.setRequestMethod(method);
                return conn;
        } else {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod(method);
                return conn;
        }
	}
	
	public IdByteAndName parseOrderResponse(String xml) {
		IdByteAndName result = new IdByteAndName(UnSuccess);
		try {
			Document doc = new SAXBuilder(XMLReaders.NONVALIDATING).build(new StringReader(xml));
			if (doc != null) {
				Element rootElement = doc.getRootElement();
				List<Element> rootChildList = rootElement.getChildren();
				for (Element child : rootChildList) {
					if (child.getName().equalsIgnoreCase(Const.Response.Status)) {
						result.setId(Byte.valueOf(child.getValue()));
					} else if (child.getName().equalsIgnoreCase(Const.Response.StatusDesc)) {
						result.setName(child.getValue());
					}
				}
			}
		} catch (Exception e) {
			_logger.error("Exception", e);
		} finally {
			_logger.debug("Result = " + result);
		}
		return result;
	}

	public String createRequest(String recipient, String content){
		Document xml = new Document();
		Element root = new Element(Const.COMMAND);
		xml.addContent(root);
		
		root.addContent(new Element(Const.Request.FAX_ITEM).addContent(content).setAttribute(Const.Request.FaxItem.RECIPIENT, recipient));
		return new XMLOutputter(Format.getRawFormat()).outputString(xml).trim();
	}

	private static class Const {
		public static final String COMMAND = "Command";
		
		public static class Request {
			public static final String FAX_ITEM = "FaxItem";
			
			public static class FaxItem {
				public static final String RECIPIENT = "recipient";
			}
		}
		
		public static class Response {
			public static final String Status = "StatusCode";
			public static final String StatusDesc = "StatusDesc";
		}
	}
}
