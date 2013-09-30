package hunternif.mc.dota2items.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Team;

public class TeamUtil {
	public static boolean areTeammates(EntityPlayer player1, EntityPlayer player2) {
		Team team1 = player1.getTeam();
		Team team2 = player2.getTeam();
		return team1 == null && team2 == null ||
				team1 != null && team2 != null && team1.isSameTeam(team2);
	}
}
