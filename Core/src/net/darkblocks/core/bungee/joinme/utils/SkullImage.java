package net.darkblocks.core.bungee.joinme.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Created by LartyHD on 04.02.2018  17:47.
 */
public class SkullImage
{
	public static final String RANDOM;
	private static final Color[] colors;
	
	static
	{
		RANDOM = String.valueOf(new Random().nextInt());
		colors = new Color[]{new Color(0, 0, 0), new Color(0, 0, 170), new Color(0, 170, 0), new Color(0, 170, 170), new Color(170, 0, 0), new Color(170, 0, 170), new Color(255, 170, 0), new Color(170, 170, 170), new Color(85, 85, 85), new Color(85, 85, 255), new Color(85, 255, 85), new Color(85, 255, 255), new Color(255, 85, 85), new Color(255, 85, 255), new Color(255, 255, 85), new Color(255, 255, 255)};
	}
	
	public static ChatColor[][] toChatColorArray(BufferedImage image, int height)
	{
		double ratio = (double) (image.getHeight() / image.getWidth());
		int width = (int) ((double) height / ratio);
		if (width > 10)
		{
			boolean var11 = true;
		}
		BufferedImage resized = resizeImage(image, (int) ((double) height / ratio), height);
		ChatColor[][] chatImg = new ChatColor[resized.getWidth()][resized.getHeight()];
		for (int x = 0; x < resized.getWidth(); ++x)
		{
			for (int y = 0; y < resized.getHeight(); ++y)
			{
				int rgb = resized.getRGB(x, y);
				ChatColor closest = getClosestChatColor(new Color(rgb));
				chatImg[x][y] = closest;
			}
		}
		return chatImg;
	}
	
	public static String[] toImgMessage(ChatColor[][] colors, char imgchar)
	{
		String[] lines = new String[colors[0].length];
		for (int y = 0; y < colors[0].length; ++y)
		{
			String line = "";
			for (int x = 0; x < colors.length; ++x)
			{
				line = line + colors[x][y].toString() + imgchar;
			}
			lines[y] = line + ChatColor.RESET;
		}
		return lines;
	}
	
	public static String[] appendTextToImg(String[] chatImg, String... text)
	{
		for (int y = 0; y < chatImg.length; ++y)
		{
			if (text.length > y)
			{
				chatImg[y] = chatImg[y] + " " + text[y];
			}
		}
		return chatImg;
	}
	
	public static String[] appendCenteredTextToImg(String[] chatImg, String... text)
	{
		for (int y = 0; y < chatImg.length; ++y)
		{
			if (text.length <= y)
			{
				return chatImg;
			}
			int len = 65 - chatImg[y].length();
			chatImg[y] = chatImg[y] + center(text[y], len);
		}
		return chatImg;
	}
	
	public static String center(String s, int length)
	{
		if (s.length() > length)
		{
			return s.substring(0, length);
		}
		else if (s.length() == length)
		{
			return s;
		}
		else
		{
			int leftPadding = (length - s.length()) / 2;
			StringBuilder leftBuilder = new StringBuilder();
			for (int i = 0; i < leftPadding; ++i)
			{
				leftBuilder.append(" ");
			}
			return leftBuilder.toString() + s;
		}
	}
	
	public static void imgMessage(ProxiedPlayer player, BufferedImage image, int height, char imgchar, ProxiedPlayer sender, String s, String... text)
	{
		imgMessage(player, image, height, imgchar, true, sender, s, text);
	}
	
	public static void imgMessage(ProxiedPlayer player, BufferedImage image, int height, char imgchar, boolean centered, ProxiedPlayer sender, String s, String... text)
	{
		ChatColor[][] colors = toChatColorArray(image, height);
		String[] lines = toImgMessage(colors, imgchar);
		if (centered)
		{
			lines = appendCenteredTextToImg(lines, text);
		}
		else
		{
			lines = appendTextToImg(lines, text);
		}
		String[] arrayOfString1 = lines;
		int j = lines.length;
		for (int i = 0; i < j; ++i)
		{
			String line = arrayOfString1[i];
			net.md_5.bungee.api.chat.TextComponent HEAD = new net.md_5.bungee.api.chat.TextComponent(line);
			HEAD.setColor(ChatColor.AQUA);
			HEAD.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ExecuteJoinMe " + RANDOM + " " + sender.getServer().getInfo().getName()));
			HEAD.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(s)}));
			player.sendMessage(HEAD);
		}
	}
	
	private static BufferedImage resizeImage(BufferedImage originalImage, int width, int height)
	{
		BufferedImage resizedImage = new BufferedImage(width, height, 6);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();
		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		return resizedImage;
	}
	
	private static ChatColor getClosestChatColor(Color c)
	{
		if (c.getAlpha() < 128)
		{
			return null;
		}
		else
		{
			int index = 0;
			double best = -1.0D;
			int i;
			for (i = 0; i < colors.length; ++i)
			{
				if (areIdentical(colors[i], c))
				{
					return ChatColor.values()[i];
				}
			}
			for (i = 0; i < colors.length; ++i)
			{
				double distance = getDistance(c, colors[i]);
				if (distance < best || best == -1.0D)
				{
					best = distance;
					index = i;
				}
			}
			return ChatColor.values()[index];
		}
	}
	
	private static double getDistance(Color c1, Color c2)
	{
		double rmean = (double) (c1.getRed() + c2.getRed()) / 2.0D;
		double r = (double) (c1.getRed() - c2.getRed());
		double g = (double) (c1.getGreen() - c2.getGreen());
		int b = c1.getBlue() - c2.getBlue();
		double weightR = 2.0D + rmean / 256.0D;
		double weightG = 4.0D;
		double weightB = 2.0D + (255.0D - rmean) / 256.0D;
		return weightR * r * r + weightG * g * g + weightB * (double) b * (double) b;
	}
	
	private static boolean areIdentical(Color c1, Color c2)
	{
		return Math.abs(c1.getRed() - c2.getRed()) <= 5 && Math.abs(c1.getGreen() - c2.getGreen()) <= 5 && Math.abs(c1.getBlue() - c2.getBlue()) <= 5;
	}
	
	public enum ImgChar
	{
		BLOCK('█'),
		DARK_SHADE('▓'),
		MEDIUM_SHADE('▒'),
		LIGHT_SHADE('░');
		private final char c;
		
		ImgChar(char c)
		{
			this.c = c;
		}
		
		public char getChar()
		{
			return this.c;
		}
	}
}
