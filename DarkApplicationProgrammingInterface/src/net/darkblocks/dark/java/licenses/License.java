/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.dark.java.licenses;

import com.sun.javaws.exceptions.InvalidArgumentException;

/**
 * Created by Segdogamer on 28.11.2017  22:15.
 * Project: CraftPluginAPI
 * <p>
 * Web: https://segdogames.com
 * Mail: segdogamer@segdogames.com
 */
@SuppressWarnings("ALL")
public class License
{
	//1A2B-3C4D-5E6F-7G8H-9I0J - Ausgabe
	//1A2B3C4D5E6F7G8H9I0J - System
	/**
	 * System Style
	 */
	private String license;
	
	public static String unformatLicense(String formattedLicense) throws InvalidArgumentException
	{
		if (formattedLicense == null)
		{
			throw new NullPointerException("Got license as null object");
		}
		formattedLicense = formattedLicense.toUpperCase();
		if (!formattedLicense.matches("[A-Z0-9][A-Z0-9][A-Z0-9][A-Z0-9]-[A-Z0-9][A-Z0-9][A-Z0-9][A-Z0-9]-[A-Z0-9][A-Z0-9][A-Z0-9][A-Z0-9]-[A-Z0-9][A-Z0-9][A-Z0-9][A-Z0-9]-[A-Z0-9][A-Z0-9][A-Z0-9][A-Z0-9]"))
		{
			throw new InvalidArgumentException(new String[]{"Got license in invalid format: \"" + formattedLicense + "\""});
		}
		return formattedLicense.replaceAll("-", "");
	}
	
	public String getLicense()
	{
		return license;
	}
	
	public void setLicense(String license) throws InvalidArgumentException
	{
		if (license == null)
		{
			throw new NullPointerException("Got license as null object");
		}
		license = license.toUpperCase();
		if (license.length() != 20)
		{
			throw new InvalidArgumentException(new String[]{"Got license in invalid format: \"" + license + "\""});
		}
		for (char c : license.toCharArray())
		{
			if (!String.valueOf(c).matches("[A-Z0-9]"))
			{
				throw new InvalidArgumentException(new String[]{"Got license in invalid format: \"" + license + "\""});
			}
		}
		this.license = license;
	}
	
	private String getFormattedLicense()
	{
		if (license == null)
		{
			throw new NullPointerException("Lizenz nicht gesetzt");
		}
		return String.format("%s-%s-%s-%s-%s", license.substring(0, 4), license.substring(4, 8), license.substring(8, 12), license.substring(12, 16), license.substring(16, 20));
	}
}
