package com.fans.fun;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;

/**
 * 微信網頁版的主要功能
 * 
 * @author fans.fan
 *
 */
public class PageFun {
	
	public void getUUID() throws ClientProtocolException, IOException{
		String url = "https://login.weixin.qq.com/jslogin";
		String appid = "wx782c26e4c19acffb",
         fun = "new",
         lang = "zh_CN";
        long _ = System.currentTimeMillis();
		HttpUtil.doPostSSL(url, "");
//		System.out.println(EntityUtils.toString(entity));  
	}
	
	/** 
     * 创建SSL安全连接 
     * 
     * @return 
     */  
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {  
        SSLConnectionSocketFactory sslsf = null;  
        try {  
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {  
  
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {  
                    return true;  
                }  
            }).build();  
            sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {  
  
                @Override  
                public boolean verify(String arg0, SSLSession arg1) {  
                    return true;  
                }  
  
                @Override  
                public void verify(String host, SSLSocket ssl) throws IOException {  
                }  
  
                @Override  
                public void verify(String host, X509Certificate cert) throws SSLException {  
                }  
  
                @Override  
                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {  
                }  
            });  
        } catch (GeneralSecurityException e) {  
            e.printStackTrace();  
        }  
        return sslsf;  
    }  

	public static void main(String[] args) {
		PageFun pageFun = new PageFun();
		try {
			pageFun.getUUID();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		String url = "https://login.weixin.qq.com/jslogin";
//		HttpUtil.doPostSSL(url, "");
	}
	
}
