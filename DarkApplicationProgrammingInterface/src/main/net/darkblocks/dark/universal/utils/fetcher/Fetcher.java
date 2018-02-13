package net.darkblocks.dark.universal.utils.fetcher;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 * Created by LartyHD on 13.02.2018  14:28.
 */
public class Fetcher
{
	public static String callURL(String URL)
	{
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try
		{
			java.net.URL url = new URL(URL);
			urlConn = url.openConnection();
			if (urlConn != null)
			{
				urlConn.setReadTimeout(60 * 1000);
			}
			if (urlConn != null && urlConn.getInputStream() != null)
			{
				in = new InputStreamReader(urlConn.getInputStream(), Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null)
				{
					int cp;
					while ((cp = bufferedReader.read()) != -1)
					{
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
			in.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return sb.toString();
	}
}
