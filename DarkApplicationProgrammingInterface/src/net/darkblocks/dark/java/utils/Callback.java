/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */

package net.darkblocks.dark.java.utils;

import java.sql.SQLException;

/**
 * Created by LartyHD on 10.11.2017  00:43.
 */
public interface Callback<T>
{
	void call(T result) throws SQLException;
}
