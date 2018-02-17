/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.dark.java.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by LartyHD on 15.12.2017  04:22.
 */
@Getter
@AllArgsConstructor
class Double<F, S>
{
	private final F first;
	private final S second;
}
