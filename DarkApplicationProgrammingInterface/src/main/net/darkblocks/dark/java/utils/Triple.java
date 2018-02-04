package net.darkblocks.dark.java.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by LartyHD on 15.12.2017  04:22.
 */
@Getter
@AllArgsConstructor
public class Triple<F, S, T>
{
	private final F first;
	private final S second;
	private final T third;
}
