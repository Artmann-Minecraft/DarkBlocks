package net.darkblocks.core.universal.logger;

import java.beans.ConstructorProperties;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Created by LartyHD on 11.02.2018  19:54.
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class Logger extends java.util.logging.Logger
{
	private final Logger.AsyncLogDispatcher asyncLogDispatcher = new Logger.AsyncLogDispatcher();
	private final String name;
	private final String separator = System.getProperty("line.separator");
	
	public Logger(String name, String directory) throws Exception
	{
		super(name, null);
		this.name = name;
		Field field = Charset.class.getDeclaredField("defaultCharset");
		field.setAccessible(true);
		field.set(null, Charset.forName("UTF-8"));
		if (!new File(directory).isDirectory())
		{
			new File(directory).mkdirs();
		}
		this.setLevel(Level.ALL);
		Logger.FileLoggerHandler handler = new FileLoggerHandler(new FileFormatter(), directory);
		this.addHandler(handler);
		System.setOut(new PrintStream(new LoggingOutputStream(Level.INFO), true, "UTF-8"));
		System.setErr(new PrintStream(new LoggingOutputStream(Level.SEVERE), true, "UTF-8"));
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public class AsyncLogDispatcher extends Thread
	{
		private final LinkedBlockingQueue<Runnable> queueTasks = new LinkedBlockingQueue<>();
		
		AsyncLogDispatcher()
		{
			this.setDaemon(true);
			this.start();
		}
		
		public void run()
		{
			while (!this.isInterrupted())
			{
				try
				{
					Runnable runnable = this.queueTasks.take();
					runnable.run();
				} catch (InterruptedException var2)
				{
					var2.printStackTrace();
				}
			}
		}
		
		public BlockingQueue<Runnable> getQueueTasks()
		{
			return this.queueTasks;
		}
	}
	
	public class FileLoggerHandler extends Handler
	{
		private final String directory;
		private final PrintWriter printWriter;
		
		FileLoggerHandler(Formatter formatter, String directory) throws Exception
		{
			this.setLevel(Level.INFO);
			this.directory = directory;
			try
			{
				this.setEncoding(StandardCharsets.UTF_8.name());
			} catch (UnsupportedEncodingException var5)
			{
				var5.printStackTrace();
			}
			this.setFormatter(formatter);
			File file = new File(directory + "/latest.log");
			if (file.exists())
			{
				file.renameTo(new File(directory + "/latest_" + (new SimpleDateFormat("dd_MM_yyyy-HH_mm_ss")).format(System.currentTimeMillis()) + ".log"));
			}
			file.createNewFile();
			this.printWriter = new PrintWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(directory + "/latest.log")), StandardCharsets.UTF_8));
		}
		
		public void publish(LogRecord record)
		{
			Logger.this.asyncLogDispatcher.getQueueTasks().add(() -> {
				this.printWriter.write(this.getFormatter().format(record));
				this.printWriter.flush();
			});
		}
		
		public void flush()
		{
		}
		
		@SuppressWarnings("ResultOfMethodCallIgnored")
		public void close() throws SecurityException
		{
			this.printWriter.close();
			if (Files.exists(Paths.get(this.directory + "/latest.log")))
			{
				new File(this.directory + "/latest.log").renameTo(new File(this.directory + "/latest_" + (new SimpleDateFormat("dd_MM_yyyy-HH_mm_ss")).format(System.currentTimeMillis()) + ".log"));
			}
		}
	}
	
	private class FileFormatter extends Formatter
	{
		private final DateFormat format;
		
		private FileFormatter()
		{
			this.format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		}
		
		public String format(LogRecord record)
		{
			StringBuilder builder = new StringBuilder();
			if (record.getThrown() != null)
			{
				StringWriter writer = new StringWriter();
				record.getThrown().printStackTrace(new PrintWriter(writer));
				builder.append(writer).append("\n");
			}
			return "[" + this.format.format(System.currentTimeMillis()) + "|" + Logger.this.name + "]" + record.getLevel().getLocalizedName() + ":" + this.formatMessage(record) + "\n " + builder.substring(0);
		}
	}
	
	private class LoggingOutputStream extends ByteArrayOutputStream
	{
		private final Level level;
		
		@ConstructorProperties({"level"})
		LoggingOutputStream(Level level)
		{
			this.level = level;
		}
		
		public void flush() throws IOException
		{
			String contents = this.toString(StandardCharsets.UTF_8.name());
			super.reset();
			if (!contents.isEmpty() && !contents.equals(Logger.this.separator))
			{
				Logger.this.logp(this.level, "", "", contents);
			}
		}
	}
}
