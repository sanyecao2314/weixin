package com.fans.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.fans.model.User;

public class FunctionUtil {

	/**
	 * 显示登陆的二维码图片
	 * 
	 * @param apiUrl
	 * @param params
	 * @throws Exception
	 */
	public static void showLoginImg(String apiUrl, Map<String, Object> params) throws Exception {
		CloseableHttpResponse response = null;
		HttpEntity entity = HttpUtil.doPostSSL(apiUrl, params, response);
		if (entity != null) {
			InputStream input = entity.getContent();
			File file = File.createTempFile("weixin", ".png");
			OutputStream output = new FileOutputStream(file);
			IOUtils.copy(input, output);
			output.flush();
			close(input);
			close(output);
			// exec(new String[]{file.getAbsolutePath()});
			Runtime.getRuntime().exec("mspaint " + file.getAbsolutePath());
		}
		HttpUtil.closeResponse(response);
	}

	// <error><ret>0</ret><message></message><skey>@crypt_415fe54d_d87b836baacae5c117d0b1d58d535b93</skey><wxsid>GfdekP9KPG4IhFE2</wxsid><wxuin>1448148081</wxuin><pass_ticket>xMhzw%2BMb%2B8n6DFEjSPoI3iFdKs46ZTG5Lb4S4ETaOM3tUEoPtPDQtisgSGatRO9O</pass_ticket><isgrayscale>1</isgrayscale></error>

	public static void readXml(String xmlStr, User user) {
		DocumentBuilderFactory a = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder b = a.newDocumentBuilder();
			Document document = b.parse(IOUtils.toInputStream(xmlStr));
			NodeList booklist = document.getElementsByTagName("error");
			for (int i = 0; i < booklist.getLength(); i++) {
				// 循环遍历获取每一个book
				Node book = booklist.item(i);
				// 解析book节点的子节点
				NodeList childlist = book.getChildNodes();
				for (int t = 0; t < childlist.getLength(); t++) {
					// 区分出text类型的node以及element类型的node
					Node node = childlist.item(t);
					if (node.getNodeType() != Node.ELEMENT_NODE) {
						continue;
					}
					if ("skey".equals(node.getNodeName())) {
						user.setSkey(node.getTextContent());
					} else if ("wxsid".equals(node.getNodeName())) {
						user.setSid(node.getTextContent());
					} else if ("wxuin".equals(node.getNodeName())) {
						user.setUin(Integer.parseInt(node.getTextContent()));
					} else if ("pass_ticket".equals(node.getNodeName())) {
						user.setPass_ticket(node.getTextContent());
					} else if ("webwx_data_ticket".equals(node.getNodeName())) {
						user.setWebwx_data_ticket(node.getTextContent());
					} else if ("webwx_auth_ticket".equals(node.getNodeName())) {
						user.setWebwx_auth_ticket(node.getTextContent());
					}
				}

			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void close(AutoCloseable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/* 
	 * 返回长度为【strLength】的随机数，在前面补0 
	 */  
	public static String getFixLenthString(int strLength) {  
	    Random rm = new Random();  
	    // 获得随机数  
	    double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);  
	    // 将获得的获得随机数转化为字符串  
	    String fixLenthString = String.valueOf(pross);  
	    // 返回固定的长度的随机数  
	    return fixLenthString.substring(2, strLength + 2);  
	}  
}
