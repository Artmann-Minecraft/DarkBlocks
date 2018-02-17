/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.dark.spigot.vote;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.darkblocks.dark.universal.messages.Colors;
import net.darkblocks.dark.universal.messages.Messages;

import java.util.Set;

@Getter
@AllArgsConstructor
public abstract class Votes
{
	private final Set<Vote> votes;
	
	public String addVote(String name, String voteName)
	{
		String result = null;
		if (name != null && !name.equalsIgnoreCase("") && !name.equalsIgnoreCase(" "))
		{
			for (Vote vote : getVotes())
			{
				Set<String> voter = vote.getVoter();
				if (voter.contains(name))
				{
					voter.remove(name);
				}
				if (vote.getName().equalsIgnoreCase(voteName))
				{
					voter.add(name);
					result = Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.TEXT + "Du hast für " + Colors.IMPORTANT + voteName + Colors.TEXT + " abgestimmt";
				}
			}
		}
		return result;
	}
	
	public void getResult()
	{
		int max = -1;
		String winner = "";
		for (Vote vote : getVotes())
		{
			int i = vote.getVoter().size();
			if (i > max)
			{
				max = i;
				winner = vote.getName();
			}
		}
		finishVotes(winner);
	}
	
	protected abstract void finishVotes(String winner);
}
