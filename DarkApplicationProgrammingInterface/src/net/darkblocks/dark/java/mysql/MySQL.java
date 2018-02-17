/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.dark.java.mysql;

import net.darkblocks.dark.java.config.PropertiesConfig;
import net.darkblocks.dark.java.utils.Callback;
import net.darkblocks.dark.java.utils.ClearCallback;

import java.io.File;
import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by LartyHD and Segdogamer on 10.11.2017 00:44.
 */
public class MySQL
{
	private final String host;
	private final String port;
	private final String username;
	private final String password;
	private final String database;
	private final ExecutorService executorService;
	private Connection connection;
	
	public MySQL()
	{
		@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
		PropertiesConfig properties = new PropertiesConfig(new File("databases"), "mysql.properties");
		this.host = (String) properties.get("Host");
		this.port = (String) properties.get("Port");
		this.username = (String) properties.get("Username");
		this.password = (String) properties.get("Password");
		this.database = (String) properties.get("Database");
		this.executorService = Executors.newCachedThreadPool();
		connect();
	}
	
	public MySQL(String host, String port, String username, String password, String database)
	{
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.database = database;
		this.executorService = Executors.newCachedThreadPool();
		connect();
	}
	
	private void connect()
	{
		if (!isConnected())
		{
			try
			{
				this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
				System.out.println(" ");
				System.out.println("[MySQL] Die Verbindung wurde erfolgreich aufgebaut");
				System.out.println(" ");
			} catch (SQLException ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	private void disconnect()
	{
		if (isConnected())
		{
			try
			{
				this.connection.close();
				this.connection = null;
				System.out.println(" ");
				System.out.println("[MySQL] Die Verbindung wurde erfolgreich geschlossen");
				System.out.println(" ");
			} catch (SQLException ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	private Connection getConnection()
	{
		try
		{
			if (!this.connection.isValid(3))
			{
				disconnect();
				connect();
			}
		} catch (SQLException ex)
		{
			disconnect();
			connect();
		}
		return this.connection;
	}
	
	public void update(String update)
	{
		update(update, null);
	}
	
	public void update(String update, ClearCallback callback)
	{
		if (isConnected())
		{
			this.executorService.execute(() -> {
				try
				{
					getConnection().createStatement().executeUpdate(update);
					if (callback != null)
					{
						callback.call();
					}
				} catch (SQLException ex)
				{
					ex.printStackTrace();
				}
			});
		}
	}
	
	public void updateSync(String update)
	{
		if (isConnected())
		{
			try
			{
				getConnection().createStatement().executeUpdate(update);
			} catch (SQLException ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	public void preparedUpdate(PreparedStatement update)
	{
		preparedUpdate(update, null);
	}
	
	@SuppressWarnings("SameParameterValue")
	private void preparedUpdate(PreparedStatement update, ClearCallback callback)
	{
		if (isConnected())
		{
			this.executorService.execute(() -> {
				try
				{
					update.executeUpdate();
					if (callback != null)
					{
						callback.call();
					}
				} catch (SQLException ex)
				{
					ex.printStackTrace();
				}
			});
		}
	}
	
	public void preparedUpdateSync(PreparedStatement update)
	{
		if (isConnected())
		{
			try
			{
				update.executeUpdate();
			} catch (SQLException ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	public void query(String query, Callback<ResultSet> callback)
	{
		if (isConnected())
		{
			this.executorService.execute(() -> {
				try
				{
					callback.call(getConnection().createStatement().executeQuery(query));
				} catch (SQLException ex)
				{
					ex.printStackTrace();
				}
			});
		}
	}
	
	public ResultSet querySync(String query)
	{
		if (isConnected())
		{
			try
			{
				return getConnection().createStatement().executeQuery(query);
			} catch (SQLException ex)
			{
				ex.printStackTrace();
				return null;
			}
		}
		else
		{
			return null;
		}
	}
	
	public void preparedQuery(PreparedStatement query, Callback<ResultSet> callback)
	{
		if (isConnected())
		{
			this.executorService.execute(() -> {
				try
				{
					callback.call(query.executeQuery());
				} catch (SQLException ex)
				{
					ex.printStackTrace();
				}
			});
		}
	}
	
	public ResultSet preparedQuerySync(PreparedStatement query)
	{
		if (isConnected())
		{
			try
			{
				return query.executeQuery();
			} catch (SQLException ex)
			{
				ex.printStackTrace();
				return null;
			}
		}
		else
		{
			return null;
		}
	}
	
	private boolean isConnected()
	{
		return this.connection != null;
	}
}